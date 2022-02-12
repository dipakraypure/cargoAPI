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
@Table(name = "master_location")
@Getter @Setter
public class LocationEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	
	@Column(name = "location_code")
	private String locationcode;
	
	
	@Column(name = "country_code")
	private String countrycode;
	
	
	@Column(name = "country_name")
	private String countryname;
	
	
	@Column(name = "city_code")
	private String citycode;
	
	
	@Column(name = "location_name")
	private String locationname;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
