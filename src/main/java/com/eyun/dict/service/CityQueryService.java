package com.eyun.dict.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.eyun.dict.domain.City;
import com.eyun.dict.domain.*; // for static metamodels
import com.eyun.dict.repository.CityRepository;
import com.eyun.dict.service.dto.CityCriteria;

import com.eyun.dict.service.dto.CityDTO;
import com.eyun.dict.service.mapper.CityMapper;

/**
 * Service for executing complex queries for City entities in the database.
 * The main input is a {@link CityCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CityDTO} or a {@link Page} of {@link CityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CityQueryService extends QueryService<City> {

    private final Logger log = LoggerFactory.getLogger(CityQueryService.class);


    private final CityRepository cityRepository;

    private final CityMapper cityMapper;

    public CityQueryService(CityRepository cityRepository, CityMapper cityMapper) {
        this.cityRepository = cityRepository;
        this.cityMapper = cityMapper;
    }

    /**
     * Return a {@link List} of {@link CityDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CityDTO> findByCriteria(CityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<City> specification = createSpecification(criteria);
        return cityMapper.toDto(cityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CityDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CityDTO> findByCriteria(CityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<City> specification = createSpecification(criteria);
        final Page<City> result = cityRepository.findAll(specification, page);
        return result.map(cityMapper::toDto);
    }

    /**
     * Function to convert CityCriteria to a {@link Specifications}
     */
    private Specifications<City> createSpecification(CityCriteria criteria) {
        Specifications<City> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), City_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), City_.name));
            }
            if (criteria.getPinyin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPinyin(), City_.pinyin));
            }
            if (criteria.getCountyId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCountyId(), City_.counties, County_.id));
            }
            if (criteria.getProvinceId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getProvinceId(), City_.province, Province_.id));
            }
        }
        return specification;
    }

}
