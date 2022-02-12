package com.cargo.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.AddScheduleChargeRequest;
import com.cargo.load.request.AddScheduleLegsRequest;
import com.cargo.load.request.AddScheduleRequest;
import com.cargo.load.request.CompanyDetailsRequest;
import com.cargo.load.request.ConfigureAlertRequest;
import com.cargo.load.request.SearchTransportRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.repository.UserRepository;
import com.cargo.security.services.forwarder.ForwarderServices;
import com.cargo.security.services.user.UserServices;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/forwarder")
@Api(value = "/api/forwarder", tags = "Forwarder Management")
public class ForwarderController {

private static final Logger logger = LoggerFactory.getLogger(ForwarderController.class);
	
	BaseResponse response = null;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ForwarderServices forwarderServices;
	
	
	@GetMapping("/getForwarderBookingCountByStatus")
	public ResponseEntity<Object> getForwarderBookingCountByStatus(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType ) {
		logger.info("********************UserController getUserBookingCountByStatus******************");
		response = new BaseResponse();
		
		try {
			response = forwarderServices.getForwarderBookingCountByStatus(userId,shipmentType);			
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
	
	// get booking list for all status
		@GetMapping("/getForwarderBookingList")
		public ResponseEntity<Object> getForwarderBookingList(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType,@RequestParam (value="bookingStatus")String bookingStatus ) {
			logger.info("********************ForwarderController getForwarderBookingList******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.getForwarderBookingList(userId,shipmentType,bookingStatus);				
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
			logger.info("********************ForwarderController updateBookingStatus******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.updateBookingStatus(id,userId,bookingStatus);
				
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
		
		@GetMapping("/getForwarderBookingDetailsById")
		public ResponseEntity<Object> getForwarderBookingDetailsById(@RequestParam (value="bookingReff")String bookingReff) {
			logger.info("********************ForwarderController getForwarderBookingDetailsById******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.getForwarderBookingDetailsById(bookingReff);
				
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
		
		@GetMapping("/getForwarderEnquiryCountByStatus")
		public ResponseEntity<Object> getForwarderEnquiryCountByStatus(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType ) {
			logger.info("********************ForwarderController getForwarderEnquiryCountByStatus******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.getForwarderEnquiryCountByStatus(userId,shipmentType);
				
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
		@GetMapping("/getForwarderEnquiryList")
		public ResponseEntity<Object> getForwarderEnquiryList(@RequestParam (value="userId")String userId,@RequestParam (value="shipmentType")String shipmentType,@RequestParam (value="enquiryStatus")String enquiryStatus ) {
			logger.info("********************ForwarderController getForwarderEnquiryList******************");
			response = new BaseResponse();
					
			try {
					response = forwarderServices.getForwarderEnquiryList(userId,shipmentType,enquiryStatus);						
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
				
		// get enquiry with schedule By Id
	    @GetMapping("/getForwarderEnquiryScheduleById")
		public ResponseEntity<Object> getForwarderEnquiryScheduleById(@RequestParam (value="id")String id) {
			logger.info("********************ForwarderController getForwarderEnquiryScheduleById******************");
			response = new BaseResponse();
							
			try {
					response = forwarderServices.getForwarderEnquiryScheduleById(id);
								
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
		// get enquiry with charges By Id
	    @GetMapping("/getForwarderEnquiryChargesById")
		public ResponseEntity<Object> getForwarderEnquiryChargesById(@RequestParam (value="id")String id) {
			logger.info("********************ForwarderController getForwarderEnquiryChargesById******************");
			response = new BaseResponse();
							
			try {
					response = forwarderServices.getForwarderEnquiryChargesById(id);
								
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
	    

	    //addSchedule by forwarder
	    @PostMapping("/addSchedule/{userid}")
		public ResponseEntity<Object> addSchedule(@RequestBody AddScheduleRequest addScheduleRequest,@PathVariable String userid) {
	    	//List<Map<String, String>> attributeMap
			logger.info("********************ForwarderController addSchedule()******************");
			
			response = new BaseResponse();
			
			try {
				
				 response = forwarderServices.addSchedule(addScheduleRequest,userid);
					
 				 return ResponseEntity.ok(response);
 				 
						
			}catch (Exception e) {
				
				logger.error(e.getMessage()); 
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
	    
	    //addScheduleCharge by forwarder
	    @PostMapping("/addScheduleCharge/{userid}")
		public ResponseEntity<Object> addScheduleCharge(@RequestBody AddScheduleChargeRequest addScheduleChargeRequest,@PathVariable String userid) {
	    	
			logger.info("********************ForwarderController addScheduleCharge()******************");
			
			response = new BaseResponse();
			
			try {				
				 response = forwarderServices.addScheduleCharge(addScheduleChargeRequest,userid);
					
 				 return ResponseEntity.ok(response);
						
			}catch (Exception e) {
				
				logger.error(e.getMessage()); 
				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);
				
			}
			
		}
	    
	    @GetMapping("/updateEnquiryStatus")
		public ResponseEntity<Object> updateEnquiryStatus(@RequestParam (value="id")String id,@RequestParam (value="userId")String userId,@RequestParam (value="enquiryStatus")String enquiryStatus) {
			logger.info("********************ForwarderController updateEnquiryStatus******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.updateEnquiryStatus(id,userId,enquiryStatus);
				
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
	    
	 // get enquiry schedule charges details By Id
	    @GetMapping("/getForwEnqScheduleChargeDetailsById")
		public ResponseEntity<Object> getForwarderEnquiryScheduleChargesById(@RequestParam (value="id")String forwEnqId,@RequestParam (value="userId")String userId) {
			logger.info("********************ForwarderController getForwarderEnquiryScheduleChargesById******************");
			response = new BaseResponse();
							
			try {
					response = forwarderServices.getForwarderEnquiryScheduleChargesById(forwEnqId,userId);
								
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
	    	    
	    @DeleteMapping("/deleteForwarderEnquiryById/{id}/{userId}")
		public ResponseEntity<Object> deleteForwarderEnquiryById(@PathVariable("id")String id,@PathVariable ("userId")String userId) {
			logger.info("********************ForwarderController deleteForwarderEnquiryById******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.deleteForwarderEnquiryById(id,userId);				
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
	    
	    @DeleteMapping("/deleteForwarderBookingById/{id}/{userId}")
		public ResponseEntity<Object> deleteForwarderBookingById(@PathVariable ("id")String id,@PathVariable ("userId")String userId) {
			logger.info("********************ForwarderController deleteForwarderBookingById******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.deleteForwarderBookingById(id,userId);				
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
	    
	  /*Start Configure Alerts API*/
	  
	   @PostMapping("/addOriginLocationAlert")
	   public ResponseEntity<Object> addOriginLocationAlert(@RequestBody ConfigureAlertRequest configureAlertRequest) {
	    	
	      logger.info("********************ForwarderController addOriginLocationAlert()******************");			
		  response = new BaseResponse();
			
		  try {				
				response = forwarderServices.addOriginLocationAlert(configureAlertRequest);					
 				return ResponseEntity.ok(response); 										
		  }catch (Exception e) {
				
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
	   
	   @PutMapping("/removeOriginLocationAlert")
	   public ResponseEntity<Object> removeOriginLocationAlert(@RequestBody ConfigureAlertRequest configureAlertRequest) {
	    	
	      logger.info("********************ForwarderController removeOriginLocationAlert()******************");			
		  response = new BaseResponse();
			
		  try {				
				response = forwarderServices.removeOriginLocationAlert(configureAlertRequest);					
 				return ResponseEntity.ok(response); 										
		  }catch (Exception e) {
				
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
	  
	   @PostMapping("/addDestinationLocationAlert")
	   public ResponseEntity<Object> addDestinationLocationAlert(@RequestBody ConfigureAlertRequest configureAlertRequest) {
	    	
	      logger.info("********************ForwarderController addDestinationLocationAlert()******************");			
		  response = new BaseResponse();
			
		  try {				
				response = forwarderServices.addDestinationLocationAlert(configureAlertRequest);					
 				return ResponseEntity.ok(response); 										
		  }catch (Exception e) {
				
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
	   
	   @PutMapping("/removeDestinationLocationAlert")
	   public ResponseEntity<Object> removeDestinationLocationAlert(@RequestBody ConfigureAlertRequest configureAlertRequest) {
	    	
	      logger.info("********************ForwarderController removeDestinationLocationAlert()******************");			
		  response = new BaseResponse();
			
		  try {				
				response = forwarderServices.removeDestinationLocationAlert(configureAlertRequest);					
 				return ResponseEntity.ok(response); 										
		  }catch (Exception e) {
				
		      	logger.error(e.getMessage()); 				
				response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				response.setRespData(e.getMessage());
				
				return ResponseEntity.badRequest().body(response);				
			}			
		} 
	   
	   @GetMapping("/getAllConfiguredLocation")
		public ResponseEntity<Object> getAllConfiguredLocation(@RequestParam (value="userId")String userId) {
			logger.info("********************ForwarderController getAllConfiguredLocationByUserId******************");
			response = new BaseResponse();
			
			try {

				response = forwarderServices.getAllConfiguredLocationByUserId(userId);				
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
	  /*Start Configure Alerts API*/
}
