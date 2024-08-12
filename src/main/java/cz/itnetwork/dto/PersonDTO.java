package cz.itnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.itnetwork.constant.Countries;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    @JsonProperty("_id")
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name cannot exceed 255 characters")
    private String name;

    @NotBlank(message = "Identification number is required")
    @Pattern(regexp = "^[0-9]{8}$", message = "Identification number must be 8 digits")
    private String identificationNumber;

    @NotBlank(message = "Tax number is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{8,10}$", message = "Tax number must be in the format CC########")
    private String taxNumber;

    @NotBlank(message = "Account number is required")
    @Pattern(regexp = "^[0-9]{1,17}$", message = "Account number must be up to 17 digits")
    private String accountNumber;

    @NotBlank(message = "Bank code is required")
    @Pattern(regexp = "^[0-9]{4}$", message = "Bank code must be 4 digits")
    private String bankCode;

    @NotBlank(message = "IBAN is required")
    @Pattern(regexp = "^[A-Z]{2}[0-9]{2}[A-Z0-9]{1,30}$", message = "IBAN must be in the correct format")
    private String iban;

    @NotBlank(message = "Telephone is required")
    @Pattern(regexp = "^\\+?[0-9]{9,15}$", message = "Telephone must be in a valid format")
    private String telephone;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String mail;

    @NotBlank(message = "Street is required")
    @Size(max = 255, message = "Street cannot exceed 255 characters")
    private String street;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^[0-9]{5}$", message = "ZIP code must be 5 digits")
    private String zip;

    @NotBlank(message = "City is required")
    @Size(max = 255, message = "City cannot exceed 255 characters")
    private String city;

    @NotNull(message = "Country is required")
    private Countries country;

    private Boolean hidden;

    @Size(max = 1000, message = "Note cannot exceed 1000 characters")
    private String note;
}