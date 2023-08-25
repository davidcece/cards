package com.cece.cards.services;

import com.cece.cards.datalayer.repositories.RoleRepository;
import com.cece.cards.datalayer.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

}
