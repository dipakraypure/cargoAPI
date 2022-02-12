package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequestBkp {
	private String loginuserid;
	private String username;
	
	private String email;
	private String password;
	
	private String mobilenumber;

	private String role;
	
	private String state;
	private String location;
	
	private String status;
	private String companyname;
	private String userAddress1;
	private String userAddress2;
	
	private String userPincode;
	
	private String userPanNumber;
	//private MultipartFile userPanFile;
	
	private String userGSTINNumber;
	//private MultipartFile userGSTINFile;
	
	private String userIECCode;
	//private MultipartFile userIESFile;
	
	private String website;
}
