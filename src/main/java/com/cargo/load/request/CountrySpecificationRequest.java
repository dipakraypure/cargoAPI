package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CountrySpecificationRequest {

    private String id;
    private String userid;
	private String countryname;
	private String countrycode;
	private String specification;
	private String status;
	
}
