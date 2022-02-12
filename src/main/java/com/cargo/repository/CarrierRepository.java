package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.CarrierEntity;

@Repository
public interface CarrierRepository extends JpaRepository<CarrierEntity, Long> {

	List<CarrierEntity> findByIsdeleted(String isdeleted);

	CarrierEntity findByIdAndIsdeleted(Long carrierid, String string);

}
