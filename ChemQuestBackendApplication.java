package com.chemquest.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Starting point of the backend. @SpringBootApplication tells Spring Boot to
 * scan this package (com.chemquest.backend) and everything below it for
 * controllers, services and repositories.
 */
@SpringBootApplication
public class ChemQuestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChemQuestBackendApplication.class, args);
    }
}
