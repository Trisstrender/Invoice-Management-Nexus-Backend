package com.invoice.management.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invoice.management.constant.Countries;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) representing a person (individual or company).
 * This class includes validation annotations to ensure data integrity.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    /**
     * The unique identifier of the person.
     */
    @JsonProperty("_id")
    private Long id;

    /**
     * The name of the person or company.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    /**
     * The identification number of the person or company.
     * Must be exactly 8 digits.
     */
    @NotBlank(message = "Identification number is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "Identification number must be 8 digits")
    private String identificationNumber;

    /**
     * The tax number of the person or company.
     * Must be in the format CC########, where CC is a two-letter country code.
     */
    @NotBlank(message = "Tax number is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{8,10}$", message = "Tax number must be in the format CC########")
    private String taxNumber;

    /**
     * The account number of the person or company.
     * Must be up to 17 digits.
     */
    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{1,17}$", message = "Account number must be up to 17 digits")
    private String accountNumber;

    /**
     * The bank code associated with the account.
     * Must be exactly 4 digits.
     */
    @NotBlank(message = "Bank code is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Bank code must be 4 digits")
    private String bankCode;

    /**
     * The International Bank Account Number (IBAN).
     * Must be in the correct IBAN format.
     */
    @NotBlank(message = "IBAN is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "IBAN must be in the correct format")
    private String iban;

    /**
     * The telephone number of the person or company.
     * Must be in a valid format, optionally starting with '+'.
     */
    @NotBlank(message = "Telephone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Telephone must be in a valid format")
    private String telephone;

    /**
     * The email address of the person or company.
     * Must be a valid email format.
     */
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String mail;

    /**
     * The street address of the person or company.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street cannot exceed 255 characters")
    private String street;

    /**
     * The ZIP code of the person or company.
     * Must be exactly 5 digits.
     */
    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^[0-9]{5}$", message = "ZIP code must be 5 digits")
    private String zip;

    /**
     * The city of the person or company.
     * Must not be blank and cannot exceed 255 characters.
     */
    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City cannot exceed 255 characters")
    private String city;

    /**
     * The country of the person or company.
     * Must be one of the predefined Countries enum values.
     */
    @NotNull(message = "Country is required")
    private Countries country;

    /**
     * Indicates whether the person or company is hidden.
     */
    private Boolean hidden;

    /**
     * Additional notes about the person or company.
     * Cannot exceed 1000 characters.
     */
    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;
}
