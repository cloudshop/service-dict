package com.eyun.dict.service.impl;

import com.eyun.dict.service.CountyService;
import com.eyun.dict.domain.County;
import com.eyun.dict.repository.CountyRepository;
import com.eyun.dict.service.dto.CountyDTO;
import com.eyun.dict.service.mapper.CountyMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing County.
 */
@Service
@Transactional
public class CountyServiceImpl implements CountyService {

    private final Logger log = LoggerFactory.getLogger(CountyServiceImpl.class);

    private final CountyRepository countyRepository;

    private final CountyMapper countyMapper;

    public CountyServiceImpl(CountyRepository countyRepository, CountyMapper countyMapper) {
        this.countyRepository = countyRepository;
        this.countyMapper = countyMapper;
    }

    /**
     * Save a county.
     *
     * @param countyDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CountyDTO save(CountyDTO countyDTO) {
        log.debug("Request to save County : {}", countyDTO);
        County county = countyMapper.toEntity(countyDTO);
        county = countyRepository.save(county);
        return countyMapper.toDto(county);
    }

    /**
     * Get all the counties.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CountyDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Counties");
        return countyRepository.findAll(pageable)
            .map(countyMapper::toDto);
    }

    /**
     * Get one county by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public CountyDTO findOne(Long id) {
        log.debug("Request to get County : {}", id);
        County county = countyRepository.findOne(id);
        return countyMapper.toDto(county);
    }

    /**
     * Delete the county by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete County : {}", id);
        countyRepository.delete(id);
    }
}
