package com.invoice.management.controller;

import com.invoice.management.dto.InvoiceDTO;
import com.invoice.management.dto.PaginatedResponse;
import com.invoice.management.dto.PersonDTO;
import com.invoice.management.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing persons.
 * This controller handles CRUD operations for persons and provides endpoints for
 * retrieving person-related data such as sales, purchases, and statistics.
 */
@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Creates a new person.
     *
     * @param personDTO The person data transfer object containing the person details
     * @return The created person DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO addPerson(@Valid @RequestBody PersonDTO personDTO) {
        return personService.addPerson(personDTO);
    }

    /**
     * Retrieves a person by their ID.
     *
     * @param personId The ID of the person to retrieve
     * @return The person DTO
     */
    @GetMapping("/{personId}")
    public PersonDTO getPerson(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    /**
     * Updates an existing person.
     *
     * @param personId  The ID of the person to update
     * @param personDTO The person data transfer object containing the updated person details
     * @return The updated person DTO
     */
    @PutMapping("/{personId}")
    public PersonDTO updatePerson(@PathVariable Long personId, @Valid @RequestBody PersonDTO personDTO) {
        return personService.updatePerson(personId, personDTO);
    }

    /**
     * Deletes a person by their ID.
     *
     * @param personId The ID of the person to delete
     */
    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long personId) {
        personService.removePerson(personId);
    }

    /**
     * Retrieves a paginated list of persons based on the provided parameters.
     *
     * @param params A map of query parameters for filtering and sorting
     * @param sort   The sort order (default: "id,asc")
     * @param page   The page number (default: 1)
     * @param limit  The number of items per page (default: 10)
     * @return A paginated response containing the list of person DTOs
     */
    @GetMapping
    public PaginatedResponse<PersonDTO> getPersons(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        params.put("sort", sort);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        return personService.getPersons(params);
    }

    /**
     * Retrieves a paginated list of sales invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @param page                 The page number (default: 1)
     * @param limit                The number of items per page (default: 10)
     * @return A paginated response containing the list of sales invoice DTOs
     */
    @GetMapping("/identification/{identificationNumber}/sales")
    public PaginatedResponse<InvoiceDTO> getPersonSales(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonSales(identificationNumber, page, limit);
    }

    /**
     * Retrieves a paginated list of purchase invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @param page                 The page number (default: 1)
     * @param limit                The number of items per page (default: 10)
     * @return A paginated response containing the list of purchase invoice DTOs
     */
    @GetMapping("/identification/{identificationNumber}/purchases")
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonPurchases(identificationNumber, page, limit);
    }

    /**
     * Retrieves person statistics.
     *
     * @param page  The page number (default: 1)
     * @param limit The number of items per page (default: 10)
     * @param sort  The sort order (default: "personName,asc")
     * @return A ResponseEntity containing a map of person statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getPersonStatistics(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "personName,asc") String sort) {
        return ResponseEntity.ok(personService.getPersonStatistics(page, limit, sort));
    }
}
