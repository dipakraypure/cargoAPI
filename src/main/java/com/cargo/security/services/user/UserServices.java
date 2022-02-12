package com.cargo.security.services.user;



import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.BookScheduleRequest;
import com.cargo.load.request.CompanyDetailsRequest;
import com.cargo.load.request.SearchTransportRequest;
import com.cargo.load.request.SendEnquiryRequest;
import com.cargo.load.request.UserDetailsRequest;
import com.cargo.load.response.BaseResponse;


public interface UserServices {

	BaseResponse checkTransportationDetailsBySearch(SearchTransportRequest searchTransRequest) throws Exception;
	
	BaseResponse getExistCompanyDetailsByUserId(String userId) throws Exception;

	BaseResponse updateUserCompanyDetails(CompanyDetailsRequest companyDetailsRequest, MultipartFile userPanFile, MultipartFile userIECFile,MultipartFile logo) throws Exception;

	BaseResponse getRecentSearchData()throws Exception;

	BaseResponse getRecentSearchDataByDataId(String id) throws Exception;

	BaseResponse getPackageUnitsList() throws Exception;
	
	BaseResponse bookSchedule(BookScheduleRequest bookScheduleRequest)throws Exception;

	BaseResponse getUserBookingList(String userId,String shipmentType,String bookingStatus)throws Exception;

	BaseResponse getUserBookingCountByStatus(String userId,String shipmentType)throws Exception;

	BaseResponse getUserBookingDetailsById(String bookingReff)throws Exception;

	BaseResponse updateBookingStatus(String id,String userId, String bookingStatus)throws Exception;

	BaseResponse getUserEnquiryList(String userId,String shipmentType, String enquiryStatus) throws Exception;

	BaseResponse getUserEnquiryCountByStatus(String userId,String shipmentType)throws Exception;

	BaseResponse getUserProfileDetailsById(String userId) throws Exception;

	BaseResponse updateUserMyProfile(UserDetailsRequest userDetailsRequest) throws Exception;

	BaseResponse updateMyProfileResetPassword(UserDetailsRequest userDetailsRequest) throws Exception;

	BaseResponse sendVerifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception;

	BaseResponse verifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception;

	BaseResponse resendVerifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception;

	BaseResponse updateEnquiryStatus(String id, String userId, String enquiryStatus)throws Exception;

	BaseResponse getEnquiryAcceptedList(String userId, String enquiryId)throws Exception;

	BaseResponse bookScheduleFromEnquiry(BookScheduleRequest bookScheduleRequest)throws Exception;

	/*
	 * BaseResponse getEnquiryScheduleChargesDetailsByEnquiryReff(String
	 * enquiryReff,String forwarderid)throws Exception;
	 */

	BaseResponse sendEnquirySearchRequest(SendEnquiryRequest sendEnquiryRequest)throws Exception;

	BaseResponse getForwarderEnquiryStatusByReference(String userId, String enquiryReference) throws Exception;

	BaseResponse deleteUserEnquiryById(String id, String userId)throws Exception;

	BaseResponse deleteUserBookingById(String id, String userId)throws Exception;

	BaseResponse getUserEnquiryDetailsById(String enquiryId)throws Exception;

	BaseResponse deactivateAccount(UserDetailsRequest userDetailsRequest)throws Exception;

	
}
