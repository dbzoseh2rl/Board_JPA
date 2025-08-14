package com.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import jakarta.validation.constraints.NotEmpty;

@Getter
@ToString
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @NotEmpty
    private String id;

    @NotEmpty
    private String password;

    private String accessToken;
    private String refreshToken;

    public User(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public User(String id, String accessToken, String refreshToken) {
        this.id = id;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
