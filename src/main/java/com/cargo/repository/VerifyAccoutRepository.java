package com.cargo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.cargo.models.VerifyAccountEntity;


@Repository
public interface VerifyAccoutRepository extends JpaRepository<VerifyAccountEntity, Long>{

	VerifyAccountEntity findByVerifyemailtokenAndVerifymobiletoken(String verifyemailtoken, String verifymobiletoken);

	VerifyAccountEntity findByEmailAndMobilenumber(String email, String mobilenumber);

	VerifyAccountEntity findByVerifymobiletoken(String mobileVerifyOtp);

	VerifyAccountEntity findByUserid(long userId);
	
	@Transactional
	@Modifying      // to mark delete or update query
    @Query(value = "DELETE FROM VerifyAccountEntity e WHERE e.userid = :userid") 
    int deleteByUserid(long userid);

	VerifyAccountEntity findByUseridAndEmail(long userid, String email);

}
