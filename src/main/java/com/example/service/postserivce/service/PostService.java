package com.example.service.postserivce.service;

import com.example.dto.PageList;
import com.example.dto.PageRequest;
import com.example.dto.Result;
import com.example.model.Post;

public interface PostService {
    PageList<Post> getPostList(PageRequest pageRequest, long boardSeq);

    Post getPost(long postSeq);

    Post getPostAndIncreaseViewCount(long postSeq);

    Post updateReplyCount(long postSeq);

    Result deletePost(Post post);

    Post createPost(Post post);

    Post modifyPost(Post post);

    void validatePostSeq(long postSeq);
}
