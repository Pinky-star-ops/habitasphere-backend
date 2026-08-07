package com.habitasphere;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HabitasphereApplication {

	public static void main(String[] args) {
		SpringApplication.run(HabitasphereApplication.class, args);
	}

}

