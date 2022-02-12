package com.cargo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "master_upload_rate_temporary")
@Getter @Setter
public class UploadRateTemporaryEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private String userid;
	
	@Column(name = "sr_no")
	private String serialnumber;
	
	@Column(name = "origin")
	private String origin;
	
	@Column(name = "origin_loc_id")
	private String originlocid;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "destination_loc_id")
	private String destinationlocid;
	
	@Column(name = "forwarder_cha_id")
	private String forwarderid;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	@Column(name = "carrier_id")
	private String carrierid;
	
	@Column(name = "charges_grouping")
	private String chargesgrouping;
	
	@Column(name = "charges_grouping_code")
	private String chargesgroupingcode;
	
	@Column(name = "charges_type")
	private String chargestype;
	
	@Column(name = "charges_type_code")
	private String chargestypecode;
	
	@Column(name = "cargo_category")
	private String cargocategory;
	
	@Column(name = "valid_date_from")
	private String validdatefrom;
	
	@Column(name = "valid_date_to")
	private String validdateto;
	
	@Column(name = "routing")
	private String routing;
	
	@Column(name = "transit_time")
	private String transittime;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "basis")
	private String basis;
	
	@Column(name = "quantity")
	private String quantity;
	
	@Column(name = "rate")
	private String rate;
	
	@Column(name = "error_status")
	private String errorstatus;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
