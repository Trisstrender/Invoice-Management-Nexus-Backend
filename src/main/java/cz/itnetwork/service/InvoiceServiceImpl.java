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
        // Convert InvoiceDTO to InvoiceEntity
        InvoiceEntity entity = invoiceMapper.toEntity(invoiceDTO);

        // Fetch buyer from the database by ID, throw exception if not found
        entity.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                .orElseThrow(() -> new EntityNotFoundException("Buyer not found")));

        // Fetch seller from the database by ID, throw exception if not found
        entity.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                .orElseThrow(() -> new EntityNotFoundException("Seller not found")));

        // Save the entity to the repository and convert it back to DTO
        entity = invoiceRepository.save(entity);
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public List<InvoiceDTO> getInvoices(Map<String, String> params) {
        // Retrieve all invoices from the repository
        List<InvoiceEntity> invoices = invoiceRepository.findAll();

        // Apply filtering
        invoices = applyFilters(invoices, params);

        // Apply sorting
        invoices = applySorting(invoices, params.get("sort"));

        // Apply pagination
        invoices = applyPagination(invoices, params);

        // Convert the filtered list of entities to a list of DTOs
        return invoices.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

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

    private List<InvoiceEntity> applyPagination(List<InvoiceEntity> invoices, Map<String, String> params) {
        int page = params.containsKey("page") && !params.get("page").isEmpty() ? Integer.parseInt(params.get("page")) : 1;
        int limit = params.containsKey("limit") && !params.get("limit").isEmpty() ? Integer.parseInt(params.get("limit")) : 10;

        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, invoices.size());

        if (fromIndex >= invoices.size()) {
            return new ArrayList<>(); // Return empty list if page is out of range
        }

        return invoices.subList(fromIndex, toIndex);
    }

    @Override
    public InvoiceDTO getInvoiceById(long id) {
        // Fetch the invoice by ID, throw exception if not found
        InvoiceEntity entity = invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found"));
        // Convert the entity to DTO and return
        return invoiceMapper.toDTO(entity);
    }

    @Override
    public InvoiceDTO updateInvoice(long id, InvoiceDTO invoiceDTO) {
        // Fetch the existing invoice by ID, throw exception if not found
        InvoiceEntity existingInvoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        // Update the existing entity with data from the DTO
        invoiceMapper.updateEntityFromDto(invoiceDTO, existingInvoice);

        // Update buyer if provided in the DTO
        if (invoiceDTO.getBuyer() != null && invoiceDTO.getBuyer().getId() != null) {
            existingInvoice.setBuyer(personRepository.findById(invoiceDTO.getBuyer().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Buyer not found")));
        }

        // Update seller if provided in the DTO
        if (invoiceDTO.getSeller() != null && invoiceDTO.getSeller().getId() != null) {
            existingInvoice.setSeller(personRepository.findById(invoiceDTO.getSeller().getId())
                    .orElseThrow(() -> new EntityNotFoundException("Seller not found")));
        }

        // Save the updated entity and convert it back to DTO
        existingInvoice = invoiceRepository.save(existingInvoice);

        return invoiceMapper.toDTO(existingInvoice);
    }

    @Override
    public void deleteInvoice(long id) {
        // Delete the invoice by ID
        invoiceRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> getInvoiceStatistics() {
        // Get the current year
        long currentYear = LocalDate.now().getYear();

        // Calculate the sum of prices for invoices issued this year
        long currentYearSum = invoiceRepository.findAll().stream()
                .filter(invoice -> invoice.getIssued().getYear() == currentYear)
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        // Calculate the sum of prices for all invoices
        long allTimeSum = invoiceRepository.findAll().stream()
                .mapToLong(InvoiceEntity::getPrice)
                .sum();

        // Count the total number of invoices
        long invoicesCount = invoiceRepository.count();

        // Create a map to hold the statistics
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("currentYearSum", currentYearSum);
        statistics.put("allTimeSum", allTimeSum);
        statistics.put("invoicesCount", invoicesCount);

        return statistics;
    }

    @Override
    public List<InvoiceDTO> getPersonSales(String identificationNumber) {
        // Fetch invoices where the person is the seller by identification number
        List<InvoiceEntity> sales = invoiceRepository.findBySeller_IdentificationNumber(identificationNumber);
        // Convert the list of entities to a list of DTOs
        return sales.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<InvoiceDTO> getPersonPurchases(String identificationNumber) {
        // Fetch invoices where the person is the buyer by identification number
        List<InvoiceEntity> purchases = invoiceRepository.findByBuyer_IdentificationNumber(identificationNumber);
        // Convert the list of entities to a list of DTOs
        return purchases.stream().map(invoiceMapper::toDTO).collect(Collectors.toList());
    }
}