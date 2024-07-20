package cz.itnetwork.service;

import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.InvoiceDTO;

import java.util.List;
import java.util.Map;

public interface PersonService {
    // CRUD operations
    PersonDTO addPerson(PersonDTO personDTO);
    PersonDTO getPersonById(long id);
    PersonDTO updatePerson(long id, PersonDTO personDTO);
    void removePerson(long id);

    // List operations
    List<PersonDTO> getAll();

    // Person-specific operations
    List<InvoiceDTO> getPersonSales(String identificationNumber);
    List<InvoiceDTO> getPersonPurchases(String identificationNumber);

    // Statistics
    List<Map<String, Object>> getPersonStatistics();
}