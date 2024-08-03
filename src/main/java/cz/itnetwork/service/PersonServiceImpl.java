package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of the PersonService interface.
 * This class provides the business logic for person-related operations.
 */
@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Adds a new person.
     *
     * @param personDTO The person data to add
     * @return The added person DTO
     */
    @Override
    public PersonDTO addPerson(PersonDTO personDTO) {
        PersonEntity entity = personMapper.toEntity(personDTO);
        entity = personRepository.save(entity);
        return personMapper.toDTO(entity);
    }

    /**
     * Retrieves a person by their ID.
     *
     * @param id The ID of the person to retrieve
     * @return The retrieved person DTO
     * @throws NotFoundException if the person is not found
     */
    @Override
    public PersonDTO getPersonById(long id) {
        PersonEntity personEntity = fetchPersonById(id);
        return personMapper.toDTO(personEntity);
    }

    /**
     * Updates an existing person.
     * This method creates a new person entity and hides the old one to maintain historical data.
     *
     * @param id The ID of the person to update
     * @param personDTO The updated person data
     * @return The updated person DTO
     * @throws NotFoundException if the person is not found
     */
    @Override
    public PersonDTO updatePerson(long id, PersonDTO personDTO) {
        // Set existing person as hidden and save
        PersonEntity existingPerson = fetchPersonById(id);
        existingPerson.setHidden(true);
        personRepository.save(existingPerson);

        // Create and save a new person entity from DTO
        PersonEntity newPerson = personMapper.toEntity(personDTO);
        newPerson.setId(0); // Ensure a new entity is created
        newPerson = personRepository.save(newPerson);

        return personMapper.toDTO(newPerson);
    }

    /**
     * Removes a person by their ID.
     * This method actually hides the person instead of deleting them to maintain historical data.
     *
     * @param id The ID of the person to remove
     */
    @Override
    public void removePerson(long id) {
        try {
            PersonEntity person = fetchPersonById(id);
            person.setHidden(true);
            personRepository.save(person);
        } catch (NotFoundException ignored) {
            // Do nothing if person not found
        }
    }

    /**
     * Retrieves all non-hidden persons.
     *
     * @return List of all non-hidden person DTOs
     */
    @Override
    public List<PersonDTO> getAll() {
        return personRepository.findByHidden(false)
                .stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves sales invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's sales
     */
    @Override
    public List<InvoiceDTO> getPersonSales(String identificationNumber) {
        return invoiceService.getPersonSales(identificationNumber);
    }

    /**
     * Retrieves purchase invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's purchases
     */
    @Override
    public List<InvoiceDTO> getPersonPurchases(String identificationNumber) {
        return invoiceService.getPersonPurchases(identificationNumber);
    }

    /**
     * Retrieves person statistics.
     * This method calculates the total revenue for each person based on their sales.
     *
     * @return List of maps containing person statistics (personId, personName, revenue)
     */
    @Override
    public List<Map<String, Object>> getPersonStatistics() {
        return personRepository.findAll().stream()
                .map(person -> {
                    long revenue = person.getSales().stream().mapToLong(InvoiceEntity::getPrice).sum();
                    Map<String, Object> statistics = new HashMap<>();
                    statistics.put("personId", person.getId());
                    statistics.put("personName", person.getName());
                    statistics.put("revenue", revenue);
                    return statistics;
                })
                .collect(Collectors.toList());
    }

    /**
     * Helper method to fetch a person by ID.
     *
     * @param id The ID of the person to fetch
     * @return The PersonEntity
     * @throws NotFoundException if the person is not found
     */
    private PersonEntity fetchPersonById(long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Person with id " + id + " wasn't found in the database."));
    }
}