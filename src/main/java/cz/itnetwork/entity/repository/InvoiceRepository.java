package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long> {
    // Find invoices by seller's identification number
    List<InvoiceEntity> findBySeller_IdentificationNumber(String identificationNumber);

    // Find invoices by buyer's identification number
    List<InvoiceEntity> findByBuyer_IdentificationNumber(String identificationNumber);
}