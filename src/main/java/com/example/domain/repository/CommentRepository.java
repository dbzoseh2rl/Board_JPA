package com.example.domain.repository;

import com.example.domain.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    int countByPostId(long postId);

    Page<Comment> findByPostId(long postId, Pageable pageable);

}
