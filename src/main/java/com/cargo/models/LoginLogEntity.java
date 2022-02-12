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
@Table(name = "master_login_log")
@Getter @Setter
public class LoginLogEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	

	@Column(name = "user_id")
	private Long userId;
	
	@Column(name = "user_name")
	private String username;
	
	
	@Column(name = "user_code")
	private String usercode;
	
	@Column(name = "login_date_time")
	private String logindatetime;
	
	@Column(name = "session_id")
	private String sessionid;
	
	@Column(name = "ip_address")
	private String ipaddress;
	
}
