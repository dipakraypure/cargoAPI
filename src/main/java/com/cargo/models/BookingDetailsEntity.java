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
@Table(	name = "master_booking_details")
@Getter @Setter
public class BookingDetailsEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "enquiry_id")
	private Long enquiryid;
	
	@Column(name = "booking_reff")
	private String bookingreff;
	
	@Column(name = "user_id")
	private Long userid;
	
	/*
	 @Column(name = "origin_id") 
	 private Long originid;
	  
	 @Column(name = "destination_id")
	 private Long destinationid;
	 
	
	@Column(name = "carrier_id")
	private Long carrierid;
	*/
	
	@Column(name = "forwarder_id")
	private Long forwarderid;
	
	
	@Column(name = "schedule_id")
	private Long scheduleid;
	
	@Column(name = "charge_rate_ids")
	private String chargerateids;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	/*
	@Column(name = "twenty_ft_count")
	private int twentyftcount;
	
	@Column(name = "fourty_ft_count")
	private int fourtyftcount;
	
	@Column(name = "fourty_ft_hc_count")
	private int fourtyfthccount;
	
	@Column(name = "fourty_five_ft_count")
	private int fourtyfiveftcount;
	
	*/
	
	@Column(name = "booking_date")
	private String bookingdate;
	
	@Column(name = "booking_updated_date")
	private String bookingupdateddate;
	
	
	@Column(name = "booking_status")
	private String bookingstatus;
	
	@Column(name = "customer_is_deleted")
	private String customerisdeleted;
	
	@Column(name = "forwarder_is_deleted")
	private String forwarderisdeleted;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
