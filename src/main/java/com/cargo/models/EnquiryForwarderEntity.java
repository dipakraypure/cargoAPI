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
@Table(name = "master_enquiry_forwarder")
@Getter @Setter
public class EnquiryForwarderEntity extends CreateUpdate{
   
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "enquiry_id")
	private Long enquiryid;
	
	@Column(name = "enquiry_reference")
	private String enquiryreference;
	
	@Column(name = "user_id")
	private Long userid;
	
	@Column(name = "forwarder_id")
	private Long forwarderid;
	
	@Column(name = "origin_loc_id")
	private Long originlocid;
	
	@Column(name = "destination_loc_id")
	private Long destinationlocid;
	
	@Column(name = "cargo_ready_date")
	private String cargoreadydate;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	@Column(name = "is_schedule_upload")
	private String isscheduleupload;
	
	@Column(name = "is_charges_upload")
	private String ischargesupload;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
