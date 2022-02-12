package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.ChargesTypeEntity;

@Repository
public interface ChargesTypeRepository  extends JpaRepository<ChargesTypeEntity, Long>{

	List<ChargesTypeEntity> findByIsdeleted(String isdeleted);

	ChargesTypeEntity findByIdAndIsdeleted(long chargetypeid, String isdeleted);

	List<ChargesTypeEntity> findByChargesgroupingidAndIsdeleted(long chargesgroupingid, String isdeleted);

}
