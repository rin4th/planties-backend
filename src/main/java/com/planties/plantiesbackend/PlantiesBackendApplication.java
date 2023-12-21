package com.planties.plantiesbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PlantiesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PlantiesBackendApplication.class, args);
	}

}
