package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.models.LocationEntity;

@Repository
public interface LocationRepository extends JpaRepository<LocationEntity, Long>{

	/*
	//@Query("SELECT ue FROM LocationEntity ue where ue.isdeleted = :isdeleted AND ue.locationname LIKE %:origin% ")
	@Query(value="SELECT * FROM master_location u where u.is_deleted=?1 AND u.location_name like ?2 AND city_code IN ('NSA','ANR','DXB','SIN','PKG','NMB','MUL')",nativeQuery = true)
	List<LocationEntity> findByIsDeletedAndLocationStartWith(String isdeleted,String origin);
	*/

	@Query(value="SELECT * FROM master_location u where u.is_deleted=:isdeleted AND u.location_name like :location% limit 15",nativeQuery = true)
	List<LocationEntity> findByIsDeletedAndLocationStartWith(String isdeleted,String location);
	
	LocationEntity findByLocationcodeAndIsdeleted(String locationcode, String isdeleted);

	@Query("SELECT ue FROM LocationEntity ue where ue.locationname LIKE ?1 AND ue.isdeleted = ?2")
	List<LocationEntity> findByLocationnameLikeWithIgnoreCaseAndIsdeleted(String locationname, String isdeleted);

	@Query(value="SELECT * FROM master_location WHERE country_name != '' AND is_deleted=?1 group by country_name ORDER BY country_name",nativeQuery = true)
	List<LocationEntity> findAllCountryAndIsdeleted(String isdeleted);

	List<LocationEntity> findByCountrycodeAndIsdeleted(String countrycode, String isdeleted);

	LocationEntity findByIdAndIsdeleted(long id, String isdeleted);
	
}
