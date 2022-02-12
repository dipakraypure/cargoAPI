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
@Table(	name = "master_charges_type")
@Getter @Setter
public class ChargesTypeEntity extends CreateUpdate{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "charge_code")
	private String chargecode;
	
	@Column(name = "charge_code_description")
	private String chargecodedescription;
	
	@Column(name = "charges_grouping_id")
	private Long chargesgroupingid;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
