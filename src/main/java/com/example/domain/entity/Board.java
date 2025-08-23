package com.example.domain.entity;

import com.example.global.model.Timestamp;
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

    // 정적 팩토리 메서드
    public static Board from(String name) {
        return Board.builder()
                .name(name)
                .build();
    }

    // 비즈니스 로직 메서드들
    public void updateName(String newName) {
        this.name = newName;
    }

    public boolean hasName(String name) {
        return this.name.equals(name);
    }

}
