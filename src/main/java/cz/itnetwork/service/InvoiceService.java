package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;

import java.util.List;
import java.util.Map;

public interface InvoiceService {
    InvoiceDTO createInvoice(InvoiceDTO invoiceDTO);
    List<InvoiceDTO> getInvoices(Map<String, String> params);
    InvoiceDTO getInvoiceById(long id);
    InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO);
    void deleteInvoice(long id);
    Map<String, Object> getInvoiceStatistics();
    List<InvoiceDTO> getPersonSales(String identificationNumber);
    List<InvoiceDTO> getPersonPurchases(String identificationNumber);
}