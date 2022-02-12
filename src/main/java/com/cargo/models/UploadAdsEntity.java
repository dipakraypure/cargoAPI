package com.cargo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "master_upload_ads")
@Getter @Setter
public class UploadAdsEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "company_name")
	private String companyname;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "start_date")
	private String startdate;
	
	@Column(name = "end_date")
	private String enddate;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
