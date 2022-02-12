package com.cargo.security.services.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
import com.cargo.load.response.CarrierResponse;
import com.cargo.load.response.ChargesGroupingResponse;
import com.cargo.load.response.CompanyResponse;
import com.cargo.load.response.CountrySpecificationResponse;
import com.cargo.load.response.FeedbackResponse;
import com.cargo.load.response.ForwarderDetailsResponse;
import com.cargo.load.response.ForwarderSetupResponse;
import com.cargo.load.response.LoginLogResponse;
import com.cargo.load.response.UploadAdsResponse;
import com.cargo.load.response.UploadOfferResponse;
import com.cargo.load.response.UploadRateHistoryResponse;
import com.cargo.load.response.UserDetailsResponse;
import com.cargo.mail.EmailService;
import com.cargo.mail.Mail;
import com.cargo.mail.SignupEmailMapper;
import com.cargo.models.CarrierEntity;
import com.cargo.models.ChargesGroupingEntity;
import com.cargo.models.CompanyEntity;
import com.cargo.models.CountrySpecificationEntity;
import com.cargo.models.FeedbackEntity;
import com.cargo.models.ForwarderSetupEntity;
import com.cargo.models.LoginLogEntity;
import com.cargo.models.LocationEntity;
import com.cargo.models.RoleEntity;
import com.cargo.models.UploadAdsEntity;
import com.cargo.models.UploadOfferEntity;
import com.cargo.models.UploadRateHistoryEntity;
import com.cargo.models.UploadRateTemporaryEntity;
import com.cargo.models.UserEntity;
import com.cargo.repository.BookingDetailsRepository;
import com.cargo.repository.CarrierRepository;
import com.cargo.repository.ChargeGroupingRepository;
import com.cargo.repository.ChargesRateRepository;
import com.cargo.repository.CompanyRepository;
import com.cargo.repository.CountrySpecificationRepository;
import com.cargo.repository.FeedbackRepository;
import com.cargo.repository.ForwarderSetupRepository;
import com.cargo.repository.LoginLogRepository;
import com.cargo.repository.LocationRepository;
import com.cargo.repository.RoleRepository;
import com.cargo.repository.TransDetailsRepository;
import com.cargo.repository.UploadAdsRepository;
import com.cargo.repository.UploadOfferRepository;
import com.cargo.repository.UploadRateHistoryRepository;
import com.cargo.repository.UploadRateTemporaryRepository;
import com.cargo.repository.UserRepository;
import com.cargo.utils.FileStorageUtility;
import com.cargo.utils.FileStringsUtils;
import com.cargo.utils.StringsUtils;
import com.cargo.utils.UploadPathContUtils;

@Service
public class AdminServicesImpl implements AdminServices{
	private static final Logger logger = LoggerFactory.getLogger(AdminServicesImpl.class);
	
	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	BookingDetailsRepository bookingDetailsRepository;
	
	@Autowired
	UploadRateHistoryRepository uploadRateHistoryRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired 
	FileStorageUtility fileStorageUtility;
	
	@Autowired
	CompanyRepository companyRepository;
		
	@Autowired
	TransDetailsRepository transDetailsRepository;
	
	@Autowired
	ChargesRateRepository chargesRateRepository;
	
	@Autowired
	ChargeGroupingRepository chargeGroupingRepository;
	
	@Autowired
	UploadRateTemporaryRepository uploadRateTemporaryRepository;
	
	@Autowired
	UploadOfferRepository uploadOfferRepository;
	
	@Autowired
	CampaignCodeGenerator campaignCodeGenerator;
	
	@Autowired
	LoginLogRepository loginLogRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired 
	CompanyRepository compnayRepository;
	
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	UploadAdsRepository uploadAdsRepository;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	CountrySpecificationRepository countrySpecificationRepository;
	
	@Autowired
	ForwarderSetupRepository forwarderSetupRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	PasswordEncoder encoder;
	
	BaseResponse baseResponse	= null;

	
	@Override
	public BaseResponse updateMyProfileResetPassword(UserDetailsRequest userDetailsRequest) throws Exception {
        logger.info("********************resetPassword Method in AdminServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		long userid = Long.parseLong(userDetailsRequest.getUserid());
		String password = userDetailsRequest.getPassword();
		String newpassword = userDetailsRequest.getNewpassword();
			
		UserEntity userEntities  = userRepository.getOne(userid);
		
		if(userEntities == null) {
			throw new Exception("Error: Invalid User!");
		}

		String encodedPassword = userEntities.getPassword();
		
		if(!encoder.matches(password, encodedPassword)) {
			throw new Exception("Error: Current Password doesn't match!");
		}
		
		String email = userEntities.getEmail();
		logger.info("newpassword : "+newpassword);
		
		userEntities.setPassword(encoder.encode(newpassword));
		userRepository.save(userEntities);
		
		//successful mail to customer with change new credentials
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(email);
				
		mailExceed.setSubject("Book My Cargo: Reset Password");
		
		SignupEmailMapper signupEmail = new SignupEmailMapper();
		signupEmail.setEmail(email);
		signupEmail.setPassword(newpassword);
		
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
	public BaseResponse getAllForwarderCha() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getAllForwarderCha method in AdminServicesImpl*******************");
			
		List<UserEntity> userEntityList = userRepository.findByRoleAndIsactiveAndIsdeleted("FF","ACTIVE","N");
		
		if (userEntityList == null ) {
			throw new Exception("Error: Forwarder/CHA Not Found");
		}
		
		if(userEntityList.size() == 0) {
			throw new Exception("Error: Forwarder/CHA Not Found");
		}
		
		List<CompanyResponse> companyResponseList = new ArrayList<CompanyResponse>();
		
		for (UserEntity userEntity : userEntityList) {
			 logger.info("User : "+userEntity); 
			 
			 if(userEntity.getCompanyId() != null) {
				 long companyId = userEntity.getCompanyId();
				 
				 Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
				 if(companyEntity != null) {
					 CompanyResponse companyResponse = new CompanyResponse();
					 
					 companyResponse.setId(companyEntity.get().getId());
					 companyResponse.setTradename(companyEntity.get().getTradename());
					 companyResponseList.add(companyResponse);
				 }
				 
			 }

		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(companyResponseList);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getUploadRateHistory() throws Exception {
		    baseResponse = new BaseResponse();
			
			logger.info("*****************getUploadRateHistory method in AdminServicesImpl*******************");
			
			
			List<UploadRateHistoryEntity> uploadRateHistoryList = uploadRateHistoryRepository.findByIsdeletedOrderByUploadeddateDesc("N");
			
			if (uploadRateHistoryList == null ) {
				throw new Exception("Error: No Data Found");
			}
			
			if(uploadRateHistoryList.size() == 0) {
				throw new Exception("Error: No Data Found");
			}
			
			List<UploadRateHistoryResponse> uploadRateHistoryResponseList = new ArrayList<UploadRateHistoryResponse>();
			int serialnumber = 1;
			for (UploadRateHistoryEntity uploadRateHistoryEntity : uploadRateHistoryList) {
				 logger.info("Upload Rate History : "+uploadRateHistoryEntity); 
				 
				 UploadRateHistoryResponse uploadRateHistoryResponse = new UploadRateHistoryResponse();
				 
				 
				 uploadRateHistoryResponse.setId(uploadRateHistoryEntity.getId());
				 uploadRateHistoryResponse.setSerialnumber(serialnumber);
				 
				 String forwarder = null;
				 if(uploadRateHistoryEntity.getForwarderid() != null) {
					 long companyId = uploadRateHistoryEntity.getForwarderid();
					 Optional<CompanyEntity> compEntity = companyRepository.findById(companyId);
					 forwarder = compEntity.get().getTradename();
				 }
				 
				 uploadRateHistoryResponse.setForwarder(forwarder);
				 uploadRateHistoryResponse.setShipmenttype(uploadRateHistoryEntity.getShipmenttype());
				 
				 uploadRateHistoryResponse.setCarrierid(uploadRateHistoryEntity.getCarrierid());
				 CarrierEntity carrierEntity = carrierRepository.findByIdAndIsdeleted(uploadRateHistoryEntity.getCarrierid(),"N");
				 String carrierName = carrierEntity.getCarriername()+" ("+carrierEntity.getScaccode()+")";
				 uploadRateHistoryResponse.setCarriername(carrierName);
				 uploadRateHistoryResponse.setCarriershortname(carrierEntity.getCarriershortname());
				 
				 uploadRateHistoryResponse.setChargetypeid(uploadRateHistoryEntity.getChargetypeid());
				 ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(uploadRateHistoryEntity.getChargetypeid(),"N");
				 uploadRateHistoryResponse.setChargetype(chargesGroupingEntity.getChargesgrouping());
				 
				 uploadRateHistoryResponse.setFilename(uploadRateHistoryEntity.getFilename());
				 uploadRateHistoryResponse.setRecordscount(uploadRateHistoryEntity.getRecordscount());
				 
				 uploadRateHistoryResponse.setValiddatefrom(uploadRateHistoryEntity.getValiddatefrom());
				 uploadRateHistoryResponse.setValiddateto(uploadRateHistoryEntity.getValiddateto());
				 uploadRateHistoryResponse.setUploadeddate(uploadRateHistoryEntity.getUploadeddate());
				 uploadRateHistoryResponse.setStatus(uploadRateHistoryEntity.getStatus());
			 
				 uploadRateHistoryResponseList.add(uploadRateHistoryResponse);
				 
				 serialnumber ++ ;
			}
			    
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(uploadRateHistoryResponseList);
			return baseResponse;
	}
 
	@Override
	public BaseResponse updateTemplateRateRecordInMaster(UpdateRateTemplateRecordRequest updateRateTemplateRecordRequest) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************updateTemplateRateRecordInMaster method in AdminServicesImpl*******************");
		
		UploadRateTemporaryEntity uploadRateTemporaryEntity = uploadRateTemporaryRepository.getOne(Long.parseLong(updateRateTemplateRecordRequest.getRecordId()));
		
		if (uploadRateTemporaryEntity == null ) {
			throw new Exception("Error: Record Not Found");
		}
		
		String pod = updateRateTemplateRecordRequest.getDestination();
		List<LocationEntity> destinationEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(pod+"%","N");
	    
		if(destinationEntityList.size() == 0) {
			throw new Exception("Error: Destination Not Found in master");
	    }
		
		
		uploadRateTemporaryEntity.setDestination(pod);
		String destinationLocId = destinationEntityList.get(0).getId().toString();  
		uploadRateTemporaryEntity.setDestinationlocid(destinationLocId);
		uploadRateTemporaryEntity.setRate(updateRateTemplateRecordRequest.getRate());
		uploadRateTemporaryEntity.setErrorstatus("N");
		
		uploadRateTemporaryRepository.save(uploadRateTemporaryEntity);
		
		HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Record Updated Seccessfully!");
	
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		
		
		return baseResponse;
	}

	@Override
	public BaseResponse uploadNewOffer(UploadOfferRequest uploadOfferRequest, MultipartFile offerImage,MultipartFile footerImage)throws Exception {
		
        logger.info("********************uploadNewOffer Method******************");

		baseResponse = new BaseResponse();
		
		String userId = uploadOfferRequest.getUserId();
		String tital = uploadOfferRequest.getTital();
		String origin = uploadOfferRequest.getOrigin();	
		String destination = uploadOfferRequest.getDestination();
		String fromDate = uploadOfferRequest.getFromDate();
		String toDate = uploadOfferRequest.getToDate();
		String templateType = uploadOfferRequest.getTemplateType();
		
		String description = uploadOfferRequest.getDescription();
		String footer = uploadOfferRequest.getFooter();
		
		String offerImageName = fileStorageUtility.storeOfferImageFile(offerImage,UploadPathContUtils.FILE_OFFER_DIR,tital,uploadOfferRequest.getUserId());
		
		String campaigncode = campaignCodeGenerator.getCampaignCode(origin, destination);
		logger.info("campaign code:"+campaigncode );
		String offerimagefullpath = UploadPathContUtils.FILE_OFFER_DIR+"/"+offerImageName;
		
		logger.info("offerimagefullpath:"+offerimagefullpath);
		
		UploadOfferEntity uploadOfferEntity = new UploadOfferEntity();
		
		uploadOfferEntity.setCreateby("system");
		uploadOfferEntity.setUserid(Long.parseLong(userId));
		uploadOfferEntity.setCampaigncode(campaigncode);
		uploadOfferEntity.setTital(tital);
		uploadOfferEntity.setOrigin(origin);
		uploadOfferEntity.setDestination(destination);

		uploadOfferEntity.setValiddatefrom(fromDate);
		uploadOfferEntity.setValiddateto(toDate);
		
		uploadOfferEntity.setOfferimagename(offerImageName);
		uploadOfferEntity.setOfferimagefullpath(offerimagefullpath);
		
		uploadOfferEntity.setTemplatetype(templateType);
		
		if(templateType.equals("template2")) {
			uploadOfferEntity.setDescription(description);
			uploadOfferEntity.setFooter(footer);
			
			String footerImageName = fileStorageUtility.storeOfferImageFile(footerImage,UploadPathContUtils.FILE_OFFER_DIR,tital,uploadOfferRequest.getUserId());
			String footerimagefullpath = UploadPathContUtils.FILE_OFFER_DIR+"/"+footerImageName;
			uploadOfferEntity.setFooterimage(footerImageName);
			uploadOfferEntity.setFooterimagepath(footerimagefullpath);
		}
				
		String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		uploadOfferEntity.setUploaddate(timeStamp);
				
		uploadOfferEntity.setStatus("ACTIVE");
		uploadOfferEntity.setIsdeleted("N");
		
		uploadOfferRepository.save(uploadOfferEntity);
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Offer Uploaded successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
		
	}

	@Override
	public BaseResponse getUploadOfferHistory() throws Exception {
		
		baseResponse = new BaseResponse();
		
		logger.info("*****************getUploadOfferHistory method in AdminServicesImpl*******************");
		
		
		List<UploadOfferEntity> uploadOfferEntityList = uploadOfferRepository.findByIsdeletedOrderByUploaddateDesc("N");
		
		if (uploadOfferEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(uploadOfferEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<UploadOfferResponse> uploadOfferResponseList = new ArrayList<UploadOfferResponse>();
		int serialnumber = 1;
		for (UploadOfferEntity uploadOfferEntity : uploadOfferEntityList) {
			 logger.info("Upload Offer History : "+uploadOfferEntity); 
			 
			 UploadOfferResponse uploadOfferResponse = new UploadOfferResponse();
			 			 
			 uploadOfferResponse.setId(uploadOfferEntity.getId());
			 uploadOfferResponse.setSerialnumber(serialnumber);
			 uploadOfferResponse.setCampaigncode(uploadOfferEntity.getCampaigncode());
			 uploadOfferResponse.setTital(uploadOfferEntity.getTital());	
			 uploadOfferResponse.setOrigin(uploadOfferEntity.getOrigin());
			 uploadOfferResponse.setDestination(uploadOfferEntity.getDestination());
			 uploadOfferResponse.setFromDate(uploadOfferEntity.getValiddatefrom());
			 uploadOfferResponse.setToDate(uploadOfferEntity.getValiddateto());
			 uploadOfferResponse.setTemplateType(uploadOfferEntity.getTemplatetype());
			 uploadOfferResponse.setUploaddate(uploadOfferEntity.getUploaddate());
			 uploadOfferResponse.setOfferimagename(uploadOfferEntity.getOfferimagename());
			 uploadOfferResponse.setOfferimagefullpath(uploadOfferEntity.getOfferimagefullpath());
			 uploadOfferResponse.setDescription(uploadOfferEntity.getDescription());
			 uploadOfferResponse.setFooter(uploadOfferEntity.getFooter());
			 uploadOfferResponse.setFooterimagename(uploadOfferEntity.getFooterimage());
			 uploadOfferResponse.setFooterimagefullpath(uploadOfferEntity.getFooterimagepath());
			 
			 uploadOfferResponse.setStatus(uploadOfferEntity.getStatus());			 			
			 uploadOfferResponseList.add(uploadOfferResponse);
			 
			 serialnumber ++ ;
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(uploadOfferResponseList);
		return baseResponse;
		
	}

	@Override
	public BaseResponse deleteUploadRateRecord(String userId, String recordId) throws Exception {
		
        baseResponse = new BaseResponse();
		
		logger.info("*****************deleteUploadRateRecord method in AdminServicesImpl*******************");
		
		long id = Long.parseLong(recordId);
		UploadRateHistoryEntity uploadRateHistory = uploadRateHistoryRepository.findByIdAndIsdeleted(id,"N");
		String filename = uploadRateHistory.getFilename();
		
		 Long carrierId = uploadRateHistory.getCarrierid();
		 Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
		 String carrierName = carrierEntity.get().getCarriershortname();
		
		fileStorageService.deleteFileWithoutException(userId, carrierName, filename);
		
		uploadRateHistoryRepository.deleteById(id);
		
        HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Record deleted successfully!");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getUseLoginLog() throws Exception {
         baseResponse = new BaseResponse();
		
		logger.info("*****************getUseLoginLog method in AdminServicesImpl*******************");
		
		
		List<LoginLogEntity> loginLogEntityList = loginLogRepository.findAll();
		
		if (loginLogEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(loginLogEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<LoginLogResponse> loginLogResponseList = new ArrayList<LoginLogResponse>();
		int serialnumber = 1;
		for (LoginLogEntity loginLogEntity : loginLogEntityList) {
			 logger.info("Login log entity : "+loginLogEntity.getId()+" | User Name: "+loginLogEntity.getUsername()); 
			 
			 LoginLogResponse loginLogResponse = new LoginLogResponse();
			 
			 loginLogResponse.setSrno(serialnumber);
			 loginLogResponse.setUsername(loginLogEntity.getUsername());
			 
			 String usercode = loginLogEntity.getUsercode();
			 RoleEntity roleEntity = roleRepository.findByCode(usercode);
			 String userrole = roleEntity.getName();
			 loginLogResponse.setUserrole(userrole);
			 
			 UserEntity userEntity = userRepository.findByIdAndIsdeleted(loginLogEntity.getUserId(),"N");			
			 Long companyId = userEntity.getCompanyId();
			 			 
			 Optional<CompanyEntity> companyEntity = null;
			 if(companyId != null) {
				 companyEntity = compnayRepository.findById(companyId);
			 }
			 
			 if(companyEntity != null) {
				 String companyname = companyEntity.get().getTradename();
				 loginLogResponse.setCompanyname(companyname);
			 }
			 
			 loginLogResponse.setSessionid(loginLogEntity.getSessionid());
			 loginLogResponse.setIpaddress(loginLogEntity.getIpaddress());
			 loginLogResponse.setLogindatetime(loginLogEntity.getLogindatetime());
			 			
			 loginLogResponseList.add(loginLogResponse);
			 
			 serialnumber ++ ;
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(loginLogResponseList);
		return baseResponse;
	}
	
	@Override
	public BaseResponse deleteAllLoginLog(String userId) throws Exception {
	
		baseResponse = new BaseResponse();
			
		logger.info("*****************deleteAllLoginLog method in AdminServicesImpl*******************");
			
		loginLogRepository.deleteAll();	
			
	    HashMap<String ,String>  info = new HashMap<String ,String>();
			
	    info.put("msg", "Records deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserListByRole(String userRole) throws Exception {
		
        baseResponse = new BaseResponse();
		
		logger.info("*****************getUserListByRole method in AdminServicesImpl*******************");
		
        List<UserEntity> userEntityList = userRepository.findByRoleAndIsdeleted(userRole,"N");
		
		if (userEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(userEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<UserDetailsResponse> userDetailsResponseList = new ArrayList<UserDetailsResponse>();
		
		for (UserEntity userEntity : userEntityList) {
			
			UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
			userDetailsResponse.setEmail(userEntity.getEmail());
			userDetailsResponse.setId(userEntity.getId());
			userDetailsResponse.setRole(userEntity.getRole());
			userDetailsResponseList.add(userDetailsResponse);
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userDetailsResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse uploadAds(UploadAdsRequest uploadAdsRequest) throws Exception {
		
        baseResponse = new BaseResponse();
        
		logger.info("*****************uploadAds method in AdminServicesImpl*******************");
		
		String companyname = uploadAdsRequest.getCompanyName();
		String email = uploadAdsRequest.getEmail();
		String content = uploadAdsRequest.getContent();
		String startdate = uploadAdsRequest.getStartDate();
		String enddate = uploadAdsRequest.getEndDate();
		String priority = uploadAdsRequest.getPriority();
		String status = uploadAdsRequest.getStatus();
		
		UploadAdsEntity uploadAdsEntity = new UploadAdsEntity();	
		
		uploadAdsEntity.setCompanyname(companyname);
		uploadAdsEntity.setEmail(email);
		uploadAdsEntity.setContent(content);
		uploadAdsEntity.setStartdate(startdate);
		uploadAdsEntity.setEnddate(enddate);
		uploadAdsEntity.setPriority(priority);
		uploadAdsEntity.setStatus(status);		
		uploadAdsEntity.setCreateby("admin");
		uploadAdsEntity.setIsdeleted("N");
		
		uploadAdsRepository.save(uploadAdsEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Ads added successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getUploadedAdsList() throws Exception {
		    baseResponse = new BaseResponse();
			
			logger.info("*****************getUploadedAdsList method in AdminServicesImpl*******************");
						
			List<UploadAdsEntity> uploadAdsEntityList = uploadAdsRepository.findByIsdeleted("N");
			
			if (uploadAdsEntityList == null ) {
				throw new Exception("Error: No Data Found");
			}
			
			if(uploadAdsEntityList.size() == 0) {
				throw new Exception("Error: No Data Found");
			}
			
			List<UploadAdsResponse> uploadAdsResponseList = new ArrayList<UploadAdsResponse>();
	
			for (UploadAdsEntity uploadAdsEntity : uploadAdsEntityList) {
							 
				 UploadAdsResponse uploadAdsResponse = new UploadAdsResponse();
				 
				 uploadAdsResponse.setId(uploadAdsEntity.getId());
				 uploadAdsResponse.setCompanyname(uploadAdsEntity.getCompanyname());
				 uploadAdsResponse.setEmail(uploadAdsEntity.getEmail());
				 uploadAdsResponse.setContent(uploadAdsEntity.getContent());
				 uploadAdsResponse.setPriority(uploadAdsEntity.getPriority());
				 uploadAdsResponse.setStartdate(uploadAdsEntity.getStartdate());
				 uploadAdsResponse.setEnddate(uploadAdsEntity.getEnddate());
				 uploadAdsResponse.setStatus(uploadAdsEntity.getStatus());
				 
				 uploadAdsResponseList.add(uploadAdsResponse);
				 
			}
			    
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(uploadAdsResponseList);
			return baseResponse;
	}

	@Override
	public BaseResponse deleteAds(String id, String userId) throws Exception {
		
		baseResponse = new BaseResponse();
		
		Long idAds = Long.parseLong(id);
		
		UploadAdsEntity uploadAdsEntity =  uploadAdsRepository.findByIdAndIsdeleted(idAds,"N");
		
		if(uploadAdsEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		UploadAdsEntity uploadAdsEnti = uploadAdsRepository.getOne(idAds);
		uploadAdsEnti.setIsdeleted("Y");
		uploadAdsEnti.setUpdateby(userId);
		uploadAdsRepository.save(uploadAdsEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Advertisment deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getAdsDetailsById(String id, String userId) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getAdsDetailsById method in AdminServicesImpl*******************");
		
		Long idAds = Long.parseLong(id);	
		
		UploadAdsEntity uploadAdsEntity = uploadAdsRepository.findByIdAndIsdeleted(idAds,"N");
		
		if (uploadAdsEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
	    UploadAdsResponse uploadAdsResponse = new UploadAdsResponse();
			 
		uploadAdsResponse.setId(uploadAdsEntity.getId());
		uploadAdsResponse.setCompanyname(uploadAdsEntity.getCompanyname());
		uploadAdsResponse.setEmail(uploadAdsEntity.getEmail());
		uploadAdsResponse.setContent(uploadAdsEntity.getContent());
		uploadAdsResponse.setPriority(uploadAdsEntity.getPriority());
		uploadAdsResponse.setStartdate(uploadAdsEntity.getStartdate());
		uploadAdsResponse.setEnddate(uploadAdsEntity.getEnddate());
		uploadAdsResponse.setStatus(uploadAdsEntity.getStatus());
		
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(uploadAdsResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse updateAdsById(UploadAdsRequest uploadAdsRequest) throws Exception {
		
        baseResponse = new BaseResponse();
        
		logger.info("*****************updateAdsById method in AdminServicesImpl*******************");
		
		String id = uploadAdsRequest.getId();
		Long idAds = Long.parseLong(id);
		
		String userid = uploadAdsRequest.getUserId();
		String companyname = uploadAdsRequest.getCompanyName();
		String email = uploadAdsRequest.getEmail();
		String content = uploadAdsRequest.getContent();
		String startdate = uploadAdsRequest.getStartDate();
		String enddate = uploadAdsRequest.getEndDate();
		String priority = uploadAdsRequest.getPriority();
		String status = uploadAdsRequest.getStatus();
		
        UploadAdsEntity uploadAdsEntity = uploadAdsRepository.findByIdAndIsdeleted(idAds,"N");
		
		if (uploadAdsEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		UploadAdsEntity uploadAdsEnti = uploadAdsRepository.getOne(idAds);
		
		uploadAdsEnti.setCompanyname(companyname);
		uploadAdsEnti.setEmail(email);
		uploadAdsEnti.setContent(content);
		uploadAdsEnti.setStartdate(startdate);
		uploadAdsEnti.setEnddate(enddate);
		uploadAdsEnti.setPriority(priority);
		uploadAdsEnti.setStatus(status);		
		uploadAdsEnti.setUpdateby(userid);
		
		uploadAdsRepository.save(uploadAdsEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Advertisement updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getFeedbackList() throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getFeedbackList method in AdminServicesImpl*******************");
					
		List<FeedbackEntity> feedbackEntityList = feedbackRepository.findByIsdeleted("N");
		
		if (feedbackEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(feedbackEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<FeedbackResponse> feedbackResponseList = new ArrayList<FeedbackResponse>();

		for (FeedbackEntity feedbackEntity : feedbackEntityList) {
						 
			 FeedbackResponse feedbackResponse = new FeedbackResponse();
			 feedbackResponse.setId(Long.toString(feedbackEntity.getId()));
			 String createddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(feedbackEntity.getCreatedate());
			 feedbackResponse.setCreateddate(createddate);
			 feedbackResponse.setName(feedbackEntity.getName());
			 feedbackResponse.setCompanyname(feedbackEntity.getCompanyname());
			 feedbackResponse.setEmail(feedbackEntity.getEmail());
			 feedbackResponse.setMobile(feedbackEntity.getMobilenumber());
			 feedbackResponse.setLocation(feedbackEntity.getLocation());
			 feedbackResponse.setMessage(feedbackEntity.getMessage());
			 feedbackResponse.setStatus(feedbackEntity.getStatus());
			 feedbackResponse.setReplydate(feedbackEntity.getReplydate());
			 feedbackResponseList.add(feedbackResponse);
			 
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(feedbackResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getFeedbackDetailsById(String id) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getFeedbackDetailsById method in AdminServicesImpl*******************");
		
		Long idFds = Long.parseLong(id);	
		
		FeedbackEntity feedbackEntity = feedbackRepository.findByIdAndIsdeleted(idFds,"N");
		
		if (feedbackEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		FeedbackResponse feedbackResponse = new FeedbackResponse();
		feedbackResponse.setId(Long.toString(feedbackEntity.getId()));
		String createddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(feedbackEntity.getCreatedate());
		feedbackResponse.setCreateddate(createddate);
		feedbackResponse.setName(feedbackEntity.getName());
		feedbackResponse.setCompanyname(feedbackEntity.getCompanyname());
		feedbackResponse.setEmail(feedbackEntity.getEmail());
		feedbackResponse.setMobile(feedbackEntity.getMobilenumber());
		feedbackResponse.setLocation(feedbackEntity.getLocation());
		feedbackResponse.setMessage(feedbackEntity.getMessage());
		feedbackResponse.setActionmessage(feedbackEntity.getActionmessage());
		feedbackResponse.setStatus(feedbackEntity.getStatus());
		feedbackResponse.setReplydate(feedbackEntity.getReplydate());
				    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(feedbackResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteFeedback(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		Long idFd = Long.parseLong(id);
		
		FeedbackEntity feedbackEntity =  feedbackRepository.findByIdAndIsdeleted(idFd,"N");
		
		if(feedbackEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		FeedbackEntity feedbackEnti = feedbackRepository.getOne(idFd);
		feedbackEnti.setIsdeleted("Y");
		feedbackEnti.setUpdateby(userId);
		feedbackRepository.save(feedbackEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Feedback deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse updateFeedbackStatus(FeedbackRequest feedbackRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************updateFeedbackStatus method in AdminServicesImpl*******************");
		
		String id = feedbackRequest.getId();
		Long idFd = Long.parseLong(id);
		
		String userid = feedbackRequest.getUserid();		
		String status = feedbackRequest.getStatus();
		
		FeedbackEntity feedbackEntity =  feedbackRepository.findByIdAndIsdeleted(idFd,"N");
		
		if (feedbackEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		FeedbackEntity feedbackEnti = feedbackRepository.getOne(idFd);
		
		feedbackEnti.setStatus(status);		
		feedbackEnti.setUpdateby(userid);
		
		feedbackRepository.save(feedbackEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Feedback Status updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse replyToFeedback(FeedbackRequest feedbackRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************replyToFeedback method in AdminServicesImpl*******************");
		
		String id = feedbackRequest.getId();
		Long idFd = Long.parseLong(id);
		
		String userid = feedbackRequest.getUserid();		
		String actionmessage = feedbackRequest.getActionmessage();
		
		FeedbackEntity feedbackEntity =  feedbackRepository.findByIdAndIsdeleted(idFd,"N");
		
		if (feedbackEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		String email = feedbackEntity.getEmail();
		
		FeedbackEntity feedbackEnti = feedbackRepository.getOne(idFd);
		String currentdate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		feedbackEnti.setStatus("Closed");
		feedbackEnti.setActionmessage(actionmessage);
		feedbackEnti.setReplydate(currentdate);		
		feedbackEnti.setUpdateby(userid);
		
		feedbackRepository.save(feedbackEnti);
		
		FeedbackResponse feedbackResponse = new FeedbackResponse();
		feedbackResponse.setId(Long.toString(feedbackEntity.getId()));
		String createddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(feedbackEntity.getCreatedate());
		feedbackResponse.setCreateddate(createddate);
		feedbackResponse.setName(feedbackEntity.getName());
		feedbackResponse.setCompanyname(feedbackEntity.getCompanyname());
		feedbackResponse.setEmail(feedbackEntity.getEmail());
		feedbackResponse.setMobile(feedbackEntity.getMobilenumber());
		feedbackResponse.setLocation(feedbackEntity.getLocation());
		feedbackResponse.setMessage(feedbackEntity.getMessage());
		feedbackResponse.setStatus(feedbackEntity.getStatus());
		feedbackResponse.setReplydate(feedbackEntity.getReplydate());
		feedbackResponse.setActionmessage(actionmessage);
		
	    Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(email);
		mailExceed.setSubject("Book My Cargo: Feedback Reply");
						
		try {
			emailService.sendFeedbackReplyToUserMail(mailExceed, feedbackResponse);
		}catch(Exception e) {
			e.printStackTrace();
		}		
		logger.info("Email send successfully!");
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Feedback Status updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse addCountrySpecification(CountrySpecificationRequest countrySpecificationRequest)throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************addCountrySpecification method in AdminServicesImpl*******************");
		
		String userid = countrySpecificationRequest.getUserid();
		String countryname = countrySpecificationRequest.getCountryname();
		String countrycode = countrySpecificationRequest.getCountrycode();
		String specification = countrySpecificationRequest.getSpecification();
		String status = countrySpecificationRequest.getStatus();

		CountrySpecificationEntity countrySpecificationEnti = countrySpecificationRepository.findByCountrycodeAndIsdeleted(countrycode,"N");
		
		if (countrySpecificationEnti != null ) {
			throw new Exception("Error: Country specification already exist");
		}
		
		CountrySpecificationEntity countrySpecificationEntity = new CountrySpecificationEntity();	
		countrySpecificationEntity.setCountryname(countryname);
		countrySpecificationEntity.setCountrycode(countrycode);
		countrySpecificationEntity.setSpecification(specification);
		countrySpecificationEntity.setStatus(status);		
		countrySpecificationEntity.setCreateby(userid);
		countrySpecificationEntity.setIsdeleted("N");
		
		countrySpecificationRepository.save(countrySpecificationEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Country Specification added successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getCountrySpecificationList() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getCountrySpecificationList method in AdminServicesImpl*******************");
					
		List<CountrySpecificationEntity> countrySpecificationEntityList = countrySpecificationRepository.findByIsdeleted("N");
		
		if (countrySpecificationEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(countrySpecificationEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<CountrySpecificationResponse> countrySpecificationResponseList = new ArrayList<CountrySpecificationResponse>();

		for (CountrySpecificationEntity countrySpecificationEntity : countrySpecificationEntityList) {
						 
			CountrySpecificationResponse countrySpecificationResponse = new CountrySpecificationResponse();
			countrySpecificationResponse.setId(Long.toString(countrySpecificationEntity.getId()));
			String createddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getCreatedate());
			countrySpecificationResponse.setCreateddate(createddate);
			countrySpecificationResponse.setCountryname(countrySpecificationEntity.getCountryname());
			countrySpecificationResponse.setCountrycode(countrySpecificationEntity.getCountrycode());
			countrySpecificationResponse.setSpecification(countrySpecificationEntity.getSpecification());			
			countrySpecificationResponse.setStatus(countrySpecificationEntity.getStatus());
			
			if(countrySpecificationEntity.getUpdatedate() == null) {
				String updateddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getCreatedate());
				countrySpecificationResponse.setUpdateddate(updateddate);
			}else {
				String updateddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getUpdatedate());
				countrySpecificationResponse.setUpdateddate(updateddate);
			}
				
			countrySpecificationResponseList.add(countrySpecificationResponse);
			 
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(countrySpecificationResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteCountrySpecification(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		Long idCs = Long.parseLong(id);
		
		CountrySpecificationEntity countrySpecificationEntity =  countrySpecificationRepository.findByIdAndIsdeleted(idCs,"N");
		
		if(countrySpecificationEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		CountrySpecificationEntity countrySpecificationEnti = countrySpecificationRepository.getOne(idCs);
		countrySpecificationEnti.setIsdeleted("Y");
		countrySpecificationEnti.setUpdateby(userId);
		countrySpecificationRepository.save(countrySpecificationEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Country specification deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getCountySpecificationDetailsById(String id) throws Exception {
		 baseResponse = new BaseResponse();
			
		logger.info("*****************getCountySpecificationDetailsById method in AdminServicesImpl*******************");
			
		Long idCs = Long.parseLong(id);	
			
		CountrySpecificationEntity countrySpecificationEntity = countrySpecificationRepository.findByIdAndIsdeleted(idCs,"N");
			
		if (countrySpecificationEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
			
		CountrySpecificationResponse countrySpecificationResponse = new CountrySpecificationResponse();
		countrySpecificationResponse.setId(Long.toString(countrySpecificationEntity.getId()));
		String createddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getCreatedate());
		countrySpecificationResponse.setCreateddate(createddate);
		countrySpecificationResponse.setCountryname(countrySpecificationEntity.getCountryname());
		countrySpecificationResponse.setCountrycode(countrySpecificationEntity.getCountrycode());
		countrySpecificationResponse.setSpecification(countrySpecificationEntity.getSpecification());			
		countrySpecificationResponse.setStatus(countrySpecificationEntity.getStatus());
		
		if(countrySpecificationEntity.getUpdatedate() == null) {
			String updateddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getCreatedate());
			countrySpecificationResponse.setUpdateddate(updateddate);
		}else {
			String updateddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(countrySpecificationEntity.getUpdatedate());
			countrySpecificationResponse.setUpdateddate(updateddate);
		}
					    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(countrySpecificationResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse updateCountrySpecification(CountrySpecificationRequest countrySpecificationRequest)throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************updateCountrySpecification method in AdminServicesImpl*******************");
		
		String id = countrySpecificationRequest.getId();
		Long idCs = Long.parseLong(id);
		
		String userid = countrySpecificationRequest.getUserid();		
		String status = countrySpecificationRequest.getStatus();
		String specification = countrySpecificationRequest.getSpecification();
		
		CountrySpecificationEntity countrySpecificationEntity =  countrySpecificationRepository.findByIdAndIsdeleted(idCs,"N");
		
		if (countrySpecificationEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		CountrySpecificationEntity countrySpecificationEnti = countrySpecificationRepository.getOne(idCs);
		
		countrySpecificationEnti.setStatus(status);		
		countrySpecificationEnti.setUpdateby(userid);
		countrySpecificationEnti.setSpecification(specification);
		countrySpecificationRepository.save(countrySpecificationEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Country specification updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse addCarrier(CarrierRequest carrierRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************addCarrier method in AdminServicesImpl*******************");
		
		String carrier = carrierRequest.getCarrier();
		String carrierCode = carrierRequest.getCarrierCode();
		String website = carrierRequest.getWebsite();
		String logopath = carrierRequest.getLogopath();
		String scacCode = carrierRequest.getScacCode();
		
		CarrierEntity carrierEntity = new CarrierEntity();	
		
		carrierEntity.setCarriername(carrier);	
		carrierEntity.setCarriershortname(carrierCode);
		carrierEntity.setWebsite(website);
		carrierEntity.setLogopath(logopath);
		carrierEntity.setScaccode(scacCode);
		carrierEntity.setCreateby("admin");
		carrierEntity.setIsdeleted("N");
		
		carrierRepository.save(carrierEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Carrier added successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getCarrierList() throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getCarrierList method in AdminServicesImpl*******************");
					
		List<CarrierEntity> carrierEntityList = carrierRepository.findByIsdeleted("N");
		
		if (carrierEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(carrierEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<CarrierResponse> carrierResponseList = new ArrayList<CarrierResponse>();

		for (CarrierEntity carrierEntity : carrierEntityList) {
						 
			CarrierResponse carrierResponse = new CarrierResponse();
			 
			carrierResponse.setId(carrierEntity.getId());
			carrierResponse.setCarriername(carrierEntity.getCarriername());
			carrierResponse.setCarriershortname(carrierEntity.getCarriershortname());
			carrierResponse.setWebsite(carrierEntity.getWebsite());
			carrierResponse.setLogopath(carrierEntity.getLogopath());
						 
			carrierResponseList.add(carrierResponse);
			 
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(carrierResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getCarrierDetailsById(String id) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getCarrierDetailsById method in AdminServicesImpl*******************");
					
		long carrid = Long.parseLong(id); 
		CarrierEntity carrierEntity = carrierRepository.findByIdAndIsdeleted(carrid,"N");
		
		if (carrierEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
						 
		CarrierResponse carrierResponse = new CarrierResponse();
			 
		carrierResponse.setId(carrierEntity.getId());
		carrierResponse.setCarriername(carrierEntity.getCarriername());
		carrierResponse.setCarriershortname(carrierEntity.getCarriershortname());
		carrierResponse.setWebsite(carrierEntity.getWebsite());
		carrierResponse.setLogopath(carrierEntity.getLogopath());			
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(carrierResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse updateCarrierById(CarrierRequest carrierRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************updateCarrierById method in AdminServicesImpl*******************");
		
		String id = carrierRequest.getId();
		Long carrId = Long.parseLong(id);
		
		String userid = carrierRequest.getUserId();
		String website = carrierRequest.getWebsite();
		String logopath = carrierRequest.getLogopath();
				
        CarrierEntity carrierEntity = carrierRepository.findByIdAndIsdeleted(carrId,"N");
		
		if (carrierEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		CarrierEntity carrierEnti = carrierRepository.getOne(carrId);
		
		carrierEnti.setWebsite(website);	
		carrierEnti.setLogopath(logopath);
		carrierEnti.setUpdateby(userid);
		
		carrierRepository.save(carrierEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Carrier updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteCarrier(String id, String userId) throws Exception {
       baseResponse = new BaseResponse();
		
		Long idCarrier = Long.parseLong(id);
		
		CarrierEntity carrierEntity =  carrierRepository.findByIdAndIsdeleted(idCarrier,"N");
		
		if(carrierEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		CarrierEntity carrierEnti = carrierRepository.getOne(idCarrier);
		carrierEnti.setIsdeleted("Y");
		carrierEnti.setUpdateby(userId);
		carrierRepository.save(carrierEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Carrier deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getCompanyNameList() throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************getCompanyNameList method in AdminServicesImpl*******************");
		
		List<UserEntity> userEntityList = userRepository.findByRoleAndIsdeleted("FF","N");
		    
	    if(userEntityList == null){
			throw new Exception("Error: No Forwarder(s) Found");
		}
		    
		List<ForwarderDetailsResponse> forwarderDetailsResponseList = new ArrayList<ForwarderDetailsResponse>();				
		for(UserEntity userEnt : userEntityList) {
				
			long forwarderid = userEnt.getId();
			String email = userEnt.getEmail();
			long compId = userEnt.getCompanyId();
				
			Optional<CompanyEntity> compEntity = companyRepository.findById(compId);	
            String forwarder = compEntity.get().getTradename();
            String location = compEntity.get().getDestination();
                
			ForwarderDetailsResponse forwarderDetailsResponse = new ForwarderDetailsResponse();
			forwarderDetailsResponse.setId(forwarderid);
			forwarderDetailsResponse.setEmail(email);
			forwarderDetailsResponse.setForwarder(forwarder);
			forwarderDetailsResponse.setLocation(location);
			forwarderDetailsResponseList.add(forwarderDetailsResponse);					
		}
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(forwarderDetailsResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse addForwarderSetup(ForwarderSetupRequest forwarderSetupRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************addForwarderSetup method in AdminServicesImpl*******************");
		
		String forwarderId = forwarderSetupRequest.getForwarderId();
		long forwId = Long.parseLong(forwarderId);
		String startDate = forwarderSetupRequest.getStartDate();
		String endDate = forwarderSetupRequest.getEndDate();		
		String status = forwarderSetupRequest.getStatus();
		String priority = forwarderSetupRequest.getPriority();
		
		ForwarderSetupEntity forwarderSetupEnti = forwarderSetupRepository.findByForwarderidAndIsdeleted(forwId,"N");
		
		if (forwarderSetupEnti != null ) {
			throw new Exception("Company already configured");
		}
		
		ForwarderSetupEntity forwarderSetupEntity = new ForwarderSetupEntity();
		
		forwarderSetupEntity.setForwarderid(forwId);
		forwarderSetupEntity.setStartdate(startDate);
		forwarderSetupEntity.setEnddate(endDate);
		forwarderSetupEntity.setPriority(priority);
		forwarderSetupEntity.setStatus(status);		
		forwarderSetupEntity.setCreateby("admin");
		forwarderSetupEntity.setIsdeleted("N");
		
		forwarderSetupRepository.save(forwarderSetupEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Forwarder Setup added successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getForwarderSetupList() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderSetupList method in AdminServicesImpl*******************");
					
		List<ForwarderSetupEntity> forwarderSetupEntityList = forwarderSetupRepository.findByIsdeleted("N");
		
		if (forwarderSetupEntityList == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		if(forwarderSetupEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
		
		List<ForwarderSetupResponse> forwarderSetupResponseList = new ArrayList<ForwarderSetupResponse>();

		for (ForwarderSetupEntity forwarderSetupEntity : forwarderSetupEntityList) {
						 
			ForwarderSetupResponse forwarderSetupResponse = new ForwarderSetupResponse();
			 
			long forwId = forwarderSetupEntity.getForwarderid();

			UserEntity userEntity = userRepository.findByIdAndIsdeleted(forwId,"N");
			
			long compId = userEntity.getCompanyId();
			
			Optional<CompanyEntity> compEntity = companyRepository.findById(compId);	
            String forwarder = compEntity.get().getTradename();
            String location = compEntity.get().getDestination();
            
            forwarderSetupResponse.setId(Long.toString(forwarderSetupEntity.getId()));
            forwarderSetupResponse.setCompanyname(forwarder);
            forwarderSetupResponse.setLocation(location);
			forwarderSetupResponse.setStartdate(forwarderSetupEntity.getStartdate());
			forwarderSetupResponse.setEnddate(forwarderSetupEntity.getEnddate());
			forwarderSetupResponse.setPriority(forwarderSetupEntity.getPriority());
			forwarderSetupResponse.setStatus(forwarderSetupEntity.getStatus());
			
			forwarderSetupResponseList.add(forwarderSetupResponse);
			 
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(forwarderSetupResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getForwarderSetupDetailsById(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderSetupDetailsById method in AdminServicesImpl*******************");
		
		Long idSetup = Long.parseLong(id);	
		
		ForwarderSetupEntity forwarderSetupEntity = forwarderSetupRepository.findByIdAndIsdeleted(idSetup,"N");
		
		if (forwarderSetupEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		ForwarderSetupResponse forwarderSetupResponse = new ForwarderSetupResponse();
		
		long forwId = forwarderSetupEntity.getForwarderid();

		UserEntity userEntity = userRepository.findByIdAndIsdeleted(forwId,"N");
		
		long compId = userEntity.getCompanyId();
		
		logger.info("*****************compId*******************"+compId);		
		Optional<CompanyEntity> compEntity = companyRepository.findById(compId);	
        String forwarder = compEntity.get().getTradename();
        String location = compEntity.get().getDestination();
        
        forwarderSetupResponse.setId(id);
        forwarderSetupResponse.setCompanyname(forwarder);
        forwarderSetupResponse.setLocation(location);
		forwarderSetupResponse.setStartdate(forwarderSetupEntity.getStartdate());
		forwarderSetupResponse.setEnddate(forwarderSetupEntity.getEnddate());
		forwarderSetupResponse.setPriority(forwarderSetupEntity.getPriority());
		forwarderSetupResponse.setStatus(forwarderSetupEntity.getStatus());
				    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(forwarderSetupResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteForwarderSetup(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		Long idForSetup = Long.parseLong(id);
		
		ForwarderSetupEntity forwarderSetupEntity =  forwarderSetupRepository.findByIdAndIsdeleted(idForSetup,"N");
		
		if(forwarderSetupEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		ForwarderSetupEntity forwarderSetupEnti = forwarderSetupRepository.getOne(idForSetup);
		forwarderSetupEnti.setIsdeleted("Y");
		forwarderSetupEnti.setUpdateby(userId);
		forwarderSetupRepository.save(forwarderSetupEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
		info.put("msg", "Forwarder Setup deleted successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse updateForwSetupById(ForwarderSetupRequest forwarderSetupRequest) throws Exception {
        baseResponse = new BaseResponse();
        
		logger.info("*****************updateForwSetupById method in AdminServicesImpl*******************");
		
		String id = forwarderSetupRequest.getId();
		Long forwSetupId = Long.parseLong(id);
		
		String userid = forwarderSetupRequest.getUserid();
				
		String startDate = forwarderSetupRequest.getStartDate();
		String endDate = forwarderSetupRequest.getEndDate();
		String priority = forwarderSetupRequest.getPriority();
		String status = forwarderSetupRequest.getStatus();
		
		ForwarderSetupEntity forwarderSetupEntity = forwarderSetupRepository.findByIdAndIsdeleted(forwSetupId,"N");
		
		if (forwarderSetupEntity == null ) {
			throw new Exception("Error: No Data Found");
		}
		
		ForwarderSetupEntity forwarderSetupEnti = forwarderSetupRepository.getOne(forwSetupId);
		forwarderSetupEnti.setStartdate(startDate);		
		forwarderSetupEnti.setEnddate(endDate);
		forwarderSetupEnti.setPriority(priority);
		forwarderSetupEnti.setStatus(status);
		forwarderSetupEnti.setUpdateby(userid);
		forwarderSetupRepository.save(forwarderSetupEnti);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
			
		info.put("msg", "Forwarder Setup updated successfully!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}
	

}
