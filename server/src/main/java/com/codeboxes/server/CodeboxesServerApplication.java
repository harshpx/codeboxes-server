package com.codeboxes.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CodeboxesServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeboxesServerApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return runner -> {
			System.out.println("Codeboxes Server is running!");
		};
	}

}
