package com.example.domain.service;

import com.example.domain.dto.*;
import com.example.domain.entity.Post;
import com.example.domain.repository.PostRepository;
import com.example.global.common.exception.DataNotFoundException;
import com.example.global.common.exception.InvalidParameterException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PageList<Post> getPostList(PageRequest pageRequest, long boardSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageIndex() - 1,
                pageRequest.getPageSize()
        );

        Page<Post> postPage = postRepository.findByBoardId(boardSeq, pageable);

        int totalCount = (int) postPage.getTotalElements();
        List<Post> postList = postPage.getContent();

        return new PageList<>(pageRequest.getPageSize(), totalCount, postList);
    }

    public Post get(long postSeq) {
        return postRepository.findById(postSeq).orElseThrow(DataNotFoundException::new);
    }

    @Transactional
    public Post getPostAndIncreaseViewCount(long userSeq, long boardSeq, long postSeq) {
        Post post = getValidatedPost(userSeq, boardSeq, postSeq);
        post.setViewCnt(post.getViewCnt() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Post updateReplyCount(long postSeq) {
        Post post = get(postSeq);
        post.setReplyCnt(post.getReplyCnt() + 1);
        return postRepository.save(post);
    }

    @Transactional
    public Result deletePost(Post post) {
        postRepository.deleteById(post.getId());
        return new Result(ResultType.OK);
    }

    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }


    @Transactional
    public Post modifyPost(long userSeq, long boardSeq, long postSeq, PostBody body) {
        Post post = getValidatedPost(userSeq, boardSeq, postSeq);
        post.setTitle(body.getTitle());
        post.setContent(body.getContent());
        return postRepository.save(post);
    }

    public void validatePostSeq(long postSeq) {
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

        if (post.getMember().getId() != userSeq) {
            throw new InvalidParameterException();
        }
    }

}
