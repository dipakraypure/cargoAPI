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
@Table(	name = "master_company")
@Getter @Setter
public class CompanyEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "gstin_number")
	private String gstinnumber;
	
	@Column(name = "state_jurisdiction_code")
	private String statejurisdictioncode;
	
	@Column(name = "state_jurisdiction")
	private String statejurisdiction;
	
	
	@Column(name = "legal_name")
	private String legalname;
	
	@Column(name = "trade_name")
	private String tradename;
	
	@Column(name = "building_name")
	private String buildingname;
	
	@Column(name = "building_number")
	private String buildingnumber;
	
	@Column(name = "floor_number")
	private String floornumber;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "pin_code")
	private String pincode;
	
	@Column(name = "tax_payer_type")
	private String taxpayertype;
	
	
	@Column(name = "centre_jurisdiction")
	private String centrejurisdiction;
	
	@Column(name = "gstin_status")
	private String gstinstatus;
	
	
	@Column(name = "registration_date")
	private String registrationdate;
	
	
	@Column(name = "last_update_date")
	private String lastupdatedate;
	
	
	@Column(name = "state_code")
	private String statecode;
	
	
	@Column(name = "user_pan_no")
	private String userpanno;
	
	
	@Column(name = "user_pan_filename")
	private String userpanfilename;
	
	
	@Column(name = "user_pan_file_status")
	private String userpanfilestatus;
	
	
	@Column(name = "user_iec_code")
	private String userieccode;
	
	@Column(name = "user_iec_filename")
	private String useriecfilename;
	
	@Column(name = "user_iec_file_status")
	private String useriecfilestatus;
	
	@Column(name = "alias")
	private String alias;
	
	@Column(name = "alias_filename")
	private String aliasfilename;
	
	@Column(name = "alias_file_status")
	private String aliasfilestatus;
	
	@Column(name = "website")
	private String website;
	
	@Column(name = "upload_date")
	private String uploaddate;
	
	@Column(name = "user_status")
	private String userstatus;
	
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
	
}
