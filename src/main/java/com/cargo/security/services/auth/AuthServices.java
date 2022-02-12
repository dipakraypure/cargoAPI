package com.cargo.security.services.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.LoginRequest;
import com.cargo.load.request.SignupRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.CompanyResponse;

public interface AuthServices {

    public BaseResponse addUser(SignupRequest signupRequest) throws Exception;
	public BaseResponse authenticateUser(LoginRequest loginRequest , HttpServletRequest request) throws Exception;
	//public BaseResponse addUser(SignupRequest signUpRequest, MultipartFile userPanFile, MultipartFile userGSTINFile,MultipartFile userIESFile)throws Exception;
	public BaseResponse forgotPassword(SignupRequest signUpRequest) throws Exception;
	public BaseResponse resetPassword(SignupRequest signUpRequest) throws Exception;
	public BaseResponse addUserVerifyOtp(SignupRequest signUpRequest)throws Exception;
	public BaseResponse resendEmailOtp(SignupRequest signUpRequest)throws Exception;
	public BaseResponse resendMobileOtp(SignupRequest signUpRequest)throws Exception;
	
}
