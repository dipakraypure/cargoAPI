package com.cargo.load.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ConfiguredLocationResponse {
	//private String countryname;
	//private String countrycode;	
	//List<LocationResponse> location;
	
	private List<LocationResponse> originLocationResponse;
	private List<LocationResponse> destinationLocationResponse;
}
