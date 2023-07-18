package com.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.ems.productcrudapp", "com.ems.productcrudapp.emsrepository","com.ems.productcrudapp.entity","com.ems.productcrudapp.interceptor","com.ems.service"})
public class EmsApplication {

	public static void main(String[] args) 
	{
		SpringApplication.run(EmsApplication.class, args);
	}

}
