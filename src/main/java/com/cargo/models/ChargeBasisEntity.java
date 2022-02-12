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
@Table(	name = "master_charge_basis")
@Getter @Setter
public class ChargeBasisEntity extends CreateUpdate{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	@Column(name = "basis")
	private String basis;
	
	@Column(name = "basis_code")
	private String basiscode;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
