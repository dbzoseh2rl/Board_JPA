package com.example.global.util;

import com.example.domain.dto.common.request.PageRequest;
import com.example.domain.dto.common.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {
    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public static Pageable toPageable(PageRequest pageRequest) {
        int pageIndex = validatePageIndex(pageRequest.pageIndex());
        int pageSize = validatePageSize(pageRequest.pageSize());

        return org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
    }

    public static <T> PageResponse<T> toPageResponse(Page<T> page, PageRequest pageRequest) {
        return PageResponse.of(
                pageRequest.pageSize(),
                (int) page.getTotalElements(),
                page.getContent()
        );
    }

    private static int validatePageIndex(Integer pageIndex) {
        if (pageIndex == null || pageIndex < 1) {
            return DEFAULT_PAGE_INDEX;
        }
        return pageIndex;
    }

    private static int validatePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        if (pageSize > MAX_PAGE_SIZE) {
            return MAX_PAGE_SIZE;
        }
        return pageSize;
    }
}