package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargo.models.LoginLogEntity;

@Repository
public interface LoginLogRepository extends JpaRepository<LoginLogEntity, Long>{

	List<LoginLogEntity> findAllByOrderByLogindatetimeDesc();
}
