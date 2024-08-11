package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.PersonEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for Person entities.
 * This interface provides CRUD operations and custom query methods for PersonEntity.
 */
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    Page<PersonEntity> findByNameContainingAndIdentificationNumberContainingAndHiddenFalse(String name, String identificationNumber, Pageable pageable);

    Page<PersonEntity> findByHiddenFalse(Pageable pageable);
}