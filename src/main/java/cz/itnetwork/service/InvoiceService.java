package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;

import java.util.Map;

public interface InvoiceService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    InvoiceDTO getInvoiceById(long id);
    InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO);
    void deleteInvoice(long id);
    PaginatedResponse<InvoiceDTO> getInvoices(Map<String, String> params);
    Map<String, Object> getInvoiceStatistics();
    PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit);
    PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit);
}