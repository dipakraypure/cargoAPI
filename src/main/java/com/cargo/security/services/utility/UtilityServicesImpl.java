package com.cargo.security.services.utility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.load.request.FeedbackRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.CarrierResponse;
import com.cargo.load.response.ChargeBasisResponse;
import com.cargo.load.response.ChargesGroupingResponse;
import com.cargo.load.response.ChargesTypeResponse;
import com.cargo.load.response.CountryResponse;
import com.cargo.load.response.CountrySpecificationResponse;
import com.cargo.load.response.FeedbackResponse;
import com.cargo.load.response.IncotermResponse;
import com.cargo.load.response.LocationResponse;
import com.cargo.load.response.RoleResponse;
import com.cargo.load.response.UploadAdsResponse;
import com.cargo.mail.EmailService;
import com.cargo.mail.Mail;
import com.cargo.mail.SignupEmailMapper;
import com.cargo.models.BookingDetailsEntity;
import com.cargo.models.CarrierEntity;
import com.cargo.models.ChargeBasisEntity;
import com.cargo.models.ChargesGroupingEntity;
import com.cargo.models.ChargesTypeEntity;
import com.cargo.models.CompanyEntity;
import com.cargo.models.CountrySpecificationEntity;
import com.cargo.models.FeedbackEntity;
import com.cargo.models.IncotermEntity;
import com.cargo.models.LocationEntity;
import com.cargo.models.RoleEntity;
import com.cargo.models.UploadAdsEntity;
import com.cargo.models.UserEntity;
import com.cargo.repository.CarrierRepository;
import com.cargo.repository.ChargeBasisRepository;
import com.cargo.repository.ChargeGroupingRepository;
import com.cargo.repository.ChargesTypeRepository;
import com.cargo.repository.CompanyRepository;
import com.cargo.repository.CountrySpecificationRepository;
import com.cargo.repository.FeedbackRepository;
import com.cargo.repository.IncotermRepository;
import com.cargo.repository.LocationRepository;
import com.cargo.repository.RoleRepository;
import com.cargo.repository.UploadAdsRepository;
import com.cargo.repository.UserRepository;
import com.cargo.utils.StringsUtils;


@Service
public class UtilityServicesImpl implements UtilityServices{

	private static final Logger logger = LoggerFactory.getLogger(UtilityServicesImpl.class);

	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	ChargeGroupingRepository chargeGroupingRepository;
	
	@Autowired
	ChargesTypeRepository chargesTypeRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	UploadAdsRepository uploadAdsRepository;
	
	@Autowired
	IncotermRepository incotermRepository;
	
	@Autowired
	FeedbackRepository feedbackRepository;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	CountrySpecificationRepository countrySpecificationRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ChargeBasisRepository chargeBasisRepository;
	
	BaseResponse baseResponse	= null;
	
	
	@Override
	public BaseResponse getAllLocation(String location)throws Exception {
				
        baseResponse = new BaseResponse();
		
		logger.info("*****************getAllLocation method in UtilityServicesImpl*******************");
		logger.info("get existing location from database :: location: "+location);
		
		if(location.equals("")) {
		    throw new Exception("Error: Location Not Null");	
		}
		
		List<LocationResponse> locationResponse = new ArrayList<LocationResponse>();
		
		List<LocationEntity> locationEntityList = locationRepository.findByIsDeletedAndLocationStartWith("N",location);
		
		if (locationEntityList == null) {
			throw new Exception("Error: Location Not Found");
		}
		
		if(locationEntityList.size() == 0) {
			throw new Exception("Error: Location Not Found");
		}
		
		for (LocationEntity locationEntity : locationEntityList) {
			logger.info("location Name: "+locationEntity.getLocationname());
				
			LocationResponse newOriginResponse = new LocationResponse();
			newOriginResponse.setId(locationEntity.getId());
			newOriginResponse.setLocationcode(locationEntity.getLocationcode());
			newOriginResponse.setLocationname(locationEntity.getLocationname());
			    
			locationResponse.add(newOriginResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(locationResponse);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getAllCarriers() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getAllCarriers method in UtilityServicesImpl*******************");
		
		
		List<CarrierEntity> carrierEntityList = carrierRepository.findByIsdeleted("N");
		
		if (carrierEntityList == null ) {
			throw new Exception("Error: Carriers Not Found");
		}
		
		if(carrierEntityList.size() == 0) {
			throw new Exception("Error: Carriers Not Found");
		}
		
		List<CarrierResponse> carrierResponseList = new ArrayList<CarrierResponse>();
		
		for (CarrierEntity carrierEntity : carrierEntityList) {
			 logger.info("carrier : "+carrierEntity); 
			 
			 CarrierResponse carrierResponse = new CarrierResponse();
			 
			 carrierResponse.setId(carrierEntity.getId());
			 carrierResponse.setCarriershortname(carrierEntity.getCarriershortname());
			 carrierResponse.setCarriername(carrierEntity.getCarriername());
			 carrierResponse.setScaccode(carrierEntity.getScaccode());
		 
			 carrierResponseList.add(carrierResponse);
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(carrierResponseList);
		return baseResponse;
	}
	
	@Override
	public List<RoleResponse> getAllRoles() throws Exception {
		List<RoleResponse> userResponses = new ArrayList<>();
		List<RoleEntity> roleResponses = new ArrayList<>();
		
		logger.info("********************UtilityServicesImpl getAllRoles from database******************");
		
		roleResponses = roleRepository.getAllRolesByIsDeletedAndNotAdmin("N");
		
		if(roleResponses != null) {
			Iterator itr = roleResponses.iterator();
			
			while(itr.hasNext()){
				RoleEntity roleSingleResp = (RoleEntity) itr.next();
				RoleResponse response = new RoleResponse();
				
				response.setId(roleSingleResp.getId());
				response.setCode(roleSingleResp.getCode());				
				response.setName(roleSingleResp.getName());
				response.setStatus(roleSingleResp.getStatus());
				
				userResponses.add(response);
			}
		}
		
		return userResponses;
	}
	
	@Override
	public BaseResponse getAllChargeGrouping() throws Exception {
		 baseResponse = new BaseResponse();
			
			logger.info("*****************getAllChargeGrouping method in UtilityServicesImpl*******************");
			
			
			List<ChargesGroupingEntity> chargesGroupingEntityList = chargeGroupingRepository.findByIsdeleted("N");
			
			if (chargesGroupingEntityList == null ) {
				throw new Exception("Error: Charge Grouping Not Found");
			}
			
			if(chargesGroupingEntityList.size() == 0) {
				throw new Exception("Error: Charge Grouping Not Found");
			}
			
			List<ChargesGroupingResponse> chargesGroupingResponse = new ArrayList<ChargesGroupingResponse>();
			
			for (ChargesGroupingEntity chargesGroupingEntity : chargesGroupingEntityList) {
				 logger.info("chargesGroupingEntity : "+chargesGroupingEntity); 
				 
				 ChargesGroupingResponse chargesGroupingResp = new ChargesGroupingResponse();
				 
				 chargesGroupingResp.setId(chargesGroupingEntity.getId());
				 chargesGroupingResp.setChargesgrouping(chargesGroupingEntity.getChargesgrouping());
				 chargesGroupingResp.setChargesgroupingcode(chargesGroupingEntity.getChargesgroupingcode());
			 
				 chargesGroupingResponse.add(chargesGroupingResp);
			}
			    
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(chargesGroupingResponse);
			return baseResponse;
	}

	@Override
	public BaseResponse getAllChargesSubType(String groupingChargeId) throws Exception {
		    baseResponse = new BaseResponse();
			
			logger.info("*****************getAllChargesSubType method in UtilityServicesImpl*******************");
			
			long chargesgroupingid = Long.parseLong(groupingChargeId);
			List<ChargesTypeEntity> chargesTypeEntityList = chargesTypeRepository.findByChargesgroupingidAndIsdeleted(chargesgroupingid,"N");
			
			if (chargesTypeEntityList == null ) {
				throw new Exception("Error: Charge Rate Not Found");
			}
			
			if(chargesTypeEntityList.size() == 0) {
				throw new Exception("Error: Charge Rate Not Found");
			}
			
			List<ChargesTypeResponse> chargesTypeResponseList = new ArrayList<ChargesTypeResponse>();
			
			for (ChargesTypeEntity chargesTypeEntity : chargesTypeEntityList) {
				 logger.info("chargesTypeEntity : "+chargesTypeEntity); 
				 
				 ChargesTypeResponse chargesTypeResponse = new ChargesTypeResponse();
				 
				 chargesTypeResponse.setId(chargesTypeEntity.getId());
			     chargesTypeResponse.setChargecode(chargesTypeEntity.getChargecode());
			     chargesTypeResponse.setChargecodedescription(chargesTypeEntity.getChargecodedescription());
				 
				 chargesTypeResponseList.add(chargesTypeResponse);
			}
			    
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(chargesTypeResponseList);
			return baseResponse;
	}

	@Override
	public BaseResponse getAllAds() throws Exception {
		   baseResponse = new BaseResponse();
			
			logger.info("*****************getAllAds method in UserServicesImpl*******************");
					
			String currentdate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
			
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
						
			List<UploadAdsResponse> uploadAdsResponseList = new ArrayList<UploadAdsResponse>();
						
			List<UploadAdsEntity> uploadAdsEntityList = uploadAdsRepository.findByStatusAndIsdeleted("Active","N");
								
			if (uploadAdsEntityList == null) {
				throw new Exception("Error: Advertisement Not Found");
			}
			
			if(uploadAdsEntityList.size() == 0) {
				throw new Exception("Error: Advertisement Not Found");
			}
			
			for (UploadAdsEntity uploadAdsEntity : uploadAdsEntityList) {
				
				String endDate = uploadAdsEntity.getEnddate();
				
				Date curDate = format.parse(currentdate);
	            Date eDate = format.parse(endDate);
				
	            if (eDate.after(curDate) || eDate.equals(curDate)) {
	                UploadAdsResponse uploadAdsResponse = new UploadAdsResponse();
					uploadAdsResponse.setContent(uploadAdsEntity.getContent());					
					uploadAdsResponseList.add(uploadAdsResponse);
	            }				
			}
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(uploadAdsResponseList);
			return baseResponse;
	}

	@Override
	public BaseResponse getAllIncotermList() throws Exception {
		   baseResponse = new BaseResponse();
		
		   logger.info("*****************getAllIncotermList method in UtilityServicesImpl*******************");
				
		   List<IncotermEntity> incotermEntityList = incotermRepository.findByIsdeleted("N");
		
		   if (incotermEntityList == null ) {
			   throw new Exception("Error: Incoterm Not Found");
		   }
		
		   if(incotermEntityList.size() == 0) {
			   throw new Exception("Error: Incoterm Not Found");
		   }
		
		   List<IncotermResponse> incotermResponseList = new ArrayList<IncotermResponse>();
		
		   for (IncotermEntity incotermEntity : incotermEntityList) {
			    logger.info("IncotermEntity : "+incotermEntity); 
			 
			    IncotermResponse incotermResponse = new IncotermResponse();
			    incotermResponse.setId(incotermEntity.getId());
			    incotermResponse.setIncoterm(incotermEntity.getIncoterm());
			    incotermResponse.setDescription(incotermEntity.getDescription());
			  
			    incotermResponseList.add(incotermResponse);
		   }
		    
		   baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		   baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		   baseResponse.setRespData(incotermResponseList);
		   return baseResponse;
	}

	@Override
	public BaseResponse getAllCountry() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getAllCountry method in UtilityServicesImpl*******************");
		
		List<CountryResponse> countryResponse = new ArrayList<CountryResponse>();
		
		List<LocationEntity> locationEntityList = locationRepository.findAllCountryAndIsdeleted("N");
		
		if (locationEntityList == null) {
			throw new Exception("Error: Country Not Found");
		}
		
		if(locationEntityList.size() == 0) {
			throw new Exception("Error: Country Not Found");
		}
		
		for (LocationEntity locationEntity : locationEntityList) {
			logger.info("Country Name: "+locationEntity.getCountryname());
				
			CountryResponse newResponse = new CountryResponse();
			newResponse.setCountrycode(locationEntity.getCountrycode());
			newResponse.setCountryname(locationEntity.getCountryname());
			    
			countryResponse.add(newResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(countryResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse getLocationByCountryCode(String countrycode) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getLocationByCountryCode method in UtilityServicesImpl*******************");
		logger.info("get existing location from database :: location: "+countrycode);
		
		if(countrycode.equals("")) {
		    throw new Exception("Error: countrycode Not Null");	
		}
		
		List<LocationResponse> locationResponse = new ArrayList<LocationResponse>();
		
		List<LocationEntity> locationEntityList = locationRepository.findByCountrycodeAndIsdeleted(countrycode,"N");
		
		if (locationEntityList == null) {
			throw new Exception("Error: Location Not Found");
		}
		
		if(locationEntityList.size() == 0) {
			throw new Exception("Error: Location Not Found");
		}
		
		for (LocationEntity locationEntity : locationEntityList) {
			logger.info("location Name: "+locationEntity.getLocationname());
				
			LocationResponse newResponse = new LocationResponse();
			newResponse.setId(locationEntity.getId());
			newResponse.setLocationcode(locationEntity.getLocationcode());
			newResponse.setLocationname(locationEntity.getLocationname());
			newResponse.setCountrycode(locationEntity.getCountrycode());
			    
			locationResponse.add(newResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(locationResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse submitFeedback(FeedbackRequest feedbackRequest) throws Exception {
        baseResponse = new BaseResponse();
		
        String userid = feedbackRequest.getUserid();
        System.out.println("userid: "+userid);
        String companyname = "";
	    String email = "";
	    String mobile = "";
	    String location = "";
	    
	    FeedbackEntity feedbackEntity = new FeedbackEntity();
	    
	    String name = feedbackRequest.getName();		
	    String message = feedbackRequest.getMessage();
	    feedbackEntity.setCreateby("system");
	    
        if(userid != "" && userid != null) {
        	long id = Long.parseLong(userid);
            feedbackEntity.setUserid(id);
            
        	UserEntity userEntity = userRepository.findByIdAndIsdeleted(id,"N");			
			Long compId = userEntity.getCompanyId();
			email = userEntity.getEmail();
    	    mobile = userEntity.getMobileno();
    	    
    	    String rolecode = userEntity.getRole();
    	    if(rolecode.equals("AA")) {
    	    	throw new Exception("Error: No Company Details Found");
    	    }
            Optional<CompanyEntity> compEntity = companyRepository.findById(compId);			 
            if (compEntity == null ) {
    			throw new Exception("Error: No Company Details Found");
    		}
        	companyname = compEntity.get().getTradename();    	    
    	    location = compEntity.get().getDestination();
    	    
        }else {
        	 companyname = feedbackRequest.getCompanyname();
    	     email = feedbackRequest.getEmail();
    	     mobile = feedbackRequest.getMobile();
    	     location = feedbackRequest.getLocation();
        }
        
	    feedbackEntity.setName(name);
	    feedbackEntity.setCompanyname(companyname);
	    feedbackEntity.setEmail(email);
	    feedbackEntity.setMobilenumber(mobile);
	    feedbackEntity.setLocation(location);
	    feedbackEntity.setMessage(message);
	    feedbackEntity.setStatus("Open");
	    feedbackEntity.setIsdeleted("N");
		
	    feedbackRepository.save(feedbackEntity);
	    
	    FeedbackResponse feedbackResponse = new FeedbackResponse();
		feedbackResponse.setId(Long.toString(feedbackEntity.getId()));
		String createddate = new SimpleDateFormat("dd-MMM-yyyy").format(feedbackEntity.getCreatedate());
		feedbackResponse.setCreateddate(createddate);
		feedbackResponse.setName(feedbackEntity.getName());
		feedbackResponse.setCompanyname(feedbackEntity.getCompanyname());
		feedbackResponse.setEmail(feedbackEntity.getEmail());
		feedbackResponse.setMobile(feedbackEntity.getMobilenumber());
		feedbackResponse.setLocation(feedbackEntity.getLocation());
		feedbackResponse.setMessage(feedbackEntity.getMessage());
		feedbackResponse.setStatus(feedbackEntity.getStatus());
		feedbackResponse.setReplydate(feedbackEntity.getReplydate());
		
		
	    Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo("info@absortio.com");
		mailExceed.setSubject("Book My Cargo: User Feedback");
						
		try {
			emailService.sendFeedbackToAdminMail(mailExceed, feedbackResponse);
		}catch(Exception e) {
			e.printStackTrace();
		}		
		logger.info("Email send successfully!");
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Feedback received, we will respond shortly.");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getCountrySpecByCode(String countrycode) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getCountrySpecByCode method in UtilityServicesImpl*******************");
					
		CountrySpecificationEntity countrySpecificationEntity = countrySpecificationRepository.findByCountrycodeAndStatusAndIsdeleted(countrycode,"Active","N");
			
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
	public BaseResponse getAllChargeBasis() throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getAllChargeBasis method in UtilityServicesImpl*******************");
		
		
		List<ChargeBasisEntity> chargeBasisEntityList = chargeBasisRepository.findByIsdeleted("N");
		
		if (chargeBasisEntityList == null ) {
			throw new Exception("Error: Charge Basis Not Found");
		}
		
		if(chargeBasisEntityList.size() == 0) {
			throw new Exception("Error: Charge Basis Not Found");
		}
		
		List<ChargeBasisResponse> chargeBasisResponseList = new ArrayList<ChargeBasisResponse>();
		
		for (ChargeBasisEntity chargeBasisEntity : chargeBasisEntityList) {
			 
			ChargeBasisResponse chargeBasisResponse = new ChargeBasisResponse();
			 
			chargeBasisResponse.setId(Long.toString(chargeBasisEntity.getId()));
			chargeBasisResponse.setShipmenttype(chargeBasisEntity.getShipmenttype());
			chargeBasisResponse.setBasis(chargeBasisEntity.getBasis());
			chargeBasisResponse.setBasiscode(chargeBasisEntity.getBasiscode());
					
			chargeBasisResponseList.add(chargeBasisResponse);
		}
		    
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(chargeBasisResponseList);
		return baseResponse;
	}
	
}
