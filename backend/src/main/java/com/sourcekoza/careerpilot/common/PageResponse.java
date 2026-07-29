package com.sourcekoza.careerpilot.common;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Standard pagination response model.
 *
 * <p>Wraps paginated data with metadata for client consumption.</p>
 *
 * @param <T> the type of elements in the page
 */
public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean first,
        boolean last
) {

    /**
     * Creates a PageResponse from a Spring Data Page object.
     *
     * @param page the Spring Data Page
     * @param <T>  the type of elements
     * @return a PageResponse containing the page data and metadata
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isFirst(),
                page.isLast()
        );
    }
}
