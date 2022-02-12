package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.load.response.EnquiryCountByStatusResponse;
import com.cargo.models.EnquiryEntity;


@Repository
public interface EnquiryRepository extends JpaRepository<EnquiryEntity, Long> {
	
	@Query(value="SELECT * FROM master_enquiry u where u.user_id=?1 AND u.status like ?2 AND is_deleted = ?3 ",nativeQuery = true)
	List<EnquiryEntity> findByUseridAndStatusLikeAndIsdeleted(long userid, String status, String isdeleted);

	@Query("select count(e) from EnquiryEntity e WHERE e.userid=(?1) AND e.shipmenttype=(?2) AND e.isdeleted= (?3)")
	long enquiryDetailsEntityCoutByUserId(long userid,String shipmentType,String isdeleted);

	//get booking count by status
	@Query("SELECT new com.cargo.load.response.EnquiryCountByStatusResponse("
		   +"SUM(CASE WHEN status = 'Requested' THEN 1 ELSE 0 END) AS requestCount,"
		   +"SUM(CASE WHEN status = 'Accepted' THEN 1 ELSE 0 END) AS acceptedCount,"
		   +"SUM(CASE WHEN status = 'Cancelled' THEN 1 ELSE 0 END) AS cancelledCount,"
		   +"SUM(CASE WHEN status = 'Rejected' THEN 1 ELSE 0 END) AS rejectedCount)"
		   +"FROM  EnquiryEntity WHERE userid= (?1) AND shipmenttype=(?2) AND isdeleted= (?3)")
	EnquiryCountByStatusResponse enquiryCountByUserIdAndIsdeletedAndGroupByStatus(long userid,String shipmentType ,String isdeleted);

	@Query(value="SELECT * FROM master_enquiry u where u.user_id=?1 AND u.shipment_type=?2 AND u.status like ?3 AND u.is_deleted = ?4 ",nativeQuery = true)
	List<EnquiryEntity> findByUseridAndStatusLikeAndIsdeleted(long userid,String shipmentType ,String status,String isdeleted);

	@Query("select count(e) from EnquiryEntity e ")
	long enquiryDetailsEntityCout();

	EnquiryEntity findByIdAndIsdeleted(long enquiryid, String isdeleted);

	EnquiryEntity findByEnquiryreferenceAndIsdeleted(String enquiryReff, String isdeleted);
		
}
