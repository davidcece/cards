package com.cece.cards.datalayer.repositories;

import com.cece.cards.datalayer.models.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
