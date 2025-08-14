package com.example.comment.repository;

import com.example.comment.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 게시글(postNo) 댓글 개수 조회
    int countByPostNo(long postNo);

    // 게시글(postNo) 댓글 리스트 페이징 조회
    Page<Comment> findByPostNo(long postNo, Pageable pageable);

    // getComment(long commentNo) → JpaRepository.findById(id) 사용 가능
    // deleteComment(long commentNo) → JpaRepository.deleteById(id) 사용 가능
    // insertComment(Comment comment) → JpaRepository.save(comment) 사용 가능
    // updateComment(Comment comment) → JpaRepository.save(comment) 사용 가능
}
