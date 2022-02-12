package com.cargo.load.request;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AddChargesTypeRequest implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String chargegrouping;
	private String chargetype;
	private String currency;
	private String rate;
	private String basis;
	private String quantity;
	private String freighttotalval;
	
}
