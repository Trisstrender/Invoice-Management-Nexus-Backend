package com.invoice.management.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Map;

/**
 * Utility class for creating Pageable objects for pagination.
 */
public class PaginationUtils {

    /**
     * Creates a Pageable object based on the provided parameters.
     *
     * @param params a map of pagination parameters including page, limit, and sort
     * @return a Pageable object configured with the provided parameters
     */
    public static Pageable createPageable(Map<String, String> params) {
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int limit = Integer.parseInt(params.getOrDefault("limit", "10"));
        String sortParam = params.getOrDefault("sort", "id,asc");
        String[] sortParams = sortParam.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDirection = sortParams.length > 1 && sortParams[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;

        return PageRequest.of(page - 1, limit, Sort.by(sortDirection, sortField));
    }
}
