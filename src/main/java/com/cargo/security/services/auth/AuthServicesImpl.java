package com.cargo.security.services.auth;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.cargo.load.request.LoginRequest;
import com.cargo.load.request.SignupRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.JwtResponse;
import com.cargo.mail.EmailService;
import com.cargo.mail.Mail;
import com.cargo.mail.SignupEmailMapper;
import com.cargo.models.CompanyEntity;
import com.cargo.models.LoginLogEntity;
import com.cargo.models.RoleEntity;
import com.cargo.models.UserEntity;
import com.cargo.models.VerifyAccountEntity;
import com.cargo.repository.CompanyRepository;
import com.cargo.repository.LoginLogRepository;
import com.cargo.repository.RoleRepository;
import com.cargo.repository.UserRepository;
import com.cargo.repository.VerifyAccoutRepository;
import com.cargo.security.jwt.JwtUtils;
import com.cargo.security.services.admin.UserDetailsImpl;
import com.cargo.sms.SmsService;
import com.cargo.utils.CargoUtility;
import com.cargo.utils.FileStorageUtility;
import com.cargo.utils.GstinVerificationAPIServices;
import com.cargo.utils.RandomKeyServices;
import com.cargo.utils.StringsUtils;

import net.bytebuddy.utility.RandomString;


@Service
public class AuthServicesImpl implements AuthServices{
	private static final Logger logger = LoggerFactory.getLogger(AuthServicesImpl.class);

	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	RandomKeyServices randomKeyServices;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired 
	FileStorageUtility fileStorageUtility;
	
	@Autowired
	LoginLogRepository loginLogRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	VerifyAccoutRepository verifyAccoutRepository;
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;
	
	BaseResponse baseResponse	= null;
	
	@Override
	public BaseResponse addUser(SignupRequest signupRequest) throws Exception {
		
		logger.info("********************addUser Method in AuthServicesImpl******************");
		signupRequest.setUsername(signupRequest.getEmail());
		signupRequest.setLoginuserid(signupRequest.getUsername());
		
		String mobilenumber = signupRequest.getMobilenumber();
		mobilenumber = mobilenumber.replaceAll("\\s", "");
		
		logger.info("signupRequest.getLoginuserid()"+ signupRequest.getLoginuserid());
		
		baseResponse = new BaseResponse();
	
		UserEntity userEntities  = userRepository.findByEmail(signupRequest.getEmail());
		if(userEntities != null) {
			throw new Exception("Error: This email id already exists");
		}
		
		UserEntity userEnti  = userRepository.findByMobileno(mobilenumber);
		if(userEnti != null) {
			throw new Exception("Error: This mobile number already exists");
		}
				
		RoleEntity uRole = roleRepository.findRoleById(Long.parseLong(signupRequest.getRole()));
			
		if (uRole == null) {
			throw new Exception("Error: This Role does not exist!");
		}
		
		
		String userId = signupRequest.getUsername();
		String gstinnumber = signupRequest.getGstinnumber();
		
		CompanyEntity companyEntity = GstinVerificationAPIServices.getGstinDetailsByGstinNumber(gstinnumber);
		
		if(companyEntity == null) {
			throw new Exception("Error: Invalid GSTIN Number");
		}
		
		VerifyAccountEntity verifyAccountEntity = new VerifyAccountEntity();
		verifyAccountEntity.setCreateby("system");
		
		//String verifyemailtoken = RandomString.make(8);
		int verifyemailtokenInt = emailService.generateRandomEmailOTP();
		String verifyemailtoken = Integer.toString(verifyemailtokenInt);
		
		SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int minutesToAdd = 20;
 
		Calendar date = Calendar.getInstance();
		logger.info("Initial Time: " + currentDateTime.format(date.getTime()));
		
        Calendar startTimes = date;
        startTimes.add(date.MINUTE, minutesToAdd);
		String newDateString = currentDateTime.format(startTimes.getTime());
		
		String expiredemailtoken = newDateString;
		
		verifyAccountEntity.setVerifyemailtoken(verifyemailtoken);
		verifyAccountEntity.setExpiredemailtoken(expiredemailtoken);
		
		String expiredmobiletoken = newDateString;
		int verifymobiletoken = smsService.generateRandomMobileOTP();
		String verifymobiletokenString = Integer.toString(verifymobiletoken);
		verifyAccountEntity.setEmail(signupRequest.getEmail());
		verifyAccountEntity.setMobilenumber(mobilenumber);
		verifyAccountEntity.setVerifymobiletoken(verifymobiletokenString);
		verifyAccountEntity.setExpiredmobiletoken(expiredmobiletoken);
		
		verifyAccoutRepository.save(verifyAccountEntity);
		
		//send OTP Email to customer 
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(signupRequest.getEmail());
		mailExceed.setSubject("Book My Cargo: Registration OTP");
				
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(signupRequest.getEmail());
		signupEmail.setToken(verifyemailtoken);
				
				
		try {
				emailService.sendSignupOTPMail(mailExceed, signupEmail);
		}catch(Exception e) {
				e.printStackTrace();
		}
				
		logger.info("Please check Email for OTP!");
		
		//send OTP SMS to customer
		smsService.sendSignupOTPMobile(verifymobiletokenString,mobilenumber);
		logger.info("Please check Mobile number "+mobilenumber+" for SMS OTP!");
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "OTP Sent successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse authenticateUser(LoginRequest loginRequest, HttpServletRequest request) throws Exception {
        
		logger.info("********************authenticateUser Method in AuthServicesImpl******************");
		
        baseResponse = new BaseResponse();
		
		logger.info("loginRequest.username:"+ loginRequest.getUsername());
		
		if(loginRequest.getUsername().equalsIgnoreCase("")) {
			throw new Exception("Error: Login Id cannot be empty!");
		}
		if(loginRequest.getPassword().equalsIgnoreCase("")) {
			throw new Exception("Error: Password cannot be empty!");
		}
		
		
		if(!userRepository.existsByUsernameAndIsdeletedAndIsactiveAndIsemailverify(loginRequest.getUsername(),"N","ACTIVE","Y")) {
			throw new Exception("Error: User unauthorized!");
		}
				
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

	
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		
		
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();	
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(new JwtResponse(jwt, 
				 userDetails.getId(), 
				 userDetails.getUserid(),
				 userDetails.getUsername(), 
				 userDetails.getEmail(), 
				 userDetails.getRole()));
		
		String logindatetime = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		
		LoginLogEntity loginLog =new LoginLogEntity();
		loginLog.setCreateby("system");
		loginLog.setUserId(userDetails.getId());
		loginLog.setUsername(userDetails.getUsername());
		loginLog.setUsercode(userDetails.getRole());
		loginLog.setSessionid(jwt);
		String userIpAddress = new CargoUtility().getClientIpAddress(request);
		loginLog.setIpaddress(userIpAddress);
		loginLog.setLogindatetime(logindatetime);
		loginLog.setIsdeleted("N");
		
		loginLogRepository.save(loginLog);
		
		return baseResponse;
	}

	@Override
	public BaseResponse forgotPassword(SignupRequest signUpRequest) throws Exception {
		logger.info("********************forgotPassword Method in AuthServicesImpl******************");
		
		baseResponse = new BaseResponse();
	
		String email = signUpRequest.getEmail();
		
		UserEntity userEntities  = userRepository.findByEmail(email);
		if(userEntities == null) {
			throw new Exception("Error: Email Id not valid!");
		}

		long id = userEntities.getId();
		
		UserEntity userEnti = userRepository.getOne(id);
		//String token = RandomString.make(8);
		int verifyemailtokenInt = emailService.generateRandomEmailOTP();
		String token = Integer.toString(verifyemailtokenInt);
		
		logger.info("Token : "+token);
		userEnti.setResetpasswordtoken(token);
		userRepository.save(userEnti);
		
		//successful mail to customer with change password link
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(email);
		mailExceed.setSubject("Book My Cargo: Forgot Password");
		
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(email);
		signupEmail.setToken(token);
		
		try {
			emailService.sendForgotPasswordMail(mailExceed, signupEmail);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Email send successfully!");
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Forgot password Link send successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;

	}

	@Override
	public BaseResponse resetPassword(SignupRequest signUpRequest) throws Exception {
        logger.info("********************resetPassword Method in AuthServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		String password = signUpRequest.getPassword();
		String token = signUpRequest.getResettoken();
		
		UserEntity userEntities  = userRepository.findByResetpasswordtoken(token);
		
		if(userEntities == null) {
			throw new Exception("Error: Invalid Token!");
		}

		long id = userEntities.getId();
		
		UserEntity userEnti = userRepository.getOne(id);
		
		String email = userEnti.getEmail();
		logger.info("Password : "+password);
		userEnti.setPassword(encoder.encode(password));
		userRepository.save(userEnti);
		
		//successful mail to customer with change password link
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(email);
		mailExceed.setSubject("Book My Cargo: Reset Password");
		
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(email);
		signupEmail.setPassword(password);
		
		try {
			emailService.sendResetPasswordMail(mailExceed, signupEmail);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Email send successfully!");
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "You have successfully changed your password!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse addUserVerifyOtp(SignupRequest signUpRequest) throws Exception {
		logger.info("********************addUserVerifyOtp Method in AuthServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		String emailVerifyOtp = signUpRequest.getEmailverifyotp().trim();
		String mobileVerifyOtp = signUpRequest.getMobileverifyotp().trim();
		
		VerifyAccountEntity verifyAccountEntity = verifyAccoutRepository.findByVerifyemailtokenAndVerifymobiletoken(emailVerifyOtp,mobileVerifyOtp);
		
		if(verifyAccountEntity == null) {
			throw new Exception("Error: Invalid OTP");
		}
		
		String emailOtpExpiryTime = verifyAccountEntity.getExpiredemailtoken();
		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info("check OTP expiry: "+sdf.parse(currentTimestamp).before(sdf.parse(emailOtpExpiryTime)));
		
		if(! sdf.parse(currentTimestamp).before(sdf.parse(emailOtpExpiryTime))) {
			throw new Exception("Error: Email OTP has Expired");
		}
		
		String mobileOtpExpiryTime = verifyAccountEntity.getExpiredmobiletoken();
		
		SimpleDateFormat sdfM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info("check OTP expiry: "+sdf.parse(currentTimestamp).before(sdfM.parse(mobileOtpExpiryTime)));
		
		if(! sdf.parse(currentTimestamp).before(sdfM.parse(mobileOtpExpiryTime))) {
			throw new Exception("Error: Mobile OTP has Expired");
		}
		
		RoleEntity uRole = roleRepository.findRoleById(Long.parseLong(signUpRequest.getRole()));
		
		if (uRole == null) {
			throw new Exception("Error: This Role does not exist!");
		}
		
		//String userId = signUpRequest.getUsername();
		String userId = signUpRequest.getEmail();
		String username = signUpRequest.getEmail();
		String gstinnumber = signUpRequest.getGstinnumber();
		
        CompanyEntity companyEntity = GstinVerificationAPIServices.getGstinDetailsByGstinNumber(gstinnumber);
		
		if(companyEntity != null) {
			companyRepository.save(companyEntity);
		}
		
		String mobilenumber = signUpRequest.getMobilenumber();
		mobilenumber = mobilenumber.replaceAll("\\s", "");
		
		UserEntity user = new UserEntity(
				username,
				mobilenumber,
				signUpRequest.getEmail(),
				userId,				
				encoder.encode(signUpRequest.getPassword()),
				signUpRequest.getStatus()
				
			);
		
		user.setGstinnumber(gstinnumber);
		if(companyEntity != null) {
			user.setCompanyId(companyEntity.getId());
		}
		
		String verifyDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		
		
		user.setCreateby("system");
		user.setIsemailverify("Y");
		user.setEmailverifydate(verifyDate);
		user.setIsmobileverify("Y");
		user.setMobileverifydate(verifyDate);
		user.setIsdeleted("N");

	    user.setUserroleid(uRole.getId());
	    user.setRole(uRole.getCode());		
		user.setIsactive("ACTIVE");
		
		userRepository.save(user);
		
		//successful mail to customer with credentials
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(signUpRequest.getEmail());
		mailExceed.setSubject("Book My Cargo: Registration");
		
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(signUpRequest.getEmail());
		signupEmail.setPassword(signUpRequest.getPassword());
		
		try {
			emailService.sendSignupSuccessMail(mailExceed, signupEmail);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		logger.info("Email send to user successfully!");
		
		//successful mail to Admin with credentials
		UserEntity userAdmin = userRepository.findByRole("AA");
		
		Mail mailExceedAdmin = new Mail();
		mailExceedAdmin.setFrom("BMC<BookMyCargoNow@gmail.com>");
		mailExceedAdmin.setTo(userAdmin.getEmail());
		mailExceedAdmin.setSubject("Book My Cargo:New User Registration");
				
		SignupEmailMapper signupEmailAdmin = new SignupEmailMapper();
		signupEmailAdmin.setEmail(signUpRequest.getEmail());
		signupEmailAdmin.setRole(user.getRole());
						
		try {
			emailService.sendSignupSuccessToAdminMail(mailExceedAdmin, signupEmailAdmin);
	    }catch(Exception e) {
			e.printStackTrace();
		}
	
		logger.info("Email send to Admin successfully!");
		
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "User Registered successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse resendEmailOtp(SignupRequest signUpRequest) throws Exception {
		
		logger.info("********************resendEmailOtp Method in AuthServicesImpl******************");
		
		String email = signUpRequest.getEmail();
		String mobilenumber = signUpRequest.getMobilenumber();
		mobilenumber = mobilenumber.replaceAll("\\s", "");
		
        VerifyAccountEntity verifyAccountEntity = verifyAccoutRepository.findByEmailAndMobilenumber(email,mobilenumber);
		
		if(verifyAccountEntity == null) {
			throw new Exception("Error: Invalid Email and Mobile number");
		}
		
		long id = verifyAccountEntity.getId();
		
		VerifyAccountEntity verifyAccountEnti =  verifyAccoutRepository.getOne(id);
		
		//String verifyemailtoken = RandomString.make(8);
		int verifyemailtokenInt = emailService.generateRandomEmailOTP();
		String verifyemailtoken = Integer.toString(verifyemailtokenInt);
		
		SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int minutesToAdd = 20;
 
		Calendar date = Calendar.getInstance();
		logger.info("Initial Time: " + currentDateTime.format(date.getTime()));
		
        Calendar startTimes = date;
        startTimes.add(date.MINUTE, minutesToAdd);
		String newDateString = currentDateTime.format(startTimes.getTime());
		
		String expiredemailtoken = newDateString;
		
		verifyAccountEntity.setVerifyemailtoken(verifyemailtoken);
		verifyAccountEntity.setExpiredemailtoken(expiredemailtoken);
		
		verifyAccoutRepository.save(verifyAccountEnti);
		
		//send OTP Email to customer 
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(signUpRequest.getEmail());
		mailExceed.setSubject("Book My Cargo: Resend Registration OTP");
				
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(signUpRequest.getEmail());
		signupEmail.setToken(verifyemailtoken);
				
				
		try {
				emailService.sendSignupOTPMail(mailExceed, signupEmail);
		}catch(Exception e) {
				e.printStackTrace();
		}
				
		logger.info("Please check Email for OTP!");
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "OTP Resend on your mail successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse resendMobileOtp(SignupRequest signUpRequest) throws Exception {
	
		logger.info("********************resendMobileOtp Method in AuthServicesImpl******************");
		
		String email = signUpRequest.getEmail();
		String mobilenumber = signUpRequest.getMobilenumber();
		mobilenumber = mobilenumber.replaceAll("\\s", "");
		
        VerifyAccountEntity verifyAccountEntity = verifyAccoutRepository.findByEmailAndMobilenumber(email,mobilenumber);
		
		if(verifyAccountEntity == null) {
			throw new Exception("Error: Invalid Email and Mobile number");
		}
		
		long id = verifyAccountEntity.getId();
		
		VerifyAccountEntity verifyAccountEnti =  verifyAccoutRepository.getOne(id);
				
		SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int minutesToAdd = 20;
 
		Calendar date = Calendar.getInstance();
		logger.info("Initial Time: " + currentDateTime.format(date.getTime()));
		
        Calendar startTimes = date;
        startTimes.add(date.MINUTE, minutesToAdd);
		String newDateString = currentDateTime.format(startTimes.getTime());
		
		String expiredmobiletoken = newDateString;
		int verifymobiletoken = smsService.generateRandomMobileOTP();
		String verifymobiletokenString = Integer.toString(verifymobiletoken);
		
		verifyAccountEntity.setVerifymobiletoken(verifymobiletokenString);
		verifyAccountEntity.setExpiredmobiletoken(expiredmobiletoken);
		
		verifyAccoutRepository.save(verifyAccountEnti);
		
		//send OTP SMS to customer
		smsService.sendSignupOTPMobile(verifymobiletokenString,mobilenumber);
		logger.info("Please check Mobile SMS for OTP!");
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "OTP Resend on your mobile successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
		
	}


}
