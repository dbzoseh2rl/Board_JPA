package com.example.domain.controller;

import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.content.request.CommentRequest;
import com.example.domain.entity.Comment;
import com.example.domain.entity.Member;
import com.example.domain.service.AuthService;
import com.example.domain.service.BoardService;
import com.example.domain.service.CommentService;
import com.example.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/boards/{boardSeq}/posts/{postSeq}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final AuthService authService;
    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping(value = "")
    public PageResponse<Comment> getCommentList(@PathVariable long boardSeq, @PathVariable long postSeq, PageRequest pageRequest) {
        checkExistence(boardSeq, postSeq);
        return commentService.getCommentList(pageRequest, postSeq);
    }

    @PostMapping(value = "")
    public Comment createComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @RequestBody @Valid CommentRequest body) {
        checkExistence(boardSeq, postSeq);
        
        // Service의 새로운 메서드 사용
        return commentService.createComment(userSeq, postSeq, body);
    }

    @PutMapping(value = "/{commentSeq}")
    public Comment updateComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @PathVariable long commentSeq, @RequestBody @Valid CommentRequest body) {
        checkExistence(boardSeq, postSeq);
        
        // Service의 새로운 메서드 사용
        return commentService.updateComment(commentSeq, getUserId(userSeq), body);
    }

    @DeleteMapping(value = "/{commentSeq}")
    public ApiResponse deleteComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @PathVariable long commentSeq) {
        checkExistence(boardSeq, postSeq);
        
        // Service의 새로운 메서드 사용
        return commentService.deleteComment(commentSeq, getUserId(userSeq));
    }

    private void checkExistence(long boardSeq, long postSeq) {
        boardService.validateBoardSeq(boardSeq);
        postService.validatePostSeq(postSeq);
    }

    private String getUserId(long userSeq) {
        Member member = authService.get(userSeq);
        return member.getUserId();
    }
}