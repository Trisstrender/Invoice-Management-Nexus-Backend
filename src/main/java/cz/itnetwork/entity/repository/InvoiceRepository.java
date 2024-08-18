package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for Invoice entities.
 * Extends JpaRepository to inherit basic CRUD operations and JpaSpecificationExecutor for specification-based querying.
 */
public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {

    /**
     * Finds all invoices where the given identification number matches the seller's identification number.
     *
     * @param identificationNumber the identification number of the seller
     * @param pageable             pagination information
     * @return a Page of InvoiceEntity objects
     */
    Page<InvoiceEntity> findBySeller_IdentificationNumber(String identificationNumber, Pageable pageable);

    /**
     * Finds all invoices where the given identification number matches the buyer's identification number.
     *
     * @param identificationNumber the identification number of the buyer
     * @param pageable             pagination information
     * @return a Page of InvoiceEntity objects
     */
    Page<InvoiceEntity> findByBuyer_IdentificationNumber(String identificationNumber, Pageable pageable);
}