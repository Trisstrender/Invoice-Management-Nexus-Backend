package com.invoice.management.controller;

import com.invoice.management.dto.InvoiceDTO;
import com.invoice.management.dto.PaginatedResponse;
import com.invoice.management.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST Controller for managing invoices.
 * This controller handles CRUD operations for invoices and provides endpoints for
 * retrieving invoice statistics and filtered invoice lists.
 */
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Creates a new invoice.
     *
     * @param invoiceDTO The invoice data transfer object containing the invoice details
     * @return The created invoice DTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to retrieve
     * @return The invoice DTO
     */
    @GetMapping("/{invoiceId}")
    public InvoiceDTO getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    /**
     * Updates an existing invoice.
     *
     * @param invoiceId  The ID of the invoice to update
     * @param invoiceDTO The invoice data transfer object containing the updated invoice details
     * @return The updated invoice DTO
     */
    @PutMapping("/{invoiceId}")
    public InvoiceDTO updateInvoice(@PathVariable Long invoiceId, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(invoiceId, invoiceDTO);
    }

    /**
     * Deletes an invoice by its ID.
     *
     * @param invoiceId The ID of the invoice to delete
     */
    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    /**
     * Retrieves a paginated list of invoices based on the provided parameters.
     *
     * @param params A map of query parameters for filtering and sorting
     * @param sort   The sort order (default: "id,asc")
     * @param page   The page number (default: 1)
     * @param limit  The number of items per page (default: 10)
     * @return A paginated response containing the list of invoice DTOs
     */
    @GetMapping
    public PaginatedResponse<InvoiceDTO> getInvoices(
            @RequestParam Map<String, String> params,
            @RequestParam(defaultValue = "id,asc") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        params.put("sort", sort);
        params.put("page", String.valueOf(page));
        params.put("limit", String.valueOf(limit));
        return invoiceService.getInvoices(params);
    }

    /**
     * Retrieves invoice statistics.
     *
     * @return A map containing various invoice statistics
     */
    @GetMapping("/statistics")
    public Map<String, Object> getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }

    /**
     * Retrieves a paginated list of sales invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @param page                 The page number (default: 1)
     * @param limit                The number of items per page (default: 10)
     * @return A paginated response containing the list of sales invoice DTOs
     */
    @GetMapping("/identification/{identificationNumber}/sales")
    public PaginatedResponse<InvoiceDTO> getPersonSales(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return invoiceService.getPersonSales(identificationNumber, page, limit);
    }

    /**
     * Retrieves a paginated list of purchase invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @param page                 The page number (default: 1)
     * @param limit                The number of items per page (default: 10)
     * @return A paginated response containing the list of purchase invoice DTOs
     */
    @GetMapping("/identification/{identificationNumber}/purchases")
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return invoiceService.getPersonPurchases(identificationNumber, page, limit);
    }
}
