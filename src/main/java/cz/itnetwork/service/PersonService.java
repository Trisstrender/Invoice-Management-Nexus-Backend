package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;

import java.util.List;
import java.util.Map;

/**
 * Service interface for Person-related operations.
 * This interface defines the contract for person management operations.
 */
public interface PersonService {

    /**
     * Adds a new person.
     *
     * @param personDTO The person data to add
     * @return The added person DTO
     */
    PersonDTO addPerson(PersonDTO personDTO);

    /**
     * Retrieves a person by their ID.
     *
     * @param id The ID of the person to retrieve
     * @return The retrieved person DTO
     */
    PersonDTO getPersonById(long id);

    /**
     * Updates an existing person.
     *
     * @param id        The ID of the person to update
     * @param personDTO The updated person data
     * @return The updated person DTO
     */
    PersonDTO updatePerson(long id, PersonDTO personDTO);

    /**
     * Removes a person by their ID.
     *
     * @param id The ID of the person to remove
     */
    void removePerson(long id);

    /**
     * Retrieves all persons.
     *
     * @return List of all person DTOs
     */
    PaginatedResponse<PersonDTO> getPersons(Map<String, String> params);

    PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit);

    PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit);

    /**
     * Retrieves person statistics.
     *
     * @return List of maps containing person statistics
     */
    List<Map<String, Object>> getPersonStatistics();
}