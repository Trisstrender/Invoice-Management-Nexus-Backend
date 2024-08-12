package cz.itnetwork.service;

import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.PersonDTO;
import cz.itnetwork.dto.mapper.PersonMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.exception.PersonNotFoundException;
import cz.itnetwork.utils.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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

    @InjectMocks
    private PersonServiceImpl personService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        PersonEntity updatedEntity = new PersonEntity(); // Create a new entity to represent the updated person
        PersonDTO updatedDTO = TestDataFactory.createValidPersonDTO();
        updatedDTO.setId(personId);

        when(personRepository.findById(personId)).thenReturn(Optional.of(existingEntity));
        when(personMapper.toEntity(inputDTO)).thenReturn(updatedEntity);
        when(personRepository.save(any(PersonEntity.class))).thenReturn(updatedEntity);
        when(personMapper.toDTO(updatedEntity)).thenReturn(updatedDTO);

        PersonDTO result = personService.updatePerson(personId, inputDTO);

        assertNotNull(result);
        assertEquals(personId, result.getId());
        verify(personRepository, times(2)).save(any(PersonEntity.class)); // Verify two saves: one for hiding, one for new entity
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
    void getPersonStatistics_ReturnsCorrectStatistics() {
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

        when(personRepository.findAll()).thenReturn(Arrays.asList(person1, person2));

        List<Map<String, Object>> result = personService.getPersonStatistics();

        assertEquals(2, result.size());
        assertEquals(300L, result.get(0).get("revenue"));
        assertEquals(300L, result.get(1).get("revenue"));
    }

    // Helper method for creating InvoiceEntity
    private InvoiceEntity createInvoiceEntity(Long price) {
        InvoiceEntity invoice = new InvoiceEntity();
        invoice.setPrice(price);
        return invoice;
    }
}