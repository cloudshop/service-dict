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

import com.eyun.dict.domain.County;
import com.eyun.dict.domain.*; // for static metamodels
import com.eyun.dict.repository.CountyRepository;
import com.eyun.dict.service.dto.CountyCriteria;

import com.eyun.dict.service.dto.CountyDTO;
import com.eyun.dict.service.mapper.CountyMapper;

/**
 * Service for executing complex queries for County entities in the database.
 * The main input is a {@link CountyCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyDTO} or a {@link Page} of {@link CountyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyQueryService extends QueryService<County> {

    private final Logger log = LoggerFactory.getLogger(CountyQueryService.class);


    private final CountyRepository countyRepository;

    private final CountyMapper countyMapper;

    public CountyQueryService(CountyRepository countyRepository, CountyMapper countyMapper) {
        this.countyRepository = countyRepository;
        this.countyMapper = countyMapper;
    }

    /**
     * Return a {@link List} of {@link CountyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyDTO> findByCriteria(CountyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<County> specification = createSpecification(criteria);
        return countyMapper.toDto(countyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountyDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyDTO> findByCriteria(CountyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<County> specification = createSpecification(criteria);
        final Page<County> result = countyRepository.findAll(specification, page);
        return result.map(countyMapper::toDto);
    }

    /**
     * Function to convert CountyCriteria to a {@link Specifications}
     */
    private Specifications<County> createSpecification(CountyCriteria criteria) {
        Specifications<County> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), County_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), County_.name));
            }
            if (criteria.getPinyin() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPinyin(), County_.pinyin));
            }
            if (criteria.getCityId() != null) {
                specification = specification.and(buildReferringEntitySpecification(criteria.getCityId(), County_.city, City_.id));
            }
        }
        return specification;
    }

}
