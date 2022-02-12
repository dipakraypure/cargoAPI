package com.cargo.load.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnquiryScheduleChargesDetailsResponse {

    private Long id;
	
	private Long userid;
	private String enquiryreference;
	private String userEmail;
	
	private String incoterm;
	private String origincode;
	private String origin;
	
	private String destinationcode;
	private String destination;
	
	private String cargocategory;
	private String shipmenttype;
	private String status;
	
	private String scheduletype;
	private String numberoflegs;
	
	private String chargerateids;
	
	private String fclselected;
	private String lclselected;

	List<ScheduleLegsResponse> scheduleLegsResponseList;
	List<ChargesRateResponse> chargesRateResponseList;
	
	private String searchdate;
	private String cargoreadydate;
	
	private String isscheduleupload;
	private String ischargesupload;
	
	
	
}
