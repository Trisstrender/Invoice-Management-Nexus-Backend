package com.invoice.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Data Transfer Object (DTO) representing an error response.
 * This class is used to encapsulate error information to be sent back to the client.
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
    /**
     * The HTTP status code of the error.
     */
    private int status;

    /**
     * The error message providing details about the error.
     */
    private String message;
}
