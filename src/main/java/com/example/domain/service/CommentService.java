package com.example.domain.service;

import com.example.domain.dto.common.ResultType;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.content.request.CommentRequest;
import com.example.domain.entity.Comment;
import com.example.domain.entity.Post;
import com.example.domain.entity.User;
import com.example.domain.repository.CommentRepository;
import com.example.global.exception.DataNotFoundException;
import com.example.global.exception.NoAuthorityException;
import com.example.global.util.PageableUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final UserService userService;
    private final BoardService boardService;
    private final PostService postService;

    private final CommentRepository commentRepository;

    @Transactional
    public Comment create(Long userId, Long boardId, Long postId, CommentRequest commentRequest) {
        checkExistence(boardId, postId);

        // 데이터 조회
        User user = userService.get(userId);
        Post post = postService.get(postId);

        // Entity의 정적 팩토리 메서드 사용
        Comment comment = Comment.from(user, post, commentRequest);

        // Entity의 비즈니스 로직 호출
        comment.incrementReplyCount();

        return commentRepository.save(comment);
    }

    public Comment get(long id) {
        return commentRepository.findById(id).orElseThrow(DataNotFoundException::new);
    }

    public PageResponse<Comment> getComments(long postId, PageRequest pageRequest) {
/*
        // 1. Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.pageIndex() - 1,
                pageRequest.pageSize()
        );

        // 2. return 된 Page<Entity>
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        long totalCount = commentPage.getTotalElements();
        // 3. 깐다
        List<Comment> comments = commentPage.getContent();

        return PageResponse.of(pageRequest.pageSize(), (int) totalCount, comments);
*/
        // 공통 유틸리티 사용
        Pageable pageable = PageableUtil.toPageable(pageRequest);
        Page<Comment> commentPage = commentRepository.findByPostId(postId, pageable);

        // 공통 응답 변환
        return PageableUtil.toPageResponse(commentPage, pageRequest);
    }

    @Transactional
    public Comment update(Long userId, Long boardId, Long postId, Long commentId, CommentRequest request) {
        checkExistence(boardId, postId);

        User user = userService.get(userId);
        Comment comment = get(commentId);

        // Entity의 비즈니스 로직 사용
        if (!comment.isWrittenBy(user.getEmail())) {
            throw new NoAuthorityException();
        }

        comment.updateContent(request.content());
        return commentRepository.save(comment);
    }

    @Transactional
    public ApiResponse delete(Long userId, Long boardId, Long postId, Long commentId) {
        checkExistence(boardId, postId);

        User user = userService.get(userId);
        Comment comment = get(commentId);

        // Entity의 비즈니스 로직 사용
        if (!comment.isWrittenBy(user.getEmail())) {
            throw new NoAuthorityException();
        }

        comment.decrementReplyCount();
        commentRepository.deleteById(commentId);

        return new ApiResponse(ResultType.OK);
    }

    public boolean isCommentExist(long postId) {
        return commentRepository.countByPostId(postId) > 0;
    }

    private void checkExistence(long boardSeq, long postId) {
        boardService.validateBoardSeq(boardSeq);
        postService.validatePostId(postId);
    }

    private String getUserId(long userSeq) {
        User user = userService.get(userSeq);
        return user.getEmail();
    }

}
