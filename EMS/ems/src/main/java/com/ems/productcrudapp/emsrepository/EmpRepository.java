package com.ems.productcrudapp.emsrepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.productcrudapp.entity.Emp;

public interface EmpRepository extends JpaRepository<Emp, Integer> {

	Emp findByEmailAndPasswordAndUserType(String email, String password, String userType);

	List<Emp> getAllEmpRowDetailsByEmail(String userEmail);

	boolean existsByEmail(String email);
	

}
