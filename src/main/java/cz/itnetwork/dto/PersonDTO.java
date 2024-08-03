package cz.itnetwork.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import cz.itnetwork.constant.Countries;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Person.
 * This class represents the person data that is transferred between the client and the server.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {

    @JsonProperty("_id")
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String identificationNumber;

    @NotNull
    private String taxNumber;

    @NotNull
    private String accountNumber;

    @NotNull
    private String bankCode;

    @NotNull
    private String iban;

    @NotNull
    private String telephone;

    @NotNull
    private String mail;

    @NotNull
    private String street;

    @NotNull
    private String zip;

    @NotNull
    private String city;

    @NotNull
    private Countries country;

    private Boolean hidden;

    @NotNull
    private String note;
}