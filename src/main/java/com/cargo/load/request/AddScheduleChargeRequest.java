package com.cargo.load.request;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddScheduleChargeRequest {

	private String enquiryreference;
	private String enquiryid;
	private String origin;
	private String destination;
	private String validdatefrom;
	private String validdateto;
	private String incoterm;
	private String remark;
	
	List<AddChargesTypeRequest> addChargesTypeRequest;
	
}