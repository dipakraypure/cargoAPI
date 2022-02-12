package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RecentSearchResponse {

	private Long id;
	
	private String origin;
	private String origincode;
	private String destination;
	private String destinationcode;
	
	private String cargocategory;
	private String commodity;
	private String imco;
	private String temprange;
	private String cargoreadydate;
	private String shipmenttype;
	
	private String twentyftcount;
	private String fourtyftcount;
	private String fourtyfthccount;
	private String fourtyfiveftcount;
	
	private String lcltotalweight;
	private String lclweightunit;
	private String lclvolume;
	private String lclvolumeunit;
	private String lclnumberpackage;
	private String lclpackageunit;
	
	private String searchdate;
	private String isdeleted;
}
