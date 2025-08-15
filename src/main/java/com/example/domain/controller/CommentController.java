package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
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
    public Comment createComment(@RequestAttribute Long userId, @PathVariable Long boardId, @PathVariable Long postId, @RequestBody @Valid CommentRequest commentRequest) {
        return commentService.create(userId, boardId, postId, commentRequest);
    }

    @GetMapping("/{commentId}")
    public Comment getComment(@PathVariable Long commentId) {
        return commentService.get(commentId);
    }

    @GetMapping
    public PageResponse<Comment> getComments(@PathVariable Long postId, PageRequest pageRequest) {
        return commentService.getComments(postId, pageRequest);
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(@RequestAttribute Long userId, @PathVariable Long boardId, @PathVariable Long postId, @PathVariable Long commentId, @RequestBody @Valid CommentRequest body) {
        return commentService.update(userId, boardId, postId, commentId, body);
    }

    @DeleteMapping("/{commentId}")
    public ApiResponse deleteComment(@RequestAttribute Long userId, @PathVariable Long boardId, @PathVariable Long postId, @PathVariable Long commentId) {
        return commentService.delete(userId, boardId, postId, commentId);
    }

}