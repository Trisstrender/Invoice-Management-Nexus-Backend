package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;

import java.util.Map;

public interface PersonService {
    PersonDTO addPerson(PersonDTO personDTO);

    PersonDTO getPersonById(long id);

    PersonDTO updatePerson(long id, PersonDTO personDTO);

    void removePerson(long id);

    PaginatedResponse<PersonDTO> getPersons(Map<String, String> params);

    PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit);

    PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit);

    Map<String, Object> getPersonStatistics(int page, int limit, String sort);
}