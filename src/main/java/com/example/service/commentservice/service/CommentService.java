package com.example.service.commentservice.service;

import com.example.model.Comment;
import com.example.dto.PageList;
import com.example.dto.PageRequest;
import com.example.dto.Result;

public interface CommentService {
    PageList<Comment> getCommentList(PageRequest pageRequest, long postSeq);

    Comment getComment(long commentSeq);

    Result deleteComment(Comment comment);

    Comment createComment(Comment comment);

    Comment modifyComment(Comment comment);

    boolean isCommentExist(long postSeq);
}
