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

/**
 * Implementation of the InvoiceService interface.
 * This class provides the business logic for managing invoices.
 */
@Service
public class InvoiceServiceImpl extends BaseService<InvoiceEntity, Long> implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceMapper invoiceMapper;
    private final PersonRepository personRepository;

    /**
     * Constructs a new InvoiceServiceImpl with the necessary dependencies.
     *
     * @param invoiceRepository the repository for invoice entities
     * @param invoiceMapper the mapper for converting between InvoiceEntity and InvoiceDTO
     * @param personRepository the repository for person entities
     */
    @Autowired
    public InvoiceServiceImpl(InvoiceRepository invoiceRepository,
                              InvoiceMapper invoiceMapper,
                              PersonRepository personRepository) {
        super(invoiceRepository, invoiceRepository);
        this.invoiceRepository = invoiceRepository;
        this.invoiceMapper = invoiceMapper;
        this.personRepository = personRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);

        entity.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                .orElseThrow(() -> new PersonNotFoundException("Buyer not found")));

        entity.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                .orElseThrow(() -> new PersonNotFoundException("Seller not found")));

        entity = create(entity);
        return invoiceMapper.toDTO(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PaginatedResponse<InvoiceDTO> getInvoices(Map<String, String> params) {
        Pageable pageable = PaginationUtils.createPageable(params);
        Specification<InvoiceEntity> spec = FilterUtils.createInvoiceSpecification(params);

        Page<InvoiceEntity> invoicePage = findAll(spec, pageable);

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

    /**
     * {@inheritDoc}
     */
    @Override
    public InvoiceDTO getInvoiceById(long id) {
        InvoiceEntity entity = findById(id)
                .orElseThrow(() -> new InvoiceNotFoundException("Invoice not found"));
        return invoiceMapper.toDTO(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO) {
        InvoiceEntity existingInvoice = findById(id)
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

        existingInvoice = update(existingInvoice);

        return invoiceMapper.toDTO(existingInvoice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteInvoice(long id) {
        delete(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> getInvoiceStatistics() {
        long currentYear = LocalDate.now().getYear();
        List<InvoiceEntity> allInvoices = invoiceRepository.findAll();

        long currentYearSum = allInvoices.stream()
                .filter(invoice -> invoice.getIssued().getYear() == currentYear)
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        long allTimeSum = allInvoices.stream()
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        long invoicesCount = allInvoices.size();

        Map<String, Object> statistics = new HashMap<>();
        statistics.put("currentYearSum", currentYearSum);
        statistics.put("allTimeSum", allTimeSum);
        statistics.put("invoicesCount", invoicesCount);

        return statistics;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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