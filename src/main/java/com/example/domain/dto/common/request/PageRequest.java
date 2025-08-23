package com.example.domain.dto.common.request;

import org.springframework.data.domain.Pageable;

public record PageRequest(

        Integer pageIndex,
        Integer pageSize

) {

    private static final int DEFAULT_PAGE_INDEX = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public PageRequest {
        if (pageIndex == null) pageIndex = 1;
        if (pageSize == null) pageSize = 10;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getStartPage() {
        return (pageIndex - 1) * pageSize;
    }

    public static Pageable toPageable(PageRequest pageRequest) {
        int pageIndex = validatePageIndex(pageRequest.pageIndex());
        int pageSize = validatePageSize(pageRequest.pageSize());

        return org.springframework.data.domain.PageRequest.of(pageIndex - 1, pageSize);
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
