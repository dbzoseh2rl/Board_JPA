package com.example.domain.dto.common.response;

import java.util.List;

public record PageResponse<T>(
        int totalCount,
        int totalPage,
        List<T> list
) {
    // 정적 팩토리 메서드로 페이지 계산 로직 처리
    public static <T> PageResponse<T> of(int pageSize, int totalCount, List<T> list) {
        int totalPage = (totalCount + pageSize - 1) / pageSize; // 올림 계산
        return new PageResponse<>(totalCount, totalPage, list);
    }
}