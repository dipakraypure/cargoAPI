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
@Table(	name = "master_offer")
@Getter @Setter
public class UploadOfferEntity extends CreateUpdate {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "user_id")
	private Long userid;
	
	@Column(name = "campaign_code")
	private String campaigncode;
	
	@Column(name = "tital")
	private String tital;
	
	@Column(name = "origin")
	private String origin;
	 
	@Column(name = "destination")
	private String destination;
	
	@Column(name = "template_type")
	private String templatetype;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "footer")
	private String footer;
	
	@Column(name = "footer_image")
	private String footerimage;
	
	@Column(name = "footer_image_path")
	private String footerimagepath;
	
	@Column(name = "valid_date_from")
	private String validdatefrom;
	
	@Column(name = "valid_date_to")
	private String validdateto;
		
	@Column(name = "upload_date")
	private String uploaddate;
	
	@Column(name = "offer_image_name")
	private String offerimagename;
		
	@Column(name = "offer_image_fullpath")
	private String offerimagefullpath;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;
}
