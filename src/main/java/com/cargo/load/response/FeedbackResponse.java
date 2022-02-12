package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FeedbackResponse {

	private String id;
	private String createddate;
	private String name;
	private String companyname;
	private String email;
	private String mobile;
	private String location;
	private String message;
	private String actionmessage;
	private String status;
	private String replydate;
}
