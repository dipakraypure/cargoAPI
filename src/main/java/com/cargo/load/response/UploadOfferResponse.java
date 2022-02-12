package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadOfferResponse {
	
	private Long id;
	private Long userid;
	
	private int serialnumber;
	
	private String campaigncode;

	private String tital;

	private String origin;
	private String destination;
	private String fromDate;
	private String toDate;
	
	private String templateType;
	
	private String uploaddate;
	private String offerimagename;
	private String offerimagefullpath;
	
	private String description;
	private String footer;
	
	private String footerimagename;
	private String footerimagefullpath;
	
	private String status;

}
