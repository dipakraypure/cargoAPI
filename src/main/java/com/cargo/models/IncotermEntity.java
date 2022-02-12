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
@Table(	name = "master_inco_term")
@Getter @Setter
public class IncotermEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "inco_term")
	private String incoterm;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
