package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
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
    List<PersonDTO> getAll();

    /**
     * Retrieves sales invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's sales
     */
    List<InvoiceDTO> getPersonSales(String identificationNumber);

    /**
     * Retrieves purchase invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's purchases
     */
    List<InvoiceDTO> getPersonPurchases(String identificationNumber);

    /**
     * Retrieves person statistics.
     *
     * @return List of maps containing person statistics
     */
    List<Map<String, Object>> getPersonStatistics();
}