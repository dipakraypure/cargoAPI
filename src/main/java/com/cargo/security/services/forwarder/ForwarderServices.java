package com.cargo.security.services.forwarder;

import com.cargo.load.request.AddScheduleChargeRequest;
import com.cargo.load.request.AddScheduleRequest;
import com.cargo.load.request.ConfigureAlertRequest;
import com.cargo.load.response.BaseResponse;

public interface ForwarderServices {

	BaseResponse getForwarderBookingCountByStatus(String userId,String shipmentType)throws Exception;

	BaseResponse getForwarderBookingList(String userId, String shipmentType,String bookingStatus)throws Exception;

	BaseResponse updateBookingStatus(String id, String userId, String bookingStatus)throws Exception;

	BaseResponse getForwarderBookingDetailsById(String bookingReff)throws Exception;

	BaseResponse getForwarderEnquiryCountByStatus(String userId,String shipmentType) throws Exception;

	BaseResponse getForwarderEnquiryList(String userId,String shipmentType ,String enquiryStatus)throws Exception;

	BaseResponse getForwarderEnquiryChargesById(String id)throws Exception;

	BaseResponse addSchedule(AddScheduleRequest addScheduleRequest, String userid)throws Exception;

	BaseResponse addScheduleCharge(AddScheduleChargeRequest addScheduleChargeRequest, String userid)throws Exception;

	BaseResponse updateEnquiryStatus(String id, String userId, String enquiryStatus)throws Exception;

	BaseResponse getForwarderEnquiryScheduleChargesById(String forwEnqId,String userId)throws Exception;

	BaseResponse deleteForwarderEnquiryById(String id,String userId)throws Exception;

	BaseResponse deleteForwarderBookingById(String id,String userId)throws Exception;

	BaseResponse addOriginLocationAlert(ConfigureAlertRequest configureAlertRequest)throws Exception;

	BaseResponse addDestinationLocationAlert(ConfigureAlertRequest configureAlertRequest)throws Exception;

	BaseResponse getAllConfiguredLocationByUserId(String userId)throws Exception;

	BaseResponse removeOriginLocationAlert(ConfigureAlertRequest configureAlertRequest)throws Exception;

	BaseResponse removeDestinationLocationAlert(ConfigureAlertRequest configureAlertRequest)throws Exception;

	BaseResponse getForwarderEnquiryScheduleById(String id)throws Exception;

}
