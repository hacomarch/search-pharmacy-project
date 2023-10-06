package com.example.searchpharmacyproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SearchPharmacyProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SearchPharmacyProjectApplication.class, args);
    }

}
