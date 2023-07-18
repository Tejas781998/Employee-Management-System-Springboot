package com.ems.productcrudapp;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ems.productcrudapp.emsrepository.EmpRepository;
import com.ems.productcrudapp.entity.Address;
import com.ems.productcrudapp.entity.Emp;
import com.ems.service.EmpService;



@Controller
@RequestMapping("")
public class HomeController {
//    private final EmpRepository empRepository;

//    @Autowired
//    public HomeController(EmpRepository empRepository) {
//        this.empRepository = empRepository;
//    }
    @Autowired
   private EmpService empService;
    
 

    @RequestMapping("/test")
    public String test() {
        System.out.println("Test Runned");
        return "test";
    }

    @RequestMapping("/index")
    public String index() {
        return "index";
    }  

    @RequestMapping("loginsignup")
    public String loginsignup() {
        return "loginsignup";
    }

    @RequestMapping("/register")
    public String register() {
        return "register";
    }

    @RequestMapping("/home")
    public String home(Model m) {
        List<Emp> empList = empService.getAllEmp();
        m.addAttribute("empList", empList);
        return "Home";
    }

    @RequestMapping("/addEmp")
    public String addEmp() {
        return "addEmp";
    }

    @RequestMapping(path = "/checkEmailExistence", method = RequestMethod.POST)
    @ResponseBody
    public boolean checkEmailExistence(@RequestParam("email") String email) {
        return empService.checkEmailExistence(email);
    }

    @Transactional
    @RequestMapping(path = "/createEmp", method = RequestMethod.POST)
    public String createEmp(@ModelAttribute Emp emp, HttpSession session) {
        String email = emp.getEmail();
        empService.checkEmailForEmp(email, session);
        empService.saveEmp(emp);

        session.setAttribute("msg", "Registered Successfully");
        return "addEmp";
    }

    @RequestMapping(path = "/registerUser", method = RequestMethod.POST)
    public String registerPage(@ModelAttribute Emp emp, HttpSession session) {
        String email = emp.getEmail();
        empService.checkEmailForUser(email, session);
        for (Address address : emp.getAddress()) {
            address.setEmp(emp);
        }

        empService.saveEmp(emp);

        session.setAttribute("msg", "Registered Successfully");
        return "register";
    }
    
    //findById(id).orElse(null);

    @RequestMapping("/editEmp/{id}")
    public String editEmp(@PathVariable int id, HttpSession session) {
        Emp emp = empService.getEmpById(id);
        session.setAttribute("emp", emp);
        return "editEmp";
    }

    @RequestMapping("/editUser/{id}")
    public String editUser(@PathVariable int id, Model m) {
        Emp emp = empService.getEmpById(id);
        m.addAttribute("emp", emp);
        return "editUser";
    }

    @RequestMapping(path = "/updateUser", method = RequestMethod.POST)
    public String updateUser(@ModelAttribute Emp emp, HttpSession session) {
        String oldEmail = (String) session.getAttribute("email");
        String newEmail = emp.getEmail();
		String password = emp.getPassword();
		
        empService.checkMailUpdateUser(oldEmail, newEmail, session);
        empService.passwordValidationUpdateUser(password,session);
        
        for (Address address : emp.getAddress()) {
            address.setEmp(emp);
        }

        empService.saveEmp(emp);

        session.setAttribute("msg", "Updated Successfully");
        session.setAttribute("updateduser", emp);

      
        
        List<Emp> list = empService.getAllEmpRowDetailsByEmail(newEmail);
        session.setAttribute("empList", list);

        return "/profile";
    }

    @RequestMapping(path = "/updateEmp", method = RequestMethod.POST)
    public String updateEmp(@ModelAttribute Emp emp, HttpSession session) {
    	
    	List<Address> addresses = emp.getAddress();
		if (addresses != null && !addresses.isEmpty()) {
			for (Address add : addresses) {
				add.setEmp(emp);
			}
		}
        empService.saveEmp(emp);

        session.setAttribute("msg", "Updated Successfully");
        return "redirect:/home";
    }

    @RequestMapping("/deleteEmp/{id}")
    public String deleteEmp(@PathVariable int id, HttpSession session) {
        empService.deleteEmp(id);;
        session.setAttribute("msg", "Employee deleted Successfully");
        return "redirect:/home";
    }

    @Transactional
    @RequestMapping("/deleteAddress/{addressId}")
    public String deleteAddress(@PathVariable int addressId, HttpSession session) {
        empService.deleteAddress(addressId);
        session.setAttribute("msg", "Address deleted Successfully");
        return "editEmp";
    }

    @RequestMapping("/login")
    public String loginPage() {
        return "login";
    }
    String email;
	String userTypeString;
	

    @RequestMapping(path = "/userlogin", method = RequestMethod.POST)
    public String userLogin(@RequestParam("email") String em, @RequestParam("password") String password,
                            @RequestParam("userType") String userType, HttpSession session, Model m) {
        Emp user = empService.loginUser(em, password, userType);
        if (userType.equals("admin") && user != null) {
			session.setAttribute("loginuser", user);
			List<Emp> list =empService.getAllEmp();
			m.addAttribute("empList", list);
			return "Home";
		} else if (user != null) {
			session.setAttribute("loginuser", user);
			List<Emp> list = empService.getAllEmpRowDetailsByEmail(em);
			m.addAttribute("empList", list);

			// Store the email in a session attribute
			session.setAttribute("email", em);
			email = em	;
			userTypeString = userType;
			return "profile";

		} else {
			session.setAttribute("msg", "invalid Email or Password");
			return "redirect:/login";
		}
    }

    @RequestMapping("/myProfile")
    public String myProfile(Model m, HttpSession session) {
        String email = (String) session.getAttribute("email");
        List<Emp> list = empService.getAllEmpRowDetailsByEmail(email);
        m.addAttribute("empList", list);
        return "profile";
    }

    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.removeAttribute("loginuser");
        session.setAttribute("msg", "Logout Successfully");
        return "login";
    }

    @RequestMapping(path = "/userAddress", method = RequestMethod.POST)
    public ResponseEntity<String> userData(@ModelAttribute Emp emp, HttpSession session) {
        empService.saveEmp(emp);
        return ResponseEntity.ok("Data Insert");
    }
}
