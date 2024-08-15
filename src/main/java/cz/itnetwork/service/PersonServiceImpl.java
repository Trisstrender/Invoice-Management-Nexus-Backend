package cz.itnetwork.service;

import cz.itnetwork.dto.*;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.exception.PersonNotFoundException;
import cz.itnetwork.utils.FilterUtils;
import cz.itnetwork.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
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
        newPerson.setId(0);
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
    public PaginatedResponse<PersonStatisticsDTO> getPersonStatistics(int page, int limit, String sort) {
        // Default sort field and direction
        String sortField = "id";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        // Parse and validate sort parameters
        if (sort != null && !sort.isEmpty()) {
            String[] sortParams = sort.split(",");
            String requestedSortField = sortParams[0].trim();
            sortDirection = sortParams.length > 1 && sortParams[1].trim().equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC : Sort.Direction.ASC;

            Set<String> validSortFields = Set.of("id", "name", "revenue");
            if (validSortFields.contains(requestedSortField)) {
                sortField = requestedSortField;
            }
        }

        // Fetch all persons
        List<PersonEntity> allPersons = personRepository.findAll();

        // Map to DTOs and filter
        List<PersonStatisticsDTO> statistics = allPersons.stream()
                .map(person -> {
                    long revenue = person.getSales().stream().mapToLong(InvoiceEntity::getPrice).sum();
                    return new PersonStatisticsDTO(person.getId(), person.getName(), revenue);
                })
                .filter(stat -> stat.getRevenue() > 0)  // Filter out people with zero revenue
                .collect(Collectors.toList());

        // Sort the filtered list using Comparator
        Comparator<PersonStatisticsDTO> comparator = switch (sortField) {
            case "name" -> Comparator.comparing(PersonStatisticsDTO::getPersonName, String.CASE_INSENSITIVE_ORDER);
            case "revenue" -> Comparator.comparingLong(PersonStatisticsDTO::getRevenue);
            case "id" -> Comparator.comparingLong(PersonStatisticsDTO::getPersonId);
            default -> throw new IllegalStateException("Unexpected value: " + sortField);
        };
        if (sortDirection == Sort.Direction.DESC) {
            comparator = comparator.reversed();
        }
        statistics.sort(comparator);

        // Apply pagination safely
        int totalItems = statistics.size();
        int start = Math.max(0, (page - 1) * limit);
        int end = Math.min(start + limit, totalItems);

        List<PersonStatisticsDTO> paginatedStatistics = start < end ? statistics.subList(start, end) : Collections.emptyList();

        int totalPages = (int) Math.ceil((double) totalItems / limit);

        return new PaginatedResponse<>(
                paginatedStatistics,
                page,
                totalPages,
                totalItems
        );
    }
}