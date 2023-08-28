package com.cece.cards.seeds;

import com.cece.cards.datalayer.models.Role;
import com.cece.cards.auth.Roles;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username")
    private String adminUsername;
    @Value("${app.admin.password")
    private String adminPassword;

    @Value("${app.user.username")
    private String userUsername;
    @Value("${app.user.password")
    private String userPassword;

    @Override
    public void run(String... args) {
        createUser(Roles.ADMIN, adminUsername, adminPassword);
        createUser(Roles.USER, userUsername, userPassword);
    }

    private void createUser(Roles roles, String username, String password) {
        Role role = createRole(roles);
        String encodedPassword = passwordEncoder.encode(password);
        User user = User.builder()
                .id(0)
                .email(username)
                .password(encodedPassword)
                .roles(Set.of(role))
                .build();
        userService.createUser(user);
    }

    private Role createRole(Roles roles) {
        Role role= Role.builder()
                .id(0)
                .name(roles.name())
                .build();
        return userService.createRole(role);
    }
}
