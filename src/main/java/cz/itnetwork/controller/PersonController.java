package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PersonController {

    @Autowired
    private PersonService personService;

    // Create a new person
    @PostMapping("/persons")
    public PersonDTO addPerson(@RequestBody PersonDTO personDTO) {
        return personService.addPerson(personDTO);
    }

    // Retrieve a specific person by ID
    @GetMapping("/persons/{personId}")
    public PersonDTO getPerson(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    // Update an existing person by ID
    @PutMapping("/persons/{personId}")
    public PersonDTO updatePerson(@PathVariable Long personId, @RequestBody PersonDTO personDTO) {
        return personService.updatePerson(personId, personDTO);
    }

    // Delete a person by ID
    @DeleteMapping("/persons/{personId}")
    public void deletePerson(@PathVariable Long personId) {
        personService.removePerson(personId);
    }

    // Retrieve a list of all persons
    @GetMapping("/persons")
    public List<PersonDTO> getPersons() {
        return personService.getAll();
    }

    // Retrieve sales for a specific person by identification number
    @GetMapping("/identification/{identificationNumber}/sales")
    public List<InvoiceDTO> getPersonSales(@PathVariable String identificationNumber) {
        return personService.getPersonSales(identificationNumber);
    }

    // Retrieve purchases for a specific person by identification number
    @GetMapping("/identification/{identificationNumber}/purchases")
    public List<InvoiceDTO> getPersonPurchases(@PathVariable String identificationNumber) {
        return personService.getPersonPurchases(identificationNumber);
    }

    // Retrieve statistics related to persons
    @GetMapping("/persons/statistics")
    public List<Map<String, Object>> getPersonStatistics() {
        return personService.getPersonStatistics();
    }
}