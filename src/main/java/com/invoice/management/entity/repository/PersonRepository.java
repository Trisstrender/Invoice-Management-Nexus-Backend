package com.invoice.management.entity.repository;

import com.invoice.management.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Repository interface for Person entities.
 * Extends JpaRepository to inherit basic CRUD operations and JpaSpecificationExecutor for specification-based querying.
 */
public interface PersonRepository extends JpaRepository<PersonEntity, Long>, JpaSpecificationExecutor<PersonEntity> {
    // This interface doesn't define any custom methods.
    // It inherits standard CRUD operations from JpaRepository and
    // advanced querying capabilities from JpaSpecificationExecutor.
}
