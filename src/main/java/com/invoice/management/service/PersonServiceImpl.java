package com.invoice.management.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.PersonStatisticsDTO;
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

import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of the PersonService interface.
 * This class provides the business logic for managing persons.
 */
@Service
public class PersonServiceImpl extends BaseService<PersonEntity, Long> implements PersonService {

    private final PersonRepository personRepository;
    private final PersonMapper personMapper;
    private final InvoiceService invoiceService;

    /**
     * Constructs a new PersonServiceImpl with the necessary dependencies.
     *
     * @param personRepository the repository for person entities
     * @param personMapper     the mapper for converting between PersonEntity and PersonDTO
     * @param invoiceService   the service for managing invoices
     */
    @Autowired
    public PersonServiceImpl(PersonRepository personRepository,
                             PersonMapper personMapper,
                             InvoiceService invoiceService) {
        super(personRepository, personRepository);
        this.personRepository = personRepository;
        this.personMapper = personMapper;
        this.invoiceService = invoiceService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonDTO addPerson(PersonDTO personDTO) {
        PersonEntity entity = personMapper.toEntity(personDTO);
        entity = create(entity);
        return personMapper.toDTO(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonDTO getPersonById(long id) {
        PersonEntity personEntity = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        return personMapper.toDTO(personEntity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PersonDTO updatePerson(long id, PersonDTO personDTO) {
        PersonEntity existingPerson = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        existingPerson.setHidden(true);
        update(existingPerson);

        PersonEntity newPerson = personMapper.toEntity(personDTO);
        newPerson.setId(0);
        newPerson = create(newPerson);

        return personMapper.toDTO(newPerson);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removePerson(long id) {
        PersonEntity person = findById(id)
                .orElseThrow(() -> new PersonNotFoundException("Person not found"));
        person.setHidden(true);
        update(person);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit) {
        return invoiceService.getPersonSales(identificationNumber, page, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit) {
        return invoiceService.getPersonPurchases(identificationNumber, page, limit);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getPersonStatistics(int page, int limit, String sort) {
        List<PersonEntity> allPersons = personRepository.findAll();

        List<PersonStatisticsDTO> statistics = allPersons.stream()
                .map(person -> new PersonStatisticsDTO(
                        person.getId(),
                        person.getName(),
                        person.getSales().stream().mapToLong(InvoiceEntity::getPrice).sum()
                ))
                .filter(stat -> stat.getRevenue() > 0)
                .collect(Collectors.toList());

        List<PersonStatisticsDTO> top5ByRevenue = statistics.stream()
                .sorted(Comparator.comparingLong(PersonStatisticsDTO::getRevenue).reversed())
                .limit(5)
                .collect(Collectors.toList());

        sortStatistics(statistics, sort);

        int totalItems = statistics.size();
        int start = (page - 1) * limit;
        int end = Math.min(start + limit, totalItems);
        List<PersonStatisticsDTO> paginatedStatistics = start < end
                ? statistics.subList(start, end)
                : Collections.emptyList();

        int totalPages = (int) Math.ceil((double) totalItems / limit);

        PaginatedResponse<PersonStatisticsDTO> paginatedResponse = new PaginatedResponse<>(
                paginatedStatistics,
                page,
                totalPages,
                totalItems
        );

        Map<String, Object> result = new HashMap<>();
        result.put("paginatedData", paginatedResponse);
        result.put("top5ByRevenue", top5ByRevenue);

        return result;
    }

    /**
     * Sorts the list of PersonStatisticsDTO based on the provided sort parameter.
     *
     * @param statistics the list of PersonStatisticsDTO to sort
     * @param sort       the sort parameter in the format "field,direction"
     */
    private void sortStatistics(List<PersonStatisticsDTO> statistics, String sort) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0].trim();
        boolean isAsc = sortParams.length == 1 || sortParams[1].trim().equalsIgnoreCase("asc");

        Comparator<PersonStatisticsDTO> comparator = switch (sortField) {
            case "name" -> Comparator.comparing(PersonStatisticsDTO::getPersonName, String.CASE_INSENSITIVE_ORDER);
            case "revenue" -> Comparator.comparingLong(PersonStatisticsDTO::getRevenue);
            case "id" -> Comparator.comparingLong(PersonStatisticsDTO::getPersonId);
            default -> throw new IllegalArgumentException("Invalid sort field: " + sortField);
        };

        if (!isAsc) {
            comparator = comparator.reversed();
        }

        statistics.sort(comparator);
    }
}
