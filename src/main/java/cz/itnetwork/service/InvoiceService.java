package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;

import java.util.List;
import java.util.Map;

public interface InvoiceService {

    // CRUD operations
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);

    InvoiceDTO getInvoiceById(long id);

    InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO);

    void deleteInvoice(long id);

    // Retrieval operations
    List<InvoiceDTO> getInvoices(Map<String, String> params);

    // Statistics and reporting
    Map<String, Object> getInvoiceStatistics();

    // Person-specific operations
    List<InvoiceDTO> getPersonSales(String identificationNumber);

    List<InvoiceDTO> getPersonPurchases(String identificationNumber);
}