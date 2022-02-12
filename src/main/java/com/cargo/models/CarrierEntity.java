package com.cargo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(	name = "master_carrier")
@Getter @Setter
public class CarrierEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "carrier_short_name")
	private String carriershortname;
	
	@Column(name = "carrier_name")
	private String carriername;
	
	@Column(name = "scac_code")
	private String scaccode;
	
	@Column(name = "logo_path")
	private String logopath;
	
	
	@Column(name = "website")
	private String website;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
