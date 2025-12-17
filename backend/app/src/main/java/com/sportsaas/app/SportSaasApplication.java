package com.sportsaas.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée principal de l'application Sport SaaS.
 *
 * <p>Cette application est une plateforme multi-tenant de gestion
 * de matériels sportifs.</p>
 */
@SpringBootApplication(scanBasePackages = "com.sportsaas")
public class SportSaasApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportSaasApplication.class, args);
    }
}
