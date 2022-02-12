package com.cargo.load.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddScheduleLegsRequest implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String origin;	 
	 private String destination;
	 private String mode;
	 private String carrier;
	 private String vessel;
	 private String voyage;
	 private String etddate;
	 private String etadate;
	 private String transittime;
	
}
