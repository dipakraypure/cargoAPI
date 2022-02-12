package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.UnitsEntity;

@Repository
public interface UnitsRepository extends JpaRepository<UnitsEntity, Long> {

	List<UnitsEntity> findByIsdeleted(String isdeleted);

}
