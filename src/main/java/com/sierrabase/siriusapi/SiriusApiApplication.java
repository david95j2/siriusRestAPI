package com.sierrabase.siriusapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SiriusApiApplication {

	public static void main(String[] args) {
		String currentUserName = System.getProperty("user.name");
		System.out.println(currentUserName);
		SpringApplication.run(SiriusApiApplication.class, args);
	}


}
