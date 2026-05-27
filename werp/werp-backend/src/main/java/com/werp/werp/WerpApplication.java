package com.werp.werp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import com.werp.werp.configuration.JwtProperties;

/**
 * Classe principal refatorada:
 *  - @EnableMethodSecurity habilita @PreAuthorize nos controllers
 *  - @EnableConfigurationProperties registra JwtProperties
 */
@SpringBootApplication
@EnableMethodSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class WerpApplication {

    public static void main(String[] args) {
        SpringApplication.run(WerpApplication.class, args);
    }
}
