package com.example.model;

import com.example.global.common.model.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;     // 기존 코드와 맞춤

    @Column(nullable = false, unique = true)
    private String userId;     // 기존 코드와 맞춤

    @Column(nullable = false)
    private String password;

    private String role;

}
