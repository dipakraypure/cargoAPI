package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CommonRateTemplateRequest implements Cloneable{

	private Long id;	
	private String origin;
	private String originlocid;
	private String destination;
	private String destinationlocid;
	private String forwarderid;
	private String carrierid;
	private String shipmenttype;
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
	
	
	 public Object clone() throws CloneNotSupportedException { 
      return super.clone(); 
     } 

}
