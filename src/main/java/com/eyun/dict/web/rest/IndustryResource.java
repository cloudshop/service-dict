package com.eyun.dict.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.eyun.dict.service.IndustryService;
import com.eyun.dict.web.rest.errors.BadRequestAlertException;
import com.eyun.dict.web.rest.util.HeaderUtil;
import com.eyun.dict.web.rest.util.PaginationUtil;
import com.eyun.dict.service.dto.IndustryDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Industry.
 */
@RestController
@RequestMapping("/api")
public class IndustryResource {

    private final Logger log = LoggerFactory.getLogger(IndustryResource.class);

    private static final String ENTITY_NAME = "industry";

    private final IndustryService industryService;

    public IndustryResource(IndustryService industryService) {
        this.industryService = industryService;
    }

    /**
     * POST  /industries : Create a new industry.
     *
     * @param industryDTO the industryDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new industryDTO, or with status 400 (Bad Request) if the industry has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/industries")
    @Timed
    public ResponseEntity<IndustryDTO> createIndustry(@RequestBody IndustryDTO industryDTO) throws URISyntaxException {
        log.debug("REST request to save Industry : {}", industryDTO);
        if (industryDTO.getId() != null) {
            throw new BadRequestAlertException("A new industry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IndustryDTO result = industryService.save(industryDTO);
        return ResponseEntity.created(new URI("/api/industries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /industries : Updates an existing industry.
     *
     * @param industryDTO the industryDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated industryDTO,
     * or with status 400 (Bad Request) if the industryDTO is not valid,
     * or with status 500 (Internal Server Error) if the industryDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/industries")
    @Timed
    public ResponseEntity<IndustryDTO> updateIndustry(@RequestBody IndustryDTO industryDTO) throws URISyntaxException {
        log.debug("REST request to update Industry : {}", industryDTO);
        if (industryDTO.getId() == null) {
            return createIndustry(industryDTO);
        }
        IndustryDTO result = industryService.save(industryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, industryDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /industries : get all the industries.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of industries in body
     */
    @GetMapping("/industries")
    @Timed
    public ResponseEntity<List<IndustryDTO>> getAllIndustries(Pageable pageable) {
        log.debug("REST request to get a page of Industries");
        Page<IndustryDTO> page = industryService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/industries");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /industries/:id : get the "id" industry.
     *
     * @param id the id of the industryDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the industryDTO, or with status 404 (Not Found)
     */
    @GetMapping("/industries/{id}")
    @Timed
    public ResponseEntity<IndustryDTO> getIndustry(@PathVariable Long id) {
        log.debug("REST request to get Industry : {}", id);
        IndustryDTO industryDTO = industryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(industryDTO));
    }

    /**
     * DELETE  /industries/:id : delete the "id" industry.
     *
     * @param id the id of the industryDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/industries/{id}")
    @Timed
    public ResponseEntity<Void> deleteIndustry(@PathVariable Long id) {
        log.debug("REST request to delete Industry : {}", id);
        industryService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
