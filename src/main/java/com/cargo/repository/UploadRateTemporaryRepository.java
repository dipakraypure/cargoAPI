package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import com.cargo.models.UploadRateTemporaryEntity;


@Repository
public interface UploadRateTemporaryRepository extends JpaRepository<UploadRateTemporaryEntity, Long> {
	
	@Query("SELECT ue FROM UploadRateTemporaryEntity ue where ue.carrierid = (?1) AND ue.chargesgroupingcode = (?2) AND ue.errorstatus = (?3)")
	List<UploadRateTemporaryEntity> findByCarrierIdAndChargesGroupingIdAndErrorStatus(String carrierIdReq, String chargesGroupingCodeReq,String errorStatus);

}
