package cz.itnetwork.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) for Invoice.
 * This class represents the invoice data that is transferred between the client and the server.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO {

    @JsonProperty("_id")
    private Long id;

    @NotNull
    private Integer invoiceNumber;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issued;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;

    @NotNull
    private String product;

    @NotNull
    private Long price;

    @NotNull
    private Integer vat;

    private String note;

    @NotNull
    private PersonDTO buyer;

    @NotNull
    private PersonDTO seller;
}