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
@Table(	name = "master_trans_details")
@Getter @Setter
public class TransDetailsEntity  extends CreateUpdate {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "enquiry_reference")
	private String enquiryreference;
	
	@Column(name="origin_id")
	private long originid;
	
	
	@Column(name="destination_id")
	private long destinationid;
	
	@Column(name="cargo_category")
	private String cargocategory;
	
	@Column(name="commodity")
	private String commodity;
	
	@Column(name ="schedule_type")
	private String scheduletype;
	
	@Column(name="number_of_legs")
	private String numberoflegs;
	
	@Column(name="forwarder_id")
	private long forwarderid;
	
	@Column(name="forwarder_png")
	private String forwarderpng;
		
	@Column(name="cargo_ready_date")
	private String cargoreadydate;
	
	@Column(name="cut_off_date")
	private String cutoffdate;
	
	@Column(name="transit_time")
	private String transittime;

	@Column(name="is_active")
	private String isactive;

	@Column(name="is_deleted")
	private String isdeleted;
	
}
