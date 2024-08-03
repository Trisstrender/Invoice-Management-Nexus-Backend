package cz.itnetwork.entity.repository;

import cz.itnetwork.entity.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Person entities.
 * This interface provides CRUD operations and custom query methods for PersonEntity.
 */
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {

    /**
     * Finds persons by their hidden status.
     *
     * @param hidden The hidden status to search for
     * @return List of persons with the specified hidden status
     */
    List<PersonEntity> findByHidden(boolean hidden);
}