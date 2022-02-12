package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserDetailsRequest {
	
	private String userid;
	private String email;
	private String password;
	
	private String newpassword;
	
	private String mobilenumber;

	private String role;

	private String status;
	
    private String emailverifyotp;
	private String mobileverifyotp;

}
