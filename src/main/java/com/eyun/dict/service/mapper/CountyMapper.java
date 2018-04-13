package com.eyun.dict.service.mapper;

import com.eyun.dict.domain.*;
import com.eyun.dict.service.dto.CountyDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity County and its DTO CountyDTO.
 */
@Mapper(componentModel = "spring", uses = {CityMapper.class})
public interface CountyMapper extends EntityMapper<CountyDTO, County> {

    @Mapping(source = "city.id", target = "cityId")
    CountyDTO toDto(County county);

    @Mapping(source = "cityId", target = "city")
    County toEntity(CountyDTO countyDTO);

    default County fromId(Long id) {
        if (id == null) {
            return null;
        }
        County county = new County();
        county.setId(id);
        return county;
    }
}
