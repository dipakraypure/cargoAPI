package com.cargo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cargo.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByUserid(String userid);
	
	UserEntity findByEmail(String email);
	
	boolean existsByUsernameAndIsdeletedAndIsactive(String username, String isdeleted, String isactive);

	UserEntity findByMobileno(String mobilenumber);

	List<UserEntity> findByRoleAndIsdeleted(String role, String isdeleted);

	UserEntity findByResetpasswordtoken(String token);

	UserEntity findByRole(String role);

	boolean existsByUsernameAndIsdeletedAndIsactiveAndIsemailverify(String username, String isdeleted, String isactive,String Isemailverify);

	UserEntity findByIdAndIsdeleted(Long userId, String isdeleted);

	List<UserEntity> findByRoleAndIsactiveAndIsdeleted(String role, String isactive, String isdeleted);

}
