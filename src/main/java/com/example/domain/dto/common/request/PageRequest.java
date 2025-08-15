package com.example.domain.dto.common.request;

public record PageRequest(
        Integer pageIndex,
        Integer pageSize
) {
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
}
