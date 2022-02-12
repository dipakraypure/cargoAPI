package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class FreightChargesResponse {

	
	private Long id;
	private Long transdetailsid;
	
	private String chargerateids;
	
	private String oftwentyrate;
	private String offourtyrate;
	private String offourtyhcrate;
	private String offourtyfiverate;
	
	private String totaloceanfreight;
	private String totalfreight;
	
	private String basis;
	private String quantity;
	private String currency;
	
	private String bunkeradjfactor;	
	private String currencyadjfactor;

	private String isdeleted;
}
