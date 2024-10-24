package com.invoice.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Data Transfer Object (DTO) representing a paginated response.
 * This class is used to encapsulate paginated data along with pagination metadata.
 *
 * @param <T> The type of items contained in the paginated response
 */
@Data
@AllArgsConstructor
public class PaginatedResponse<T> {
    /**
     * The list of items for the current page.
     */
    private List<T> items;

    /**
     * The current page number.
     */
    private int currentPage;

    /**
     * The total number of pages.
     */
    private int totalPages;

    /**
     * The total number of items across all pages.
     */
    private int totalItems;
}
