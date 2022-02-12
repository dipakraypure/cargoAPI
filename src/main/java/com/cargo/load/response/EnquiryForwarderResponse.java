package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EnquiryForwarderResponse {

    private Long id;	
	private String enquiryreference;
	private String forwarder;
	private String status;
	private String updateddate;
	
}
