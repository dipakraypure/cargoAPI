package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.models.RecentSearchEntity;


@Repository
public interface RecentSearchRepository extends JpaRepository<RecentSearchEntity, Long>{

	/*
	List<RecentSearchEntity> findByOriginAndDestinationAndSearchdateAndIsdeleted(String origin, String destination,
			String timeStamp, String isdeleted);
			*/
/*
	@Query("SELECT ue FROM RecentSearchEntity ue ORDER BY ue.createdate ROWNUM <= 5")
	List<RecentSearchEntity> findTopFiveRecentSearchRecords();
	*/
	
	List<RecentSearchEntity> findTop5ByOrderByCreatedateDesc();

	List<RecentSearchEntity> findByOriginidAndDestinationidAndSearchdateAndIsdeleted(long originid, long destinationid,
			String timeStamp, String isdeleted);

	RecentSearchEntity findByIdAndIsdeleted(long id, String isdeleted);

}
