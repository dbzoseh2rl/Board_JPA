package com.example.model;

import com.example.global.common.model.Timestamp;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "comment") // 테이블명 맞게 수정
public class Comment extends Timestamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB 자동증가 전략
    @Column(name = "comment_no")
    private Long commentNo;

    @Column(name = "post_no", nullable = false)
    private Long postNo;

    @Column(name = "member_no", nullable = false)
    private Long memberNo;

    @Column(name = "user_id")
    private String userId;

    @Setter
    @Column(nullable = false)
    private String content;

    @Builder
    public Comment(Long postNo, Long memberNo, String userId, String content) {
        this.postNo = postNo;
        this.memberNo = memberNo;
        this.userId = userId;
        this.content = content;
    }
}
