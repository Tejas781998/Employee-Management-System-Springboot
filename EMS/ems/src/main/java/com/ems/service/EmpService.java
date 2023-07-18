package com.ems.service;

import com.ems.productcrudapp.entity.Address;
import com.ems.productcrudapp.entity.Emp;
import com.ems.productcrudapp.emsrepository.AddressRepository;
import com.ems.productcrudapp.emsrepository.EmpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

@Service
public class EmpService {

	@Autowired
	private EmpRepository empRepository;

	@Autowired
	private AddressRepository addressRepository;

//    @Autowired
//    public EmpService(EmpRepository empRepository) {
//        this.empRepository = empRepository;
//    }
//
//
//    @Autowired
//    public EmpService(AddressRepository addressRepository)
//    {
//    	this.addressRepository = addressRepository;
//    }

	public String checkEmailForEmp(String email, HttpSession session) {
		boolean emailExists = checkEmailExistence(email);
		if (emailExists) {
			session.setAttribute("msg", "Email already exists");
			return "addEmp";
		}

		return null;
	}

	public String checkEmailForUser(String email, HttpSession session) {

		boolean emailExists = checkEmailExistence(email);
		if (emailExists) {
			session.setAttribute("msg", "Email already exists");
			return "register";
		}
		return null;

	}

	public String checkMailUpdateUser(String oldEmail, String newEmail, HttpSession session) {

		boolean emailExists = checkEmailExistence(newEmail);

		if (!oldEmail.equals(newEmail)) {
			if (emailExists) {
				session.setAttribute("msg", "Email already exists");
				return "editUser";
			}
		}
		return null;

	}

	public String passwordValidationUpdateUser(String password, HttpSession session) {

		// Password validation
		if (password == null || password.isEmpty()) {
			session.setAttribute("msg", "Password is required");
			return "editUser";
		} else if (password.length() < 8 || password.length() > 20) {
			session.setAttribute("msg", "Password must be between 8 to 20 characters");
			return "editUser";
		} else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*|?.,])[A-Za-z\\d!@#$%^&*|?.,]+$")) {
			session.setAttribute("msg",
					"Password must contain at least one uppercase letter, lowercase letter, digit, and special character");
			return "editUser";
		}
		return null;
	}

	public String passwordValidationUpdateEmp(String password, HttpSession session) {
		if (password == null || password.isEmpty()) {
			session.setAttribute("msg", "Password is required");
			return "editEmp";
		} else if (password.length() < 8 || password.length() > 20) {
			session.setAttribute("msg", "Password must be between 8 to 20 characters");
			return "editEmp";
		} else if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*|?.,])[A-Za-z\\d!@#$%^&*|?.,]+$")) {
			session.setAttribute("msg",
					"Password must contain at least one uppercase letter, lowercase letter, digit, and special character");
			return "editEmp";
		}

		return null;
	}

	public Emp saveEmp(Emp emp) {

		for (Address address : emp.getAddress()) {
			address.setEmp(emp);
		}
		return empRepository.save(emp);
	}

	public List<Emp> getAllEmp() {
		return empRepository.findAll();
	}

	public Emp getEmpById(int id) {
		return empRepository.findById(id).orElse(null);
	}

	public void updateEmp(Emp emp) {
		for (Address address : emp.getAddress()) {
			address.setEmp(emp);
		}
		empRepository.save(emp);
	}

	public void deleteEmp(int id) {
		empRepository.deleteById(id);
	}
	
	public void deleteAddress(int aId) {
		Integer k = aId;
//		System.out.println("Address id is = " + aId);
//		boolean ad = addressRepository.findById(aId).isPresent();
//		System.out.println("Address is present/absent = " + ad);
		Address add = addressRepository.findById(k).get();
		 add.getEmp().getAddress().remove(add); 
//		System.out.println("address is " + add);
//		addressRepository.deleteById(aId);
		addressRepository.delete(add);
	}
	
	


	public Emp loginUser(String email, String password, String userType) {
//		String sql = "from Emp where email=:nm and password=:pwd and userType=:ut";
//
//		Emp us = (Emp) hibernateTemplate.execute(s -> {
//			Query q = s.createQuery(sql);
//			q.setString("nm", email);
//			q.setString("pwd", password);
//			q.setString("ut", userType);
//			return q.uniqueResult();
//		});
//
//		return us;
		return empRepository.findByEmailAndPasswordAndUserType(email, password, userType);

	}
	
	

	public List<Emp> getAllEmpRowDetailsByEmail(String userEmail) {
		return empRepository.getAllEmpRowDetailsByEmail(userEmail);
	}

	public boolean checkEmailExistence(String email) {
		return empRepository.existsByEmail(email);
	}

}