package com.cece.cards.datalayer.models;

import com.cece.cards.dto.responses.CardResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String description;
    private String color;
    private String status;

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
                .build();
    }
}
