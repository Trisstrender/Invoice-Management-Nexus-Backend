package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);
        entity = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public List<InvoiceDTO> getInvoices(Map<String, String> params) {
        // Implement filtering logic based on params
        List<InvoiceEntity> invoices = invoiceRepository.findAll(); // Replace with filtered query
        return invoices.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public InvoiceDTO getInvoiceById(long id) {
        InvoiceEntity entity = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO) {
        InvoiceEntity existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));

        // Update the existing invoice with new data
        invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);
        existingInvoice = invoiceRepository.save(existingInvoice);

        return invoiceMapper.toDTO(existingInvoice);
    }

    @Override
    public void deleteInvoice(long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getInvoiceStatistics() {
        // Implement statistics calculation
        // Add logic to calculate currentYearSum, allTimeSum, and invoicesCount
        return new HashMap<>();
    }

    @Override
    public List<InvoiceDTO> getPersonSales(String identificationNumber) {
        List<InvoiceEntity> sales = invoiceRepository.findBySeller_IdentificationNumber(identificationNumber);
        return sales.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getPersonPurchases(String identificationNumber) {
        List<InvoiceEntity> purchases = invoiceRepository.findByBuyer_IdentificationNumber(identificationNumber);
        return purchases.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }
}