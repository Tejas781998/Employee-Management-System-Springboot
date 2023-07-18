package com.ems.productcrudapp.emsrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ems.productcrudapp.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer>
{
	

}
