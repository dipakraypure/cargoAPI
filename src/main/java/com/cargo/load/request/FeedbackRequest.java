package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedbackRequest {

	private String id;
	private String userid;
	private String name;
	private String companyname;
	private String email;
	private String mobile;
	private String location;
	private String message;
	private String actionmessage;
	private String status;
	
}
