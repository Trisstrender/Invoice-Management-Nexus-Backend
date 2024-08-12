package cz.itnetwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import cz.itnetwork.validation.ValidDueDate;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@ValidDueDate
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    @JsonProperty("_id")
    private Long id;

    @NotNull(message = "Invoice number is required")
    @Positive(message = "Invoice number must be positive")
    private Integer invoiceNumber;

    @NotNull(message = "Issue date is required")
    @PastOrPresent(message = "Issue date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issued;

    @NotNull(message = "Due date is required")
    @Future(message = "Due date must be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotBlank(message = "Product is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String product;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    private Long price;

    @NotNull(message = "VAT is required")
    @Min(value = 0, message = "VAT must be at least 0")
    @Max(value = 100, message = "VAT cannot exceed 100")
    private Integer vat;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;

    @NotNull(message = "Buyer is required")
    @Valid
    private PersonDTO buyer;

    @NotNull(message = "Seller is required")
    @Valid
    private PersonDTO seller;
}