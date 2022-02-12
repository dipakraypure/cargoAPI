package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForwarderSetupResponse {
	private String id;
	private String companyname;
	private String location;
	private String startdate;
	private String enddate;
	private String status;
	private String priority;
}
