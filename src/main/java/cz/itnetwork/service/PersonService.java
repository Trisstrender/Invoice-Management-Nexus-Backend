package cz.itnetwork.service;

import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.InvoiceDTO;

import java.util.List;
import java.util.Map;

public interface PersonService {

    PersonDTO addPerson(PersonDTO personDTO);

    void removePerson(long id);

    List<PersonDTO> getAll();

    PersonDTO getPersonById(long id);

    PersonDTO updatePerson(long id, PersonDTO personDTO);

    List<InvoiceDTO> getPersonSales(String identificationNumber);

    List<InvoiceDTO> getPersonPurchases(String identificationNumber);

    List<Map<String, Object>> getPersonStatistics();
}