package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.PersonStatisticsDTO;
import cz.itnetwork.service.PersonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PersonDTO addPerson(@Valid @RequestBody PersonDTO personDTO) {
        return personService.addPerson(personDTO);
    }

    @GetMapping("/{personId}")
    public PersonDTO getPerson(@PathVariable Long personId) {
        return personService.getPersonById(personId);
    }

    @PutMapping("/{personId}")
    public PersonDTO updatePerson(@PathVariable Long personId, @Valid @RequestBody PersonDTO personDTO) {
        return personService.updatePerson(personId, personDTO);
    }

    @DeleteMapping("/{personId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable Long personId) {
        personService.removePerson(personId);
    }

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

    @GetMapping("/identification/{identificationNumber}/sales")
    public PaginatedResponse<InvoiceDTO> getPersonSales(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonSales(identificationNumber, page, limit);
    }

    @GetMapping("/identification/{identificationNumber}/purchases")
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return personService.getPersonPurchases(identificationNumber, page, limit);
    }

    @GetMapping("/statistics")
    public PaginatedResponse<PersonStatisticsDTO> getPersonStatistics(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "personName,asc") String sort) {
        return personService.getPersonStatistics(page, limit, sort);
    }
}