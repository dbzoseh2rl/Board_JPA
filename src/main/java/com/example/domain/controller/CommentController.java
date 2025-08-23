package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.request.PathVariableIdDto;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.content.request.CommentRequest;
import com.example.domain.entity.Comment;
import com.example.domain.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public Comment createComment(PathVariableIdDto idDto, @RequestBody @Valid CommentRequest commentRequest) {
        return commentService.create(
                idDto.userId(),
                idDto.boardId(),
                idDto.postId(),
                commentRequest
        );
    }


    @GetMapping("/{commentId}")
    public Comment getComment(PathVariableIdDto idDto) {
        return commentService.get(idDto.commentId());
    }

    @GetMapping
    public PageResponse<Comment> getComments(PathVariableIdDto idDto, PageRequest pageRequest) {
        return commentService.getComments(idDto.postId(), pageRequest);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(PathVariableIdDto idDto, @RequestBody @Valid CommentRequest body) {
        return commentService.update(
                idDto.userId(),
                idDto.boardId(),
                idDto.postId(),
                idDto.commentId(),
                body
        );
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse deleteComment(PathVariableIdDto idDto) {
        return commentService.delete(
                idDto.userId(),
                idDto.boardId(),
                idDto.postId(),
                idDto.commentId()
        );
    }

}