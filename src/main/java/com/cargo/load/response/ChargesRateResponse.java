package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChargesRateResponse {

	 private Long id;
	 
	 private Long incotermid;
	 private Long chargesgroupingid;
	 private String chargesgrouping;
	 private Long chargestypeid;
	 private String chargestype;
	 private String currency;
	 private String remark;
	 private String validdatefrom;
	 private String validdateto;
	 private String rate;
	 private String basiscode;
	 private String basis;
	 private String quantity;	 
	 private String totalamount;
}
