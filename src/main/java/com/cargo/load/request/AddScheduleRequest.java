package com.cargo.load.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddScheduleRequest {

	private String enquiryreference;
	private String enquiryid;
	private String origin;
	private String destination;
	private String scheduletype;
	private String legscount;
	private String cutoffdate;
	
	private List<AddScheduleLegsRequest> addScheduleLegsRequest;
	
}
