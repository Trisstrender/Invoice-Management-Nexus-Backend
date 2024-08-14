package cz.itnetwork.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public abstract class BaseService<T, ID> {

    protected final JpaRepository<T, ID> repository;
    protected final JpaSpecificationExecutor<T> specificationExecutor;

    public BaseService(JpaRepository<T, ID> repository, JpaSpecificationExecutor<T> specificationExecutor) {
        this.repository = repository;
        this.specificationExecutor = specificationExecutor;
    }

    public T create(T entity) {
        return repository.save(entity);
    }

    public Optional<T> findById(ID id) {
        return repository.findById(id);
    }

    public T update(T entity) {
        return repository.save(entity);
    }

    public void delete(ID id) {
        repository.deleteById(id);
    }

    public Page<T> findAll(Specification<T> spec, Pageable pageable) {
        return specificationExecutor.findAll(spec, pageable);
    }
}