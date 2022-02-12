package com.cargo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(	name = "master_user_new")
@Getter @Setter @ToString
public class UserEntity extends CreateUpdate{
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Column(name = "user_name")
	private String username;

	@Column(name = "user_mobile_no")
	private String mobileno;
	
	@Column(name = "gstin_number")
	private String gstinnumber;
	
	@Column(name = "company_id")
	private Long companyId;
	
	
	@Column(name = "user_email")
	private String email;

	
	@Column(name = "user_login_id")
	private String userid;
	
		
	@Column(name = "user_password")
	private String password;

	@Column(name = "user_role_id")
	private Long userroleid;
	
	@Column(name = "role_code")
	private String role;
	
	
	@Column(name = "reset_password_token")
	private String resetpasswordtoken;
	
	
	@Column(name = "is_email_verify")
	private String isemailverify;
	
	@Column(name = "email_verify_date")
	private String emailverifydate;
	
	@Column(name = "is_mobile_verify")
	private String ismobileverify;
	
	@Column(name = "mobile_verify_date")
	private String mobileverifydate;
	
	@Column(name = "is_active")
	private String isactive;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	

	public UserEntity() {
	}

	public UserEntity(String username,
			String mobileno,
			String email, 
			String userid,
			String password, 
			String isactive) {
		super();
		this.username = username;
		this.mobileno = mobileno;
		this.email = email;
		this.userid = userid;		
		this.password = password;
		this.isactive = isactive;
	}
	
	/*
	public UserEntity(@NotBlank @Size(max = 20) String username, String mobileno,
			@NotBlank @Size(max = 50) @Email String email, @NotBlank String userid, @NotBlank String companyname,
			@NotBlank @Size(max = 120) String password, String isactive) {
		super();
		this.username = username;
		this.mobileno = mobileno;
		this.email = email;
		this.userid = userid;
		this.companyname = companyname;
		this.password = password;
		this.isactive = isactive;
	}
	*/
}
