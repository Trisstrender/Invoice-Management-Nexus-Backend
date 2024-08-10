package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for managing Invoice-related operations.
 * This controller handles CRUD operations and other invoice-related functionalities.
 */
@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    /**
     * Creates a new invoice.
     *
     * @param invoiceDTO The invoice data transfer object containing invoice details
     * @return The created invoice DTO
     */
    @PostMapping
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }

    /**
     * Retrieves a specific invoice by ID.
     *
     * @param invoiceId The ID of the invoice to retrieve
     * @return The requested invoice DTO
     */
    @GetMapping("/{invoiceId}")
    public InvoiceDTO getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    /**
     * Updates an existing invoice.
     *
     * @param invoiceId  The ID of the invoice to update
     * @param invoiceDTO The updated invoice data transfer object
     * @return The updated invoice DTO
     */
    @PutMapping("/{invoiceId}")
    public InvoiceDTO updateInvoice(@PathVariable Long invoiceId, @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(invoiceId, invoiceDTO);
    }

    /**
     * Deletes an invoice.
     *
     * @param invoiceId The ID of the invoice to delete
     */
    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    /**
     * Retrieves a list of invoices with optional filtering, sorting, and pagination.
     *
     * @param params Map of query parameters for filtering and sorting
     * @param sort   Sorting parameter (default: "id,asc")
     * @param page   Page number (default: 1)
     * @param limit  Number of items per page (default: 10)
     * @return List of invoice DTOs matching the criteria
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
     * Retrieves statistics related to invoices.
     *
     * @return Map of invoice statistics
     */
    @GetMapping("/statistics")
    public Map<String, Object> getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }
}