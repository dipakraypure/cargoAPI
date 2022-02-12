package com.cargo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cargo.models.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

	List<RoleEntity> findAllByIsdeleted(String isdeleted);
	
	
	@Query("SELECT ue FROM RoleEntity ue where ue.isdeleted = (?1) AND ue.code != 'AA'")
	List<RoleEntity> getAllRolesByIsDeletedAndNotAdmin(String isdeleted); // get roles for reg page dropdown
	
	RoleEntity findRoleById(Long id);
	
	Optional<RoleEntity> findById(Long id);
	
	
	//Above methods are new methods
	RoleEntity findByCode(String name);
	RoleEntity findByName(String name);

	
	List<RoleEntity> findByStatus( String status);
	List<RoleEntity> findByStatusAndIsdeleted(String status, String isdeleted);
}
