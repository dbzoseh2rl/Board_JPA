package com.example.domain.controller;

import com.example.domain.dto.*;
import com.example.domain.entity.Comment;
import com.example.domain.entity.Member;
import com.example.domain.entity.Post;
import com.example.domain.service.AuthService;
import com.example.domain.service.BoardService;
import com.example.domain.service.CommentService;
import com.example.domain.service.PostService;
import com.example.global.common.exception.DataNotFoundException;
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
    public PageList<Comment> getCommentList(@PathVariable long boardSeq, @PathVariable long postSeq, PageRequest pageRequest) {
        checkExistence(boardSeq, postSeq);
        return commentService.getCommentList(pageRequest, postSeq);
    }

    @DeleteMapping(value = "/{commentSeq}")
    public Result deleteComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @PathVariable long commentSeq) {
        checkExistence(boardSeq, postSeq);
        Comment comment = getValidatedComment(userSeq, postSeq, commentSeq);
        return commentService.deleteComment(comment);
    }

    @PostMapping(value = "")
    public Comment createComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @RequestBody @Valid CommentBody body) {
        Member member = authService.get(userSeq);
        Post post = postService.get(postSeq);

        checkExistence(boardSeq, postSeq);
        postService.updateReplyCount(postSeq);
        return commentService.createComment(Comment.from(member, post, body));
    }

    @PutMapping(value = "/{commentSeq}")
    public Comment modifyComment(@RequestAttribute long userSeq, @PathVariable long boardSeq, @PathVariable long postSeq, @PathVariable long commentSeq, @RequestBody @Valid CommentBody body) {
        PathVariableSequenceDto pathVariableSequenceDto = PathVariableSequenceDto.builder()
                .userSeq(userSeq)
                .boardSeq(boardSeq)
                .postSeq(postSeq)
                .commentSeq(commentSeq)
                .build();

        checkExistence(pathVariableSequenceDto);

        Comment comment = getValidatedComment(pathVariableSequenceDto);

        comment.setContent(body.getContent());

        return commentService.modifyComment(comment);
    }

    private void checkExistence(long boardSeq, long postSeq) {
        boardService.validateBoardSeq(boardSeq);
        postService.validatePostSeq(postSeq);
    }

    private void checkExistence(PathVariableSequenceDto pathVariableSequenceDto) {
        boardService.validateBoardSeq(pathVariableSequenceDto.getBoardSeq());
        postService.validatePostSeq(pathVariableSequenceDto.getPostSeq());
    }

    private Comment getValidatedComment(long userSeq, long postSeq, long commentSeq) {
        Comment comment = commentService.getComment(commentSeq);

        if (comment == null) {
            throw new DataNotFoundException();
        }

        checkEquality(comment, userSeq, postSeq);
        return comment;
    }

    private Comment getValidatedComment(PathVariableSequenceDto pathVariableSequenceDto) {
        Comment comment = commentService.getComment(pathVariableSequenceDto.getCommentSeq());

        if (comment == null) {
            throw new DataNotFoundException();
        }

        checkEquality(comment, pathVariableSequenceDto);
        return comment;
    }

    private void checkEquality(Comment comment, long userSeq, long postSeq) {
//            if (comment.getMember() != userSeq) {
//                throw new InvalidParameterException();
//            }
//
//            if (comment.getPostNo() != postSeq) {
//                throw new InvalidParameterException();
//            }
    }

    private void checkEquality(Comment comment, PathVariableSequenceDto pathVariableSequenceDto) {
//            if (comment.getMember() != pathVariableSequenceDto.getUserSeq()) {
//                throw new InvalidParameterException();
//            }
//
//            if (comment.getPostNo() != pathVariableSequenceDto.getPostSeq()) {
//                throw new InvalidParameterException();
//            }
    }

}