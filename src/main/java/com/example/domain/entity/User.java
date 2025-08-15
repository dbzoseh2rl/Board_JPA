package com.example.domain.entity;

import com.example.domain.dto.user.UserResponse;
import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String role;

    @Builder
    private User(Long id, String email, String password, String role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static User from(UserResponse userResponse) {
        return User.builder()
                .email(userResponse.id())        // record의 getter 메서드 사용
                .password(userResponse.password()) // record의 getter 메서드 사용
                .build();
    }

    // 추가 비즈니스 로직 메서드들
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRole(String newRole) {
        this.role = newRole;
    }

    public boolean hasRole(String role) {
        return this.role != null && this.role.equals(role);
    }

    public boolean isAdmin() {
        return hasRole("ADMIN");
    }

    public boolean isUser() {
        return hasRole("USER");
    }

}
