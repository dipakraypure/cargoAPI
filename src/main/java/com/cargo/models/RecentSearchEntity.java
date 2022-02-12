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
@Table(	name = "master_recent_search")
@Getter @Setter
public class RecentSearchEntity extends CreateUpdate{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="origin_id")
	private Long originid;
	
	@Column(name="destination_id")
	private Long destinationid;
	
	@Column(name="cargo_category")
	private String cargocategory;
	
	@Column(name = "commodity")
	private String commodity;
	
	@Column(name = "imco")
	private String imco;
	
	@Column(name="temperature_range")
	private String temprange;
	
	@Column(name="cargo_ready_date")
	private String cargoreadydate;
	
	
	@Column(name="shipment_type")
	private String shipmenttype;
	
	
	@Column(name="twenty_ft_count")
	private String twentyftcount;
	
	
	@Column(name="fourty_ft_count")
	private String fourtyftcount;
	
	
	@Column(name="fourty_ft_hc_count")
	private String fourtyfthccount;
	
	
	@Column(name="fourty_five_ft_count")
	private String fourtyfiveftcount;
	
	@Column(name = "lcl_total_weight")
	private String lcltotalweight;
	
	@Column(name = "lcl_weight_unit")
	private String lclweightunit;
	
	@Column(name = "lcl_volume")
	private String lclvolume;
	
	@Column(name = "lcl_volume_unit")
	private String lclvolumeunit;
	
	@Column(name = "lcl_number_packege")
	private String lclnumberpackage;
	
	@Column(name = "lcl_packege_unit")
	private String lclpackageunit;
	
	@Column(name="search_date")
	private String searchdate;
	
	@Column(name = "is_deleted")
	private String isdeleted;
	
}
