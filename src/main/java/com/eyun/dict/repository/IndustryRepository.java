package com.eyun.dict.repository;

import com.eyun.dict.domain.Industry;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Industry entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IndustryRepository extends JpaRepository<Industry, Long> {

}
