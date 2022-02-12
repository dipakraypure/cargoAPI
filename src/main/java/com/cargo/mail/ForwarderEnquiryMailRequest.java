package com.cargo.mail;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForwarderEnquiryMailRequest {

	private String userId;
	private String origin;
	private String destination;
	
	private String enquiryReference;
	private String enquiryBy;
	private String enquiryParty;
	
    private String cargoCategory;
    private String commodity;
    private String imco;
    private String temprange;
    private String cargoReadyDate;
    
    private String shipmentType;
    
    private String twentyFtCount;
    private String fourtyFtCount;
    private String fourtyFtHcCount;
    private String fourtyFiveFtCount;
	
    private String selectedFcl;
    private String selectedLcl;
    private String searchDate;
   
    private String enquiryStatus;
}
