package com.eyun.dict.repository;

import com.eyun.dict.domain.County;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the County entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CountyRepository extends JpaRepository<County, Long>, JpaSpecificationExecutor<County> {

}
