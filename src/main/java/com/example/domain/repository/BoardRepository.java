package com.example.domain.repository;

import com.example.domain.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);
    
    // 이름으로 게시판 찾기
    Optional<Board> findByName(String name);
    
    // 이름으로 게시판 존재 여부 확인
    boolean existsByName(String name);

}