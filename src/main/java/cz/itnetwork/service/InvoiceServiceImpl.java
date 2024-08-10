package cz.itnetwork.service;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.dto.PaginatedResponse;
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

/**
 * Implementation of the InvoiceService interface.
 * This class provides the business logic for invoice-related operations.
 */
@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private InvoiceMapper invoiceMapper;

    /**
     * Creates a new invoice.
     *
     * @param invoiceDTO The invoice data to create
     * @return The created invoice DTO
     * @throws EntityNotFoundException if the buyer or seller is not found
     */
    @Override
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);

        entity.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found")));

        entity.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found")));

        entity = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(entity);
    }

    /**
     * Retrieves a list of invoices based on given parameters.
     *
     * @param params Map of parameters for filtering, sorting, and pagination
     * @return List of invoice DTOs matching the criteria
     */
    @Override
    public PaginatedResponse<InvoiceDTO> getInvoices(Map<String, String> params) {
        List<InvoiceEntity> invoices = invoiceRepository.findAll();

        invoices = applyFilters(invoices, params);
        invoices = applySorting(invoices, params.get("sort"));

        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int limit = Integer.parseInt(params.getOrDefault("limit", "10"));

        int totalItems = invoices.size();
        int totalPages = (int) Math.ceil((double) totalItems / limit);

        invoices = applyPagination(invoices, page, limit);

        List<InvoiceDTO> invoiceDTOs = invoices.stream()
                .map(invoiceMapper::toDTO)
                .collect(Collectors.toList());

        return new PaginatedResponse<>(invoiceDTOs, page, totalPages, totalItems);
    }

    /**
     * Applies filters to the list of invoices based on the provided parameters.
     * Filters include buyerID, sellerID, product, minPrice, and maxPrice.
     *
     * @param invoices The list of invoices to filter
     * @param params   The filter parameters
     * @return The filtered list of invoices
     */
    private List<InvoiceEntity> applyFilters(List<InvoiceEntity> invoices, Map<String, String> params) {
        if (params.containsKey("buyerID") && !params.get("buyerID").isEmpty()) {
            long buyerID = Long.parseLong(params.get("buyerID"));
            invoices = invoices.stream()
                    .filter(invoice -> invoice.getBuyer().getId() == buyerID)
                    .collect(Collectors.toList());
        }

        if (params.containsKey("sellerID") && !params.get("sellerID").isEmpty()) {
            long sellerID = Long.parseLong(params.get("sellerID"));
            invoices = invoices.stream()
                    .filter(invoice -> invoice.getSeller().getId() == sellerID)
                    .collect(Collectors.toList());
        }

        if (params.containsKey("product") && !params.get("product").isEmpty()) {
            String product = params.get("product");
            invoices = invoices.stream()
                    .filter(invoice -> invoice.getProduct().toLowerCase().contains(product.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (params.containsKey("minPrice") && !params.get("minPrice").isEmpty()) {
            long minPrice = Long.parseLong(params.get("minPrice"));
            invoices = invoices.stream()
                    .filter(invoice -> invoice.getPrice() >= minPrice)
                    .collect(Collectors.toList());
        }

        if (params.containsKey("maxPrice") && !params.get("maxPrice").isEmpty()) {
            long maxPrice = Long.parseLong(params.get("maxPrice"));
            invoices = invoices.stream()
                    .filter(invoice -> invoice.getPrice() <= maxPrice)
                    .collect(Collectors.toList());
        }

        return invoices;
    }

    /**
     * Applies sorting to the list of invoices based on the provided sort parameter.
     * Supports sorting by invoiceNumber, issued date, product, and price.
     *
     * @param invoices  The list of invoices to sort
     * @param sortParam The sort parameter
     * @return The sorted list of invoices
     */
    private List<InvoiceEntity> applySorting(List<InvoiceEntity> invoices, String sortParam) {
        if (sortParam != null && !sortParam.isEmpty()) {
            String[] sortParams = sortParam.split(",");
            String field = sortParams[0];
            boolean ascending = sortParams.length == 1 || sortParams[1].equalsIgnoreCase("asc");

            Comparator<InvoiceEntity> comparator = switch (field) {
                case "invoiceNumber" -> Comparator.comparing(InvoiceEntity::getInvoiceNumber);
                case "issued" -> Comparator.comparing(InvoiceEntity::getIssued);
                case "product" -> Comparator.comparing(InvoiceEntity::getProduct);
                case "price" -> Comparator.comparing(InvoiceEntity::getPrice);
                default -> Comparator.comparing(InvoiceEntity::getId);
            };

            if (!ascending) {
                comparator = comparator.reversed();
            }

            return invoices.stream().sorted(comparator).collect(Collectors.toList());
        }
        return invoices;
    }

    /**
     * Applies pagination to the list of invoices based on the provided page and limit parameters.
     *
     * @param invoices The list of invoices to paginate
     * @param page     The page number
     * @param limit    The number of items per page
     * @return The paginated list of invoices
     */
    private List<InvoiceEntity> applyPagination(List<InvoiceEntity> invoices, int page, int limit) {
        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, invoices.size());

        if (fromIndex >= invoices.size()) {
            return new ArrayList<>();
        }

        return invoices.subList(fromIndex, toIndex);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param id The ID of the invoice to retrieve
     * @return The retrieved invoice DTO
     * @throws NotFoundException if the invoice is not found
     */
    @Override
    public InvoiceDTO getInvoiceById(long id) {
        InvoiceEntity entity = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));
        return invoiceMapper.toDTO(entity);
    }

    /**
     * Updates an existing invoice.
     *
     * @param id         The ID of the invoice to update
     * @param invoiceDTO The updated invoice data
     * @return The updated invoice DTO
     * @throws EntityNotFoundException if the invoice, buyer, or seller is not found
     */
    @Override
    public InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO) {
        InvoiceEntity existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);

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

    /**
     * Deletes an invoice by its ID.
     *
     * @param id The ID of the invoice to delete
     */
    @Override
    public void deleteInvoice(long id) {
        invoiceRepository.deleteById(id);
    }

    /**
     * Retrieves invoice statistics.
     *
     * @return Map of invoice statistics
     */
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

    /**
     * Retrieves sales invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's sales
     */
    @Override
    public List<InvoiceDTO> getPersonSales(String identificationNumber) {
        List<InvoiceEntity> sales = invoiceRepository.findBySeller_IdentificationNumber(identificationNumber);
        return sales.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

    /**
     * Retrieves purchase invoices for a specific person.
     *
     * @param identificationNumber The identification number of the person
     * @return List of invoice DTOs representing the person's purchases
     */
    @Override
    public List<InvoiceDTO> getPersonPurchases(String identificationNumber) {
        List<InvoiceEntity> purchases = invoiceRepository.findByBuyer_IdentificationNumber(identificationNumber);
        return purchases.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }
}