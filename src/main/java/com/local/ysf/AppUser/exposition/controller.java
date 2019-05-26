package com.local.ysf.AppUser.exposition;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.javafaker.Faker;

@RestController
@RequestMapping("/data")
public class controller {

	private Faker faker = new Faker();

	@GetMapping(value="onlyAdmin")
	public List<String> returnedData(){
		List lst = new ArrayList<>();
		for (int i = 0; i < 15; i++) {
			lst.add(faker.address().cityName().toString());
		}
		return lst;
	}
	
	
	@GetMapping(value="allRoles")
	public List<String> returnedDatatestUserRoles(){
		List lst = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			lst.add(faker.address().city());
		}
		return lst;
	}
	
}
