package com.example.domain.entity;

import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Builder
    private Board(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
