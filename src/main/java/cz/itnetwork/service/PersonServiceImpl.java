package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.exception.PersonNotFoundException;
import cz.itnetwork.utils.FilterUtils;
import cz.itnetwork.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PersonServiceImpl extends BaseService<PersonEntity, Long> implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final InvoiceService invoiceService;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             PersonMapper personMapper,
                             InvoiceService invoiceService) {
        super(personRepository, personRepository);
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.invoiceService = invoiceService;
    }

    @Override
    public PersonDTO addPerson(PersonDTO personDTO) {
        PersonEntity entity = personMapper.toEntity(personDTO);
        entity = create(entity);
        return personMapper.toDTO(entity);
    }

    @Override
    public PersonDTO getPersonById(long id) {
        PersonEntity personEntity = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        return personMapper.toDTO(personEntity);
    }

    @Override
    public PersonDTO updatePerson(long id, PersonDTO personDTO) {
        PersonEntity existingPerson = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        existingPerson.setHidden(true);
        update(existingPerson);

        PersonEntity newPerson = personMapper.toEntity(personDTO);
        newPerson.setId(0); // Ensure a new entity is created
        newPerson = create(newPerson);

        return personMapper.toDTO(newPerson);
    }

    @Override
    public void removePerson(long id) {
        PersonEntity person = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        person.setHidden(true);
        update(person);
    }

    @Override
    public PaginatedResponse<PersonDTO> getPersons(Map<String, String> params) {
        Pageable pageable = PaginationUtils.createPageable(params);
        Specification<PersonEntity> spec = FilterUtils.createPersonSpecification(params);

        Page<PersonEntity> personPage = findAll(spec, pageable);

        List<PersonDTO> personDTOs = personPage.getContent().stream()
                .map(personMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                personDTOs,
                personPage.getNumber() + 1,
                personPage.getTotalPages(),
                (int) personPage.getTotalElements()
        );
    }

    @Override
    public PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit) {
        return invoiceService.getPersonSales(identificationNumber, page, limit);
    }

    @Override
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit) {
        return invoiceService.getPersonPurchases(identificationNumber, page, limit);
    }

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
}