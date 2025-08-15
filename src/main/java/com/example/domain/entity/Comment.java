package com.example.domain.entity;

import com.example.domain.dto.content.request.CommentRequest;
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

    public static Comment from(Member member, Post post, CommentRequest body) {
        return Comment.builder()
                .member(member)
                .post(post)
                .userId(member.getUserId())
                .content(body.content())
                .build();
    }

    // 비즈니스 로직 메서드들
    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public boolean isWrittenBy(String userId) {
        return this.userId.equals(userId);
    }

    public void incrementReplyCount() {
        if (this.post != null) {
            this.post.setReplyCnt(this.post.getReplyCnt() + 1);
        }
    }

    public void decrementReplyCount() {
        if (this.post != null) {
            this.post.setReplyCnt(Math.max(0, this.post.getReplyCnt() - 1));
        }
    }
}
