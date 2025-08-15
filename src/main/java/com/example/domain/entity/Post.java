package com.example.domain.entity;

import com.example.domain.dto.content.request.PostRequest;
import com.example.global.common.model.Timestamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
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

    public static Post from(Member member, Board board, PostRequest postRequest) {
        return Post.builder()
                .member(member)
                .board(board)
                .userId(member.getUserId()) // Member의 userId 필드 사용
                .name(board.getName()) // Board의 name 필드 사용
                .title(postRequest.title()) // record의 getter 메서드 사용
                .content(postRequest.content()) // record의 getter 메서드 사용
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
        return this.userId.equals(userId);
    }

    public boolean hasReplies() {
        return this.replyCnt > 0;
    }

    public boolean isPopular() {
        return this.viewCnt > 100; // 조회수 100 이상을 인기 글로 간주
    }
}
