package com.example.domain.entity;

import com.example.domain.dto.content.request.PostRequest;
import com.example.global.model.Timestamp;
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
    private User user;

    @ManyToOne
    @JoinColumn
    private Board board;

    private String name;

    private String title;

    private String content;

    private int viewCnt;

    private int replyCnt;

    @Builder
    private Post(long id, User user, Board board, String name, String title, String content, int viewCnt, int replyCnt) {
        this.id = id;
        this.user = user;
        this.board = board;
        this.name = name;
        this.title = title;
        this.content = content;
        this.viewCnt = viewCnt;
        this.replyCnt = replyCnt;
    }

    public static Post from(User user, Board board, PostRequest postRequest) {
        return Post.builder()
                .user(user)
                .board(board)
                .name(board.getName())
                .title(postRequest.title())
                .content(postRequest.content())
                .viewCnt(0)
                .replyCnt(0)
                .build();
    }

    // 추가 비즈니스 로직 메서드들
    public void updateContent(String newTitle, String newContent) {
        this.title = newTitle;
        this.content = newContent;
    }

    public void incrementViewCount() {
        this.viewCnt++;
    }

    public void incrementReplyCount() {
        this.replyCnt++;
    }

    public void decrementReplyCount() {
        this.replyCnt = Math.max(0, this.replyCnt - 1);
    }

    public boolean isWrittenBy(String userId) {
        return this.user.getEmail().equals(userId);
    }

    public boolean hasReplies() {
        return this.replyCnt > 0;
    }

    public boolean isPopular() {
        return this.viewCnt > 100; // 조회수 100 이상을 인기 글로 간주
    }

}
