package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@EnableRetry
@EnableCaching
@ComponentScan("org.example.repository")
public class Application {

    public static void main(String[] args) {SpringApplication.run(Application.class, args);}

    @Bean
    public FlywayMigrationStrategy repairFlyway() {
        return flyway -> {
            flyway.repair();
            flyway.migrate();
        };
    }
}