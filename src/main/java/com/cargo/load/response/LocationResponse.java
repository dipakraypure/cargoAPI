package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LocationResponse {

	private Long id;
	private String locationcode;
	private String locationname;
	private String countrycode;
	private String countryname;
    private String status;
}
