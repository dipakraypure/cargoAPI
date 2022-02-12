package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ForwarderSetupRequest {
	private String id;
	private String forwarderId;
	private String userid;
	private String status;
	private String startDate;
	private String endDate;
	private String priority;
}
