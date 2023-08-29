package com.cece.cards.seeds;

import com.cece.cards.datalayer.models.Role;
import com.cece.cards.datalayer.models.Roles;
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

    @Value("${app.member1.username}")
    private String member1Username;
    @Value("${app.member1.password}")
    private String member1Password;

    @Value("${app.member2.username}")
    private String member2Username;
    @Value("${app.member2.password}")
    private String member2Password;

    @Override
    public void run(String... args) {
        if(usersExists())
            return;
        createUser(Roles.ADMIN, adminUsername, adminPassword);
        createUser(Roles.MEMBER, member1Username, member1Password);
        createUser(Roles.MEMBER, member2Username, member2Password);
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
