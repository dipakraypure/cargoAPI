package com.cargo.load.response;



import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ScheduleResponse {

    private Long id;

	private String origin;
	private String destination;

	private Long transdetailsid;
	
	private String origindepartdate;	
	private String destarrivedate;
	
	private String viaportname;
	
    private String viaportarrivedate;	
	private String viaportdepartdate;
	
	private String voyage;
	private String vesselname;
	
	private String secondvoyage;
	private String secondvesselname;
	
	private String transittime;
	private String cuttoffdate;
	private String preference;
	private String status;
	private String isdeleted;
}
