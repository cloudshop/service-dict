package com.eyun.dict.service.mapper;

import com.eyun.dict.domain.*;
import com.eyun.dict.service.dto.IndustryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Industry and its DTO IndustryDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface IndustryMapper extends EntityMapper<IndustryDTO, Industry> {

    @Mapping(source = "child.id", target = "childId")
    IndustryDTO toDto(Industry industry);

    @Mapping(target = "parents", ignore = true)
    @Mapping(source = "childId", target = "child")
    Industry toEntity(IndustryDTO industryDTO);

    default Industry fromId(Long id) {
        if (id == null) {
            return null;
        }
        Industry industry = new Industry();
        industry.setId(id);
        return industry;
    }
}
