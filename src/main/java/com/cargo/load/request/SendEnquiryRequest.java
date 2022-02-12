package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SendEnquiryRequest {

	private String userId;
	private String origin;
	private String destination;
	
    private String cargoCategory;
    private String commodity;
    private String imco;
    private String temprange;
    private String cargoReadyDate;
    
    private String shipmentType;
    
    private String lcltotalweight;
    private String lclweightunit;
    private String lclvolume;
    private String lclvolumeunit;
    private String lclnumberpackage;
    private String lclpackageunit;
    
    private String twentyFtCount;
    private String fourtyFtCount;
    private String fourtyFtHcCount;
    private String fourtyFiveFtCount;
    
    private String forwarderIds;
}