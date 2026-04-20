package com.example.taskapi.common.pagination;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Setter
@Getter
public class PageResponse<TItem> {
    private List<TItem> content;
    private int page;
    private int size;
    private int totalPages;
    private Long totalElements;
    private boolean last;

    public PageResponse(
            List<TItem> content,
            int page,
            int size,
            int totalPages,
            Long totalElements,
            boolean last
    ) {

        this.content = content;
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.last = last;


    }

    public static <TItem> PageResponse<TItem> from(Page<TItem> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isLast()
        );
    }

}
