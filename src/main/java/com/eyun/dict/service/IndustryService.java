package com.eyun.dict.service;

import com.eyun.dict.domain.Industry;
import com.eyun.dict.repository.IndustryRepository;
import com.eyun.dict.service.dto.IndustryDTO;
import com.eyun.dict.service.mapper.IndustryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing Industry.
 */
@Service
@Transactional
public class IndustryService {

    private final Logger log = LoggerFactory.getLogger(IndustryService.class);

    private final IndustryRepository industryRepository;

    private final IndustryMapper industryMapper;

    public IndustryService(IndustryRepository industryRepository, IndustryMapper industryMapper) {
        this.industryRepository = industryRepository;
        this.industryMapper = industryMapper;
    }

    /**
     * Save a industry.
     *
     * @param industryDTO the entity to save
     * @return the persisted entity
     */
    public IndustryDTO save(IndustryDTO industryDTO) {
        log.debug("Request to save Industry : {}", industryDTO);
        Industry industry = industryMapper.toEntity(industryDTO);
        industry = industryRepository.save(industry);
        return industryMapper.toDto(industry);
    }

    /**
     * Get all the industries.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IndustryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Industries");
        return industryRepository.findAll(pageable)
            .map(industryMapper::toDto);
    }

    /**
     * Get one industry by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public IndustryDTO findOne(Long id) {
        log.debug("Request to get Industry : {}", id);
        Industry industry = industryRepository.findOne(id);
        return industryMapper.toDto(industry);
    }

    /**
     * Delete the industry by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Industry : {}", id);
        industryRepository.delete(id);
    }
}
