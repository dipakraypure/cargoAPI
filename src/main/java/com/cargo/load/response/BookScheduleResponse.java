package com.cargo.load.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class BookScheduleResponse {
 
	private Long id;
	private String bookingreff;
	
	private String bookedby;
	private String cargoreadydate;
	private String dateofbooking;
	private String bookingstatus;
	
	private String bookingparty;
	private String incoterm;
	
	private String shipmenttype;
	
	private String fclselected;
	private String lclselected;
	
	private String bookingdate;
	private String bookingupdateddate;
	private String origin;
	private String origincode;
	

	private String cutoffdate;
	private String destination;
	private String destinationcode;
	
	private int twentyFtCount;
	private int fourtyFtCount;
	private int fourtyFtHcCount;
	private int fourtyFiveFtCount;
	
	private String lcltotalweight;
	private String lclweightunit;
	private String lclvolume;
	private String lclvolumeunit;
	private String lclnumberpackage;
	private String lclpackageunit;
	
	private List<ScheduleLegsResponse> scheduleLegsResponse;
	
	private List<ChargesRateResponse> chargesRateResponseList;
	
	private List<ChargesGroupingCurrencyResponse> chargesGroupingCurrencyResponseList;
	
	private String totalamountusd;
	private String totalamountinr;
}
