package com.cargo.security.services.admin;


import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.CarrierRequest;
import com.cargo.load.request.CountrySpecificationRequest;
import com.cargo.load.request.FeedbackRequest;
import com.cargo.load.request.ForwarderSetupRequest;
import com.cargo.load.request.UpdateRateTemplateRecordRequest;
import com.cargo.load.request.UploadAdsRequest;
import com.cargo.load.request.UploadOfferRequest;
import com.cargo.load.request.UserDetailsRequest;
import com.cargo.load.response.BaseResponse;

public interface AdminServices {
	
	BaseResponse getUploadRateHistory() throws Exception;

	BaseResponse updateTemplateRateRecordInMaster(UpdateRateTemplateRecordRequest updateRateTemplateRecordRequest)throws Exception;

	BaseResponse uploadNewOffer(UploadOfferRequest uploadOfferRequest, MultipartFile offerImage, MultipartFile footerImage)throws Exception;

	BaseResponse getUploadOfferHistory()throws Exception;

	BaseResponse deleteUploadRateRecord(String userId, String recordId)throws Exception;

	BaseResponse getUseLoginLog()throws Exception;
	
	BaseResponse deleteAllLoginLog(String userId)throws Exception;

	BaseResponse getAllForwarderCha()throws Exception;

	BaseResponse getUserListByRole(String userRole)throws Exception;

	BaseResponse uploadAds(UploadAdsRequest uploadAdsRequest)throws Exception;

	BaseResponse getUploadedAdsList()throws Exception;

	BaseResponse deleteAds(String id, String userId)throws Exception;

	BaseResponse getAdsDetailsById(String id, String userId)throws Exception;

	BaseResponse updateAdsById(UploadAdsRequest uploadAdsRequest)throws Exception;

	BaseResponse getFeedbackList()throws Exception;

	BaseResponse getFeedbackDetailsById(String id)throws Exception;

	BaseResponse deleteFeedback(String id, String userId)throws Exception;

	BaseResponse updateFeedbackStatus(FeedbackRequest feedbackRequest)throws Exception;

	BaseResponse replyToFeedback(FeedbackRequest feedbackRequest)throws Exception;

	BaseResponse addCountrySpecification(CountrySpecificationRequest countrySpecificationRequest)throws Exception;

	BaseResponse getCountrySpecificationList()throws Exception;

	BaseResponse deleteCountrySpecification(String id, String userId)throws Exception;

	BaseResponse getCountySpecificationDetailsById(String id)throws Exception;

	BaseResponse updateCountrySpecification(CountrySpecificationRequest countrySpecificationRequest)throws Exception;

	BaseResponse deleteCarrier(String id, String userId)throws Exception;

	BaseResponse addCarrier(CarrierRequest carrierRequest)throws Exception;
	
	BaseResponse getCarrierList()throws Exception;

	BaseResponse getCarrierDetailsById(String id)throws Exception;

	BaseResponse updateCarrierById(CarrierRequest carrierRequest)throws Exception;

	BaseResponse getCompanyNameList()throws Exception;

	BaseResponse addForwarderSetup(ForwarderSetupRequest forwarderSetupRequest)throws Exception;

	BaseResponse getForwarderSetupList()throws Exception;

	BaseResponse getForwarderSetupDetailsById(String id, String userId)throws Exception;

	BaseResponse deleteForwarderSetup(String id, String userId)throws Exception;

	BaseResponse updateForwSetupById(ForwarderSetupRequest forwarderSetupRequest)throws Exception;

	BaseResponse updateMyProfileResetPassword(UserDetailsRequest userDetailsRequest)throws Exception;

		
}
