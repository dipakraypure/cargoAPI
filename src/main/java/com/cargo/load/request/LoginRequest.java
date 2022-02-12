package com.cargo.load.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@NotBlank(message = "Both fields cannot be empty")
public class LoginRequest {

	@NotBlank(message = "Username cannot be empty.")
	private String username;

	@NotBlank(message = "Password Cannot be empty.")
	private String password;
	
}