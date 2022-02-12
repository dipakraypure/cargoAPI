package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadAdsRequest {

	private String id;
	private String userId;
	private String companyName;
	private String email;
	private String startDate;
	private String endDate;
	private String content;
	private String priority;
	private String status;
	
}
