package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.models.TransDetailsEntity;


@Repository
public interface TransDetailsRepository  extends JpaRepository<TransDetailsEntity, Long>  {


	@Query("SELECT ue FROM TransDetailsEntity ue where ue.isdeleted = (?3) AND ue.originid = (?1) AND ue.destinationid = (?2) ")
	List<TransDetailsEntity> findByOriginidAndDestinationidAndIsdeleted(long originid,long destinationid,String isdeleted);

	TransDetailsEntity findByIdAndIsdeleted(long id, String isdeleted);

	TransDetailsEntity findByOriginidAndDestinationidAndForwarderidAndIsdeleted(long originlocid, long destinationlocid,
			long forwarderid, String isdeleted);

	TransDetailsEntity findByForwarderidAndEnquiryreferenceAndIsdeleted(long forwarderid, String enquiryreference,
			String isdeleted);

	TransDetailsEntity findByEnquiryreferenceAndIsdeleted(String enquiryReference, String isdeleted);

}
 