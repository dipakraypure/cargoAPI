package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CarrierRequest {

	private String id;
	private String userId;
	private String carrier;
	private String carrierCode;
	private String website;
	private String logopath;
	private String scacCode;
}
