package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargo.models.ChargesRateEntity;


@Repository
public interface ChargesRateRepository  extends JpaRepository<ChargesRateEntity, Long>{

	@Query("SELECT ue FROM ChargesRateEntity ue where ue.forwarderid=:forwarderid AND ue.originlocid=:originlocid AND ue.destinationlocid=:destinationlocid AND ue.isdeleted =:isdeleted")
	List<ChargesRateEntity> findChargeRateByOriginlocidAndDestinationlocidAndForwarderidAndIsDeleted(long originlocid,
			long destinationlocid, Long forwarderid, String isdeleted);

	List<ChargesRateEntity> findByOriginlocidAndDestinationlocidAndForwarderidAndIsdeleted(long originId,
			long destinationId, long userId, String isdeleted);

	ChargesRateEntity findByIdAndIsdeleted(Long chargeId, String isdeleted);

	List<ChargesRateEntity> findByForwarderidAndEnquiryreferenceAndIsdeleted(long forwarderid, String enquiryReference,
			String isdeleted);

	List<ChargesRateEntity> findByEnquiryreferenceAndIsdeleted(String enquiryReference, String isdeleted);

	@Modifying  
	@Transactional
    @Query(value = "DELETE FROM ChargesRateEntity e WHERE e.enquiryreference = ?1") 
	int deleteAllByEnquiryReference(String enquiryReference);

	

}
