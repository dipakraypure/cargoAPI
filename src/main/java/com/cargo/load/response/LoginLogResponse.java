package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LoginLogResponse {

	private Long id;
	private int srno;
	
	private String username;
	private String userrole;
	private String companyname;
	
	private String sessionid;
	private String ipaddress;
	
	private String logindatetime;
	
}
