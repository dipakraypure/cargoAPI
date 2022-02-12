package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnquiryForwarderAcceptedResponse {

	private String enquiryreference;
	
	private String forwarderid;
	private String forwarder;
	
	private String transid;
	private String chargerateids;
	
	private String freightcharges;
	private String ftchargescurrency;
	
	private String origincharges;
	private String orgchargescurrency;
	
	private String destinationcharges;
	private String destchargescurrency;
	
	private String othercharges;
	private String otherchargescurrency;
	
	private String totalcharges;
	
	private String usdtotalcharges;
	private String inrtotalcharges;
	
	private String twentyftcount;
	private String fourtyftcount;
	private String fourtyfthccount;
	private String fourtyfiveftcount;
	
}
