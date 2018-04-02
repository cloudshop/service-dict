package com.eyun.dict.web.rest;

import com.eyun.dict.DictApp;

import com.eyun.dict.config.SecurityBeanOverrideConfiguration;

import com.eyun.dict.domain.Industry;
import com.eyun.dict.repository.IndustryRepository;
import com.eyun.dict.service.IndustryService;
import com.eyun.dict.service.dto.IndustryDTO;
import com.eyun.dict.service.mapper.IndustryMapper;
import com.eyun.dict.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.eyun.dict.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the IndustryResource REST controller.
 *
 * @see IndustryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DictApp.class, SecurityBeanOverrideConfiguration.class})
public class IndustryResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATEDTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATEDTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_MODIFIEDTIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_MODIFIEDTIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MODIFIEDBY = 1L;
    private static final Long UPDATED_MODIFIEDBY = 2L;

    private static final Boolean DEFAULT_DELETED = false;
    private static final Boolean UPDATED_DELETED = true;

    private static final Integer DEFAULT_STATUS = 1;
    private static final Integer UPDATED_STATUS = 2;

    @Autowired
    private IndustryRepository industryRepository;

    @Autowired
    private IndustryMapper industryMapper;

    @Autowired
    private IndustryService industryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restIndustryMockMvc;

    private Industry industry;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IndustryResource industryResource = new IndustryResource(industryService);
        this.restIndustryMockMvc = MockMvcBuilders.standaloneSetup(industryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Industry createEntity(EntityManager em) {
        Industry industry = new Industry()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .createdtime(DEFAULT_CREATEDTIME)
            .modifiedtime(DEFAULT_MODIFIEDTIME)
            .modifiedby(DEFAULT_MODIFIEDBY)
            .deleted(DEFAULT_DELETED)
            .status(DEFAULT_STATUS);
        return industry;
    }

    @Before
    public void initTest() {
        industry = createEntity(em);
    }

    @Test
    @Transactional
    public void createIndustry() throws Exception {
        int databaseSizeBeforeCreate = industryRepository.findAll().size();

        // Create the Industry
        IndustryDTO industryDTO = industryMapper.toDto(industry);
        restIndustryMockMvc.perform(post("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryDTO)))
            .andExpect(status().isCreated());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeCreate + 1);
        Industry testIndustry = industryList.get(industryList.size() - 1);
        assertThat(testIndustry.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testIndustry.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testIndustry.getCreatedtime()).isEqualTo(DEFAULT_CREATEDTIME);
        assertThat(testIndustry.getModifiedtime()).isEqualTo(DEFAULT_MODIFIEDTIME);
        assertThat(testIndustry.getModifiedby()).isEqualTo(DEFAULT_MODIFIEDBY);
        assertThat(testIndustry.isDeleted()).isEqualTo(DEFAULT_DELETED);
        assertThat(testIndustry.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createIndustryWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = industryRepository.findAll().size();

        // Create the Industry with an existing ID
        industry.setId(1L);
        IndustryDTO industryDTO = industryMapper.toDto(industry);

        // An entity with an existing ID cannot be created, so this API call must fail
        restIndustryMockMvc.perform(post("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllIndustries() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);

        // Get all the industryList
        restIndustryMockMvc.perform(get("/api/industries?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(industry.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].createdtime").value(hasItem(DEFAULT_CREATEDTIME.toString())))
            .andExpect(jsonPath("$.[*].modifiedtime").value(hasItem(DEFAULT_MODIFIEDTIME.toString())))
            .andExpect(jsonPath("$.[*].modifiedby").value(hasItem(DEFAULT_MODIFIEDBY.intValue())))
            .andExpect(jsonPath("$.[*].deleted").value(hasItem(DEFAULT_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }

    @Test
    @Transactional
    public void getIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);

        // Get the industry
        restIndustryMockMvc.perform(get("/api/industries/{id}", industry.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(industry.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.createdtime").value(DEFAULT_CREATEDTIME.toString()))
            .andExpect(jsonPath("$.modifiedtime").value(DEFAULT_MODIFIEDTIME.toString()))
            .andExpect(jsonPath("$.modifiedby").value(DEFAULT_MODIFIEDBY.intValue()))
            .andExpect(jsonPath("$.deleted").value(DEFAULT_DELETED.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }

    @Test
    @Transactional
    public void getNonExistingIndustry() throws Exception {
        // Get the industry
        restIndustryMockMvc.perform(get("/api/industries/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);
        int databaseSizeBeforeUpdate = industryRepository.findAll().size();

        // Update the industry
        Industry updatedIndustry = industryRepository.findOne(industry.getId());
        // Disconnect from session so that the updates on updatedIndustry are not directly saved in db
        em.detach(updatedIndustry);
        updatedIndustry
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .createdtime(UPDATED_CREATEDTIME)
            .modifiedtime(UPDATED_MODIFIEDTIME)
            .modifiedby(UPDATED_MODIFIEDBY)
            .deleted(UPDATED_DELETED)
            .status(UPDATED_STATUS);
        IndustryDTO industryDTO = industryMapper.toDto(updatedIndustry);

        restIndustryMockMvc.perform(put("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryDTO)))
            .andExpect(status().isOk());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeUpdate);
        Industry testIndustry = industryList.get(industryList.size() - 1);
        assertThat(testIndustry.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testIndustry.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testIndustry.getCreatedtime()).isEqualTo(UPDATED_CREATEDTIME);
        assertThat(testIndustry.getModifiedtime()).isEqualTo(UPDATED_MODIFIEDTIME);
        assertThat(testIndustry.getModifiedby()).isEqualTo(UPDATED_MODIFIEDBY);
        assertThat(testIndustry.isDeleted()).isEqualTo(UPDATED_DELETED);
        assertThat(testIndustry.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingIndustry() throws Exception {
        int databaseSizeBeforeUpdate = industryRepository.findAll().size();

        // Create the Industry
        IndustryDTO industryDTO = industryMapper.toDto(industry);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restIndustryMockMvc.perform(put("/api/industries")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(industryDTO)))
            .andExpect(status().isCreated());

        // Validate the Industry in the database
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteIndustry() throws Exception {
        // Initialize the database
        industryRepository.saveAndFlush(industry);
        int databaseSizeBeforeDelete = industryRepository.findAll().size();

        // Get the industry
        restIndustryMockMvc.perform(delete("/api/industries/{id}", industry.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Industry> industryList = industryRepository.findAll();
        assertThat(industryList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Industry.class);
        Industry industry1 = new Industry();
        industry1.setId(1L);
        Industry industry2 = new Industry();
        industry2.setId(industry1.getId());
        assertThat(industry1).isEqualTo(industry2);
        industry2.setId(2L);
        assertThat(industry1).isNotEqualTo(industry2);
        industry1.setId(null);
        assertThat(industry1).isNotEqualTo(industry2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(IndustryDTO.class);
        IndustryDTO industryDTO1 = new IndustryDTO();
        industryDTO1.setId(1L);
        IndustryDTO industryDTO2 = new IndustryDTO();
        assertThat(industryDTO1).isNotEqualTo(industryDTO2);
        industryDTO2.setId(industryDTO1.getId());
        assertThat(industryDTO1).isEqualTo(industryDTO2);
        industryDTO2.setId(2L);
        assertThat(industryDTO1).isNotEqualTo(industryDTO2);
        industryDTO1.setId(null);
        assertThat(industryDTO1).isNotEqualTo(industryDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(industryMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(industryMapper.fromId(null)).isNull();
    }
}
