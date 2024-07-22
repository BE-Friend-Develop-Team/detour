package com.befriend.detour;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class DetourApplication {

    public static void main(String[] args) {
        SpringApplication.run(DetourApplication.class, args);
    }

}
