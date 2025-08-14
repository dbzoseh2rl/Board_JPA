package com.example.auth.repository;

import com.example.auth.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member, Long> {
    boolean existsByUserId(String userId);
    Optional<Member> findByUserId(String userId);
}
