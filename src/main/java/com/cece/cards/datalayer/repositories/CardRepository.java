package com.cece.cards.datalayer.repositories;

import com.cece.cards.datalayer.models.Card;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUserId(int userId, Pageable pageable);

    long countByUserId(int id);
}
