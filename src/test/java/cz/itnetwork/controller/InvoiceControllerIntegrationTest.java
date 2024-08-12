package cz.itnetwork.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.service.InvoiceService;
import cz.itnetwork.utils.TestDataFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InvoiceService invoiceService;

    @Test
    void createInvoice_ValidInvoice_ReturnsCreatedInvoice() throws Exception {
        InvoiceDTO inputDTO = TestDataFactory.createValidInvoiceDTO();
        InvoiceDTO outputDTO = TestDataFactory.createValidInvoiceDTO();
        outputDTO.setId(1L);

        when(invoiceService.createInvoice(any(InvoiceDTO.class))).thenReturn(outputDTO);

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$._id").value(1L))  // Changed from $.id to $._id
                .andExpect(jsonPath("$.invoiceNumber").value(inputDTO.getInvoiceNumber()));
    }

    @Test
    void createInvoice_InvalidInvoice_ReturnsBadRequest() throws Exception {
        InvoiceDTO invalidDTO = TestDataFactory.createValidInvoiceDTO();
        invalidDTO.setPrice(-100L);  // Invalid: negative price

        mockMvc.perform(post("/api/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getInvoice_ExistingId_ReturnsInvoice() throws Exception {
        Long invoiceId = 1L;
        InvoiceDTO invoiceDTO = TestDataFactory.createValidInvoiceDTO();
        invoiceDTO.setId(invoiceId);

        when(invoiceService.getInvoiceById(invoiceId)).thenReturn(invoiceDTO);

        mockMvc.perform(get("/api/invoices/{id}", invoiceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value(invoiceId))
                .andExpect(jsonPath("$.invoiceNumber").value(invoiceDTO.getInvoiceNumber()));
    }

    @Test
    void updateInvoice_ValidInvoice_ReturnsUpdatedInvoice() throws Exception {
        Long invoiceId = 1L;
        InvoiceDTO inputDTO = TestDataFactory.createValidInvoiceDTO();
        InvoiceDTO updatedDTO = TestDataFactory.createValidInvoiceDTO();
        updatedDTO.setId(invoiceId);

        when(invoiceService.updateInvoice(eq(invoiceId), any(InvoiceDTO.class))).thenReturn(updatedDTO);

        mockMvc.perform(put("/api/invoices/{id}", invoiceId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._id").value(invoiceId))
                .andExpect(jsonPath("$.invoiceNumber").value(updatedDTO.getInvoiceNumber()));
    }

    @Test
    void deleteInvoice_ExistingId_ReturnsNoContent() throws Exception {
        long invoiceId = 1L;

        doNothing().when(invoiceService).deleteInvoice(invoiceId);

        mockMvc.perform(delete("/api/invoices/{id}", invoiceId))
                .andExpect(status().isNoContent());
    }

    @Test
    void getInvoices_ReturnsPagedResult() throws Exception {
        PaginatedResponse<InvoiceDTO> mockResponse = new PaginatedResponse<>(
                Collections.singletonList(TestDataFactory.createValidInvoiceDTO()),
                1, 1, 1
        );

        when(invoiceService.getInvoices(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/invoices")
                        .param("page", "1")
                        .param("limit", "10")
                        .param("sort", "invoiceNumber,desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentPage").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalItems").value(1))
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.items[0].invoiceNumber").exists());
    }

    @Test
    void getInvoiceStatistics_ReturnsCorrectStatistics() throws Exception {
        Map<String, Object> mockStatistics = new HashMap<>();
        mockStatistics.put("currentYearSum", 1000L);
        mockStatistics.put("allTimeSum", 5000L);
        mockStatistics.put("invoicesCount", 10L);

        when(invoiceService.getInvoiceStatistics()).thenReturn(mockStatistics);

        mockMvc.perform(get("/api/invoices/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentYearSum").value(1000))
                .andExpect(jsonPath("$.allTimeSum").value(5000))
                .andExpect(jsonPath("$.invoicesCount").value(10));
    }
}