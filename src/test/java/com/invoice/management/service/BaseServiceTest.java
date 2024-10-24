package com.invoice.management.service;

import lombok.Data;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BaseServiceTest {

    @Mock
    private JpaRepository<TestEntity, Long> repository;

    @Mock
    private JpaSpecificationExecutor<TestEntity> specificationExecutor;

    private BaseService<TestEntity, Long> baseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        baseService = new BaseService<>(repository, specificationExecutor) {
        };
    }

    @Test
    void create_ValidEntity_ReturnsCreatedEntity() {
        TestEntity entity = new TestEntity();
        when(repository.save(entity)).thenReturn(entity);

        TestEntity result = baseService.create(entity);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void findById_ExistingId_ReturnsEntity() {
        Long id = 1L;
        TestEntity entity = new TestEntity();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        Optional<TestEntity> result = baseService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(entity, result.get());
    }

    @Test
    void update_ValidEntity_ReturnsUpdatedEntity() {
        TestEntity entity = new TestEntity();
        when(repository.save(entity)).thenReturn(entity);

        TestEntity result = baseService.update(entity);

        assertNotNull(result);
        verify(repository).save(entity);
    }

    @Test
    void delete_ExistingId_DeletesEntity() {
        Long id = 1L;
        doNothing().when(repository).deleteById(id);

        baseService.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void findAll_WithSpecificationAndPageable_ReturnsPage() {
        Specification<TestEntity> spec = mock(Specification.class);
        Pageable pageable = mock(Pageable.class);
        Page<TestEntity> page = mock(Page.class);
        when(specificationExecutor.findAll(spec, pageable)).thenReturn(page);

        Page<TestEntity> result = baseService.findAll(spec, pageable);

        assertNotNull(result);
        assertEquals(page, result);
    }

    @Data
    private static class TestEntity {
        private Long id;
        private String name;
    }
}
