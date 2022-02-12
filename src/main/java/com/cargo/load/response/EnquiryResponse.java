package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnquiryResponse {

	private Long id;
	
	private Long userid;
	private String enquiryreference;
	private String userEmail;
	
	private String customer;
	
	private String origincode;
	private String origin;
	
	private String destinationcode;
	private String destination;
	
	private String cargoreadydate;
	private String cargocategory;
	private String shipmenttype;
	private String selectedfcl;
	private String selectedlcl;
	private String searchdate;
	
	private String lcltotalweight;
	private String lclweightunit;
	private String lclvolume;
	private String lclvolumeunit;
	private String lclnumberpackage;
	private String lclpackageunit;
	
	private String isscheduleupload;
	private String ischargesupload;
	
	private String status;
}
