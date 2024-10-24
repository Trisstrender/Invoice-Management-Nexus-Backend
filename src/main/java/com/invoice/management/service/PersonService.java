package com.invoice.management.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;

import java.util.Map;

/**
 * Service interface for managing persons.
 */
public interface PersonService {

    /**
     * Adds a new person.
     *
     * @param personDTO the DTO containing the person data
     * @return the created person DTO
     */
    PersonDTO addPerson(PersonDTO personDTO);

    /**
     * Retrieves a person by their ID.
     *
     * @param id the ID of the person to retrieve
     * @return the person DTO
     */
    PersonDTO getPersonById(long id);

    /**
     * Updates an existing person.
     *
     * @param id        the ID of the person to update
     * @param personDTO the DTO containing the updated person data
     * @return the updated person DTO
     */
    PersonDTO updatePerson(long id, PersonDTO personDTO);

    /**
     * Removes a person by their ID.
     *
     * @param id the ID of the person to remove
     */
    void removePerson(long id);

    /**
     * Retrieves a paginated list of persons based on the provided parameters.
     *
     * @param params a map of query parameters for filtering and sorting
     * @return a paginated response containing person DTOs
     */
    PaginatedResponse<PersonDTO> getPersons(Map<String, String> params);

    /**
     * Retrieves a paginated list of sales invoices for a specific person.
     *
     * @param identificationNumber the identification number of the person
     * @param page                 the page number
     * @param limit                the number of items per page
     * @return a paginated response containing invoice DTOs
     */
    PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit);

    /**
     * Retrieves a paginated list of purchase invoices for a specific person.
     *
     * @param identificationNumber the identification number of the person
     * @param page                 the page number
     * @param limit                the number of items per page
     * @return a paginated response containing invoice DTOs
     */
    PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit);

    /**
     * Retrieves person statistics.
     *
     * @param page  the page number
     * @param limit the number of items per page
     * @param sort  the sorting criteria
     * @return a map containing various person statistics
     */
    Map<String, Object> getPersonStatistics(int page, int limit, String sort);
}
