package cz.itnetwork.controller;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // Create a new invoice
    @PostMapping
    public InvoiceDTO createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.createInvoice(invoiceDTO);
    }

    // Retrieve a specific invoice by ID
    @GetMapping("/{invoiceId}")
    public InvoiceDTO getInvoice(@PathVariable Long invoiceId) {
        return invoiceService.getInvoiceById(invoiceId);
    }

    // Update an existing invoice by ID
    @PutMapping("/{invoiceId}")
    public InvoiceDTO updateInvoice(@PathVariable Long invoiceId, @RequestBody InvoiceDTO invoiceDTO) {
        return invoiceService.updateInvoice(invoiceId, invoiceDTO);
    }

    // Delete an invoice by ID
    @DeleteMapping("/{invoiceId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable Long invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

    // Retrieve a list of invoices with optional filtering parameters
    @GetMapping
    public List<InvoiceDTO> getInvoices(
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

    // Retrieve statistics related to invoices
    @GetMapping("/statistics")
    public Map<String, Object> getInvoiceStatistics() {
        return invoiceService.getInvoiceStatistics();
    }
}