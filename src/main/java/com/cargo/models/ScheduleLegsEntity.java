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
@Table(	name = "master_schedule_legs")
@Getter @Setter
public class ScheduleLegsEntity  extends CreateUpdate {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="trans_id")
	private Long transid;
	
	@Column(name="origin_id")
	private long originid;
	
	
	@Column(name="destination_id")
	private long destinationid;
	

	@Column(name="mode")
	private String mode;
	
	@Column(name="carrier_id")
	private Long carrierid;
	
	@Column(name="carrier_option")
	private String carrieroption;
	
	@Column(name="vessel")
	private String vessel;
	
	@Column(name="voyage")
	private String voyage;
	
	@Column(name="etd_date")
	private String etddate;
	
	@Column(name="eta_date")
	private String etadate;
	
	@Column(name="transit_time")
	private String transittime;
	
	@Column(name="is_deleted")
	private String isdeleted;
	
}
