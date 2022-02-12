package com.cargo.controller;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import com.cargo.load.request.CarrierFileConvertFormRequest;
import com.cargo.load.request.CarrierRequest;
import com.cargo.load.request.CompanyDetailsRequest;
import com.cargo.load.request.CountrySpecificationRequest;
import com.cargo.load.request.FeedbackRequest;
import com.cargo.load.request.ForwarderSetupRequest;
import com.cargo.load.request.LoginRequest;
import com.cargo.load.request.SearchTransportRequest;
import com.cargo.load.request.UpdateRateTemplateRecordRequest;
import com.cargo.load.request.UploadAdsRequest;
import com.cargo.load.request.UploadOfferRequest;
import com.cargo.load.request.UserDetailsRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.MessageResponse;
import com.cargo.security.services.admin.AdminServices;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.security.services.fileupload.FileUploadServices;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
@Api(value = "/api/admin", tags = "Admin Management")
public class AdminController {

	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    
	BaseResponse response = null;
	
	@Autowired
	AdminServices adminServices;
	
	@Autowired
	FileUploadServices fileUploadServices;
	
	@Autowired
	FileStorageService fileStorageService;
	

	//admin my profile reset password 
	@PutMapping(value = "/updateMyProfileResetPass")
	public ResponseEntity<Object> updateMyProfileResetPassword(@ModelAttribute UserDetailsRequest userDetailsRequest) {
			
		logger.info("********************updateMyProfileResetPassword Method******************");
			
		response = new BaseResponse();
			
		try {
				response = adminServices.updateMyProfileResetPassword(userDetailsRequest);
						
				return ResponseEntity.ok(response);
		} catch (Exception e) {
			
				
			logger.error(e.getMessage());
				
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
				
			return ResponseEntity.badRequest().body(response);
				
		}
    }
		
	// Old schenario
	@GetMapping("/getForwarderCha")
	public ResponseEntity<Object> getAllForwarderCha(){
		logger.info("********************AuthController getAllForwarderCha-(ForwarderCha for Dropdown on admin home page )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = adminServices.getAllForwarderCha();	
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	// Old schenario
	// Convert Carrier maxicon to system file
	@PostMapping(value = "/convertFileMaxiconToSys" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> convertMaxiconCarrierToSys(@ModelAttribute CarrierFileConvertFormRequest carrierFileConvertFormRequest,@RequestParam (value="carrierRateFileToConvert",required=true)MultipartFile file) {
		logger.info("********************AdminController convertMaxiconCarrierToSys()******************");
		response = new BaseResponse();
		
		try {

			
			response = fileUploadServices.convertUploadedFileMaxiconToTemplate(carrierFileConvertFormRequest,file);
						
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	// Old schenario
	// Convert Carrier RCL to system file
		@PostMapping(value = "/convertFileRCLToSys" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<Object> convertRCLCarrierToSys(@ModelAttribute CarrierFileConvertFormRequest carrierFileConvertFormRequest,@RequestParam (value="carrierRateFileToConvert",required=true)MultipartFile file) {
			logger.info("********************AdminController convertRCLCarrierToSys()******************");
			response = new BaseResponse();
			
			try {

				response = fileUploadServices.convertUploadedFileRclToTemplate(carrierFileConvertFormRequest,file);
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
	
		// Old schenario
	   //uploadDirTemplateFile
		//API to direct choose file and upload into master rate table (Need to work)
		@PostMapping(value = "/uploadDirTemplateFile" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<Object> uploadDirectTemplateFileInMaster(@ModelAttribute CarrierFileConvertFormRequest carrierFileConvertFormRequest,@RequestParam (value="carrierRateTempFileToUpload",required=true)MultipartFile file) {
			logger.info("********************AdminController uploadDirectTemplateFileInMaster()******************");
			response = new BaseResponse();
			
			try {

				
				response = fileUploadServices.uploadDirectCarrierRateTemplateIntoMaster(carrierFileConvertFormRequest,file);
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
			
		// Old schenario
		//Upload Template Data into master table 
		@GetMapping(value = "/uploadTemplateData")
		public ResponseEntity<Object> uploadTemplateDataIntoRateMaster(@RequestParam("userId") String userId,@RequestParam("carrierName")String carrierName,@RequestParam("chargeType")String chargeType,@RequestParam("templateName")String templateName ) {
			logger.info("********************AdminController uploadTemplateDataIntoRateMaster()******************");
			response = new BaseResponse();
			
			try {
		
				  response = fileUploadServices.uploadTemplateDataRateMaster(userId,carrierName,chargeType,templateName);
							
				  return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		
		} 
		
		// Old schenario
		@GetMapping(value = "/previewTemplateData")
		public ResponseEntity<Object> getTemplateDataRateForPreview(@RequestParam("userId") String userId,@RequestParam("carrierName")String carrierName,@RequestParam("templateName")String templateName ) {
			logger.info("********************AdminController uploadTemplateDataIntoRateMaster()******************");
			response = new BaseResponse();
			
			try {
				   Resource resource = fileStorageService.loadRateFile(carrierName,templateName);				  
				   response = fileUploadServices.getTemplateDataRateForPreview(resource,userId,carrierName,templateName);
							
				  return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		
		} 
		
		// Old schenario
		@GetMapping(value = "/getUploadRateHistory")
		public ResponseEntity<Object> getUploadRateHistory() {
			logger.info("********************AdminController getUploadRateHistory()******************");
			response = new BaseResponse();
			
			try {
			
				response = adminServices.getUploadRateHistory();
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
		
		// Old schenario
		@PostMapping("/updateRateTemplateRecord")
		public ResponseEntity<Object> updateRateTemplateRecord(@RequestBody UpdateRateTemplateRecordRequest updateRateTemplateRecordRequest) {

			
			logger.info("********************AdminController updateRateTemplateRecord******************");
			logger.info("updateRateTemplateRecordRequest: "+updateRateTemplateRecordRequest);
			
			response = new BaseResponse();
			
			try {
				
				response = adminServices.updateTemplateRateRecordInMaster(updateRateTemplateRecordRequest);
									
			}catch (Exception e) {
				
				logger.error(e.getMessage()); 
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			return ResponseEntity.ok(response);
		}
		
		@PostMapping(value = "/uploadOffer" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
		public ResponseEntity<Object> uploadNewOffer(@ModelAttribute UploadOfferRequest uploadOfferRequest,@RequestParam (value="offerImage",required=true) MultipartFile offerImage,@RequestParam (value="footerImage",required=false) MultipartFile footerImage) {
			
			logger.info("********************uploadNewOffer Method******************");	
			response = new BaseResponse();
			
			try {
					
					response = adminServices.uploadNewOffer(uploadOfferRequest,offerImage,footerImage);
						
					return ResponseEntity.ok(response);
			} catch (Exception e) {
			
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
		
		// Old schenario
		@GetMapping(value = "/getUploadOfferHistory")
		public ResponseEntity<Object> getUploadOfferHistory() {
			logger.info("********************AdminController getUploadOfferHistory()******************");
			response = new BaseResponse();
			
			try {
			
				response = adminServices.getUploadOfferHistory();
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
	
		// Old schenario
		//Upload Template Data into master table 
		@GetMapping(value = "/deleteRateRecord")
		public ResponseEntity<Object> deleteUploadRateRecord(@RequestParam("userId") String userId,@RequestParam("recordId")String recordId) {
			logger.info("********************AdminController deleteUploadRateRecord()******************");
			response = new BaseResponse();
					
			try {
				
				 response = adminServices.deleteUploadRateRecord(userId,recordId);
									
				 return ResponseEntity.ok(response);
						
			}
			catch (Exception e) {
				 logger.error(e.getMessage());
						
				 response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				 response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				 response.setRespData(e.getMessage());
						
				 return ResponseEntity.badRequest().body(response);
						
			}
				
		}
		
		@GetMapping(value = "/getUseLoginLog")
		public ResponseEntity<Object> getUseLoginLog() {
			logger.info("********************AdminController getUseLoginLog()******************");
			response = new BaseResponse();
			
			try {
			
				response = adminServices.getUseLoginLog();
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
		
		@DeleteMapping(value = "/deleteAllLoginLog/{userId}")
		public ResponseEntity<Object> deleteAllLoginLog(@PathVariable("userId")String userId ) {
			logger.info("********************AdminController deleteAllLoginLog()******************");
			response = new BaseResponse();
			
			try {
			
				response = adminServices.deleteAllLoginLog(userId);							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
	  /******************Admin Dashboard Start******************************/
		
		// get User List By Role
		@GetMapping("/getUserListByRole")
		public ResponseEntity<Object> getUserListByRole(@RequestParam (value="selectedUserRole")String userRole ) {
			logger.info("********************AdminController getUserListByRole******************");
			response = new BaseResponse();
			
			try {

				response = adminServices.getUserListByRole(userRole);
				
				return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		
		 /******************Admin Dashboard End******************************/
		
		/*******************Upload Ads start**********************************************/
		@PostMapping("/uploadAds")
		public ResponseEntity<Object> uploadAds(@RequestBody UploadAdsRequest uploadAdsRequest, HttpServletRequest request) {
			
			logger.info("********************AdminController uploadAds******************");
			
			response = new BaseResponse();
			
			try {
				
				 response = adminServices.uploadAds(uploadAdsRequest);
					
				 return ResponseEntity.ok(response);
						
			}catch (Exception e) {
				
				logger.error(e.getMessage()); 
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		
		@PutMapping("/updateAdsById")
		public ResponseEntity<Object> updateAdsById(@RequestBody UploadAdsRequest uploadAdsRequest, HttpServletRequest request) {
			
			logger.info("********************AdminController uploadAds******************");
			
			response = new BaseResponse();
			
			try {
				
				 response = adminServices.updateAdsById(uploadAdsRequest);
					
				 return ResponseEntity.ok(response);
						
			}catch (Exception e) {
				
				logger.error(e.getMessage()); 
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		
		@GetMapping(value = "/getUploadedAdsList")
		public ResponseEntity<Object> getUploadedAdsList() {
			logger.info("********************AdminController getUploadedAdsList()******************");
			response = new BaseResponse();
			
			try {
			
				response = adminServices.getUploadedAdsList();
							
				 return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
		}
		
		@DeleteMapping("/deleteAds/{id}/{userId}")
		public ResponseEntity<Object> deleteAds(@PathVariable("id")String id,@PathVariable ("userId")String userId) {
			logger.info("********************AdminController deleteAds******************");
			response = new BaseResponse();
			
			try {

				response = adminServices.deleteAds(id,userId);
				
				return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		
		@GetMapping("/getAdsDetailsById")
		public ResponseEntity<Object> getAdsDetailsById(@RequestParam (value="id")String id,@RequestParam (value="userId")String userId) {
			logger.info("********************AdminController getAdsDetailsById******************");
			response = new BaseResponse();
			
			try {

				response = adminServices.getAdsDetailsById(id,userId);
				
				return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		/*******************Upload Ads End**********************************************/
		
		/**************Start Feedback API*******************/
		@GetMapping(value = "/getFeedbackList")
		public ResponseEntity<Object> getFeedbackList() {
			logger.info("********************AdminController getFeedbackList()******************");
			response = new BaseResponse();
			
			try {			
				response = adminServices.getFeedbackList();							
				 return ResponseEntity.ok(response);				
			}
			catch (Exception e) {			
				logger.error(e.getMessage());			
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}
		}
		
		@GetMapping("/getFeedbackDetailsById")
		public ResponseEntity<Object> getFeedbackDetailsById(@RequestParam (value="id")String id) {
			logger.info("********************AdminController getFeedbackDetailsById******************");
			response = new BaseResponse();
			
			try {

				response = adminServices.getFeedbackDetailsById(id);				
				return ResponseEntity.ok(response);
				
			}
			catch (Exception e) {
				
				logger.error(e.getMessage());				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
		
		@DeleteMapping("/deleteFeedback/{id}/{userId}")
		public ResponseEntity<Object> deleteFeedback(@PathVariable ("id")String id,@PathVariable("userId")String userId) {
			logger.info("********************AdminController deleteFeedback******************");
			response = new BaseResponse();
			
			try {
				response = adminServices.deleteFeedback(id,userId);				
				return ResponseEntity.ok(response);				
			}
			catch (Exception e) {				
				logger.error(e.getMessage());				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());				
				return ResponseEntity.badRequest().body(response);				
			}			
		}
		
		@PutMapping("/updateFeedbackStatus")
		public ResponseEntity<Object> updateFeedbackStatus(@RequestBody FeedbackRequest feedbackRequest) {
		    	
	        logger.info("********************AdminController updateFeedbackStatus()******************");			
			response = new BaseResponse();
				
			try {				
				response = adminServices.updateFeedbackStatus(feedbackRequest);					
				return ResponseEntity.ok(response); 										
			}catch (Exception e) {
					
			    logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());					
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
		
		@PostMapping("/replyToFeedback")
		public ResponseEntity<Object> replyToFeedback(@RequestBody FeedbackRequest feedbackRequest) {
		    	
	        logger.info("********************AdminController replyToFeedback()******************");			
			response = new BaseResponse();
				
			try {				
				response = adminServices.replyToFeedback(feedbackRequest);					
				return ResponseEntity.ok(response); 										
			}catch (Exception e) {
					
			    logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());					
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
	/**************end Feedback API*******************/
		
    /*************Start Country Specification****************/
		
	@PostMapping("/addCountrySpecification")
	public ResponseEntity<Object> addCountrySpecification(@RequestBody CountrySpecificationRequest countrySpecificationRequest) {
		    	
		logger.info("********************AdminController addCountrySpecification()******************");			
		response = new BaseResponse();
				
		try {				
				response = adminServices.addCountrySpecification(countrySpecificationRequest);					
				return ResponseEntity.ok(response); 										
	    }catch (Exception e) {
					
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
					
				return ResponseEntity.badRequest().body(response);				
		}			
	} 
	
	@GetMapping(value = "/getCountrySpecificationList")
	public ResponseEntity<Object> getCountrySpecificationList() {
		logger.info("********************AdminController getCountrySpecificationList()******************");
		response = new BaseResponse();
		
		try {			
			response = adminServices.getCountrySpecificationList();							
			 return ResponseEntity.ok(response);				
		}
		catch (Exception e) {			
			logger.error(e.getMessage());			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);				
		}
	}
	
	@DeleteMapping("/deleteCountrySpecification/{id}/{userId}")
	public ResponseEntity<Object> deleteCountrySpecification(@PathVariable("id")String id,@PathVariable("userId")String userId) {
		logger.info("********************AdminController deleteCountrySpecification******************");
		response = new BaseResponse();
		
		try {
			response = adminServices.deleteCountrySpecification(id,userId);				
			return ResponseEntity.ok(response);				
		}
		catch (Exception e) {				
			logger.error(e.getMessage());				
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);				
		}			
	}
	
	@GetMapping("/getCountySpecDetailsById")
	public ResponseEntity<Object> getCountySpecificationDetailsById(@RequestParam (value="id")String id) {
		logger.info("********************AdminController getCountySpecificationDetailsById******************");
		response = new BaseResponse();
		
		try {
			response = adminServices.getCountySpecificationDetailsById(id);			
			return ResponseEntity.ok(response);			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@PutMapping("/updateCountrySpecification")
	public ResponseEntity<Object> updateCountrySpecification(@RequestBody CountrySpecificationRequest countrySpecificationRequest) {
	    	
        logger.info("********************AdminController updateCountrySpecification()******************");			
		response = new BaseResponse();
			
		try {				
			response = adminServices.updateCountrySpecification(countrySpecificationRequest);					
			return ResponseEntity.ok(response); 										
		}catch (Exception e) {
				
		    logger.error(e.getMessage()); 				
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
				
			return ResponseEntity.badRequest().body(response);				
		}			
	}
     /*************end Country Specification****************/
	
	/*************Start Carrier Setup****************/
	@PostMapping("/addCarrier")
	public ResponseEntity<Object> addCarrier(@RequestBody CarrierRequest carrierRequest, HttpServletRequest request) {
		
		logger.info("********************AdminController addCarrier******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = adminServices.addCarrier(carrierRequest);
				
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@GetMapping(value = "/getCarrierList")
	public ResponseEntity<Object> getCarrierList() {
		logger.info("********************AdminController getCarrierList()******************");
		response = new BaseResponse();
		
		try {
		
			response = adminServices.getCarrierList();
						
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@GetMapping(value = "/getCarrierDetailsById")
	public ResponseEntity<Object> getCarrierDetailsById(@RequestParam (value="id")String id) {
		logger.info("********************AdminController getCarrierDetailsById()******************");
		response = new BaseResponse();
		
		try {
		
			response = adminServices.getCarrierDetailsById(id);
						
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PutMapping("/updateCarrierById")
	public ResponseEntity<Object> updateCarrierById(@RequestBody CarrierRequest carrierRequest, HttpServletRequest request) {
		
		logger.info("********************AdminController updateCarrierById******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = adminServices.updateCarrierById(carrierRequest);
				
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@DeleteMapping("/deleteCarrier/{id}/{userId}")
	public ResponseEntity<Object> deleteCarrier(@PathVariable ("id")String id,@PathVariable ("userId")String userId) {
		logger.info("********************AdminController deleteCarrier******************");
		response = new BaseResponse();
		
		try {

			response = adminServices.deleteCarrier(id,userId);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	/*************end Carrier Setup****************/
	
	/*************Start Forwarder Setup****************/
	@GetMapping(value = "/getCompanyNameList")
	public ResponseEntity<Object> getCompanyNameList() {
		logger.info("********************AdminController getCompanyNameList()******************");
		response = new BaseResponse();
		
		try {
		
			response = adminServices.getCompanyNameList();
						
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PostMapping("/addForwarderSetup")
	public ResponseEntity<Object> addForwarderSetup(@RequestBody ForwarderSetupRequest forwarderSetupRequest, HttpServletRequest request) {
		
		logger.info("********************AdminController addForwarderSetup******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = adminServices.addForwarderSetup(forwarderSetupRequest);
				
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@GetMapping(value = "/getForwarderSetupList")
	public ResponseEntity<Object> getForwarderSetupList() {
		logger.info("********************AdminController getForwarderSetupList()******************");
		response = new BaseResponse();
		
		try {
		
			response = adminServices.getForwarderSetupList();
						
			 return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@GetMapping("/getForwarderSetupDetailsById")
	public ResponseEntity<Object> getForwarderSetupDetailsById(@RequestParam (value="id")String id,@RequestParam (value="userId")String userId) {
		logger.info("********************AdminController getForwarderSetupDetailsById******************");
		response = new BaseResponse();
		
		try {

			response = adminServices.getForwarderSetupDetailsById(id,userId);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@DeleteMapping("/deleteForwarderSetup/{id}/{userId}")
	public ResponseEntity<Object> deleteForwarderSetup(@PathVariable("id")String id,@PathVariable ("userId")String userId) {
		logger.info("********************AdminController deleteForwarderSetup******************");
		response = new BaseResponse();
		
		try {

			response = adminServices.deleteForwarderSetup(id,userId);
			
			return ResponseEntity.ok(response);
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@PutMapping("/updateForwSetupById")
	public ResponseEntity<Object> updateForwSetupById(@RequestBody ForwarderSetupRequest forwarderSetupRequest, HttpServletRequest request) {
		
		logger.info("********************AdminController updateForwSetupById******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = adminServices.updateForwSetupById(forwarderSetupRequest);
				
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	/*************End Forwarder Setup****************/
}
