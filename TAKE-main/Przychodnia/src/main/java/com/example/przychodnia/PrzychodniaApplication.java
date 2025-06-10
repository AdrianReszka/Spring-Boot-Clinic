package com.example.przychodnia;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.example.model.Pacjent;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example.repositories")
@EntityScan(basePackages = "com.example.model")
public class PrzychodniaApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrzychodniaApplication.class, args);
        System.out.println(">>> PrzychodniaApplication startuje");
    }
}
