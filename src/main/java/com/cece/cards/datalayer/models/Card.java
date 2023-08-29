package com.cece.cards.datalayer.models;

import com.cece.cards.dto.responses.CardResponse;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards", uniqueConstraints = { @UniqueConstraint(columnNames = { "user_id", "name" }) })
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String color;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CardResponse response() {
        return CardResponse.builder()
                .id(id)
                .name(name)
                .description(description)
                .color(color)
                .status(status)
                .userId(user.getId())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
