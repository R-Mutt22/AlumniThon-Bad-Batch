package com.bad.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SkillLinkApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillLinkApplication.class, args);
	}

}
