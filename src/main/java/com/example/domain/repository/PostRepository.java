package com.example.domain.repository;

import com.example.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByBoardId(Long boardId, Pageable pageable);

    boolean existsById(Long postId);

}
