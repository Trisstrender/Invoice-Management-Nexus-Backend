package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.PersonEntity;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.exception.InvoiceNotFoundException;
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

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void setUp() {
        invoiceService = new InvoiceServiceImpl(invoiceRepository, invoiceMapper, personRepository);
    }

    @Test
    void createInvoice_ValidInvoice_ReturnsInvoiceDTO() {
        InvoiceDTO inputDTO = TestDataFactory.createValidInvoiceDTO();
        InvoiceEntity entity = new InvoiceEntity();
        InvoiceDTO outputDTO = TestDataFactory.createValidInvoiceDTO();
        outputDTO.setId(1L);

        when(invoiceMapper.toEntity(inputDTO)).thenReturn(entity);
        when(personRepository.findById(any())).thenReturn(Optional.of(new PersonEntity()));
        when(invoiceRepository.save(entity)).thenReturn(entity);
        when(invoiceMapper.toDTO(entity)).thenReturn(outputDTO);

        InvoiceDTO result = invoiceService.createInvoice(inputDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(invoiceRepository).save(any(InvoiceEntity.class));
    }

    @Test
    void getInvoiceById_ExistingId_ReturnsInvoiceDTO() {
        long id = 1L;
        InvoiceEntity entity = new InvoiceEntity();
        InvoiceDTO dto = TestDataFactory.createValidInvoiceDTO();

        when(invoiceRepository.findById(id)).thenReturn(Optional.of(entity));
        when(invoiceMapper.toDTO(entity)).thenReturn(dto);

        InvoiceDTO result = invoiceService.getInvoiceById(id);

        assertNotNull(result);
        assertEquals(dto, result);
    }

    @Test
    void getInvoiceById_NonExistingId_ThrowsInvoiceNotFoundException() {
        long id = 1L;
        when(invoiceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(InvoiceNotFoundException.class, () -> invoiceService.getInvoiceById(id));
    }

    @Test
    void updateInvoice_ExistingInvoice_ReturnsUpdatedInvoiceDTO() {
        Long invoiceId = 1L;
        InvoiceDTO inputDTO = TestDataFactory.createValidInvoiceDTO();
        InvoiceEntity existingEntity = new InvoiceEntity();
        InvoiceDTO updatedDTO = TestDataFactory.createValidInvoiceDTO();
        updatedDTO.setId(invoiceId);

        when(invoiceRepository.findById(invoiceId)).thenReturn(Optional.of(existingEntity));
        when(invoiceRepository.save(any(InvoiceEntity.class))).thenReturn(existingEntity);
        when(invoiceMapper.toDTO(existingEntity)).thenReturn(updatedDTO);

        InvoiceDTO result = invoiceService.updateInvoice(invoiceId, inputDTO);

        assertNotNull(result);
        assertEquals(invoiceId, result.getId());
        verify(invoiceRepository).save(any(InvoiceEntity.class));
    }

    @Test
    void deleteInvoice_ExistingInvoice_InvokesDeletion() {
        long invoiceId = 1L;

        invoiceService.deleteInvoice(invoiceId);

        verify(invoiceRepository).deleteById(invoiceId);
    }

    @Test
    void getInvoices_ReturnsPagedResult() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("limit", "10");
        params.put("sort", "invoiceNumber,desc");

        Page<InvoiceEntity> mockPage = new PageImpl<>(Collections.singletonList(new InvoiceEntity()));
        when(invoiceRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);
        when(invoiceMapper.toDTO(any(InvoiceEntity.class))).thenReturn(new InvoiceDTO());

        PaginatedResponse<InvoiceDTO> result = invoiceService.getInvoices(params);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalItems());
        assertFalse(result.getItems().isEmpty());
    }

    @Test
    void getInvoiceStatistics_ReturnsCorrectStatistics() {
        LocalDate currentYear = LocalDate.now();
        LocalDate lastYear = currentYear.minusYears(1);

        InvoiceEntity invoice1 = new InvoiceEntity();
        invoice1.setIssued(currentYear);
        invoice1.setPrice(100L);

        InvoiceEntity invoice2 = new InvoiceEntity();
        invoice2.setIssued(currentYear);
        invoice2.setPrice(200L);

        InvoiceEntity invoice3 = new InvoiceEntity();
        invoice3.setIssued(lastYear);
        invoice3.setPrice(300L);

        List<InvoiceEntity> mockInvoices = Arrays.asList(invoice1, invoice2, invoice3);

        when(invoiceRepository.findAll()).thenReturn(mockInvoices);

        Map<String, Object> result = invoiceService.getInvoiceStatistics();

        assertEquals(300L, result.get("currentYearSum"));
        assertEquals(600L, result.get("allTimeSum"));
        assertEquals(3L, result.get("invoicesCount"));

        verify(invoiceRepository).findAll();
    }

    @Test
    void getPersonSales_ReturnsPagedResult() {
        String identificationNumber = "12345678";
        int page = 1;
        int limit = 10;
        Page<InvoiceEntity> mockPage = new PageImpl<>(Collections.singletonList(new InvoiceEntity()));

        when(invoiceRepository.findBySeller_IdentificationNumber(eq(identificationNumber), any(Pageable.class)))
                .thenReturn(mockPage);
        when(invoiceMapper.toDTO(any(InvoiceEntity.class))).thenReturn(new InvoiceDTO());

        PaginatedResponse<InvoiceDTO> result = invoiceService.getPersonSales(identificationNumber, page, limit);

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
        Page<InvoiceEntity> mockPage = new PageImpl<>(Collections.singletonList(new InvoiceEntity()));

        when(invoiceRepository.findByBuyer_IdentificationNumber(eq(identificationNumber), any(Pageable.class)))
                .thenReturn(mockPage);
        when(invoiceMapper.toDTO(any(InvoiceEntity.class))).thenReturn(new InvoiceDTO());

        PaginatedResponse<InvoiceDTO> result = invoiceService.getPersonPurchases(identificationNumber, page, limit);

        assertNotNull(result);
        assertEquals(1, result.getCurrentPage());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getTotalItems());
        assertFalse(result.getItems().isEmpty());
    }
}