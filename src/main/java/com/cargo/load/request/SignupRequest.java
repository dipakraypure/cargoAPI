package com.cargo.load.request;


import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupRequest {

	private String loginuserid;
	private String username;
	
	private String email;
	private String password;
	
	private String newpassword;
	
	private String mobilenumber;
	
	private String gstinnumber;

	private String role;

	private String status;
	
    private String resettoken;	
    
    private String emailverifyotp;
    private String mobileverifyotp;
}
