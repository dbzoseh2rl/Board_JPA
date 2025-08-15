package com.example.domain.service;

import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.ResultType;
import com.example.domain.dto.content.request.CommentRequest;
import com.example.domain.entity.Comment;
import com.example.domain.entity.Member;
import com.example.domain.entity.Post;
import com.example.domain.repository.AuthRepository;
import com.example.domain.repository.CommentRepository;
import com.example.domain.repository.PostRepository;
import com.example.global.common.exception.DataNotFoundException;
import com.example.global.common.exception.NoAuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final AuthRepository memberRepository;
    private final PostRepository postRepository;

    public PageResponse<Comment> getCommentList(PageRequest pageRequest, long postSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.pageIndex() - 1,  // record getter 사용
                pageRequest.pageSize()
        );

        Page<Comment> commentPage = commentRepository.findByPostId(postSeq, pageable);

        long totalCount = commentPage.getTotalElements();
        List<Comment> commentList = commentPage.getContent();

        return PageResponse.of(pageRequest.pageSize(), (int) totalCount, commentList);
    }

    public Comment getComment(long commentSeq) {
        return commentRepository.findById(commentSeq)
                .orElseThrow(() -> new DataNotFoundException());
    }

    @Transactional
    public Comment createComment(Long memberId, Long postId, CommentRequest request) {
        // 데이터 조회
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new DataNotFoundException());
        
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new DataNotFoundException());
        
        // Entity의 정적 팩토리 메서드 사용
        Comment comment = Comment.from(member, post, request);
        
        // Entity의 비즈니스 로직 호출
        comment.incrementReplyCount();
        
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment updateComment(Long commentId, String userId, CommentRequest request) {
        Comment comment = getComment(commentId);
        
        // Entity의 비즈니스 로직 사용
        if (!comment.isWrittenBy(userId)) {
            throw new NoAuthorityException();
        }
        
        comment.updateContent(request.content());
        return commentRepository.save(comment);
    }

    @Transactional
    public ApiResponse deleteComment(Long commentId, String userId) {
        Comment comment = getComment(commentId);
        
        // Entity의 비즈니스 로직 사용
        if (!comment.isWrittenBy(userId)) {
            throw new NoAuthorityException();
        }
        
        comment.decrementReplyCount();
        commentRepository.deleteById(commentId);
        
        return new ApiResponse(ResultType.OK);
    }

    public boolean isCommentExist(long postSeq) {
        return commentRepository.countByPostId(postSeq) > 0;
    }
}
