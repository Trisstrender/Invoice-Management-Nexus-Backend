package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Invoice entities.
 * This interface provides CRUD operations and custom query methods for InvoiceEntity.
 */
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {

    /**
     * Finds invoices by seller's identification number.
     *
     * @param identificationNumber The identification number of the seller
     * @return List of invoices where the seller has the given identification number
     */
    List<InvoiceEntity> findBySeller_IdentificationNumber(String identificationNumber);

    /**
     * Finds invoices by buyer's identification number.
     *
     * @param identificationNumber The identification number of the buyer
     * @return List of invoices where the buyer has the given identification number
     */
    List<InvoiceEntity> findByBuyer_IdentificationNumber(String identificationNumber);
}