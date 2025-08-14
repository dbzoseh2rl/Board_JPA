package com.example.domain.entity;

import com.example.domain.dto.PostBody;
import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @ManyToOne
    @JoinColumn
    private Board board;

    private String userId;

    private String name;

    private String title;

    private String content;

    private int viewCnt;

    private int replyCnt;

    @Builder
    public Post(long id, Member member, Board board, String userId, String name, String title, String content) {
        this.id = id;
        this.member = member;
        this.board = board;
        this.userId = userId;
        this.name = name;
        this.title = title;
        this.content = content;
        this.viewCnt = 0;
        this.replyCnt = 0;
    }

    public static Post from(Member member, Board board, PostBody postBody) {
        return Post.builder()
                .member(member)
                .board(board)
                .title(postBody.getTitle())
                .content(postBody.getContent())
                .build();
    }

}
