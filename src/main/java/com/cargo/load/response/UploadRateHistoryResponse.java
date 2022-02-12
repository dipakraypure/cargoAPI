package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadRateHistoryResponse {

	private Long id;
	private int serialnumber;
	
	private Long forwarderid;
	private String forwarder;
	private String shipmenttype;
	
	private Long carrierid;
	private String carriername;
	private String carriershortname;
	private Long chargetypeid;
	private String chargetype;

	private String validdatefrom;
	private String validdateto;
	
	private String recordscount;
	private String filename;
	
	private String uploadeddate;
	private String status;
	
	private String isdeleted;
}
