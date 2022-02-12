package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.UploadRateHistoryEntity;


@Repository
public interface UploadRateHistoryRepository extends JpaRepository<UploadRateHistoryEntity, Long> {

	List<UploadRateHistoryEntity> findByIsdeleted(String isdeleted);

	UploadRateHistoryEntity findByIdAndIsdeleted(long id, String isdeleted);

	List<UploadRateHistoryEntity> findByIsdeletedOrderByUploadeddateDesc(String isdeleted);

}
