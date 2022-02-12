package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadOfferRequest {
	
	private String userId;
	private String tital;

	private String origin;
	private String destination;
	private String fromDate;
	private String toDate;
	private String templateType;
	private String description;
	private String footer;
	        
}
