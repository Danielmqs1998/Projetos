package com.wstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.wstore.configuration.JwtProperties;

/**
 * Refatorado:
 *  - WebMvcConfigurer removido (CORS nao e mais configurado aqui)
 *  - @EnableConfigurationProperties registra JwtProperties
 */
@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class WstoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(WstoreApplication.class, args);
    }
}
