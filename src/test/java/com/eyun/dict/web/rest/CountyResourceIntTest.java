package com.eyun.dict.web.rest;

import com.eyun.dict.DictApp;

import com.eyun.dict.config.SecurityBeanOverrideConfiguration;

import com.eyun.dict.domain.County;
import com.eyun.dict.domain.City;
import com.eyun.dict.repository.CountyRepository;
import com.eyun.dict.service.CountyService;
import com.eyun.dict.service.dto.CountyDTO;
import com.eyun.dict.service.mapper.CountyMapper;
import com.eyun.dict.web.rest.errors.ExceptionTranslator;
import com.eyun.dict.service.dto.CountyCriteria;
import com.eyun.dict.service.CountyQueryService;

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
import java.util.List;

import static com.eyun.dict.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CountyResource REST controller.
 *
 * @see CountyResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DictApp.class, SecurityBeanOverrideConfiguration.class})
public class CountyResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PINYIN = "AAAAAAAAAA";
    private static final String UPDATED_PINYIN = "BBBBBBBBBB";

    @Autowired
    private CountyRepository countyRepository;

    @Autowired
    private CountyMapper countyMapper;

    @Autowired
    private CountyService countyService;

    @Autowired
    private CountyQueryService countyQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCountyMockMvc;

    private County county;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CountyResource countyResource = new CountyResource(countyService, countyQueryService);
        this.restCountyMockMvc = MockMvcBuilders.standaloneSetup(countyResource)
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
    public static County createEntity(EntityManager em) {
        County county = new County()
            .name(DEFAULT_NAME)
            .pinyin(DEFAULT_PINYIN);
        return county;
    }

    @Before
    public void initTest() {
        county = createEntity(em);
    }

    @Test
    @Transactional
    public void createCounty() throws Exception {
        int databaseSizeBeforeCreate = countyRepository.findAll().size();

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);
        restCountyMockMvc.perform(post("/api/counties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isCreated());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate + 1);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCounty.getPinyin()).isEqualTo(DEFAULT_PINYIN);
    }

    @Test
    @Transactional
    public void createCountyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = countyRepository.findAll().size();

        // Create the County with an existing ID
        county.setId(1L);
        CountyDTO countyDTO = countyMapper.toDto(county);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCountyMockMvc.perform(post("/api/counties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isBadRequest());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllCounties() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList
        restCountyMockMvc.perform(get("/api/counties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN.toString())));
    }

    @Test
    @Transactional
    public void getCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get the county
        restCountyMockMvc.perform(get("/api/counties/{id}", county.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(county.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.pinyin").value(DEFAULT_PINYIN.toString()));
    }

    @Test
    @Transactional
    public void getAllCountiesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where name equals to DEFAULT_NAME
        defaultCountyShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the countyList where name equals to UPDATED_NAME
        defaultCountyShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountiesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCountyShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the countyList where name equals to UPDATED_NAME
        defaultCountyShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCountiesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where name is not null
        defaultCountyShouldBeFound("name.specified=true");

        // Get all the countyList where name is null
        defaultCountyShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountiesByPinyinIsEqualToSomething() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where pinyin equals to DEFAULT_PINYIN
        defaultCountyShouldBeFound("pinyin.equals=" + DEFAULT_PINYIN);

        // Get all the countyList where pinyin equals to UPDATED_PINYIN
        defaultCountyShouldNotBeFound("pinyin.equals=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    public void getAllCountiesByPinyinIsInShouldWork() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where pinyin in DEFAULT_PINYIN or UPDATED_PINYIN
        defaultCountyShouldBeFound("pinyin.in=" + DEFAULT_PINYIN + "," + UPDATED_PINYIN);

        // Get all the countyList where pinyin equals to UPDATED_PINYIN
        defaultCountyShouldNotBeFound("pinyin.in=" + UPDATED_PINYIN);
    }

    @Test
    @Transactional
    public void getAllCountiesByPinyinIsNullOrNotNull() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);

        // Get all the countyList where pinyin is not null
        defaultCountyShouldBeFound("pinyin.specified=true");

        // Get all the countyList where pinyin is null
        defaultCountyShouldNotBeFound("pinyin.specified=false");
    }

    @Test
    @Transactional
    public void getAllCountiesByCityIsEqualToSomething() throws Exception {
        // Initialize the database
        City city = CityResourceIntTest.createEntity(em);
        em.persist(city);
        em.flush();
        county.setCity(city);
        countyRepository.saveAndFlush(county);
        Long cityId = city.getId();

        // Get all the countyList where city equals to cityId
        defaultCountyShouldBeFound("cityId.equals=" + cityId);

        // Get all the countyList where city equals to cityId + 1
        defaultCountyShouldNotBeFound("cityId.equals=" + (cityId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultCountyShouldBeFound(String filter) throws Exception {
        restCountyMockMvc.perform(get("/api/counties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(county.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].pinyin").value(hasItem(DEFAULT_PINYIN.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultCountyShouldNotBeFound(String filter) throws Exception {
        restCountyMockMvc.perform(get("/api/counties?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingCounty() throws Exception {
        // Get the county
        restCountyMockMvc.perform(get("/api/counties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Update the county
        County updatedCounty = countyRepository.findOne(county.getId());
        // Disconnect from session so that the updates on updatedCounty are not directly saved in db
        em.detach(updatedCounty);
        updatedCounty
            .name(UPDATED_NAME)
            .pinyin(UPDATED_PINYIN);
        CountyDTO countyDTO = countyMapper.toDto(updatedCounty);

        restCountyMockMvc.perform(put("/api/counties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isOk());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate);
        County testCounty = countyList.get(countyList.size() - 1);
        assertThat(testCounty.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCounty.getPinyin()).isEqualTo(UPDATED_PINYIN);
    }

    @Test
    @Transactional
    public void updateNonExistingCounty() throws Exception {
        int databaseSizeBeforeUpdate = countyRepository.findAll().size();

        // Create the County
        CountyDTO countyDTO = countyMapper.toDto(county);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restCountyMockMvc.perform(put("/api/counties")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(countyDTO)))
            .andExpect(status().isCreated());

        // Validate the County in the database
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteCounty() throws Exception {
        // Initialize the database
        countyRepository.saveAndFlush(county);
        int databaseSizeBeforeDelete = countyRepository.findAll().size();

        // Get the county
        restCountyMockMvc.perform(delete("/api/counties/{id}", county.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<County> countyList = countyRepository.findAll();
        assertThat(countyList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(County.class);
        County county1 = new County();
        county1.setId(1L);
        County county2 = new County();
        county2.setId(county1.getId());
        assertThat(county1).isEqualTo(county2);
        county2.setId(2L);
        assertThat(county1).isNotEqualTo(county2);
        county1.setId(null);
        assertThat(county1).isNotEqualTo(county2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CountyDTO.class);
        CountyDTO countyDTO1 = new CountyDTO();
        countyDTO1.setId(1L);
        CountyDTO countyDTO2 = new CountyDTO();
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
        countyDTO2.setId(countyDTO1.getId());
        assertThat(countyDTO1).isEqualTo(countyDTO2);
        countyDTO2.setId(2L);
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
        countyDTO1.setId(null);
        assertThat(countyDTO1).isNotEqualTo(countyDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(countyMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(countyMapper.fromId(null)).isNull();
    }
}
