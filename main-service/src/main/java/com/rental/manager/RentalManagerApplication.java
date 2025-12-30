package com.rental.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentalManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalManagerApplication.class, args);
	}

}
