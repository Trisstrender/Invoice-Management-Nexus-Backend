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
import cz.itnetwork.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceImplTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonMapper personMapper;

    @Mock
    private InvoiceService invoiceService;

    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        personService = new PersonServiceImpl(personRepository, personMapper, invoiceService);
    }

    @Test
    void addPerson_ValidPerson_ReturnsPersonDTO() {
        PersonDTO inputDTO = TestDataFactory.createValidPersonDTO();
        PersonEntity entity = new PersonEntity();
        PersonDTO outputDTO = TestDataFactory.createValidPersonDTO();
        outputDTO.setId(1L);

        when(personMapper.toEntity(inputDTO)).thenReturn(entity);
        when(personRepository.save(entity)).thenReturn(entity);
        when(personMapper.toDTO(entity)).thenReturn(outputDTO);

        PersonDTO result = personService.addPerson(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(personRepository).save(any(PersonEntity.class));
    }

    @Test
    void getPersonById_ExistingId_ReturnsPersonDTO() {
        long id = 1L;
        PersonEntity entity = new PersonEntity();
        PersonDTO dto = TestDataFactory.createValidPersonDTO();

        when(personRepository.findById(id)).thenReturn(Optional.of(entity));
        when(personMapper.toDTO(entity)).thenReturn(dto);

        PersonDTO result = personService.getPersonById(id);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getPersonById_NonExistingId_ThrowsPersonNotFoundException() {
        long id = 1L;
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(id));
    }

    @Test
    void updatePerson_ExistingPerson_ReturnsUpdatedPersonDTO() {
        long personId = 1L;
        PersonDTO inputDTO = TestDataFactory.createValidPersonDTO();
        PersonEntity existingEntity = new PersonEntity();
        PersonEntity updatedEntity = new PersonEntity();
        PersonDTO updatedDTO = TestDataFactory.createValidPersonDTO();
        updatedDTO.setId(personId);

        when(personRepository.findById(personId)).thenReturn(Optional.of(existingEntity));
        when(personMapper.toEntity(inputDTO)).thenReturn(updatedEntity);
        when(personRepository.save(any(PersonEntity.class))).thenReturn(updatedEntity);
        when(personMapper.toDTO(updatedEntity)).thenReturn(updatedDTO);

        PersonDTO result = personService.updatePerson(personId, inputDTO);

        assertNotNull(result);
        assertEquals(personId, result.getId());
        verify(personRepository, times(2)).save(any(PersonEntity.class));
    }

    @Test
    void removePerson_ExistingPerson_SetsHiddenToTrue() {
        long personId = 1L;
        PersonEntity existingEntity = new PersonEntity();

        when(personRepository.findById(personId)).thenReturn(Optional.of(existingEntity));

        personService.removePerson(personId);

        assertTrue(existingEntity.isHidden());
        verify(personRepository).save(existingEntity);
    }

    @Test
    void getPersons_ReturnsPagedResult() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
        params.put("sort", "name,asc");

        Page<PersonEntity> mockPage = new PageImpl<>(Collections.singletonList(new PersonEntity()));
        when(personRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);
        when(personMapper.toDTO(any(PersonEntity.class))).thenReturn(new PersonDTO());

        PaginatedResponse<PersonDTO> result = personService.getPersons(params);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalItems());
        assertFalse(result.getItems().isEmpty());
    }

    @Test
    void getPersonSales_ReturnsPagedResult() {
        String identificationNumber = "12345678";
        int page = 1;
        int limit = 10;
        PaginatedResponse<InvoiceDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(new InvoiceDTO()),
                1, 1, 1
        );

        when(invoiceService.getPersonSales(identificationNumber, page, limit)).thenReturn(mockResponse);

        PaginatedResponse<InvoiceDTO> result = personService.getPersonSales(identificationNumber, page, limit);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalItems());
        assertFalse(result.getItems().isEmpty());
    }

    @Test
    void getPersonPurchases_ReturnsPagedResult() {
        String identificationNumber = "12345678";
        int page = 1;
        int limit = 10;
        PaginatedResponse<InvoiceDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(new InvoiceDTO()),
                1, 1, 1
        );

        when(invoiceService.getPersonPurchases(identificationNumber, page, limit)).thenReturn(mockResponse);

        PaginatedResponse<InvoiceDTO> result = personService.getPersonPurchases(identificationNumber, page, limit);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalItems());
        assertFalse(result.getItems().isEmpty());
    }

    @Test
    void getPersonStatistics_ReturnsCorrectMap() {
        int page = 1;
        int limit = 10;
        String sort = "name,asc";

        PersonEntity person1 = new PersonEntity();
        person1.setId(1L);
        person1.setName("Person 1");
        person1.setSales(Arrays.asList(
                createInvoiceEntity(100L),
                createInvoiceEntity(200L)
        ));

        PersonEntity person2 = new PersonEntity();
        person2.setId(2L);
        person2.setName("Person 2");
        person2.setSales(Collections.singletonList(
                createInvoiceEntity(300L)
        ));

        PersonEntity personWithZeroRevenue = new PersonEntity();
        personWithZeroRevenue.setId(3L);
        personWithZeroRevenue.setName("Person 3");
        personWithZeroRevenue.setSales(Collections.emptyList());

        List<PersonEntity> personList = Arrays.asList(person2, person1, personWithZeroRevenue);

        when(personRepository.findAll()).thenReturn(personList);

        Map<String, Object> result = personService.getPersonStatistics(page, limit, sort);

        assertNotNull(result);
        assertTrue(result.containsKey("paginatedData"));
        assertTrue(result.containsKey("top5ByRevenue"));

        PaginatedResponse<PersonStatisticsDTO> paginatedData = (PaginatedResponse<PersonStatisticsDTO>) result.get("paginatedData");
        List<PersonStatisticsDTO> top5ByRevenue = (List<PersonStatisticsDTO>) result.get("top5ByRevenue");

        // Check paginated data
        assertEquals(1, paginatedData.getCurrentPage());
        assertEquals(1, paginatedData.getTotalPages());
        assertEquals(2, paginatedData.getTotalItems());
        assertEquals(2, paginatedData.getItems().size());

        assertEquals(1L, paginatedData.getItems().get(0).getPersonId());
        assertEquals("Person 1", paginatedData.getItems().get(0).getPersonName());
        assertEquals(300L, paginatedData.getItems().get(0).getRevenue());

        assertEquals(2L, paginatedData.getItems().get(1).getPersonId());
        assertEquals("Person 2", paginatedData.getItems().get(1).getPersonName());
        assertEquals(300L, paginatedData.getItems().get(1).getRevenue());

        // Check top 5 by revenue
        assertEquals(2, top5ByRevenue.size());
        assertEquals(2L, top5ByRevenue.get(0).getPersonId());
        assertEquals("Person 2", top5ByRevenue.get(0).getPersonName());
        assertEquals(300L, top5ByRevenue.get(0).getRevenue());

        assertEquals(1L, top5ByRevenue.get(1).getPersonId());
        assertEquals("Person 1", top5ByRevenue.get(1).getPersonName());
        assertEquals(300L, top5ByRevenue.get(1).getRevenue());

        verify(personRepository).findAll();
    }

    // Helper method for creating InvoiceEntity
    private InvoiceEntity createInvoiceEntity(Long price) {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setPrice(price);
        return invoice;
    }
}
