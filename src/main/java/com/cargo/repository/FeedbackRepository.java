package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.FeedbackEntity;

@Repository
public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long>{

	List<FeedbackEntity> findByIsdeleted(String isdeleted);

	FeedbackEntity findByIdAndIsdeleted(Long id, String isdeleted);

}
