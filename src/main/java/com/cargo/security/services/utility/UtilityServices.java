package com.cargo.security.services.utility;

import java.util.List;

import com.cargo.load.request.FeedbackRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.RoleResponse;

public interface UtilityServices {
	 
	BaseResponse getAllLocation(String location) throws Exception;
	
	BaseResponse getAllCarriers()throws Exception;
	
	BaseResponse getAllChargeGrouping() throws Exception;
	
	public List<RoleResponse> getAllRoles() throws Exception;

	BaseResponse getAllChargesSubType(String groupingChargeId) throws Exception;

	BaseResponse getAllAds() throws Exception;

	BaseResponse getAllIncotermList()throws Exception;

	BaseResponse getAllCountry()throws Exception;

	BaseResponse getLocationByCountryCode(String countrycode) throws Exception;

	BaseResponse submitFeedback(FeedbackRequest feedbackRequest)throws Exception;
	 
	BaseResponse getCountrySpecByCode(String countrycode)throws Exception;

	BaseResponse getAllChargeBasis()throws Exception;
}
