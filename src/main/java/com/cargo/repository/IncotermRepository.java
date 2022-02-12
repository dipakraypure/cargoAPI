package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.IncotermEntity;


@Repository
public interface IncotermRepository extends JpaRepository<IncotermEntity, Long>{

	List<IncotermEntity> findByIsdeleted(String isdeleted);

	IncotermEntity findByIdAndIsdeleted(long incotermId, String isdeleted);

}
