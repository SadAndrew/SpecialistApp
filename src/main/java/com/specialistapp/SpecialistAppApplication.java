package com.specialistapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.specialistapp")
@EntityScan("com.specialistapp.model.entity")
@EnableJpaRepositories("com.specialistapp.model.repository")
public class SpecialistAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpecialistAppApplication.class, args);
    }

}
