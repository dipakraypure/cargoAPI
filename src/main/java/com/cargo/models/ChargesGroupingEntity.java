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
@Table(	name = "master_charges_grouping")
@Getter @Setter
public class ChargesGroupingEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "charges_grouping")
	private String chargesgrouping;
	
	@Column(name = "charges_grouping_code")
	private String chargesgroupingcode;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
