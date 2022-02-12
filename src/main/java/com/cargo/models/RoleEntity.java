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
@Table(name = "master_user_role")
@Getter @Setter
public class RoleEntity extends CreateUpdate{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	
	@Column(name = "name")
	private String name;

	
	@Column(name = "code")
	private String code;
	
	
	@Column(name = "status")
	private String status;
}
