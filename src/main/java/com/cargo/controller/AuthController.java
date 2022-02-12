package com.cargo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.LoginRequest;
import com.cargo.load.request.SignupRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.CarrierResponse;
import com.cargo.load.response.MessageResponse;
import com.cargo.load.response.RoleResponse;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.security.services.auth.AuthServices;
import com.cargo.security.services.fileupload.FileUploadServices;
import com.cargo.security.services.user.UserServices;
import com.cargo.security.services.utility.UtilityServices;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;


@CrossOrigin(origins = {"*","http://15.207.223.54:8080/","http://15.207.223.54:8081/"}, maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Api(value = "/api/auth", tags = "Authentication Management")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

	BaseResponse response = null;
	
	@Autowired
	AuthServices	authServices;
	
	@Autowired
	UtilityServices utilityServices;
	
	@Autowired
	UserServices userServices;
	
	@Autowired
	FileUploadServices fileUploadServices;
	
	
	@Autowired
	FileStorageService fileStorageService;
	

	@PostMapping("/signin")
	public ResponseEntity<Object> authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {

		
		logger.info("********************AuthController authenticateUser******************");
		
		response = new BaseResponse();
		
		try {
			
			if (loginRequest == null) {
				return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
			}
			
			response = authServices.authenticateUser(loginRequest,request);
								
		}catch (Exception e) {
			
			logger.error(e.getMessage()); //BAD credentials message comes from here
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
		return ResponseEntity.ok(response);
	}
	
	@PostMapping(value = "/signup" )
	public ResponseEntity<Object> registerUser(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************registerUser signup Method******************");
		logger.info(signUpRequest.getMobilenumber()); 
		response = new BaseResponse();
		
		try {
				response = authServices.addUser(signUpRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PostMapping(value = "/signup_verify_otp" )
	public ResponseEntity<Object> registerUserVerifyOtp(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************registerUserVerifyOtp signup Method******************");
		
		response = new BaseResponse();
		
		try {
				response = authServices.addUserVerifyOtp(signUpRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PostMapping(value = "/forgotPassword")
	public ResponseEntity<Object> forgotPassword(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************forgotPassword Method in AuthController******************");
		
		response = new BaseResponse();
		
		try {
				response = authServices.forgotPassword(signUpRequest);				
				return ResponseEntity.ok(response);
				
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PostMapping(value = "/resetPassword")
	public ResponseEntity<Object> resetPassword(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************resetPassword Method in AuthController******************");
		
		response = new BaseResponse();
		
		try {
				response = authServices.resetPassword(signUpRequest);				
				return ResponseEntity.ok(response);
				
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	
	@GetMapping("/getAllRole")
	public ResponseEntity<Object> getAllRole( ) {
		logger.info("********************AuthController getAllRole-(User Role for Dropdown)******************");
		response = new BaseResponse();
		
		try {

			List<RoleResponse> stateResponses = utilityServices.getAllRoles();
						
			response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			response.setRespData(stateResponses);
			
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
	
	// Resend OTP from after signup modal
	@PostMapping(value = "/resendEmailOtp" )
	public ResponseEntity<Object> resendEmailOtp(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************resendEmailOtp signup Method******************");
		response = new BaseResponse();
		
		try {
				response = authServices.resendEmailOtp(signUpRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	@PostMapping(value = "/resendMobileOtp" )
	public ResponseEntity<Object> resendMobileOtp(@ModelAttribute SignupRequest signUpRequest) {
		
		logger.info("********************resendMobileOtp signup Method******************");
		response = new BaseResponse();
		
		try {
				response = authServices.resendMobileOtp(signUpRequest);
					
				return ResponseEntity.ok(response);
		} catch (Exception e) {
		
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	
}
