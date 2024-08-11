package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST controller for managing Person-related operations.
 * This controller handles CRUD operations and other person-related functionalities.
 */
@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService personService;

    /**
     * Creates a new person.
     *
     * @param personDTO The person data transfer object containing person details
     * @return The created person DTO
     */
    @PostMapping("/persons")
    public PersonDTO addPerson(@RequestBody PersonDTO personDTO) {
        return personService.addPerson(personDTO);
    }

    /**
     * Retrieves a specific person by ID.
     *
     * @param personId The ID of the person to retrieve
     * @return The requested person DTO
     */
    @GetMapping("/persons/{personId}")
    public PersonDTO getPerson(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    /**
     * Updates an existing person.
     *
     * @param personId  The ID of the person to update
     * @param personDTO The updated person data transfer object
     * @return The updated person DTO
     */
    @PutMapping("/persons/{personId}")
    public PersonDTO updatePerson(@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        return personService.updatePerson(personId, personDTO);
    }

    /**
     * Deletes a person.
     *
     * @param personId The ID of the person to delete
     */
    @DeleteMapping("/persons/{personId}")
    public void deletePerson(@PathVariable Long personId) {
        personService.removePerson(personId);
    }

    /**
     * Retrieves a list of persons based on given parameters.
     *
     * @param params Map of parameters for filtering and pagination
     * @param sort   The sorting criteria
     * @param page   The page number
     * @param limit  The maximum number of items to return
     * @return Paginated response containing a list of person DTOs
     */
    @GetMapping("/persons")
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
     * Retrieves sales for a specific person by identification number.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's sales
     */
    @GetMapping("/identification/{identificationNumber}/sales")
    public PaginatedResponse<InvoiceDTO> getPersonSales(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonSales(identificationNumber, page, limit);
    }

    /**
     * Retrieves purchases for a specific person by identification number.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's purchases
     */
    @GetMapping("/identification/{identificationNumber}/purchases")
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonPurchases(identificationNumber, page, limit);
    }

    /**
     * Retrieves statistics related to persons.
     *
     * @return List of maps containing person statistics
     */
    @GetMapping("/persons/statistics")
    public List<Map<String, Object>> getPersonStatistics() {
        return personService.getPersonStatistics();
    }
}