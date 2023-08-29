package com.cece.cards.seeds;

import com.cece.cards.datalayer.models.Role;
import com.cece.cards.auth.Roles;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserSeeder implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    @Value("${app.user1.username}")
    private String user1Username;
    @Value("${app.user1.password}")
    private String user1Password;

    @Value("${app.user2.username}")
    private String user2Username;
    @Value("${app.user2.password}")
    private String user2Password;

    @Override
    public void run(String... args) {
        if(usersExists())
            return;
        createUser(Roles.ADMIN, adminUsername, adminPassword);
        createUser(Roles.USER, user1Username, user1Password);
        createUser(Roles.USER, user2Username, user2Password);
    }

    private boolean usersExists(){
        long count=userService.countAll();
        return count>0;
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
        return userService.createRoleIfNotExist(role);
    }
}
