package com.invoice.management.service;

import com.invoice.management.dto.InvoiceDTO;
import com.invoice.management.dto.PaginatedResponse;

import java.util.Map;

/**
 * Service interface for managing invoices.
 */
public interface InvoiceService {

    /**
     * Creates a new invoice.
     *
     * @param invoiceDTO the DTO containing the invoice data
     * @return the created invoice DTO
     */
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id the ID of the invoice to retrieve
     * @return the invoice DTO
     */
    InvoiceDTO getInvoiceById(long id);

    /**
     * Updates an existing invoice.
     *
     * @param id         the ID of the invoice to update
     * @param invoiceDTO the DTO containing the updated invoice data
     * @return the updated invoice DTO
     */
    InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO);

    /**
     * Deletes an invoice by its ID.
     *
     * @param id the ID of the invoice to delete
     */
    void deleteInvoice(long id);

    /**
     * Retrieves a paginated list of invoices based on the provided parameters.
     *
     * @param params a map of query parameters for filtering and sorting
     * @return a paginated response containing invoice DTOs
     */
    PaginatedResponse<InvoiceDTO> getInvoices(Map<String, String> params);

    /**
     * Retrieves invoice statistics.
     *
     * @return a map containing various invoice statistics
     */
    Map<String, Object> getInvoiceStatistics();

    /**
     * Retrieves a paginated list of sales invoices for a specific person.
     *
     * @param identificationNumber the identification number of the person
     * @param page                 the page number
     * @param limit                the number of items per page
     * @return a paginated response containing invoice DTOs
     */
    PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit);

    /**
     * Retrieves a paginated list of purchase invoices for a specific person.
     *
     * @param identificationNumber the identification number of the person
     * @param page                 the page number
     * @param limit                the number of items per page
     * @return a paginated response containing invoice DTOs
     */
    PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit);
}
