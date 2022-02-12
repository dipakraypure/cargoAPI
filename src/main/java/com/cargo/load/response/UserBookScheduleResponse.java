package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserBookScheduleResponse {

	private Long id;
	private String bookingreff;
	
	private String origin;
	private String origincode;
	private String originwithcode;
	
	private String destination;
	private String destinationcode;
	private String destinationwithcode;
	
	private String carriername;
	
	
	private String bookingdate;
	private String bookingstatus;	
	
	private String forwarder;
	
	private String cutoffdate;
/*		
	private String voyagenumber;
	private String vesselname;
	
*/
	
}
