package com.cargo.template.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForwarderQuotationResponse {

	private String acceptedDate;	
	private String origin;
	private String destination;
	private String selectedFcl;
	private String selectedLcl;
	private String shipmentType;
	
	private String enquiryReference;
	
	private String quotationFrom;
	private String quotationDate;
	private String quotationStatus;
	
	private String enqSearchDate;
	private String cargoReadyDate;
	
	private String lcltotalweight;
	private String lclweightunit;
	private String lclvolume;
	private String lclvolumeunit;
	private String lclnumberpackage;
	private String lclpackageunit;
}
