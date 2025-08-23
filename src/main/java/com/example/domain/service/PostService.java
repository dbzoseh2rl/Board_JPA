package com.example.domain.service;

import com.example.domain.dto.common.ResultType;
import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.ApiResponse;
import com.example.domain.dto.common.response.PageResponse;
import com.example.domain.dto.content.request.PostRequest;
import com.example.domain.entity.Board;
import com.example.domain.entity.Post;
import com.example.domain.entity.User;
import com.example.domain.repository.PostRepository;
import com.example.global.exception.DataNotFoundException;
import com.example.global.exception.InvalidParameterException;
import com.example.global.exception.NoAuthorityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final UserService userService;
    private final BoardService boardService;

    private final PostRepository postRepository;

    public PageResponse<Post> getPostList(PageRequest pageRequest, long boardSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.pageIndex() - 1,  // record getter 사용
                pageRequest.pageSize()
        );

        Page<Post> postPage = postRepository.findByBoardId(boardSeq, pageable);

        int totalCount = (int) postPage.getTotalElements();
        List<Post> postList = postPage.getContent();

        return PageResponse.of(pageRequest.pageSize(), totalCount, postList);
    }

    public Post get(long postSeq) {
        return postRepository.findById(postSeq).orElseThrow(DataNotFoundException::new);
    }

    @Transactional
    public Post createPost(Long memberId, Long boardId, PostRequest request) {
        // 데이터 조회
        User user = userService.get(memberId);
        Board board = boardService.get(boardId);

        return postRepository.save(Post.from(user, board, request));
    }

    @Transactional
    public Post getPostAndIncreaseViewCount(long userSeq, long boardSeq, long postSeq) {
        Post post = getValidatedPost(userSeq, boardSeq, postSeq);

        // Entity의 비즈니스 로직 사용
        post.incrementViewCount();

        return postRepository.save(post);
    }

    @Transactional
    public Post updatePost(Long postId, Long userSeq, PostRequest request) {
        Post post = get(postId);
        String userId = userService.get(userSeq).getEmail();
        // Entity의 비즈니스 로직 사용
        if (!post.isWrittenBy(userId)) {
            throw new NoAuthorityException();
        }

        post.updateContent(request.title(), request.content());
        return postRepository.save(post);
    }

    @Transactional
    public ApiResponse deletePost(Long postId, Long userSeq) {
        Post post = get(postId);
        String userId = userService.get(userSeq).getEmail();
        // Entity의 비즈니스 로직 사용
        if (!post.isWrittenBy(userId)) {
            throw new NoAuthorityException();
        }

        postRepository.deleteById(postId);
        return new ApiResponse(ResultType.OK);
    }

    public void validatePostId(long postSeq) {
        if (!postRepository.existsById(postSeq)) {
            throw new DataNotFoundException();
        }
    }

    public Post getValidatedPost(long userSeq, long boardSeq, long postSeq) {
        Post post = get(postSeq);

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

        if (post.getUser().getId() != userSeq) {
            throw new InvalidParameterException();
        }
    }

}
