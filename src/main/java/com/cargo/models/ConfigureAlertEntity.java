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
@Table(	name = "master_configure_alert")
@Getter @Setter
public class ConfigureAlertEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userid;
	
	@Column(name = "origin_location_ids")
	private String originlocids;
	
	@Column(name = "destination_location_ids")
	private String destinationlocids;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}