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
@Table(name = "master_upload_rate_history")
@Getter @Setter
public class UploadRateHistoryEntity extends CreateUpdate {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "forwarder_cha_id")
	private Long forwarderid;
	
	@Column(name = "shipment_type")
	private String shipmenttype;
	
	@Column(name = "carrier_id")
	private Long carrierid;
	
	
	@Column(name = "charge_type_id")
	private Long chargetypeid;
	
	
	@Column(name = "filename")
	private String filename;
	
	
	@Column(name = "valid_date_from")
	private String validdatefrom;
	
	@Column(name = "valid_date_to")
	private String validdateto;
	
	
	@Column(name = "records_count")
	private String recordscount;
	
	@Column(name = "uploaded_date")
	private String uploadeddate;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "is_deleted")
	private String isdeleted;

	
	
}
