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
@Table(	name = "verify_account")
@Getter @Setter
public class VerifyAccountEntity extends CreateUpdate{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userid;
	
	@Column(name = "email")
	private String email;
	
	@Column(name = "mobile_number")
	private String mobilenumber;
	
	@Column(name = "verify_email_token")
	private String verifyemailtoken;
	
	@Column(name = "expired_email_token")
	private String expiredemailtoken;
	
	@Column(name = "verify_mobile_token")
	private String verifymobiletoken;
	
	@Column(name = "expired_mobile_token")
	private String expiredmobiletoken;
	
}
