package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommonRateTemplateResponse {

	private Long id;	
	private String serialnumber;
	private String origin;
	private String originlocid;
	private String destination;
	private String destinationlocid;
	private String carrierid;
	private String chargesgroupingid;
	private String chargesgroupingcode;
	private String chargetype;	
	private String chargetypecode;
	private String cargocategory;
	private String validdatefrom;
	private String validdateto;
	private String routing;
	private String transittime;
	private String currency;
	private String basis;
	private String quantity;
	private String rate;
	private String errorflag;
	
}
