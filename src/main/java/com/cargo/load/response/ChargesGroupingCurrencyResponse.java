package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChargesGroupingCurrencyResponse {

	
	public ChargesGroupingCurrencyResponse(String group, String usd, String inr) {
		super();
		this.group = group;
		this.usd = usd;
		this.inr = inr;
	}
	public ChargesGroupingCurrencyResponse() {
		
	}
	private String group;
	private String usd;
	private String inr;
	
}
