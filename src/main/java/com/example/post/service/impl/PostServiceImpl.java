package com.example.post.service.impl;

import com.example.common.dto.PageList;
import com.example.common.dto.PageRequest;
import com.example.common.dto.Result;
import com.example.common.dto.ResultType;
import com.example.common.exception.DataNotFoundException;
import com.example.post.model.Post;
import com.example.post.repository.PostRepository;
import com.example.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Override
    public PageList<Post> getPostList(PageRequest pageRequest, long boardSeq) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageIndex() - 1,
                pageRequest.getPageSize()
        );

        Page<Post> postPage = postRepository.findByBoardNo(boardSeq, pageable);

        int totalCount = (int) postPage.getTotalElements();
        List<Post> postList = postPage.getContent();

        return new PageList<>(pageRequest.getPageSize(), totalCount, postList);
    }

    @Override
    public Post getPost(long postSeq) {
        return postRepository.findById(postSeq)
                .orElseThrow(DataNotFoundException::new);
    }

    @Override
    @Transactional
    public Post getPostAndIncreaseViewCount(long postSeq) {
        Post post = getPost(postSeq);
        post.setViewCnt(post.getViewCnt() + 1);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post updateReplyCount(long postSeq) {
        Post post = getPost(postSeq);
        post.setReplyCnt(post.getReplyCnt() + 1);
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Result deletePost(Post post) {
        postRepository.deleteById(post.getPostNo());
        return new Result(ResultType.OK);
    }

    @Override
    @Transactional
    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    public Post modifyPost(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void validatePostSeq(long postSeq) {
        if (!postRepository.existsById(postSeq)) {
            throw new DataNotFoundException();
        }
    }
}
