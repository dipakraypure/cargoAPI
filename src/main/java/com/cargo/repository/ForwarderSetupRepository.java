package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.ForwarderSetupEntity;

@Repository
public interface ForwarderSetupRepository extends JpaRepository<ForwarderSetupEntity, Long>{

	List<ForwarderSetupEntity> findByIsdeleted(String isdeleted);

	ForwarderSetupEntity findByIdAndIsdeleted(Long idSetup, String isdeleted);

	ForwarderSetupEntity findByForwarderidAndIsdeleted(long forwId, String isdeleted);

	ForwarderSetupEntity findByForwarderidAndStatusAndIsdeleted(long forwarderid, String status, String isdeleted);

}
