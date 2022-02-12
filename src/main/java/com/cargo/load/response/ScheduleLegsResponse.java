package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ScheduleLegsResponse {
    private Long id;
    
    private String origin;
    private String originid;
    
	private String destination;
	private Long destinationid;
	
	private String mode;
	
	private String carrierid;
	private String carrier;
	private String carriercode;
	private String carrierpng;
	
	private String vessel;
	private String voyage;
	
	private String etddate;
	private String etadate;
	
	private String transittime;
	
}
