package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.ChargeBasisEntity;

@Repository
public interface ChargeBasisRepository extends JpaRepository<ChargeBasisEntity, Long>{

	List<ChargeBasisEntity> findByIsdeleted(String isdeleted);

	ChargeBasisEntity findByBasiscodeAndIsdeleted(String basiscode, String isdeleted);

}
