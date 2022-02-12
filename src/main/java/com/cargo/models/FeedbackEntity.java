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
@Table(name = "master_feedback")
@Getter @Setter
public class FeedbackEntity extends CreateUpdate{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(name="user_id")
	private Long userid;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "company_name")
	private String companyname;
	
	@Column(name = "email_id")
	private String email;
	
	@Column(name = "mobile_number")
	private String mobilenumber;
	
	@Column(name = "location")
	private String location;
	
	@Column(name = "message")
	private String message;
	
	@Column(name = "action_message")
	private String actionmessage;
	
	@Column(name = "reply_date")
	private String replydate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
