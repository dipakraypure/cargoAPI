package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.CountrySpecificationEntity;

@Repository
public interface CountrySpecificationRepository extends JpaRepository<CountrySpecificationEntity, Long>{

	List<CountrySpecificationEntity> findByIsdeleted(String isdeleted);

	CountrySpecificationEntity findByCountrycodeAndIsdeleted(String countrycode, String isdeleted);

	CountrySpecificationEntity findByIdAndIsdeleted(Long idCs, String isdeleted);

	CountrySpecificationEntity findByCountrycodeAndStatusAndIsdeleted(String countrycode, String status,String isdeleted);

}
