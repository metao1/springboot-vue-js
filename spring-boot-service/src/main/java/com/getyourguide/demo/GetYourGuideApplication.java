package com.getyourguide.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
public class GetYourGuideApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetYourGuideApplication.class, args);
	}

}
