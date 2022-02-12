package com.cargo.load.response;


import javax.persistence.Column;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompanyResponse {
	
	private Long id;
	
	private String gstinnumber;
	private String statejurisdictioncode;
	private String statejurisdiction;

	private String legalname;	
	private String tradename;
	
	private String buildingname;
	private String buildingnumber;
	private String floornumber;
	private String street;
	private String location;
	private String destination;	
	private String city;
	private String pincode;
	

	private String taxpayertype;
	private String centrejurisdiction;
	private String gstinstatus;
	
	private String registrationdate;
	private String lastupdatedate;

	private String statecode;

	private String userpanno;
	private String userpanfilename;
	private String userpanfilestatus;
	
	private String userieccode;
	private String useriecfilename;
	private String useriecfilestatus;
	
	private String alias;
	private String aliasfilename;
	private String aliasfilestatus;
	private String website;
	
	private String uploaddate;
	private String userstatus;
	private String isdeleted;
	
}
