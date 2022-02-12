package com.cargo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.ConfigureAlertEntity;

@Repository
public interface ConfigureAlertRepository extends JpaRepository<ConfigureAlertEntity, Long>{

	ConfigureAlertEntity findByUseridAndIsdeleted(long userId, String isdeleted);

}
