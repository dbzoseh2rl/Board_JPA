package com.example.global.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)

public abstract class AuditTimestamp {

    // Entity가 생성되어 DB에 저장될 때 시간이 자동으로 저장
    @CreatedDate
    @Column(name = "created_date", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    // 조회한 Entity의 값을 변경할 때 시간이 자동으로 저장
    @LastModifiedDate
    @Column(name = "updated_date", nullable = false)
    private LocalDateTime updatedDate;

    // Entity를 생성한 User의 ID
    @CreatedBy
    @Column(name = "created_id", updatable = false)
    private Long createdId;

    // Entity를 수정한 User ID
    @LastModifiedBy
    @Column(name = "updated_id")
    private Long updatedId;

}