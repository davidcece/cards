package com.cece.cards.services;

import com.cece.cards.datalayer.models.Role;
import com.cece.cards.datalayer.models.User;
import com.cece.cards.datalayer.repositories.RoleRepository;
import com.cece.cards.datalayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Role createRoleIfNotExist(Role role){
        Role existing=roleRepository.findByName(role.getName());
        if(existing!=null)
            return existing;
        return roleRepository.save(role);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public long countAll() {
        return userRepository.count();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByEmail(username);
    }
}
