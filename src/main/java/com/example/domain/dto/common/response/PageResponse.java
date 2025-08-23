package com.example.domain.dto.common.response;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(

        int totalCount,
        int totalPage,
        List<T> list

) {

    public static <T> PageResponse<T> of(int pageSize, Page<T> page) {
        int totalCount = (int) page.getTotalElements();
        int totalPage = (totalCount + pageSize - 1) / pageSize;
        return new PageResponse<>(totalCount, totalPage, page.getContent());
    }

}