package com.example.domain.entity;

import com.example.domain.dto.CommentBody;
import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn
    private Post post;

    @ManyToOne
    @JoinColumn
    private Member member;

    @Column(name = "user_id")
    private String userId;

    @Setter
    @Column(nullable = false)
    private String content;

    @Builder
    private Comment(Post post, Member member, String userId, String content) {
        this.post = post;
        this.member = member;
        this.userId = userId;
        this.content = content;
    }

    public static Comment from(Member member, Post post, CommentBody body) {
        return Comment.builder()
                .member(null)
                .post(null)
                .content(body.getContent())
                .build();
    }

}
