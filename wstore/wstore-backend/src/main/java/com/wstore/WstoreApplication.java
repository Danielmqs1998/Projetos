package com.wstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class WstoreApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(WstoreApplication.class, args);
	}
	
}
