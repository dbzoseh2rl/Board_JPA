package com.example.domain.controller;

import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.content.request.PostRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.entity.Board;
import com.example.domain.entity.Member;
import com.example.domain.entity.Post;
import com.example.domain.service.AuthService;
import com.example.domain.service.BoardService;
import com.example.domain.service.CommentService;
import com.example.domain.service.PostService;
import com.example.global.common.exception.NotAllowedOperationException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/boards/{boardSeq}/posts")
@RequiredArgsConstructor
public class PostController {

    private final AuthService authService;
    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    @GetMapping(value = "")
    public PageResponse<Post> getPostList(@PathVariable long boardSeq, PageRequest pageRequest) {
        return postService.getPostList(pageRequest, boardSeq);
    }

    @GetMapping(value = "/{postSeq}")
    public Post getPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        return postService.getPostAndIncreaseViewCount(userSeq, boardSeq, postSeq);
    }

    @DeleteMapping(value = "/{postSeq}")
    public ApiResponse deletePost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        // Service의 새로운 메서드 사용
        return postService.deletePost(postSeq, getUserId(userSeq));
    }

    @PostMapping(value = "")
    public Post createPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @RequestBody @Valid PostRequest postRequest) {
        // Service의 새로운 메서드 사용
        return postService.createPost(userSeq, boardSeq, postRequest);
    }

    @PutMapping(value = "/{postSeq}")
    public Post modifyPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @RequestBody @Valid PostRequest body) {
        // Service의 새로운 메서드 사용
        return postService.updatePost(postSeq, getUserId(userSeq), body);
    }

    private String getUserId(long userSeq) {
        Member member = authService.get(userSeq);
        return member.getUserId();
    }
}
