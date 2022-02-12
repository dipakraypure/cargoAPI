package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.models.UploadOfferEntity;

@Repository
public interface UploadOfferRepository extends JpaRepository<UploadOfferEntity, Long> {

	@Query("select count(e) from UploadOfferEntity e ")
	long uploadOfferEntityCout();

	List<UploadOfferEntity> findByIsdeleted(String isdeleted);

	List<UploadOfferEntity> findByIsdeletedOrderByUploaddateDesc(String isdeleted);

}
