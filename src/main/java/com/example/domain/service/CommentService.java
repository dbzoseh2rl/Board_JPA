package com.example.domain.service;

import com.example.domain.dto.PageList;
import com.example.domain.dto.PageRequest;
import com.example.domain.dto.Result;
import com.example.domain.dto.ResultType;
import com.example.domain.entity.Comment;
import com.example.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public PageList<Comment> getCommentList(PageRequest pageRequest, long postSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageIndex() - 1,
                pageRequest.getPageSize()
        );

        Page<Comment> commentPage = commentRepository.findByPostId(postSeq, pageable);

        long totalCount = commentPage.getTotalElements();
        List<Comment> commentList = commentPage.getContent();

        return new PageList<>(pageRequest.getPageSize(), (int) totalCount, commentList);
    }

    public Comment getComment(long commentSeq) {
        Optional<Comment> optionalComment = commentRepository.findById(commentSeq);
        return optionalComment.orElse(null);
    }

    @Transactional
    public Result deleteComment(Comment comment) {
        commentRepository.deleteById(comment.getId());
        return new Result(ResultType.OK);
    }

    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment modifyComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public boolean isCommentExist(long postSeq) {
        return commentRepository.countByPostId(postSeq) > 0;
    }

}
