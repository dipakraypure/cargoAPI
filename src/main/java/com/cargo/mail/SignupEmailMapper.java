package com.cargo.mail;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignupEmailMapper {

	private Long id;
	private String email;
	private String password;
	private String role;
	private String token;
	
}
