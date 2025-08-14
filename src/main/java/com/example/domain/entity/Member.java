package com.example.domain.entity;

import com.example.domain.dto.User;
import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String userId;

    @Column(nullable = false)
    private String password;

    private String role;

    @Builder
    private Member(Long id, String userId, String password, String role) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.role = role;
    }

    public static Member from(User user) {
        return Member.builder()
                .userId(user.getId())
                .password(user.getPassword())
                .build();
    }


}
