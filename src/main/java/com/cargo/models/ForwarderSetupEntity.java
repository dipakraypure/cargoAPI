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
@Table(name = "master_forwarder_setup")
@Getter @Setter
public class ForwarderSetupEntity extends CreateUpdate{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name = "forwarder_id")
	private Long forwarderid;
	
	@Column(name = "start_date")
	private String startdate;
	
	@Column(name = "end_date")
	private String enddate;
	
	@Column(name = "priority")
	private String priority;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
