package com.cargo.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.cargo.models.CompanyEntity;


@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Long>{

	CompanyEntity findByIdAndIsdeleted(long compId, String isdeleted);

	//CompanyEntity findCompanyByCompanyname(String companyname);

	//List<CompanyEntity> findByCompanyname(String companyName);
}
