package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.load.response.BookingCountByStatusResponse;
import com.cargo.models.BookingDetailsEntity;


@Repository
public interface BookingDetailsRepository extends JpaRepository<BookingDetailsEntity, Long> {

	@Query("select count(e) from BookingDetailsEntity e ")
	long bookingDetailsEntityCout();
	
	//get booking count by status
	@Query("SELECT new com.cargo.load.response.BookingCountByStatusResponse("
	   +"SUM(CASE WHEN bookingstatus = 'Requested' THEN 1 ELSE 0 END) AS requestCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Accepted' THEN 1 ELSE 0 END) AS acceptedCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Cancelled' THEN 1 ELSE 0 END) AS cancelledCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Rejected' THEN 1 ELSE 0 END) AS rejectedCount)"
	   +"FROM  BookingDetailsEntity WHERE userid= (?1) AND shipmenttype=(?2) AND customerisdeleted= (?3)")
	public BookingCountByStatusResponse bookingCountByUserIdAndCustomerisdeletedAndGroupByStatus(long userid,String shipmentType,String customerisdeleted);

	@Query("select count(e) from BookingDetailsEntity e WHERE e.userid=(?1) AND e.shipmenttype=(?2) AND e.customerisdeleted=(?3)")
	long bookingDetailsEntityCoutByUserId(long userid,String shipmentType,String customerisdeleted);

	
	BookingDetailsEntity findByBookingreffAndIsdeleted(String bookingReff, String isdeleted);
/*
	@Query("SELECT ue FROM BookingDetailsEntity ue where ue.userid =?1 AND ue.bookingstatus LIKE ?2 AND ue.isdeleted = ?3")
	List<BookingDetailsEntity> findByUseridAndStatusLikeAndIsdeleted(long userid,String bookingstatus ,String isdeleted);
*/
	
	// with order by desc
	@Query("SELECT ue FROM BookingDetailsEntity ue where ue.userid =?1 AND ue.shipmenttype =?2 AND ue.bookingstatus LIKE ?3 AND ue.customerisdeleted = ?4 ")
	List<BookingDetailsEntity> findByUseridAndStatusLikeAndCustomerisdeleted(long userid,String shipmentType ,String bookingstatus,String customerisdeleted);
	
	
	/*****************************Forwarder Mapping***************************************/
	
	@Query("select count(e) from BookingDetailsEntity e WHERE e.forwarderid=(?1) AND e.shipmenttype=(?2) AND e.forwarderisdeleted=(?3)")
	long bookingDetailsEntityCoutByForwarderId(long userid,String shipmentType,String forwarderisdeleted);
	
	//get booking count by status
	@Query("SELECT new com.cargo.load.response.BookingCountByStatusResponse("
	   +"SUM(CASE WHEN bookingstatus = 'Requested' THEN 1 ELSE 0 END) AS requestCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Accepted' THEN 1 ELSE 0 END) AS acceptedCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Cancelled' THEN 1 ELSE 0 END) AS cancelledCount,"
	   +"SUM(CASE WHEN bookingstatus = 'Rejected' THEN 1 ELSE 0 END) AS rejectedCount)"
	   +"FROM  BookingDetailsEntity WHERE forwarderid= (?1) AND shipmenttype= (?2) AND forwarderisdeleted= (?3)")
	public BookingCountByStatusResponse bookingCountByForwarderIdAndForwarderisdeletedAndGroupByStatus(long userid,String shipmentType,String forwarderisdeleted);
	
	@Query("SELECT ue FROM BookingDetailsEntity ue where ue.forwarderid =?1 AND ue.shipmenttype=?2 AND ue.bookingstatus LIKE ?3 AND ue.forwarderisdeleted = ?4 ")
	List<BookingDetailsEntity> findByForwarderidAndStatusLikeAndForwarderisdeleted(long userid,String shipmentType,String bookingstatus,String forwarderisdeleted);
	
}
