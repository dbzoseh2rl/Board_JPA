package com.example.repository;

import com.example.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 페이징된 게시글 리스트 반환
    Page<Board> findAll(Pageable pageable);

    // 총 게시글 개수는 JpaRepository의 count()로 가능
    // 게시글 존재 여부는 existsById()로 가능
}