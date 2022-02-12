package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SearchEnquiryResponse {

	private String enquiryreference;
	private String origin;
	private String destination;
	
	private String cargoreadydate;
	private String cargocategory;
	private String shipmenttype;

	private String commodity;
	private String imco;
	private String temprange;
	
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
	

}
