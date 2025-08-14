package com.example.controller;

import com.example.model.Member;
import com.example.service.authservice.service.AuthService;
import com.example.model.Board;
import com.example.service.boardservice.service.BoardService;
import com.example.dto.CommentBody;
import com.example.dto.PathVariableSequenceDto;
import com.example.model.Comment;
import com.example.service.commentservice.service.CommentService;
import com.example.dto.PageList;
import com.example.dto.PageRequest;
import com.example.dto.Result;
import com.example.global.common.exception.DataNotFoundException;
import com.example.global.common.exception.InvalidParameterException;
import com.example.global.common.exception.NotAllowedOperationException;
import com.example.global.common.exception.UserNotFoundException;
import com.example.dto.PostBody;
import com.example.model.Post;
import com.example.service.postserivce.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/v1/boards/{boardSeq}/posts")
@RequiredArgsConstructor
public class PostController {
    final Logger logger = LoggerFactory.getLogger(PostController.class);

    private final AuthService authService;
    private final BoardService boardService;
    private final PostService postService;
    private final CommentService commentService;

    /**
     * 포스트 목록 조회
     *
     * @author hjkim
     * @param boardSeq, pageIndex(nullable), pageSize(nullable),
     * @return PostList-totalCount, totalPage, list
     */
    @GetMapping(value="")
    public PageList<Post> getPostList(@PathVariable long boardSeq,
                                      PageRequest pageRequest) {
        logger.info("getPostList ::: {} {}", boardSeq, pageRequest);

        checkExistence(boardSeq);

        return postService.getPostList(pageRequest, boardSeq);
    }


    /**
     * 포스트 조회
     *
     * @author hjkim
     * @param boardSeq, postSeq
     * @return Post
     */
    @GetMapping(value="/{postSeq}")
    public Post getPost(@RequestAttribute long userSeq,
                        @PathVariable long boardSeq,
                        @PathVariable long postSeq) {
        logger.info("getPost ::: {} {} {}",
                userSeq, boardSeq, postSeq);

        checkExistence(boardSeq);

        Post post = getValidatedPost(userSeq, boardSeq, postSeq);

        return postService.getPostAndIncreaseViewCount(post.getId());
    }


    /**
     * 포스트 삭제
     *
     * @author hjkim
     * @param boardSeq, postSeq
     * @return ResultType
     */
    @DeleteMapping(value="/{postSeq}")
    public Result deletePost(@RequestAttribute long userSeq,
                             @PathVariable long boardSeq,
                             @PathVariable long postSeq) {
        logger.info("deletePost ::: {} {} {}",
                userSeq, boardSeq, postSeq);

        checkExistence(boardSeq);

        Post post = getValidatedPost(userSeq, boardSeq, postSeq);

        if (commentService.isCommentExist(postSeq)) {
            throw new NotAllowedOperationException();
        }

        return postService.deletePost(post);
    }


    /**
     * 포스트 등록
     *
     * @author hjkim
     * @param boardSeq, title, content
     * @return Post
     */
    @PostMapping(value="")
    public Post createPost(@RequestAttribute long userSeq,
                           @PathVariable long boardSeq,
                           @RequestBody @Valid PostBody body) {
        logger.info("createPost ::: {} {} {}",
                userSeq, boardSeq, body);

        checkExistence(boardSeq);

        Member member = authService.get(userSeq).orElseThrow(UserNotFoundException::new);
        Board board = boardService.get(boardSeq).orElseThrow(UserNotFoundException::new);

        Post post = Post.builder()
                .member(member)
                .board(board)
                .build();

        post.setTitle(body.getTitle());
        post.setContent(body.getContent());

        return postService.createPost(post);
    }


    /**
     * 포스트 수정
     *
     * @author hjkim
     * @param boardSeq, postSeq, title, content
     * @return Post
     */
    @PutMapping(value="/{postSeq}")
    public Post modifyPost(@RequestAttribute long userSeq,
                           @PathVariable long boardSeq,
                           @PathVariable long postSeq,
                           @RequestBody @Valid PostBody body) {
        logger.info("modifyPost ::: {} {} {} {}",
                userSeq, boardSeq, postSeq, body);

        checkExistence(boardSeq);

        Post post = getValidatedPost(userSeq, boardSeq, postSeq);

        post.setTitle(body.getTitle());
        post.setContent(body.getContent());

        return postService.modifyPost(post);
    }


    private void checkExistence(long boardSeq) {
        boardService.validateBoardSeq(boardSeq);
    }


    private Post getValidatedPost(long userSeq, long boardSeq, long postSeq) {
        Post post = postService.getPost(postSeq);

        if (post == null) {
            throw new DataNotFoundException();
        }

        checkEquality(post, userSeq, boardSeq);
        return post;
    }


    private void checkEquality(Post post, long userSeq, long boardSeq) {
        if (post.getBoard().getId() != boardSeq) {
            throw new InvalidParameterException();
        }

        if (post.getMember().getId() != userSeq) {
            throw new InvalidParameterException();
        }
    }

    @RestController
    @RequestMapping(value="/v1/boards/{boardSeq}/posts/{postSeq}/comments")
    @RequiredArgsConstructor
    public static class CommentController {
        final Logger logger = LoggerFactory.getLogger(CommentController.class);

        private final BoardService boardService;
        private final PostService postService;
        private final CommentService commentService;

        /**
         * 댓글 목록 조회
         *
         * @author hjkim
         * @param boardSeq, postSeq, pageIndex(nullable), pageSize(nullable)
         * @return CommentList-totalCount, totalPage, list
         */
        @GetMapping(value="")
        public PageList<Comment> getCommentList(@PathVariable long boardSeq,
                                                @PathVariable long postSeq,
                                                PageRequest pageRequest) {
            logger.info("getCommentList ::: {} {} {}",
                    boardSeq, postSeq, pageRequest);

            checkExistence(boardSeq, postSeq);

            return commentService.getCommentList(pageRequest, postSeq);
        }


        /**
         * 댓글 삭제
         *
         * @author hjkim
         * @param boardSeq, postSeq, commentSeq
         * @return ResultType
         */
        @DeleteMapping(value="/{commentSeq}")
        public Result deleteComment(@RequestAttribute long userSeq,
                                    @PathVariable long boardSeq,
                                    @PathVariable long postSeq,
                                    @PathVariable long commentSeq) {
            logger.info("deleteComment ::: {} {} {} {}",
                    userSeq, boardSeq, postSeq, commentSeq);

            checkExistence(boardSeq, postSeq);

            Comment comment = getValidatedComment(userSeq, postSeq, commentSeq);

            return commentService.deleteComment(comment);
        }


        /**
         * 댓글 등록
         *
         * @author hjkim
         * @param boardSeq, postSeq, content
         * @return Post
         */
        @PostMapping(value="")
        public Comment createComment(@RequestAttribute long userSeq,
                                     @PathVariable long boardSeq,
                                     @PathVariable long postSeq,
                                     @RequestBody @Valid CommentBody body) {
            logger.info("createComment ::: {} {} {} {}",
                    userSeq, boardSeq, postSeq, body);

            checkExistence(boardSeq, postSeq);

            Comment comment = Comment.builder()
                    .memberNo(userSeq)
                    .postNo(postSeq)
                    .build();

            comment.setContent(body.getContent());

            postService.updateReplyCount(postSeq);
            return commentService.createComment(comment);
        }


        /**
         * 댓글 수정
         *
         * @author hjkim
         * @param boardSeq, postSeq, commentSeq, content
         * @return Post
         */
        @PutMapping(value="/{commentSeq}")
        public Comment modifyComment(@RequestAttribute long userSeq,
                                     @PathVariable long boardSeq,
                                     @PathVariable long postSeq,
                                     @PathVariable long commentSeq,
                                     @RequestBody @Valid CommentBody body) {
            logger.info("modifyComment ::: {} {} {} {} {}",
                    userSeq, boardSeq, postSeq, commentSeq, body);

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
            if (comment.getMemberNo() != userSeq) {
                throw new InvalidParameterException();
            }

            if (comment.getPostNo() != postSeq) {
                throw new InvalidParameterException();
            }
        }

        private void checkEquality(Comment comment, PathVariableSequenceDto pathVariableSequenceDto) {
            if (comment.getMemberNo() != pathVariableSequenceDto.getUserSeq()) {
                throw new InvalidParameterException();
            }

            if (comment.getPostNo() != pathVariableSequenceDto.getPostSeq()) {
                throw new InvalidParameterException();
            }
        }
    }
}
