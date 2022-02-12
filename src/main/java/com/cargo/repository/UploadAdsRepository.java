package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.UploadAdsEntity;

@Repository
public interface UploadAdsRepository extends JpaRepository<UploadAdsEntity, Long> {

	List<UploadAdsEntity> findByIsdeleted(String isdeleted);

	UploadAdsEntity findByIdAndIsdeleted(Long id, String isdeleted);

	List<UploadAdsEntity> findByStatusAndIsdeleted(String status, String isdeleted);

}
