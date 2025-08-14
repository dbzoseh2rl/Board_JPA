package com.example.domain.controller;

import com.example.domain.dto.PageList;
import com.example.domain.dto.PageRequest;
import com.example.domain.dto.PostBody;
import com.example.domain.dto.Result;
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
    public PageList<Post> getPostList(@PathVariable long boardSeq, PageRequest pageRequest) {
        return postService.getPostList(pageRequest, boardSeq);
    }

    @GetMapping(value = "/{postSeq}")
    public Post getPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        return postService.getPostAndIncreaseViewCount(userSeq, boardSeq, postSeq);
    }

    @DeleteMapping(value = "/{postSeq}")
    public Result deletePost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq) {
        Post post = postService.getValidatedPost(userSeq, boardSeq, postSeq);
        if (commentService.isCommentExist(postSeq)) {
            throw new NotAllowedOperationException();
        }
        return postService.deletePost(post);
    }

    @PostMapping(value = "")
    public Post createPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @RequestBody @Valid PostBody postBody) {
        Member member = authService.get(userSeq);
        Board board = boardService.get(boardSeq);
        return postService.createPost(Post.from(member, board, postBody));
    }

    @PutMapping(value = "/{postSeq}")
    public Post modifyPost(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @RequestBody @Valid PostBody body) {
        return postService.modifyPost(userSeq, boardSeq, postSeq, body);
    }

}
