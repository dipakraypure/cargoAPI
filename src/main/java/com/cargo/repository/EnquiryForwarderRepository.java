package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.load.response.EnquiryCountByStatusResponse;
import com.cargo.models.EnquiryForwarderEntity;

@Repository
public interface EnquiryForwarderRepository  extends JpaRepository<EnquiryForwarderEntity, Long>{

	@Query("select count(e) from EnquiryForwarderEntity e WHERE e.forwarderid=(?1) AND e.shipmenttype=(?2) AND e.isdeleted= (?3)")
	long enquiryDetailsEntityCoutByForwarderId(long userid,String shipmentType,String isdeleted);

	//get Enquiry count by status
	@Query("SELECT new com.cargo.load.response.EnquiryCountByStatusResponse("
		   +"SUM(CASE WHEN status = 'Requested' THEN 1 ELSE 0 END) AS requestCount,"
		   +"SUM(CASE WHEN status = 'Accepted' THEN 1 ELSE 0 END) AS acceptedCount,"
		   +"SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) AS cancelledCount,"
		   +"SUM(CASE WHEN status = 'Rejected' THEN 1 ELSE 0 END) AS rejectedCount)"
		   +"FROM  EnquiryForwarderEntity WHERE forwarderid= (?1) AND shipmenttype=(?2) AND isdeleted= (?3)")
	EnquiryCountByStatusResponse enquiryCountByForwarderIdAndIsdeletedAndGroupByStatus(long userid,String shipmentType ,String isdeleted);

	
	@Query(value="SELECT * FROM master_enquiry_forwarder u where u.forwarder_id=?1 AND u.shipment_type=?2 AND u.status like ?3 AND u.is_deleted = ?4 ",nativeQuery = true)
	List<EnquiryForwarderEntity> findByForwarderidAndStatusLikeAndIsdeleted(long userid,String shipmentType ,String status, String isdeleted);

	EnquiryForwarderEntity findByIdAndIsdeleted(long eid, String isdeleted);

	List<EnquiryForwarderEntity> findByEnquiryidAndIsdeleted(long enquiryId, String isdeleted);

	List<EnquiryForwarderEntity> findByEnquiryidAndStatusAndIsdeleted(long enquiryId, String status, String isdeleted);

	@Query("select count(e) from EnquiryForwarderEntity e WHERE e.enquiryreference=(?1)")
	long enquiryCountByEnquiryReference(String enquiryReference);

	List<EnquiryForwarderEntity> findByEnquiryreference(String enquiryReference);

	

}
