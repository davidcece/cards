package com.cece.cards.datalayer.repositories;

import com.cece.cards.datalayer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
