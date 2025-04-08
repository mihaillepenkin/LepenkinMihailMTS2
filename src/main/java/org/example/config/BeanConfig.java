package org.example.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @PersistenceUnit
    EntityManagerFactory emf;

}