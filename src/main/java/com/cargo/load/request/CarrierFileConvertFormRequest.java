package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CarrierFileConvertFormRequest {

	private String userId;
	private String forwarderCha;
	private String carrier;
	private String chargeType;
	private String shipmentType;
	private String origin;
	private String validDateFrom;
	private String validDateTo;
	
}
