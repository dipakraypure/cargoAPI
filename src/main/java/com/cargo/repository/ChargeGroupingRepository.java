package com.cargo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.ChargesGroupingEntity;

@Repository
public interface ChargeGroupingRepository extends JpaRepository<ChargesGroupingEntity, Long>{

	List<ChargesGroupingEntity> findByIsdeleted(String isdeleted);

	ChargesGroupingEntity findByChargesgroupingcodeAndIsdeleted(String chargesgroupingcode, String isdeleted);

	ChargesGroupingEntity findByIdAndIsdeleted(Long chargegroupingid, String isdeleted);
	
}
