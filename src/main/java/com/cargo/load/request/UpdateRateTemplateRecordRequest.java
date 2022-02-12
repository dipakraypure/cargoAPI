package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateRateTemplateRecordRequest {

	private String recordId;
	private String serialNumber;
	private String carrierName;
	private String templateName;
	private String destination;
	private String rate;
	@Override
	public String toString() {
		return "UpdateRateTemplateRecordRequest [recordId=" + recordId + ", serialNumber=" + serialNumber
				+ ", carrierName=" + carrierName + ", templateName=" + templateName + ", destination=" + destination
				+ ", rate=" + rate + "]";
	}
	
	
	
}
