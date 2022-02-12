package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class PopularForwarderResponse {

	private Long id;
	private String email;
	private String mobilenumber;
	
	private String forwarder;
	private String location;
	private String priority;
	private String role;
}
