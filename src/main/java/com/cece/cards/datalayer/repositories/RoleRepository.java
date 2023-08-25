package com.cece.cards.datalayer.repositories;

import com.cece.cards.datalayer.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
