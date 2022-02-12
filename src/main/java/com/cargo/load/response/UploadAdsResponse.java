package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadAdsResponse {

	private Long id;
	
	private String companyname;
	private String email;	
	private String content;

	private String startdate;
	private String enddate;
	
	private String priority;
	private String status;
	
}
