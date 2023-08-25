package com.cece.cards.config;

import com.cece.cards.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DbSeeder implements CommandLineRunner {

    private final UserService userService;
    private final String adminRole = "ADMIN";
    private final String memberRole = "MEMBER";

    @Override
    public void run(String... args) throws Exception {


    }

    private void createRoles() {

    }

    private void createUsers() {

    }

}
