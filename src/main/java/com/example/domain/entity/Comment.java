package com.example.domain.entity;

import com.example.domain.dto.content.request.CommentRequest;
import com.example.global.model.Timestamp;
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
    private User user;

    @Setter
    @Column(nullable = false)
    private String content;

    @Builder
    private Comment(Post post, User user, String userId, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }

    public static Comment from(User user, Post post, CommentRequest body) {
        return Comment.builder()
                .user(user)
                .post(post)
                .userId(user.getEmail())
                .content(body.content())
                .build();
    }

    // 비즈니스 로직 메서드들
    public void updateContent(String newContent) {
        this.content = newContent;
    }

    public boolean isWrittenBy(String email) {
        return this.user.getEmail().equals(email);
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
