package com.invoice.management.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.invoice.management.validation.ValidDueDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing an invoice.
 * This class includes validation annotations to ensure data integrity.
 */
@Data
@ValidDueDate
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    /**
     * The unique identifier of the invoice.
     */
    @JsonProperty("_id")
    private Long id;

    /**
     * The invoice number.
     * Must be a positive integer.
     */
    @NotNull(message = "Invoice number is required")
    @Positive(message = "Invoice number must be positive")
    private Integer invoiceNumber;

    /**
     * The date the invoice was issued.
     * Must be in the past or present.
     */
    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issued;

    /**
     * The due date of the invoice.
     * Must be in the future.
     */
    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    /**
     * The product or service description.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Product is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String product;

    /**
     * The price of the product or service.
     * Must be zero or positive.
     */
    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Long price;

    /**
     * The VAT percentage.
     * Must be between 0 and 100.
     */
    @NotNull(message = "VAT is required")
    @Min(value = 0, message = "VAT must be at least 0")
    @Max(value = 100, message = "VAT cannot exceed 100")
    private Integer vat;

    /**
     * Additional notes for the invoice.
     * Cannot exceed 1000 characters.
     */
    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;

    /**
     * The buyer associated with this invoice.
     */
    @NotNull(message = "Buyer is required")
    @Valid
    private PersonDTO buyer;

    /**
     * The seller associated with this invoice.
     */
    @NotNull(message = "Seller is required")
    @Valid
    private PersonDTO seller;
}
