package com.example.comment.service.impl;

import com.example.comment.model.Comment;
import com.example.comment.repository.CommentRepository;
import com.example.comment.service.CommentService;
import com.example.common.dto.PageList;
import com.example.common.dto.PageRequest;
import com.example.common.dto.Result;
import com.example.common.dto.ResultType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public PageList<Comment> getCommentList(PageRequest pageRequest, long postSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageIndex() - 1,
                pageRequest.getPageSize()
        );

        Page<Comment> commentPage = commentRepository.findByPostNo(postSeq, pageable);

        long totalCount = commentPage.getTotalElements();
        List<Comment> commentList = commentPage.getContent();

        return new PageList<>(pageRequest.getPageSize(), (int) totalCount, commentList);
    }

    @Override
    public Comment getComment(long commentSeq) {
        Optional<Comment> optionalComment = commentRepository.findById(commentSeq);
        return optionalComment.orElse(null);
    }

    @Override
    @Transactional
    public Result deleteComment(Comment comment) {
        commentRepository.deleteById(comment.getCommentNo());
        return new Result(ResultType.OK);
    }

    @Override
    @Transactional
    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    @Transactional
    public Comment modifyComment(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public boolean isCommentExist(long postSeq) {
        return commentRepository.countByPostNo(postSeq) > 0;
    }
}
