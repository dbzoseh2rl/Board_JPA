package com.example.post.model;

import com.example.auth.model.Member;
import com.example.board.model.Board;
import com.example.common.model.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Post extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private Board board;

    private String userId;

    private String name;

    @Setter
    private String title;

    @Setter
    private String content;

    @Setter
    private int viewCnt;

    @Setter
    private int replyCnt;

    @Builder
    public Post(Board board, Member member) {
        this.board = board;
        this.member = member;
        this.viewCnt = 0;
        this.replyCnt = 0;
    }

}
