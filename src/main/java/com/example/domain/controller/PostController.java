package com.example.domain.controller;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.request.PathVariableIdDto;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.content.request.PostRequest;
import com.example.domain.entity.Post;
import com.example.domain.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boards/{boardId}/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    public PageResponse<Post> getPostList(PathVariableIdDto pathDto, PageRequest pageRequest) {
        return postService.getPostList(pageRequest, pathDto.boardId());
    }

    @GetMapping("/{postId}")
    public Post getPost(PathVariableIdDto pathDto) {
        return postService.getPostAndIncreaseViewCount(pathDto.userId(), pathDto.boardId(), pathDto.postId());
    }


    @DeleteMapping("/{postId}")
    public ApiResponse deletePost(PathVariableIdDto pathDto) {
        return postService.deletePost(pathDto.postId(), pathDto.userId());
    }


    @PostMapping
    public Post createPost(PathVariableIdDto pathDto, @RequestBody @Valid PostRequest postRequest) {
        return postService.createPost(pathDto.userId(), pathDto.boardId(), postRequest);
    }

    @PutMapping("/{postId}")
    public Post modifyPost(PathVariableIdDto pathDto, @RequestBody @Valid PostRequest body) {
        return postService.updatePost(pathDto.postId(), pathDto.userId(), body);
    }

}


