package com.invoice.management.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * Abstract base class for service implementations.
 * Provides common CRUD operations and specification-based querying.
 *
 * @param <T>  the entity type
 * @param <ID> the type of the entity's identifier
 */
public abstract class BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specificationExecutor;

    /**
     * Constructs a new BaseService with the given repository and specification executor.
     *
     * @param repository            the JPA repository
     * @param specificationExecutor the JPA specification executor
     */
    public BaseService(JpaRepository<T, ID> repository, JpaSpecificationExecutor<T> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    /**
     * Creates a new entity.
     *
     * @param entity the entity to create
     * @return the created entity
     */
    public T create(T entity) {
        return repository.save(entity);
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity to find
     * @return an Optional containing the found entity, or an empty Optional if not found
     */
    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    /**
     * Updates an existing entity.
     *
     * @param entity the entity to update
     * @return the updated entity
     */
    public T update(T entity) {
        return repository.save(entity);
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     */
    public void delete(ID id) {
        repository.deleteById(id);
    }

    /**
     * Finds all entities matching the given specification.
     *
     * @param spec     the specification to apply
     * @param pageable the pagination information
     * @return a Page of entities matching the specification
     */
    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return specificationExecutor.findAll(spec, pageable);
    }
}
