package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargo.models.ScheduleLegsEntity;

@Repository
public interface ScheduleLegsRepository extends JpaRepository<ScheduleLegsEntity, Long>{

	//List<ScheduleLegsEntity> findByTransid(Long id);

	List<ScheduleLegsEntity> findByTransidAndIsdeleted(Long id, String isdeleted);


	@Modifying  
	@Transactional
    @Query(value = "DELETE FROM ScheduleLegsEntity e WHERE e.transid = ?1")      
    int deleteAllByTransId(long transid);

}
