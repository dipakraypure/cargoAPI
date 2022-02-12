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
@Table(	name = "master_charges_rate")
@Getter @Setter
public class ChargesRateEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "enquiry_reference")
	private String enquiryreference;
	
	@Column(name = "origin")
	private String origin;
	
	@Column(name = "origin_id")
	private Long originlocid;
	
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "destination_id")
	private Long destinationlocid;
	
	@Column(name = "forwarder_id")
	private Long forwarderid;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	@Column(name = "carrier_id")
	private Long carrierid;
	
	@Column(name = "charges_grouping_id")
	private Long chargesgroupingid;
	
	@Column(name = "charge_type_id")
	private Long chargetypeid;
	
	@Column(name = "incoterm_id")
	private Long incotermid;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "charge_type")
	private String chargetype;
	
	@Column(name = "charge_type_code")
	private String chargetypecode;
	
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
	
	@Column(name="is_active")
	private String isactive;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
	/*
	@OneToOne(cascade = CascadeType.ALL )
	@JoinColumn(name = "charges_grouping_id")
	private ChargesGroupingEntity chargesGrouping;
	*/
	
}
