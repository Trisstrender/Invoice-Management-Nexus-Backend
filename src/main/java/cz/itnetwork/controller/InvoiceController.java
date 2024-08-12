package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InvoiceDTO createInvoice(@Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }

    @GetMapping("/{invoiceId}")
    public InvoiceDTO getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    @PutMapping("/{invoiceId}")
    public InvoiceDTO updateInvoice(@PathVariable Long invoiceId, @Valid @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(invoiceId, invoiceDTO);
    }

    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

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

    @GetMapping("/statistics")
    public Map<String, Object> getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }

    @GetMapping("/identification/{identificationNumber}/sales")
    public PaginatedResponse<InvoiceDTO> getPersonSales(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return invoiceService.getPersonSales(identificationNumber, page, limit);
    }

    @GetMapping("/identification/{identificationNumber}/purchases")
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(
            @PathVariable String identificationNumber,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        return invoiceService.getPersonPurchases(identificationNumber, page, limit);
    }
}