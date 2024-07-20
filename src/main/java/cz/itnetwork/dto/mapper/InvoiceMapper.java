package cz.itnetwork.dto.mapper;

import cz.itnetwork.dto.InvoiceDTO;
import cz.itnetwork.entity.InvoiceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {PersonMapper.class})
public interface InvoiceMapper {

    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "seller", source = "seller")
    InvoiceEntity toEntity(InvoiceDTO source);

    @Mapping(target = "buyer", source = "buyer")
    @Mapping(target = "seller", source = "seller")
    InvoiceDTO toDTO(InvoiceEntity source);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "buyer", ignore = true)
    @Mapping(target = "seller", ignore = true)
    void updateEntityFromDto(InvoiceDTO dto, @MappingTarget InvoiceEntity entity);
}