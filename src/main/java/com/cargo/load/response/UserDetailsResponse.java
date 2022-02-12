package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserDetailsResponse {

	private Long id;
	private String email;
	private String isemailverify;
	private String emailverifydate;
	
	private String mobilenumber;
	
	private String ismobileverify;
	private String mobileverifydate;
	
	private String role;
	
	private String status;
	
}
