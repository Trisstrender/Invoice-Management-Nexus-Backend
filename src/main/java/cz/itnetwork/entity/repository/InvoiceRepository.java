package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.InvoiceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface InvoiceRepository extends JpaRepository<InvoiceEntity, Long>, JpaSpecificationExecutor<InvoiceEntity> {
    Page<InvoiceEntity> findBySeller_IdentificationNumber(String identificationNumber, Pageable pageable);
    Page<InvoiceEntity> findByBuyer_IdentificationNumber(String identificationNumber, Pageable pageable);
}