package com.invoice.management.dto.mapper;

import com.invoice.management.dto.PersonDTO;
import com.invoice.management.entity.PersonEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between PersonEntity and PersonDTO.
 * This interface uses MapStruct to generate the implementation.
 */
@Mapper(componentModel = "spring")
public interface PersonMapper {

    /**
     * Converts a PersonDTO to a PersonEntity.
     *
     * @param source The source PersonDTO
     * @return The resulting PersonEntity
     */
    @Mapping(target = "purchases", ignore = true)
    @Mapping(target = "sales", ignore = true)
    PersonEntity toEntity(PersonDTO source);

    /**
     * Converts a PersonEntity to a PersonDTO.
     *
     * @param source The source PersonEntity
     * @return The resulting PersonDTO
     */
    PersonDTO toDTO(PersonEntity source);
}
