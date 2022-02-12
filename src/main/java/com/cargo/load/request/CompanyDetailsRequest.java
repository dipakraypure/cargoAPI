package com.cargo.load.request;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompanyDetailsRequest {

	private String userId;
	
	/*
	private String username;
	
	private String email;	
	
	private String mobilenumber;

	private String role;
	
	private String state;
	private String location;
	
	private String status;
	private String companyname;
	private String userAddress1;
	private String userAddress2;
	
	private String userPincode;
	*/
	
	private String userPanNumber;
	private String userIECCode;
	private String aliasName;	
	private String website;
}
