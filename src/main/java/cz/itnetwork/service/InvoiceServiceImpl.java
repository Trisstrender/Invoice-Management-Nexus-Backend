package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);

        // Fetch buyer and seller from database
        entity.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found")));
        entity.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found")));

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
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        // Update the existing invoice with new data
        invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);

        // Update buyer and seller if they've changed
        if (invoiceDTO.getBuyer() != null && invoiceDTO.getBuyer().getId() != null) {
            existingInvoice.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Buyer not found")));
        }
        if (invoiceDTO.getSeller() != null && invoiceDTO.getSeller().getId() != null) {
            existingInvoice.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found")));
        }

        existingInvoice = invoiceRepository.save(existingInvoice);

        return invoiceMapper.toDTO(existingInvoice);
    }

    @Override
    public void deleteInvoice(long id) {
        invoiceRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getInvoiceStatistics() {
        long currentYear = LocalDate.now().getYear();

        long currentYearSum = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getIssued().getYear() == currentYear)
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        long allTimeSum = invoiceRepository.findAll().stream()
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        long invoicesCount = invoiceRepository.count();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("currentYearSum", currentYearSum);
        statistics.put("allTimeSum", allTimeSum);
        statistics.put("invoicesCount", invoicesCount);

        return statistics;
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