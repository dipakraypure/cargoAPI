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
@Table(	name = "master_currency")
@Getter @Setter
public class CurrencyEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "currency")
	private String currency;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}