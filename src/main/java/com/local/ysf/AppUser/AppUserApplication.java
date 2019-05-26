package com.local.ysf.AppUser;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.h2.server.web.WebServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;
import com.local.ysf.AppUser.Entity.BookReaderRole;
import com.local.ysf.AppUser.Entity.BookReaderUser;
import com.local.ysf.AppUser.business.BookReaderUserService;
@EntityScan(basePackages="com.local.ysf.*")
@RestController
@SpringBootApplication
public class AppUserApplication {

	
	
	@Autowired
	private BookReaderUserService service;
	private Faker faker = new Faker();
	
	public static void main(String[] args) {
		SpringApplication.run(AppUserApplication.class, args);
	}
	@Bean
    ServletRegistrationBean h2servletRegistration(){
        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/h2/*");
        return registrationBean;
    }
	@Bean
	public BCryptPasswordEncoder creatBCryptPasswordEncoder(){
		return new BCryptPasswordEncoder();
	}
	

	
	//@PostConstruct
	public void data(){
		
		service.saveRole(new BookReaderRole(null, "ADMIN"));
		service.saveRole(new BookReaderRole(null, "USER"));
		
		service.saveUser(new BookReaderUser(null, "admin","admin",null));
		service.saveUser(new BookReaderUser(null, "user","user",null));
		
		service.addRoleToUser("admin", "ADMIN");
		service.addRoleToUser("admin", "USER");
		service.addRoleToUser("user", "USER");
	
	}

}
