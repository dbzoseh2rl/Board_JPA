package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.content.request.PostRequest;
import com.example.domain.entity.Post;
import com.example.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardSeq}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public PageResponse<Post> getPostList(@PathVariable long boardSeq, PageRequest pageRequest) {
        return postService.getPostList(pageRequest, boardSeq);
    }

    @GetMapping("/{postSeq}")
    public Post getPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        return postService.getPostAndIncreaseViewCount(userSeq, boardSeq, postSeq);
    }

    @DeleteMapping("/{postSeq}")
    public ApiResponse deletePost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        return postService.deletePost(postSeq, userSeq);
    }

    @PostMapping
    public Post createPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @RequestBody @Valid PostRequest postRequest) {
        return postService.createPost(userSeq, boardSeq, postRequest);
    }

    @PutMapping("/{postSeq}")
    public Post modifyPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @RequestBody @Valid PostRequest body) {
        return postService.updatePost(postSeq, userSeq, body);
    }

}


