package com.cargo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.ConfigureAlertRequest;
import com.cargo.load.request.FeedbackRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.security.services.admin.AdminServices;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.security.services.fileupload.FileUploadServices;
import com.cargo.security.services.user.UserServices;
import com.cargo.security.services.utility.UtilityServices;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/utility")
@Api(value = "/api/utility", tags = "Utility Management")
public class UtilityController {

	private static final Logger logger = LoggerFactory.getLogger(UtilityController.class);
	
	BaseResponse response = null;
	
	@Autowired
	UtilityServices utilityServices;
	
	@Autowired
	AdminServices adminServices;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	FileUploadServices fileUploadServices;
	
	
	@Autowired
	FileStorageService fileStorageService;
		
	@GetMapping("/getAllLocation")
	public ResponseEntity<Object> getAllLocation(@RequestParam (value="location")String location  ) {
		logger.info("********************UtilityController getAllLocation-(location for Suggestion)******************");
		response = new BaseResponse();
		
		try {

			 response = utilityServices.getAllLocation(location);						
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
	
	@GetMapping("/getAllCountry")
	public ResponseEntity<Object> getAllCountry() {
		logger.info("********************UtilityController getAllCountry-(country for alert dropdown)******************");
		response = new BaseResponse();
		
		try {

			 response = utilityServices.getAllCountry();						
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
	
	@GetMapping("/getLocationByCountryCode")
	public ResponseEntity<Object> getLocationByCountryCode(@RequestParam (value="countrycode")String countrycode  ) {
		logger.info("********************UtilityController getLocationByCountryCode-(location for alert)******************");
		response = new BaseResponse();
		
		try {

			 response = utilityServices.getLocationByCountryCode(countrycode);						
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
	
	@GetMapping("/getCarriers")
	public ResponseEntity<Object> getAllCarriers() {
		logger.info("********************UtilityController getAllCarriers-(Carriers for Dropdown on home page )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = utilityServices.getAllCarriers();	
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
	
	@GetMapping("/getIncoterm")
	public ResponseEntity<Object> getAllIncotermList() {
		logger.info("********************UtilityController getAllIncoTermList-(IncoTerm List for Dropdown on Upload Charges )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = utilityServices.getAllIncotermList();	
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
	
	@GetMapping("/getChargeGrouping")
	public ResponseEntity<Object> getAllChargeGrouping( ) {
		logger.info("********************UtilityController getAllChargeGrouping-(charge type for Dropdown on home page )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = utilityServices.getAllChargeGrouping();	
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
	
	@GetMapping("/getChargesSubTypeById")
	public ResponseEntity<Object> getAllChargesSubType(@RequestParam (value="groupingChargeId")String groupingChargeId ) {
		logger.info("********************UtilityController getAllChargesSubType-(charge type for Dropdown on Forwarder Dashboard )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = utilityServices.getAllChargesSubType(groupingChargeId);	
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
	
	@GetMapping("/getChargeBasis")
	public ResponseEntity<Object> getAllChargeBasis( ) {
		logger.info("********************UtilityController getAllChargeBasis-(charge basis for Dropdown on charge update )******************");
		response = new BaseResponse();
		
		try {			
			
			 response = utilityServices.getAllChargeBasis();	
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
	
	@GetMapping("/recentSearch")
	public ResponseEntity<Object> getRecentSearchData() {
		logger.info("********************UtilityController getRecentSearchData()******************");
		response = new BaseResponse();
		
		try {

			 response = userServices.getRecentSearchData();
						
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
	
	@GetMapping("/recentSearchById")
	public ResponseEntity<Object> getRecentSearchDataByDataId(@RequestParam (value="id")String id) {
		logger.info("********************UtilityController getRecentSearchDataById()******************");
		response = new BaseResponse();
		
		try {

			 response = userServices.getRecentSearchDataByDataId(id);
						
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
	
	@GetMapping("/packageUnits")
	public ResponseEntity<Object> getPackageUnits() {
		logger.info("********************UtilityController getPackageUnits()******************");
		response = new BaseResponse();
		
		try {

			 response = userServices.getPackageUnitsList();
						
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
	
	// upload origin and destination from excel to master
	// testing purpose
		@PostMapping("/uploadOrgDestData")
		public ResponseEntity<Object> uploadOriginDestinationDataFileToMaster(@RequestParam (value="excelFile",required=true)MultipartFile file) {
			logger.info("********************UtilityController uploadOriginDestinationDataFileToMaster()******************");
			response = new BaseResponse();
			
			try {

				response = fileUploadServices.uploadOriginDestinationDataFileToMaster(file);
							
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
	
	// testing purpose
	@PostMapping("/submitTargetFile")
	public ResponseEntity<Object> submitUploadedFile(@RequestParam (value="excelFile",required=true)MultipartFile file) {
		logger.info("********************UtilityController SubmitUploadedFile()******************");
		response = new BaseResponse();
		
		try {

			response = fileUploadServices.testSubmitFileUpload(file);
						
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
	
	//testing purpose
	@PostMapping("/convertPdfToExcel")
	public ResponseEntity<Object> convertPDFToExcel(@RequestParam (value="pdfFile",required=true)MultipartFile file) {
		logger.info("********************UtilityController convertPDFToExcel()******************");
		response = new BaseResponse();
		
		try {

			response = fileUploadServices.convertPdfToExcel(file);
						
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
	
	
	@GetMapping(value = "/getUploadOfferData")
	public ResponseEntity<Object> getUploadOfferHistory() {
		logger.info("********************UtilityController getUploadOfferHistory()******************");
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
	
	@GetMapping("/getAllAds")
	public ResponseEntity<Object> getAllAds() {
		logger.info("********************UtilityController getAllAds-(Ads for Landing/Home page)******************");
		response = new BaseResponse();
		
		try {

			 response = utilityServices.getAllAds();						
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
	
	/*********submit feedback*****************/
	
	@PostMapping("/submitFeedback")
	   public ResponseEntity<Object> submitFeedback(@RequestBody FeedbackRequest feedbackRequest) {
	    	
	      logger.info("********************UtilityController submitFeedback()******************");			
		  response = new BaseResponse();
			
		  try {				
				response = utilityServices.submitFeedback(feedbackRequest);					
				return ResponseEntity.ok(response); 										
		  }catch (Exception e) {
				
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}			
		} 

	/**********submit feedback****************/
	
	@GetMapping("/getCountrySpecByCode")
	public ResponseEntity<Object> getCountrySpecByCode(@RequestParam (value="countrycode")String countrycode) {
		logger.info("********************UtilityController getRecentSearchDataById()******************");
		response = new BaseResponse();
		
		try {
			 response = utilityServices.getCountrySpecByCode(countrycode);						
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
	
}
