package com.cargo.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.cargo.load.request.BookScheduleRequest;
import com.cargo.load.request.CompanyDetailsRequest;
import com.cargo.load.request.SearchTransportRequest;
import com.cargo.load.request.SendEnquiryRequest;
import com.cargo.load.request.UserDetailsRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.repository.UserRepository;
import com.cargo.security.services.user.UserServices;
import com.cargo.security.services.utility.UtilityServices;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
@Api(value = "/api/user", tags = "User Management - Importer/Exporter")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	BaseResponse response = null;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UtilityServices utilityServices;
	
	@PutMapping("/deactivateAccount")
	public ResponseEntity<Object> deactivateAccount(@RequestBody UserDetailsRequest userDetailsRequest, HttpServletRequest request) {

		
		logger.info("********************UserController deactivateAccount()******************");		
		response = new BaseResponse();
		
		try {
			
			 response = userServices.deactivateAccount(userDetailsRequest);			
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@PostMapping("/checkSearchTransDetails")
	public ResponseEntity<Object> checkSearchTransportationDetails(@RequestBody SearchTransportRequest searchTransRequest, HttpServletRequest request) {

		
		logger.info("********************UserController checkSearchTransportationDetails(for search result in datatable)******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = userServices.checkTransportationDetailsBySearch(searchTransRequest);
				
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	@PostMapping("/sendEnquirySearchRequest")
	public ResponseEntity<Object> sendEnquirySearchRequest(@RequestBody SendEnquiryRequest sendEnquiryRequest, HttpServletRequest request) {

		
		logger.info("********************UserController sendEnquirySearchRequest(for search result in datatable)******************");
		
		response = new BaseResponse();
		
		try {
			
			 response = userServices.sendEnquirySearchRequest(sendEnquiryRequest);			
			 return ResponseEntity.ok(response);
					
		}catch (Exception e) {
			
			logger.error(e.getMessage()); 
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		
	}
	
	
	@PostMapping(value = "/updateCompany" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> updateUserCompanyDetails(@ModelAttribute CompanyDetailsRequest companyDetailsRequest,@RequestParam (value="userPanFile",required=false) MultipartFile userPanFile,@RequestParam (value="userIECFile",required=false) MultipartFile userIECFile,@RequestParam (value="logo",required=false) MultipartFile logo) {
		
		logger.info("********************updateUserCompanyDetails Method******************");	
		response = new BaseResponse();
		
		try {
				
				response = userServices.updateUserCompanyDetails(companyDetailsRequest,userPanFile,userIECFile,logo);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {

			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	// get booking list for all status
	@GetMapping("/getUserBookingList")
	@ApiOperation(value = "Get user booking list", notes = "Get User Booking details by userId , shipment type and booking status")
	  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Successfully retrieved the booking information"),            
      @ApiResponse(code = 404, message = "No booking data found"),
      @ApiResponse(code = 500, message = "Internal server error")
    })
	public ResponseEntity<Object> getUserBookingList(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType,@RequestParam (value="bookingStatus")String bookingStatus ) {
		logger.info("********************UserController getUserBookingList******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserBookingList(userId,shipmentType,bookingStatus);
			
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
	
	// get enquiry list for all status
		@GetMapping("/getUserEnquiryList")
		public ResponseEntity<Object> getUserEnquiryList(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType,@RequestParam (value="enquiryStatus")String enquiryStatus ) {
			logger.info("********************UserController getUserEnquiryList******************");
			response = new BaseResponse();
			
			try {

				response = userServices.getUserEnquiryList(userId,shipmentType,enquiryStatus);
				
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
	 
	@GetMapping("/getUserBookingCountByStatus")
	public ResponseEntity<Object> getUserBookingCountByStatus(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType  ) {
		logger.info("********************UserController getUserBookingCountByStatus******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserBookingCountByStatus(userId,shipmentType);
			
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
	
	
	@GetMapping("/updateBookingStatus")
	public ResponseEntity<Object> updateBookingStatus(@RequestParam (value="id")String id,@RequestParam (value="userId")String userId,@RequestParam (value="bookingStatus")String bookingStatus) {
		logger.info("********************UserController updateBookingStatus******************");
		response = new BaseResponse();
		
		try {

			response = userServices.updateBookingStatus(id,userId,bookingStatus);
			
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
	
	@GetMapping("/getUserEnquiryDetailsById")
	public ResponseEntity<Object> getUserEnquiryDetailsById(@RequestParam (value="enquiryId")String enquiryId) {
		logger.info("********************UserController getUserEnquiryDetailsById******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserEnquiryDetailsById(enquiryId);			
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
	
	@GetMapping("/getUserBookingDetailsById")
	public ResponseEntity<Object> getUserBookingDetailsById(@RequestParam (value="bookingReff")String bookingReff) {
		logger.info("********************UserController getUserBookingDetailsById******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserBookingDetailsById(bookingReff);
			
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
	

	@GetMapping("/companyDetailsByUserId")
	public ResponseEntity<Object> getCompanyDetailsByUserId(@RequestParam (value="userId")String userId ) {
		logger.info("********************UserController getCompanyDetailsByUserId******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getExistCompanyDetailsByUserId(userId);
			
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
	
	
	@PostMapping(value = "/bookSchedule")
	public ResponseEntity<Object> bookSchedule(@ModelAttribute BookScheduleRequest bookScheduleRequest) {
		
		logger.info("********************bookSchedule Method******************");
		
		response = new BaseResponse();
		
		try {
				response = userServices.bookSchedule(bookScheduleRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@GetMapping("/getUserEnquiryCountByStatus")
	public ResponseEntity<Object> getUserEnquiryCountByStatus(@RequestParam (value="userId")String userId ,@RequestParam (value="shipmentType")String shipmentType ) {
		logger.info("********************UserController getUserEnquiryCountByStatus******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserEnquiryCountByStatus(userId,shipmentType);
			
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
	
	// get user profile details by id for customer my profile page
	@GetMapping("/getUserProfileDetailsById/{userId}")
	public ResponseEntity<Object> getUserProfileDetailsById(@PathVariable String userId ) {
		logger.info("********************UserController getUserProfileDetailsById******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getUserProfileDetailsById(userId);
			
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
	
	//update user my profile
	@PutMapping(value = "/updateUserMyProfile")
	public ResponseEntity<Object> updateUserMyProfile(@ModelAttribute UserDetailsRequest userDetailsRequest) {
		
		logger.info("********************updateUserMyProfile Method******************");
		
		response = new BaseResponse();
		
		try {
				response = userServices.updateUserMyProfile(userDetailsRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	//user my profile reset password 
	@PutMapping(value = "/updateMyProfileResetPass")
	public ResponseEntity<Object> updateMyProfileResetPassword(@ModelAttribute UserDetailsRequest userDetailsRequest) {
		
		logger.info("********************updateMyProfileResetPassword Method******************");
		
		response = new BaseResponse();
		
		try {
				response = userServices.updateMyProfileResetPassword(userDetailsRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	
	//sendVerifyMobileOtp
	//user my profile sendVerifyMobileOtp
	@PostMapping(value = "/sendVerifyMobileOtp")
	public ResponseEntity<Object> sendVerifyMobileOtp(@ModelAttribute UserDetailsRequest userDetailsRequest) {
			
		logger.info("********************sendVerifyMobileOtp Method******************");
			
		response = new BaseResponse();
			
		try {
				response = userServices.sendVerifyMobileOtp(userDetailsRequest);					
				return ResponseEntity.ok(response);
				
		} catch (Exception e) {
			
				
			logger.error(e.getMessage());
				
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
				
			return ResponseEntity.badRequest().body(response);
				
		}
	}
	
	//resendVerifyMobileOtp
	//user my profile resendVerifyMobileOtp
	@PostMapping(value = "/resendVerifyMobileOtp")
		public ResponseEntity<Object> resendVerifyMobileOtp(@ModelAttribute UserDetailsRequest userDetailsRequest) {
				
		logger.info("********************sendVerifyMobileOtp Method******************");
				
		response = new BaseResponse();
				
		try {
			response = userServices.resendVerifyMobileOtp(userDetailsRequest);					
			return ResponseEntity.ok(response);					
		} catch (Exception e) {		
					
	        logger.error(e.getMessage());
					
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
					
			return ResponseEntity.badRequest().body(response);
					
		}
	}
	
	//user my profile verifyMobileOtp
	@PostMapping(value = "/verifyMobileOtp")
	public ResponseEntity<Object> verifyMobileOtp(@ModelAttribute UserDetailsRequest userDetailsRequest) {
				
		logger.info("********************verifyMobileOtp Method******************");
				
		response = new BaseResponse();
				
		try {
				response = userServices.verifyMobileOtp(userDetailsRequest);					
				return ResponseEntity.ok(response);
					
		} catch (Exception e) {
				
				
			logger.error(e.getMessage());
				
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
					
			return ResponseEntity.badRequest().body(response);
					
		}
	}
	
	@GetMapping("/updateEnquiryStatus")
	public ResponseEntity<Object> updateEnquiryStatus(@RequestParam (value="id")String id,@RequestParam (value="userId")String userId,@RequestParam (value="enquiryStatus")String enquiryStatus) {
		logger.info("********************UserController updateEnquiryStatus******************");
		response = new BaseResponse();
		
		try {

			response = userServices.updateEnquiryStatus(id,userId,enquiryStatus);
			
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
	
	@GetMapping("/getEnquiryAcceptedList")
	public ResponseEntity<Object> getEnquiryAcceptedList(@RequestParam (value="userId")String userId,@RequestParam (value="enquiryId")String enquiryId) {
		logger.info("********************UserController getEnquiryAcceptedList******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getEnquiryAcceptedList(userId,enquiryId);
			
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
	
	@PostMapping(value = "/bookScheduleFromEnquiry")
	public ResponseEntity<Object> bookScheduleFromEnquiry(@ModelAttribute BookScheduleRequest bookScheduleRequest) {
		
		logger.info("********************bookScheduleFromEnquiry Controller******************");
		
		response = new BaseResponse();
		
		try {
				response = userServices.bookScheduleFromEnquiry(bookScheduleRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	/*
	// get enquiry schedule charges details By enquiryReff
    @GetMapping("/getEnquiryScheduleChargesDetailsByEnquiryReff")
	public ResponseEntity<Object> getForwarderEnquiryScheduleChargesById(@RequestParam (value="enquiryReff")String enquiryReff,@RequestParam (value="forwarderid")String forwarderid) {
		logger.info("********************UserController getForwarderEnquiryScheduleChargesById******************");
		response = new BaseResponse();
						
		try {
				response = userServices.getEnquiryScheduleChargesDetailsByEnquiryReff(enquiryReff,forwarderid);
							
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
    */
	
    @GetMapping("/getForwarderEnquiryStatusByReference")
	public ResponseEntity<Object> getForwarderEnquiryStatusByReference(@RequestParam (value="userId")String userId,@RequestParam (value="enquiryReference")String enquiryReference) {
		logger.info("********************UserController getForwarderEnquiryStatusByReference******************");
		response = new BaseResponse();
		
		try {

			response = userServices.getForwarderEnquiryStatusByReference(userId,enquiryReference);
			
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
    
    @DeleteMapping("/deleteUserEnquiryById/{id}/{userId}")
	public ResponseEntity<Object> deleteUserEnquiryById(@PathVariable("id")String id,@PathVariable("userId")String userId) {
		logger.info("********************UserController deleteUserEnquiryById******************");
		response = new BaseResponse();
		
		try {

			response = userServices.deleteUserEnquiryById(id,userId);				
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
    
    @DeleteMapping("/deleteUserBookingById/{id}/{userId}")
	public ResponseEntity<Object> deleteUserBookingById(@PathVariable ("id")String id,@PathVariable ("userId")String userId) {
		logger.info("********************UserController deleteUserBookingById******************");
		response = new BaseResponse();
		
		try {

			response = userServices.deleteUserBookingById(id,userId);				
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
