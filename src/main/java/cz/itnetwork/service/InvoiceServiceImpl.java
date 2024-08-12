package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
import cz.itnetwork.dto.mapper.InvoiceMapper;
import cz.itnetwork.entity.InvoiceEntity;
import cz.itnetwork.entity.repository.InvoiceRepository;
import cz.itnetwork.entity.repository.PersonRepository;
import cz.itnetwork.exception.InvoiceNotFoundException;
import cz.itnetwork.exception.PersonNotFoundException;
import cz.itnetwork.utils.FilterUtils;
import cz.itnetwork.utils.PaginationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        entity.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                .orElseThrow(() -> new PersonNotFoundException("Buyer not found")));

        entity.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                .orElseThrow(() -> new PersonNotFoundException("Seller not found")));

        entity = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public PaginatedResponse<InvoiceDTO> getInvoices(Map<String, String> params) {
        Pageable pageable = PaginationUtils.createPageable(params);
        Specification<InvoiceEntity> spec = FilterUtils.createInvoiceSpecification(params);

        Page<InvoiceEntity> invoicePage = invoiceRepository.findAll(spec, pageable);

        List<InvoiceDTO> invoiceDTOs = invoicePage.getContent().stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                invoiceDTOs,
                invoicePage.getNumber() + 1,
                invoicePage.getTotalPages(),
                (int) invoicePage.getTotalElements()
        );
    }

    @Override
    public InvoiceDTO getInvoiceById(long id) {
        InvoiceEntity entity = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO) {
        InvoiceEntity existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));

        invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);

        if (invoiceDTO.getBuyer() != null && invoiceDTO.getBuyer().getId() != null) {
            existingInvoice.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                    .orElseThrow(() -> new PersonNotFoundException("Buyer not found")));
        }

        if (invoiceDTO.getSeller() != null && invoiceDTO.getSeller().getId() != null) {
            existingInvoice.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                    .orElseThrow(() -> new PersonNotFoundException("Seller not found")));
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
    public PaginatedResponse<InvoiceDTO> getPersonSales(String identificationNumber, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<InvoiceEntity> salesPage = invoiceRepository.findBySeller_IdentificationNumber(identificationNumber, pageable);

        List<InvoiceDTO> salesDTOs = salesPage.getContent().stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                salesDTOs,
                salesPage.getNumber() + 1,
                salesPage.getTotalPages(),
                (int) salesPage.getTotalElements()
        );
    }

    @Override
    public PaginatedResponse<InvoiceDTO> getPersonPurchases(String identificationNumber, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<InvoiceEntity> purchasesPage = invoiceRepository.findByBuyer_IdentificationNumber(identificationNumber, pageable);

        List<InvoiceDTO> purchasesDTOs = purchasesPage.getContent().stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(
                purchasesDTOs,
                purchasesPage.getNumber() + 1,
                purchasesPage.getTotalPages(),
                (int) purchasesPage.getTotalElements()
        );
    }
}