package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceImplTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private InvoiceMapper invoiceMapper;

    @InjectMocks
    private InvoiceServiceImpl invoiceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    }

    @Test
    void deleteInvoice_ExistingInvoice_InvokesDeletion() {
        long invoiceId = 1L;

        invoiceService.deleteInvoice(invoiceId);

        verify(invoiceRepository).deleteById(invoiceId);
    }
}