package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookScheduleRequest {
	
	private String userId;
	
	/*
	 private String scheduleId;
	 private String twentyFtCount; 
	 private String fourtyFtCount; 
	 private String fourtyFtHcCount; 
	 private String fourtyFiveFtCount; 
	 private String chargerateids; 
	 private String enquiryId;
	 */
	
    // first scenario
    private String forwarderid;
	private String enquiryreference;
    
}
