package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.request.PathVariableIdDto;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.content.request.CommentRequest;
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
    public ApiResponse createComment(PathVariableIdDto idDto, @RequestBody @Valid CommentRequest commentRequest) {
        return ApiResponse.of(commentService.create(idDto, commentRequest));
    }

    @GetMapping("/{commentId}")
    public ApiResponse getComment(PathVariableIdDto idDto) {
        return ApiResponse.of(commentService.get(idDto.commentId()));
    }

    @GetMapping
    public ApiResponse getComments(PathVariableIdDto idDto, PageRequest pageRequest) {
        return ApiResponse.of(commentService.getComments(idDto.postId(), pageRequest));
    }

    @PutMapping("/{commentId}")
    public ApiResponse updateComment(PathVariableIdDto idDto, @RequestBody @Valid CommentRequest body) {
        return ApiResponse.of(commentService.update(idDto.userId(), idDto.boardId(), idDto.postId(), idDto.commentId(), body));
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse deleteComment(PathVariableIdDto idDto) {
        return ApiResponse.of(commentService.delete(idDto.userId(), idDto.boardId(), idDto.postId(), idDto.commentId()));
    }

}