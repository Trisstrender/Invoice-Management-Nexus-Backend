package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper interface for converting between InvoiceEntity and InvoiceDTO.
 * This interface uses MapStruct to generate the implementation.
 */
@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface InvoiceMapper {

    /**
     * Converts an InvoiceDTO to an InvoiceEntity.
     *
     * @param source The source InvoiceDTO
     * @return The resulting InvoiceEntity
     */
    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "seller", source = "seller")
    InvoiceEntity toEntity(InvoiceDTO source);

    /**
     * Converts an InvoiceEntity to an InvoiceDTO.
     *
     * @param source The source InvoiceEntity
     * @return The resulting InvoiceDTO
     */
    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "seller", source = "seller")
    InvoiceDTO toDTO(InvoiceEntity source);

    /**
     * Updates an existing InvoiceEntity with data from an InvoiceDTO.
     *
     * @param dto The source InvoiceDTO
     * @param entity The target InvoiceEntity to update
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "seller", ignore = true)
    void updateEntityFromDto(InvoiceDTO dto, @MappingTarget InvoiceEntity entity);
}