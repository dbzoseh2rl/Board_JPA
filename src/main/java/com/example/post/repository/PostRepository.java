package com.example.post.repository;

import com.example.post.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시판 번호(boardNo)로 페이징된 게시글 리스트 조회
    Page<Post> findByBoardNo(Long boardNo, Pageable pageable);

    // 게시글 존재 여부 확인
    boolean existsById(Long postNo);
}
