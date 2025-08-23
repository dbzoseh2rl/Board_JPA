package com.example.domain.controller;

import com.example.domain.dto.common.ResultType;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.request.PathVariableIdDto;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.content.request.PostRequest;
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
    public ApiResponse getPostList(PathVariableIdDto pathDto, PageRequest pageRequest) {
        return ApiResponse.of(postService.getPostList(pageRequest, pathDto.boardId()));
    }

    @GetMapping("/{postId}")
    public ApiResponse getPost(PathVariableIdDto pathDto) {
        return ApiResponse.of(postService.getPostAndIncreaseViewCount(pathDto.userId(), pathDto.boardId(), pathDto.postId()));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse deletePost(PathVariableIdDto pathDto) {
        postService.deletePost(pathDto.postId(), pathDto.userId());
        return ApiResponse.of(ResultType.OK);
    }

    @PostMapping
    public ApiResponse createPost(PathVariableIdDto pathDto, @RequestBody @Valid PostRequest postRequest) {
        return ApiResponse.of(postService.createPost(pathDto.userId(), pathDto.boardId(), postRequest));
    }

    @PutMapping("/{postId}")
    public ApiResponse modifyPost(PathVariableIdDto pathDto, @RequestBody @Valid PostRequest body) {
        return ApiResponse.of(postService.updatePost(pathDto.postId(), pathDto.userId(), body));
    }

}
