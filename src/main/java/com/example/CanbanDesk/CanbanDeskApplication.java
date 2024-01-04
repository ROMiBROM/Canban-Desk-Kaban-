package com.example.CanbanDesk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.ViewResolver;

import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@SpringBootApplication
public class CanbanDeskApplication {
	public static void main(String[] args) {
		SpringApplication.run(CanbanDeskApplication.class, args);
	}
}



