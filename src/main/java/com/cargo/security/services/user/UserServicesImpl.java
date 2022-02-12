package com.cargo.security.services.user;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.BookScheduleRequest;
import com.cargo.load.request.CompanyDetailsRequest;
import com.cargo.load.request.SearchTransportRequest;
import com.cargo.load.request.SendEnquiryRequest;
import com.cargo.load.request.UserDetailsRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.BookScheduleResponse;
import com.cargo.load.response.BookingCountByStatusResponse;
import com.cargo.load.response.ChargesGroupingCurrencyResponse;
import com.cargo.load.response.ChargesRateResponse;
import com.cargo.load.response.CompanyResponse;
import com.cargo.load.response.EnquiryCountByStatusResponse;
import com.cargo.load.response.EnquiryForwarderAcceptedResponse;
import com.cargo.load.response.EnquiryForwarderResponse;
import com.cargo.load.response.EnquiryResponse;
import com.cargo.load.response.EnquiryScheduleChargesDetailsResponse;
import com.cargo.load.response.ForwarderDetailsResponse;
import com.cargo.load.response.ForwarderPopForwarderResponse;
import com.cargo.load.response.FreightChargesResponse;
import com.cargo.load.response.LocationResponse;
import com.cargo.load.response.PopularForwarderResponse;
import com.cargo.load.response.RecentSearchResponse;
import com.cargo.load.response.ScheduleLegsResponse;
import com.cargo.load.response.ScheduleResponse;
import com.cargo.load.response.SearchEnquiryResponse;
import com.cargo.load.response.UnitsResponse;
import com.cargo.load.response.UserBookScheduleResponse;
import com.cargo.load.response.UserDetailsResponse;
import com.cargo.mail.EmailService;
import com.cargo.mail.ForwarderEnquiryMailRequest;
import com.cargo.mail.Mail;
import com.cargo.mail.SignupEmailMapper;
import com.cargo.models.BookingDetailsEntity;
import com.cargo.models.CarrierEntity;
import com.cargo.models.ChargeBasisEntity;
import com.cargo.models.ChargesGroupingEntity;
import com.cargo.models.ChargesRateEntity;
import com.cargo.models.ChargesTypeEntity;
import com.cargo.models.CompanyEntity;
import com.cargo.models.ConfigureAlertEntity;
import com.cargo.models.EnquiryEntity;
import com.cargo.models.EnquiryForwarderEntity;
import com.cargo.models.ForwarderSetupEntity;
import com.cargo.models.IncotermEntity;
import com.cargo.models.LocationEntity;
import com.cargo.models.RecentSearchEntity;
import com.cargo.models.RoleEntity;
import com.cargo.models.ScheduleLegsEntity;
import com.cargo.models.TransDetailsEntity;
import com.cargo.models.UnitsEntity;
import com.cargo.models.UserEntity;
import com.cargo.models.VerifyAccountEntity;
import com.cargo.pdf.PdfService;
import com.cargo.repository.BookingDetailsRepository;
import com.cargo.repository.CarrierRepository;
import com.cargo.repository.ChargeBasisRepository;
import com.cargo.repository.ChargeGroupingRepository;
import com.cargo.repository.ChargesRateRepository;
import com.cargo.repository.ChargesTypeRepository;
import com.cargo.repository.CompanyRepository;
import com.cargo.repository.ConfigureAlertRepository;
import com.cargo.repository.EnquiryForwarderRepository;
import com.cargo.repository.EnquiryRepository;
import com.cargo.repository.ForwarderSetupRepository;
import com.cargo.repository.IncotermRepository;
import com.cargo.repository.LocationRepository;
import com.cargo.repository.RecentSearchRepository;
import com.cargo.repository.RoleRepository;
import com.cargo.repository.ScheduleLegsRepository;
import com.cargo.repository.TransDetailsRepository;
import com.cargo.repository.UnitsRepository;
import com.cargo.repository.UserRepository;
import com.cargo.repository.VerifyAccoutRepository;
import com.cargo.security.services.admin.BookingReffGenerator;
import com.cargo.security.services.admin.EnquiryReffGenerator;
import com.cargo.sms.SmsService;
import com.cargo.utils.FileStorageUtility;
import com.cargo.utils.FileStringsUtils;
import com.cargo.utils.GstinVerificationAPIServices;
import com.cargo.utils.StatusUtils;
import com.cargo.utils.StringsUtils;
import com.cargo.utils.TitleCaseConvertsionUtils;
import com.cargo.utils.UploadPathContUtils;


@Service
public class UserServicesImpl implements UserServices{
	
	private static final Logger logger = LoggerFactory.getLogger(UserServicesImpl.class);
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RoleRepository roleRepository;
	
	@Autowired
	EnquiryRepository enquiryRepository;
	
	@Autowired
	EnquiryForwarderRepository enquiryForwarderRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired 
	FileStorageUtility fileStorageUtility;
	
	@Autowired
	TransDetailsRepository transDetailsRepository;
		
	@Autowired
	ScheduleLegsRepository scheduleLegsRepository;
	
	@Autowired
	ChargeGroupingRepository chargeGroupingRepository;
	
	@Autowired
	ChargesRateRepository chargesRateRepository;
	
	@Autowired
	ChargesTypeRepository chargesTypeRepository;
	
	@Autowired
	RecentSearchRepository recentSearchRepository;
	
	@Autowired
	UnitsRepository unitsRepository;
	
	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	BookingReffGenerator bookingReffGenerator;
	
	@Autowired
	BookingDetailsRepository bookingDetailsRepository;
	
	@Autowired
	PdfService pdfService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	EnquiryReffGenerator enquiryReffGenerator;
	
	@Autowired
	VerifyAccoutRepository verifyAccoutRepository;
	
	@Autowired
	IncotermRepository incotermRepository;
	
	@Autowired
	ConfigureAlertRepository configureAlertRepository;
	
	@Autowired
	SmsService smsService;
	
	@Autowired
	ChargeBasisRepository chargeBasisRepository;
	
	@Autowired
	ForwarderSetupRepository forwarderSetupRepository;
	
	@Autowired
	PasswordEncoder encoder;
	
		
	BaseResponse baseResponse	= null;

	@Override
	public BaseResponse deactivateAccount(UserDetailsRequest userDetailsRequest) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************deactivateAccount method in UserServicesImpl*******************");
		
		long userId = Long.parseLong(userDetailsRequest.getUserid());
		
		UserEntity userEntity = userRepository.findByIdAndIsdeleted(userId, "N");
		
		if(userEntity == null) {
			throw new Exception("Error: User not found");
		}
		
		long id = userEntity.getId();
		
		UserEntity userEnti = userRepository.getOne(id);
		userEnti.setIsactive("INACTIVE");
		userEnti.setIsdeleted("Y");
		userRepository.save(userEnti);
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();		
		userInfo.put("msg", "Account deactivated successfully");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
		
	}
	
	@Override
	public BaseResponse checkTransportationDetailsBySearch(SearchTransportRequest searchTransRequest) throws Exception {
		
		 baseResponse = new BaseResponse();
			
			logger.info("*****************checkTransportationDetailsBySearch method in UserServicesImpl*******************");
			logger.info("check transportation details from database :: searchTransRequest: "+searchTransRequest);
			Long userId = Long.parseLong(searchTransRequest.getUserId());
			String origin = searchTransRequest.getOrigin();
			String destination = searchTransRequest.getDestination();
			String cargoCategory = searchTransRequest.getCargoCategory();
			String commodity = searchTransRequest.getCommodity();
			String imco = searchTransRequest.getImco();
	        String temprange = searchTransRequest.getTemprange();
			String cargoReadydate = searchTransRequest.getCargoReadyDate();
			String shipmentType = searchTransRequest.getShipmentType();
			
			if(!origin.contains("(") || !origin.contains(")")){
				throw new Exception("Error: No Matching Data Found");
			}
			
			if(!destination.contains("(") || !destination.contains(")")) {
				throw new Exception("Error: No Matching Data Found");
			}
			
			UserEntity userEntity = userRepository.findByIdAndIsdeleted(userId,"N");
			
			if(userEntity.getCompanyId() == null) {
				HashMap<String ,String>  userInfo = new HashMap<String ,String>();
				
				userInfo.put("msg", "Error: Please fill the company details first!");
				
				baseResponse.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				baseResponse.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				baseResponse.setRespData(userInfo);
				
				return baseResponse;
				
			}
			
			String newOrigin = origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")).trim();
			String newDestination = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")")).trim();
			
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			Date date = format.parse(cargoReadydate);
			
		    logger.info("Date object value: "+date);
			
		    String pattern = "dd-MM-yyyy";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String newDate = simpleDateFormat.format(date);
		    logger.info(date+" : "+newDate);
			
			LocationEntity originEnti = locationRepository.findByLocationcodeAndIsdeleted(newOrigin,"N");
			long originId = originEnti.getId();
						
			LocationEntity destEnti = locationRepository.findByLocationcodeAndIsdeleted(newDestination,"N");
			long destinationId = destEnti.getId();
									
			//add data into recent search table
			logger.info("Add searchTransRequest into recent search table: "+searchTransRequest); 
			String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
				
			RecentSearchEntity recentSearchEntity = new RecentSearchEntity();
				
			recentSearchEntity.setCreateby("system");				
			recentSearchEntity.setOriginid(originId);				
			recentSearchEntity.setDestinationid(destinationId);
			recentSearchEntity.setCargocategory(cargoCategory);
			recentSearchEntity.setCommodity(commodity);
			recentSearchEntity.setImco(imco);
			recentSearchEntity.setTemprange(temprange);
			recentSearchEntity.setCargoreadydate(cargoReadydate);
			recentSearchEntity.setShipmenttype(shipmentType);
			
			if(shipmentType.equals("FCL")) {
				String twentyFtCount = searchTransRequest.getTwentyFtCount();
				String fourtyFtCount = searchTransRequest.getFourtyFtCount();
				String fourtyFtHcCount = searchTransRequest.getFourtyFtHcCount();
				String fourtyFiveFtCount = searchTransRequest.getFourtyFiveFtCount();
				
				recentSearchEntity.setTwentyftcount(twentyFtCount);
				recentSearchEntity.setFourtyftcount(fourtyFtCount);
				recentSearchEntity.setFourtyfthccount(fourtyFtHcCount);
				recentSearchEntity.setFourtyfiveftcount(fourtyFiveFtCount);
			}else if(shipmentType.equals("LCL")){
			    String lcltotalweight = searchTransRequest.getLcltotalweight();
			    String lclweightunit = searchTransRequest.getLclweightunit();
			    String lclvolume = searchTransRequest.getLclvolume();
			    String lclvolumeunit = searchTransRequest.getLclvolumeunit();
			    String lclnumberpackage = searchTransRequest.getLclnumberpackage();
			    String lclpackageunit = searchTransRequest.getLclpackageunit();
			    
			    recentSearchEntity.setLcltotalweight(lcltotalweight);
			    recentSearchEntity.setLclweightunit(lclweightunit);
			    recentSearchEntity.setLclvolume(lclvolume);
			    recentSearchEntity.setLclvolumeunit(lclvolumeunit);
			    recentSearchEntity.setLclnumberpackage(lclnumberpackage);
			    recentSearchEntity.setLclpackageunit(lclpackageunit);
			}
			
				
			recentSearchEntity.setSearchdate(timeStamp);
			recentSearchEntity.setIsdeleted("N");
							
			recentSearchRepository.save(recentSearchEntity);			
						
		    List<UserEntity> userEntityList = userRepository.findByRoleAndIsdeleted("FF","N");
		    
		    if(userEntityList == null){
				throw new Exception("Error: No Forwarder(s) Found");
			}
		    
            String currentdate = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());			
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			
			ForwarderPopForwarderResponse forwarderPopForwarderResponse = new ForwarderPopForwarderResponse();
			
			List<ForwarderDetailsResponse> forwarderDetailsResponseList = new ArrayList<ForwarderDetailsResponse>();
			List<PopularForwarderResponse> popularForwarderResponseList = new ArrayList<PopularForwarderResponse>(); 
			
			for(UserEntity userEnt : userEntityList) {
					
				long forwarderid = userEnt.getId();
				String email = userEnt.getEmail();
				long compId = userEnt.getCompanyId();
								
				Optional<CompanyEntity> compEntity = companyRepository.findById(compId);	
	            String forwarder = compEntity.get().getTradename();
	            String location = compEntity.get().getDestination();
	             
	            ForwarderSetupEntity forwarderSetupEntity = forwarderSetupRepository.findByForwarderidAndStatusAndIsdeleted(forwarderid,"Active","N");
	            
	            if(forwarderSetupEntity != null) {
	            	
	            	String endDate = forwarderSetupEntity.getEnddate();	
	            	String priority = forwarderSetupEntity.getPriority();
					Date curDate = dateFormat.parse(currentdate);
		            Date eDate = dateFormat.parse(endDate);
					
		            if (eDate.after(curDate) || eDate.equals(curDate)) {
		            	
		            	PopularForwarderResponse popularForwarderResponse = new PopularForwarderResponse();
			            popularForwarderResponse.setId(forwarderid);
			            popularForwarderResponse.setEmail(email);
			            popularForwarderResponse.setForwarder(forwarder);
			            popularForwarderResponse.setLocation(location);
			            popularForwarderResponse.setPriority(priority);
			            popularForwarderResponse.setEmail(email);
			            popularForwarderResponseList.add(popularForwarderResponse);
		            }	            	
	            }
	            	           	            
				ForwarderDetailsResponse forwarderDetailsResponse = new ForwarderDetailsResponse();
				forwarderDetailsResponse.setId(forwarderid);
				forwarderDetailsResponse.setEmail(email);
				forwarderDetailsResponse.setForwarder(forwarder);
				forwarderDetailsResponse.setLocation(location);
				forwarderDetailsResponseList.add(forwarderDetailsResponse);					
			}
			
			forwarderPopForwarderResponse.setForwarderDetailsResponseList(forwarderDetailsResponseList);
			forwarderPopForwarderResponse.setPopularForwarderResponseList(popularForwarderResponseList);
			
			baseResponse.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(forwarderPopForwarderResponse);
			return baseResponse;			 
			
	}
	
	@Override
	public BaseResponse sendEnquirySearchRequest(SendEnquiryRequest sendEnquiryRequest) throws Exception {
		    baseResponse = new BaseResponse();
			
			logger.info("*****************sendEnquirySearchRequest method in UserServicesImpl*******************");
						
			Long userId = Long.parseLong(sendEnquiryRequest.getUserId());
			String origin = sendEnquiryRequest.getOrigin();
			String destination = sendEnquiryRequest.getDestination();
			String cargoCategory = sendEnquiryRequest.getCargoCategory();
			String commodity = sendEnquiryRequest.getCommodity();
			String imco = sendEnquiryRequest.getImco();
	        String temprange = sendEnquiryRequest.getTemprange();
			String cargoReadydate = sendEnquiryRequest.getCargoReadyDate();
			String shipmentType = sendEnquiryRequest.getShipmentType();
			
			String[] forwarderIds = sendEnquiryRequest.getForwarderIds().split(",");
						
			if(!origin.contains("(") || !origin.contains(")")){
				throw new Exception("Error: No Matching Data Found");
			}
			
			if(!destination.contains("(") || !destination.contains(")")) {
				throw new Exception("Error: No Matching Data Found");
			}
			
			UserEntity userEntity = userRepository.findByIdAndIsdeleted(userId,"N");
			
			if(userEntity.getCompanyId() == null) {
				HashMap<String ,String>  userInfo = new HashMap<String ,String>();
				
				userInfo.put("msg", "Error: Please fill the company details first!");
				
				baseResponse.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				baseResponse.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				baseResponse.setRespData(userInfo);
				
				return baseResponse;
				
			}
			
			Long companyId = userEntity.getCompanyId();
			
			String newOrigin = origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")).trim();
			String newDestination = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")")).trim();
			
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
			Date date = format.parse(cargoReadydate);
			
		    logger.info("Date object value: "+date);
			
		    String pattern = "dd-MM-yyyy";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String newDate = simpleDateFormat.format(date);
		    logger.info(date+" : "+newDate);
			
			LocationEntity originEnti = locationRepository.findByLocationcodeAndIsdeleted(newOrigin,"N");
			long originId = originEnti.getId();
			String originCityCode = originEnti.getCitycode();
			
			LocationEntity destEnti = locationRepository.findByLocationcodeAndIsdeleted(newDestination,"N");
			long destinationId = destEnti.getId();
			String destinationCityCode = destEnti.getCitycode();
										
			if(forwarderIds.length == 0) {
				throw new Exception("Error: Please choose forwarder to send enquiry!");
			}
			
			List<String> newListIds = Arrays.asList(forwarderIds);
			HashSet<String> hsetFromString = new HashSet<String>(newListIds);
			HashSet<String> removalSet = new HashSet<String>();
			Iterator<String> it = hsetFromString.iterator();
			
			while (it.hasNext())
		    {		      
				String fNewIdString = it.next();
				long fNewId = Long.parseLong(fNewIdString);
				ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(fNewId,"N");
				
				if(configureAlertEntity != null) {
					boolean flag = false;
					String orgAlertIds = configureAlertEntity.getOriginlocids();
					
					if(orgAlertIds != null && !orgAlertIds.equals("")) {
						String[] orgAlertIdsArray = orgAlertIds.split(",");	          	        
						List<String> existAlertListOrg = Arrays.asList(orgAlertIdsArray);
						
						if(existAlertListOrg.contains(Long.toString(originId))) {
							flag = true;
						}
					}
										
					String destAlertIds = configureAlertEntity.getDestinationlocids();
					if(destAlertIds != null && !destAlertIds.equals("")) {
						String[] destAlertIdsArray = destAlertIds.split(",");	          	        
						List<String> existAlertListDest = Arrays.asList(destAlertIdsArray);
						
						if(existAlertListDest.contains(Long.toString(destinationId))) {
							flag = true;
						}
					}
					
					if(flag) {												
						removalSet.add(fNewIdString);
					}
				}				
		    }
			
			hsetFromString.removeAll(removalSet);
			
			String respMessage = "";
			
			int size = hsetFromString.size();
			
			if(size > 0 ) {
				
				String newIds = String.join(",", hsetFromString);
				String[] newForwarderIds = newIds.split(",");
				
				EnquiryEntity enquiryEntity = new EnquiryEntity();
				
				enquiryEntity.setCreateby("system");				
				String searchdate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());				
				String enquiryreference = enquiryReffGenerator.getEnquiryRefferenceNumber(originCityCode, destinationCityCode);
				enquiryEntity.setEnquiryreference(enquiryreference);
				enquiryEntity.setUserid(Long.parseLong(sendEnquiryRequest.getUserId()));					
				enquiryEntity.setOriginlocid(originId);
				enquiryEntity.setDestinationlocid(destinationId);
				enquiryEntity.setCargoreadydate(sendEnquiryRequest.getCargoReadyDate());
				enquiryEntity.setCargocategory(sendEnquiryRequest.getCargoCategory());
				enquiryEntity.setCommodity(commodity);
				enquiryEntity.setImco(imco);
				enquiryEntity.setTemprange(temprange);
				enquiryEntity.setShipmenttype(sendEnquiryRequest.getShipmentType());
				
				String selectedFCL = "";
				String selectedLCL = "";
				
				if(shipmentType.equals("FCL")) {
					int twentyFtCount = Integer.parseInt(sendEnquiryRequest.getTwentyFtCount());
					int fourtyFtCount = Integer.parseInt(sendEnquiryRequest.getFourtyFtCount());
					int fourtyFtHcCount = Integer.parseInt(sendEnquiryRequest.getFourtyFtHcCount());
					int fourtyFiveFtCount = Integer.parseInt(sendEnquiryRequest.getFourtyFiveFtCount());
					HashSet<String> hSetSelectedFCL = new HashSet<String>();
					if(twentyFtCount != 0){
					    String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
					    hSetSelectedFCL.add(twentyFtCountStr);
					}
					if(fourtyFtCount != 0){
					    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
					    hSetSelectedFCL.add(fourtyFtCountStr);
					}
					if(fourtyFtHcCount != 0){
					    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
					    hSetSelectedFCL.add(fourtyFtHcCountStr);
					}
					if(fourtyFiveFtCount != 0){
					    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' ";	
					    hSetSelectedFCL.add(fourtyFiveFtCountStr);
					}
					selectedFCL = String.join(",", hSetSelectedFCL);
					
					enquiryEntity.setTwentyftcount(twentyFtCount);
					enquiryEntity.setFourtyftcount(fourtyFtCount);
					enquiryEntity.setFourtyfthccount(fourtyFtHcCount);
					enquiryEntity.setFourtyfiveftcount(fourtyFiveFtCount);
					
				}else if(shipmentType.equals("LCL")){
					String lcltotalweight = sendEnquiryRequest.getLcltotalweight();
				    String lclweightunit = sendEnquiryRequest.getLclweightunit();
				    if(lclweightunit.equals("Pounds")) {
				    	long weightInKgs = (long) (Long.parseLong(lclweightunit) * 0.453592);
				    	lcltotalweight = Long.toString(weightInKgs);
				    	lclweightunit = "Kgs";	
				    }
				    String lclvolume = sendEnquiryRequest.getLclvolume();
				    String lclvolumeunit = sendEnquiryRequest.getLclvolumeunit();
				    String lclnumberpackage = sendEnquiryRequest.getLclnumberpackage();
				    String lclpackageunit = sendEnquiryRequest.getLclpackageunit();
				    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
				    		
				    enquiryEntity.setLcltotalweight(lcltotalweight);
				    enquiryEntity.setLclweightunit(lclweightunit);
				    enquiryEntity.setLclvolume(lclvolume);
				    enquiryEntity.setLclvolumeunit(lclvolumeunit);
				    enquiryEntity.setLclnumberpackage(lclnumberpackage);
				    enquiryEntity.setLclpackageunit(lclpackageunit);
				}
									
				enquiryEntity.setSearchdate(searchdate);					
				enquiryEntity.setStatus(StatusUtils.REQUESTED);
				enquiryEntity.setIsdeleted("N");
					
				enquiryRepository.save(enquiryEntity);
				
				CompanyEntity companyEntity = companyRepository.findByIdAndIsdeleted(companyId,"N");
				if (companyEntity == null ) {
					throw new Exception("Error: No Company Details Found");
				}
				String companyname = companyEntity.getTradename();
					
				ForwarderEnquiryMailRequest forwarderEnquiryMailRequest = new ForwarderEnquiryMailRequest();
				forwarderEnquiryMailRequest.setOrigin(origin);
				forwarderEnquiryMailRequest.setDestination(destination);
				forwarderEnquiryMailRequest.setEnquiryReference(enquiryreference);
				forwarderEnquiryMailRequest.setEnquiryBy(companyname);
				forwarderEnquiryMailRequest.setCargoCategory(cargoCategory);
				forwarderEnquiryMailRequest.setCommodity(commodity);
				forwarderEnquiryMailRequest.setImco(imco);
				forwarderEnquiryMailRequest.setTemprange(temprange);
				forwarderEnquiryMailRequest.setCargoReadyDate(cargoReadydate);
				forwarderEnquiryMailRequest.setShipmentType(shipmentType);
				forwarderEnquiryMailRequest.setSelectedFcl(selectedFCL);
				forwarderEnquiryMailRequest.setSelectedLcl(selectedLCL);
				forwarderEnquiryMailRequest.setSearchDate(searchdate);
				forwarderEnquiryMailRequest.setEnquiryStatus(StatusUtils.REQUESTED);
				
				for(String fId : newForwarderIds) {
					long forwarderid = Long.parseLong(fId);
					
					UserEntity userEnt = userRepository.findByIdAndIsdeleted(forwarderid,"N");
					
					String email = userEnt.getEmail();				
					
					EnquiryForwarderEntity enquiryForwarderEntity = new EnquiryForwarderEntity();
						
					enquiryForwarderEntity.setCreateby("system");
					enquiryForwarderEntity.setEnquiryid(enquiryEntity.getId());
					enquiryForwarderEntity.setEnquiryreference(enquiryreference);
					enquiryForwarderEntity.setUserid(Long.parseLong(sendEnquiryRequest.getUserId()));
					enquiryForwarderEntity.setForwarderid(forwarderid);
						
					enquiryForwarderEntity.setOriginlocid(originId);
					enquiryForwarderEntity.setDestinationlocid(destinationId);
					enquiryForwarderEntity.setCargoreadydate(sendEnquiryRequest.getCargoReadyDate());
					enquiryForwarderEntity.setShipmenttype(shipmentType);
						
					enquiryForwarderEntity.setStatus(StatusUtils.REQUESTED);
					enquiryForwarderEntity.setIsdeleted("N");
					enquiryForwarderRepository.save(enquiryForwarderEntity);
									
					//Enquiry send to selected forwarders 		
					Mail mailExceed = new Mail();
					mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
					mailExceed.setTo(email);
					mailExceed.setSubject("Book My Cargo: "+shipmentType+" Enquiry request for enquiry <"+enquiryreference+">");
								
					try {
							emailService.sendForwarderEnquiryMail(mailExceed,forwarderEnquiryMailRequest);
							logger.info("Email Send Successfully for forwarder :: "+email);
					}catch(Exception e) {
							e.printStackTrace();
					}
						
				}
										
				respMessage = "Your enquiry has been sent to selected forwarder(s) and we will notify you when quotation is received from them.";
			
			}else {
				
				respMessage = "Selected forwarder(s) not offering service for your origin and destination.";
			}
			
			HashMap<String ,String>  searchInfo = new HashMap<String ,String>();		
			searchInfo.put("msg", respMessage );
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(searchInfo);
			return baseResponse;
	}
	
	@Override
	public BaseResponse getExistCompanyDetailsByUserId(String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getExistCompanyDetailsByUserId method in UserServicesImpl*******************");
		logger.info("get existing company details by userId:: userId: "+userId);
		
		Long userIdLong = Long.parseLong(userId);
		
		Optional<UserEntity> userEntity = userRepository.findById(userIdLong);
		Long companyId = userEntity.get().getCompanyId();
		
		if(companyId != null) {
			CompanyResponse companyResponse = new CompanyResponse();
			
			Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
			if (companyEntity == null) {
				throw new Exception("Error: Company Not Found");
			}
						
			companyResponse.setId(companyEntity.get().getId());
			companyResponse.setGstinnumber(companyEntity.get().getGstinnumber());
			
			companyResponse.setStatejurisdictioncode(companyEntity.get().getStatejurisdictioncode());
			companyResponse.setStatejurisdiction(companyEntity.get().getStatejurisdiction());
			
			companyResponse.setLegalname(companyEntity.get().getLegalname());
			companyResponse.setTradename(companyEntity.get().getTradename());
			
			companyResponse.setBuildingname(companyEntity.get().getBuildingname());
			companyResponse.setBuildingnumber(companyEntity.get().getBuildingnumber());
			companyResponse.setFloornumber(companyEntity.get().getFloornumber());
			companyResponse.setStreet(companyEntity.get().getStreet());
			companyResponse.setLocation(companyEntity.get().getLocation());
			companyResponse.setDestination(companyEntity.get().getDestination());
			
			companyResponse.setCity(companyEntity.get().getCity());
			companyResponse.setPincode(companyEntity.get().getPincode());
			
			companyResponse.setTaxpayertype(companyEntity.get().getTaxpayertype());
			companyResponse.setCentrejurisdiction(companyEntity.get().getCentrejurisdiction());
			companyResponse.setGstinstatus(companyEntity.get().getGstinstatus());
			
			
			companyResponse.setRegistrationdate(companyEntity.get().getRegistrationdate());
			companyResponse.setLastupdatedate(companyEntity.get().getLastupdatedate());
			
			companyResponse.setStatecode(companyEntity.get().getStatecode());
			
			companyResponse.setUserpanno(companyEntity.get().getUserpanno());
			companyResponse.setUserpanfilename(companyEntity.get().getUserpanfilename());
			companyResponse.setUserpanfilestatus(companyEntity.get().getUserpanfilestatus());
			
			companyResponse.setUserieccode(companyEntity.get().getUserieccode());
			companyResponse.setUseriecfilename(companyEntity.get().getUseriecfilename());
			companyResponse.setUseriecfilestatus(companyEntity.get().getUseriecfilestatus());
			
			companyResponse.setAlias(companyEntity.get().getAlias());
			companyResponse.setAliasfilename(companyEntity.get().getAliasfilename());
			companyResponse.setAliasfilestatus(companyEntity.get().getAliasfilestatus());
			
			companyResponse.setWebsite(companyEntity.get().getWebsite());
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(companyResponse);
			
		}else {
			throw new Exception("Error: Company Not Found");
		}
				
		return baseResponse;
	}

	
	
	@Override
	public BaseResponse updateUserCompanyDetails(CompanyDetailsRequest companyDetailsRequest, MultipartFile userPanFile,
			MultipartFile userIECFile, MultipartFile logo) throws Exception {
		
		
		   logger.info("********************updateUserCompanyDetails Method******************");
	
		   baseResponse = new BaseResponse();
	
		   Long userId = Long.parseLong(companyDetailsRequest.getUserId());
		
		   Optional<UserEntity> userEntity = userRepository.findById(userId);
		   Long companyId = userEntity.get().getCompanyId();
		
		   CompanyEntity compEntity = companyRepository.getOne(companyId);
		   		
		   if(compEntity != null) {
			    logger.info("companyEntity: "+compEntity);
				
				//code to save company details 	
			   
				compEntity.setUserpanno(companyDetailsRequest.getUserPanNumber());
				compEntity.setUserieccode(companyDetailsRequest.getUserIECCode());
				compEntity.setAlias(companyDetailsRequest.getAliasName());
				compEntity.setWebsite(companyDetailsRequest.getWebsite());
				compEntity.setUpdateby(Long.toString(userId));
				
				if(userPanFile != null) {
				   
				   boolean deleteExistFile = fileStorageUtility.deleteExistsFileFromDrirectory(FileStringsUtils.FILE_TYPE_PAN,UploadPathContUtils.FILE_COMPANY_DIR ,companyDetailsRequest.getUserId());
				   if(deleteExistFile) {
					   logger.info("existing file deleted from directory");
				   }
				   String panFilename = fileStorageUtility.storeFile(userPanFile,FileStringsUtils.FILE_TYPE_PAN,UploadPathContUtils.FILE_COMPANY_DIR,companyDetailsRequest.getUserId());
				   compEntity.setUserpanno(companyDetailsRequest.getUserPanNumber());
				   compEntity.setUserpanfilename(panFilename);
				   compEntity.setUserpanfilestatus("N");
				}
				
				if(userIECFile != null) {
				 
					boolean deleteExistFile = fileStorageUtility.deleteExistsFileFromDrirectory(FileStringsUtils.FILE_TYPE_IEC, UploadPathContUtils.FILE_COMPANY_DIR, companyDetailsRequest.getUserId());
				    if(deleteExistFile) {
						   logger.info("existing file deleted from directory");
					 }
				    
				    String iecFilename = fileStorageUtility.storeFile(userIECFile,FileStringsUtils.FILE_TYPE_IEC,UploadPathContUtils.FILE_COMPANY_DIR,companyDetailsRequest.getUserId());
				    compEntity.setUserieccode(companyDetailsRequest.getUserIECCode());
				    compEntity.setUseriecfilename(iecFilename);
				    compEntity.setUseriecfilestatus("N");
				    
				}
				
				if(logo != null) {
					
					   boolean deleteExistFile = fileStorageUtility.deleteExistsFileFromDrirectory(FileStringsUtils.FILE_TYPE_ALIASLOGO,UploadPathContUtils.FILE_COMPANY_DIR, companyDetailsRequest.getUserId());
					   if(deleteExistFile) {
						   logger.info("existing file deleted from directory");
					   }
					   
					   String aliasFilename = fileStorageUtility.storeFile(logo,FileStringsUtils.FILE_TYPE_ALIASLOGO,UploadPathContUtils.FILE_COMPANY_DIR,companyDetailsRequest.getUserId());
					   compEntity.setAlias(companyDetailsRequest.getAliasName());
					   compEntity.setAliasfilename(aliasFilename);
					   compEntity.setAliasfilestatus("N");
					}
					
				companyRepository.save(compEntity);
								
		   }else {
			   throw new Exception("Error: Company not found");
	       }
		
		   HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		   userInfo.put("msg", "Company Updated successfully!");
				
		   baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		   baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		   baseResponse.setRespData(userInfo);
		   return baseResponse;
		   
		}

	
	@Override
	public BaseResponse getRecentSearchData() throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getRecentSearchData method in UserServicesImpl*******************");

		List<RecentSearchResponse> recentSearchResponseList = new ArrayList<RecentSearchResponse>();
				
		List<RecentSearchEntity> recentSearchEntityList = recentSearchRepository.findTop5ByOrderByCreatedateDesc();
		
		if (recentSearchEntityList == null) {
			throw new Exception("Error: No Recent Search Data Found");
		}
		
		if(recentSearchEntityList.size() == 0) {
			throw new Exception("Error: No Recent Search Data Found");
		}
		
		for (RecentSearchEntity recentSearchEntity : recentSearchEntityList) {
			
			RecentSearchResponse recentSearchResponse = new RecentSearchResponse();
            
			recentSearchResponse.setId(recentSearchEntity.getId());
			
			Optional<LocationEntity> orgEntity = locationRepository.findById(recentSearchEntity.getOriginid());
			
			String org = orgEntity.get().getLocationname();
			String orgCode = orgEntity.get().getLocationcode();
			recentSearchResponse.setOrigin(org);
			recentSearchResponse.setOrigincode(orgCode);
			
			Optional<LocationEntity> destEntity = locationRepository.findById(recentSearchEntity.getDestinationid());
			
			String dest = destEntity.get().getLocationname();
			String destCode = destEntity.get().getLocationcode();
			recentSearchResponse.setDestination(dest);
			recentSearchResponse.setDestinationcode(destCode);
			
			recentSearchResponse.setCargocategory(recentSearchEntity.getCargocategory());
			recentSearchResponse.setCommodity(recentSearchEntity.getCommodity());
			recentSearchResponse.setCargoreadydate(recentSearchEntity.getCargoreadydate());
			recentSearchResponse.setShipmenttype(recentSearchEntity.getShipmenttype());
			recentSearchResponse.setTwentyftcount(recentSearchEntity.getTwentyftcount());
			recentSearchResponse.setFourtyftcount(recentSearchEntity.getFourtyftcount());
			recentSearchResponse.setFourtyfthccount(recentSearchEntity.getFourtyfthccount());
			recentSearchResponse.setFourtyfiveftcount(recentSearchEntity.getFourtyfiveftcount());
			recentSearchResponse.setSearchdate(recentSearchEntity.getSearchdate());
			
			recentSearchResponseList.add(recentSearchResponse);  
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(recentSearchResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getRecentSearchDataByDataId(String idstr) throws Exception {
		 baseResponse = new BaseResponse();
			
			logger.info("*****************getRecentSearchData method in UserServicesImpl*******************");

			long id = Long.parseLong(idstr);
			
						
			RecentSearchEntity recentSearchEntity = recentSearchRepository.findByIdAndIsdeleted(id,"N");
			
			if (recentSearchEntity == null) {
				throw new Exception("Error: No Recent Search Data Found");
			}
			
			RecentSearchResponse recentSearchResponse = new RecentSearchResponse();
	            
			recentSearchResponse.setId(recentSearchEntity.getId());
			
			Optional<LocationEntity> orgEntity = locationRepository.findById(recentSearchEntity.getOriginid());
			recentSearchResponse.setOrigin(orgEntity.get().getLocationname());
			recentSearchResponse.setOrigincode(orgEntity.get().getLocationcode());
			
			Optional<LocationEntity> destEntity = locationRepository.findById(recentSearchEntity.getDestinationid());		
			recentSearchResponse.setDestination(destEntity.get().getLocationname());
			recentSearchResponse.setDestinationcode(destEntity.get().getLocationcode());
			
			recentSearchResponse.setCargocategory(recentSearchEntity.getCargocategory());
			recentSearchResponse.setCommodity(recentSearchEntity.getCommodity());
			recentSearchResponse.setImco(recentSearchEntity.getImco());
			recentSearchResponse.setTemprange(recentSearchEntity.getTemprange());
			recentSearchResponse.setCargoreadydate(recentSearchEntity.getCargoreadydate());
			recentSearchResponse.setShipmenttype(recentSearchEntity.getShipmenttype());
			recentSearchResponse.setTwentyftcount(recentSearchEntity.getTwentyftcount());
			recentSearchResponse.setFourtyftcount(recentSearchEntity.getFourtyftcount());
			recentSearchResponse.setFourtyfthccount(recentSearchEntity.getFourtyfthccount());
			recentSearchResponse.setFourtyfiveftcount(recentSearchEntity.getFourtyfiveftcount());
			
			recentSearchResponse.setLcltotalweight(recentSearchEntity.getLcltotalweight());
			recentSearchResponse.setLclweightunit(recentSearchEntity.getLclweightunit());
			recentSearchResponse.setLclvolume(recentSearchEntity.getLclvolume());
			recentSearchResponse.setLclvolumeunit(recentSearchEntity.getLclvolumeunit());
			recentSearchResponse.setLclnumberpackage(recentSearchEntity.getLclnumberpackage());
			recentSearchResponse.setLclpackageunit(recentSearchEntity.getLclpackageunit());
			
			recentSearchResponse.setSearchdate(recentSearchEntity.getSearchdate());
				
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(recentSearchResponse);
			return baseResponse;
	}

	@Override
	public BaseResponse getPackageUnitsList() throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getPackageUnitsList method in UserServicesImpl*******************");
	
		List<UnitsEntity> unitsList = unitsRepository.findByIsdeleted("N");
		
		if (unitsList == null) {
			throw new Exception("Error: No Package Units Data Found");
		}
	
		List<UnitsResponse> unitsResponseList  = new ArrayList<UnitsResponse>();
		
		for (UnitsEntity unitsEntity : unitsList) {
			UnitsResponse unitsResponse = new UnitsResponse();
			
			unitsResponse.setId(unitsEntity.getId());
			unitsResponse.setUnits(unitsEntity.getUnits());
			unitsResponse.setUnitscode(unitsEntity.getUnitscode());
			
			unitsResponseList.add(unitsResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(unitsResponseList);
		return baseResponse;
	}
	
	@Override
	public BaseResponse bookSchedule(BookScheduleRequest bookScheduleRequest) throws Exception {
		baseResponse = new BaseResponse();
		
		/*
		logger.info("*****************bookSchedule method in UserServicesImpl*******************");
			
		Long userId = Long.parseLong(bookScheduleRequest.getUserId());
		String chargerateids = bookScheduleRequest.getChargerateids();
		
		
		int twentyFtCount = Integer.parseInt(bookScheduleRequest.getTwentyFtCount());
		int fourtyFtCount = Integer.parseInt(bookScheduleRequest.getFourtyFtCount());
		int fourtyFtHcCount = Integer.parseInt(bookScheduleRequest.getFourtyFtHcCount());
		int fourtyFiveFtCount = Integer.parseInt(bookScheduleRequest.getFourtyFiveFtCount());
		
        String selectedFCL = "";
		
		if(twentyFtCount != 0){
		    String twentyFtCountStr = Integer.toString(twentyFtCount) + "X20' ";
		    selectedFCL = selectedFCL + twentyFtCountStr ;
		}
		if(fourtyFtCount != 0){
		    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + "X40' ";
		    selectedFCL = selectedFCL + fourtyFtCountStr ;
		}
		if(fourtyFtHcCount != 0){
		    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + "X40' HC ";
		    selectedFCL = selectedFCL + fourtyFtHcCountStr ;
		}
		if(fourtyFiveFtCount != 0){
		    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + "X45' ";
		    selectedFCL = selectedFCL + fourtyFiveFtCountStr ;
		    
		}
		
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		
		if(userEntity.get().getCompanyId() == null) {
			HashMap<String ,String>  userInfo = new HashMap<String ,String>();
			
			userInfo.put("msg", "Error: Please fill the company details first!");
			
			baseResponse.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			baseResponse.setRespData(userInfo);
			
			return baseResponse;
			
		}
		
		Long companyId = userEntity.get().getCompanyId();
	
		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
		if (companyEntity == null ) {
			throw new Exception("Error: No Company Details Found");
		}
		String companyname = companyEntity.get().getTradename();
		String companyAddress = companyEntity.get().getBuildingnumber() +", "+companyEntity.get().getFloornumber()+", "+companyEntity.get().getBuildingname()+", "+companyEntity.get().getStreet();
		String companyNameWithAdd = companyname+", "+companyAddress;
						
		Long scheduleId = Long.parseLong(bookScheduleRequest.getScheduleId());
		
		Optional<TransDetailsEntity> transDetailsEntity = transDetailsRepository.findById(scheduleId);
		
		if (transDetailsEntity == null ) {
			throw new Exception("Error: No Transportation Details Found");
		}
		
		Long originId = transDetailsEntity.get().getOriginid();		
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originCityCode = originEntity.get().getCitycode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
		
		Long destinationId = transDetailsEntity.get().getDestinationid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();
		String destinationCityCode = destinationEntity.get().getCitycode();
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
		
		String ids [] = chargerateids.split(",");
		long carrierId = Long.parseLong(ids[0]);
		Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
				
		String carrierName = carrierEntity.get().getCarriershortname();
		
		String bookingReff = bookingReffGenerator.getBookingRefferenceNumber(companyname,originCityCode,destinationCityCode);
		
		String cutoffdate = transDetailsEntity.get().getCutoffdate();
		
		BookingDetailsEntity bookingDetailsEntity = new BookingDetailsEntity();
		bookingDetailsEntity.setCreateby("system");
		bookingDetailsEntity.setBookingreff(bookingReff);
		bookingDetailsEntity.setUserid(userId);
		bookingDetailsEntity.setOriginid(originId);
		bookingDetailsEntity.setDestinationid(destinationId);
		bookingDetailsEntity.setCarrierid(carrierId);
		bookingDetailsEntity.setForwarderid(transDetailsEntity.get().getForwarderid());
		bookingDetailsEntity.setScheduleid(scheduleId);
		bookingDetailsEntity.setChargerateids(chargerateids);
		
		bookingDetailsEntity.setTwentyftcount(twentyFtCount);
		bookingDetailsEntity.setFourtyftcount(fourtyFtCount);
		bookingDetailsEntity.setFourtyfthccount(fourtyFtHcCount);
		bookingDetailsEntity.setFourtyfiveftcount(fourtyFiveFtCount);
		
		String bookingdate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date()); 
		bookingDetailsEntity.setBookingdate(bookingdate);
		bookingDetailsEntity.setBookingupdateddate(bookingdate);
		bookingDetailsEntity.setBookingstatus(StatusUtils.REQUESTED);
		bookingDetailsEntity.setIsdeleted("N");
		bookingDetailsRepository.save(bookingDetailsEntity);
	
		
		//charge rate calculation part
		logger.info("going to calculate charge rates");
		String chargeRateId [] = chargerateids.split(",");
		
		long cost = 0;
		long otherfreight = 0;
		long totalfreight = 0;
		
		float twentyFtRate=0;
		float fourtyFtRate=0;
		float fourtyHcRate=0;
		float fourtyFiveFtrate=0;
		float otherRate = 0;
		
		List<ChargesRateResponse> chargesRateResponseList = new  ArrayList<ChargesRateResponse>();
		
		for(int i=0; i<chargeRateId.length;i++) {
			Long chargeId = Long.parseLong(chargeRateId[i]);
			
			ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
						
			ChargesRateEntity chargesRateEnti = chargesRateRepository.findByIdAndIsdeleted(chargeId,"N");
			
			long chargesgroupingid = chargesRateEnti.getChargesgroupingid();
			ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
			String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
			chargesRateResponse.setChargesgrouping(chargesgrouping);
			
			long chargetypeid = chargesRateEnti.getChargetypeid();
			ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");
			String chargecode = chargesTypeEntity.getChargecode();
			String chargestype = chargesTypeEntity.getChargecodedescription();
			chargesRateResponse.setChargestype(chargestype);
			
			chargesRateResponse.setCurrency(chargesRateEnti.getCurrency());
			chargesRateResponse.setBasis(chargesRateEnti.getBasis());
			chargesRateResponse.setQuantity(chargesRateEnti.getQuantity());
						
			if(chargecode.equals("BAS")) {
		    	
		    	String basis = chargesRateEnti.getBasis();
		    	
		    	String chargeTypeCode = "";
		    	
		    	if(basis.equals("Per 20' Container")) {
		    		chargeTypeCode = "OF20FT";
		    	}else if(basis.equals("Per 40' Container")) {
		    		chargeTypeCode = "OF40FT";
		    	}else if(basis.equals("Per 40' HC Container")) {
		    		chargeTypeCode = "OF40HC";
		    	}else if(basis.equals("Per 45' HC Container")){
		    		chargeTypeCode = "OF40HC";
		    	}
		    	
		    	if(chargeTypeCode.equals("OF20FT")) {						   
					   if(!chargesRateEnti.getRate().equals("")) {
						   twentyFtRate = Float.parseFloat(chargesRateEnti.getRate());
					   }							    					   				   
				   }else if(chargeTypeCode.equals("OF40FT")){
					   if(!chargesRateEnti.getRate().equals("")) {
						   fourtyFtRate = Float.parseFloat(chargesRateEnti.getRate());
					   }			    						   				   
				   }else if(chargeTypeCode.equals("OF40HC")){
					   if(!chargesRateEnti.getRate().equals("")) {
					       fourtyHcRate = Float.parseFloat(chargesRateEnti.getRate());
					   }
				   }else if(chargeTypeCode.equals("OF45FT")){
					   if(!chargesRateEnti.getRate().equals("")) {
						   fourtyFiveFtrate = Float.parseFloat(chargesRateEnti.getRate());  
					   }						   					   
				   }
		    	
		    }else {
		    	otherRate = Float.parseFloat(chargesRateEnti.getRate());
		    	otherfreight = (long) (otherfreight + otherRate);
		    }
			
		    chargesRateResponse.setRate(chargesRateEnti.getRate());
		    chargesRateResponseList.add(chargesRateResponse);
			 			
		}
		
		logger.info("twentyFt: caluclations");
		if(bookScheduleRequest.getTwentyFtCount() != null && bookScheduleRequest.getTwentyFtCount() != "" ) {
			int totalTwentyFtCount = Integer.parseInt(bookScheduleRequest.getTwentyFtCount());						
			if(totalTwentyFtCount != 0) {
				cost = (long) (cost + (twentyFtRate * totalTwentyFtCount));
			}
		}
	
		logger.info("fourtyFt: calculations");
		if(bookScheduleRequest.getFourtyFtCount() != null && bookScheduleRequest.getFourtyFtCount() != "" ) {
			int totalFourtyFtCount = Integer.parseInt(bookScheduleRequest.getFourtyFtCount());													
			if(totalFourtyFtCount != 0) {
				cost = (long) (cost + (fourtyFtRate * totalFourtyFtCount));
			}
		}
		
		
		logger.info("fourtyFtHc: calculations");
		if(bookScheduleRequest.getFourtyFtHcCount() != null && bookScheduleRequest.getFourtyFtHcCount() != "" ) {
			int totalFourtyFtHcCount = Integer.parseInt(bookScheduleRequest.getFourtyFtHcCount());													
			if(totalFourtyFtHcCount != 0) {
				cost = (long) (cost + (fourtyHcRate * totalFourtyFtHcCount));
			}
		}
		
		
		logger.info("fourtyFiveFt: calculations");
		if(bookScheduleRequest.getFourtyFiveFtCount() != null && bookScheduleRequest.getFourtyFiveFtCount() != "" ) {
			int totalFourtyFiveFtCount = Integer.parseInt(bookScheduleRequest.getFourtyFiveFtCount());													
			if(totalFourtyFiveFtCount != 0) {
				cost = (long) (cost + (fourtyFiveFtrate * totalFourtyFiveFtCount));
			}
		}
		
		totalfreight = (long) (cost + otherfreight);
		
		// To generate PDF
		
		BookScheduleResponse bookScheduleResponse = new BookScheduleResponse();
		bookScheduleResponse.setBookingreff(bookingReff);
		bookScheduleResponse.setBookedby(userEntity.get().getEmail());
		
		bookScheduleResponse.setDateofbooking(bookingdate);
		bookScheduleResponse.setBookingstatus(bookingDetailsEntity.getBookingstatus());
		bookScheduleResponse.setCutoffdate(cutoffdate);
		
		bookScheduleResponse.setBookingparty(companyNameWithAdd);
		bookScheduleResponse.setFclselected(selectedFCL);
		
		bookScheduleResponse.setBookingdate(bookingdate);
		bookScheduleResponse.setBookingupdateddate(bookingdate);
		
		bookScheduleResponse.setOrigin(originWithCode);
		bookScheduleResponse.setOrigincode(origincode);
		bookScheduleResponse.setDestination(destinationWithCode);
		bookScheduleResponse.setDestinationcode(destinationcode);
		
        List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transDetailsEntity.get().getId(),"N");
		
		if(scheduleLegsEntityList == null) {
			throw new Exception("Error: No Schedule Legs Found");
		}

		List<ScheduleLegsResponse> scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
		
		for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
			ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
			
			scheduleLegsResponse.setOriginid(Long.toString(scheduleLegsEntity.getOriginid()));
			
			Optional<LocationEntity> originEnti=  locationRepository.findById(scheduleLegsEntity.getOriginid());
			String orgcode = originEnti.get().getLocationcode();
			String orgCityCode = originEnti.get().getCitycode();
			String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
			scheduleLegsResponse.setOrigin(orgWithCode);
			
			scheduleLegsResponse.setDestinationid(scheduleLegsEntity.getDestinationid());
			Optional<LocationEntity> destEntity =  locationRepository.findById(scheduleLegsEntity.getDestinationid());
			String destcode = destEntity.get().getLocationcode();
			String destCityCode = destEntity.get().getCitycode();
			String destWithCode = destEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
			scheduleLegsResponse.setDestination(destWithCode);
			
			scheduleLegsResponse.setMode(scheduleLegsEntity.getMode());
			
			long carriId = scheduleLegsEntity.getCarrierid();
			Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
			String carriName = carriEntity.get().getCarriershortname();
			scheduleLegsResponse.setCarrier(carriName);
			
			scheduleLegsResponse.setVessel(scheduleLegsEntity.getVessel());
			scheduleLegsResponse.setVoyage(scheduleLegsEntity.getVoyage());
			
			scheduleLegsResponse.setEtddate(scheduleLegsEntity.getEtddate());
			scheduleLegsResponse.setEtadate(scheduleLegsEntity.getEtadate());
			scheduleLegsResponse.setTransittime(scheduleLegsEntity.getTransittime());
			
			scheduleLegsResponseList.add(scheduleLegsResponse);						
		}
		
		bookScheduleResponse.setScheduleLegsResponse(scheduleLegsResponseList);
		bookScheduleResponse.setChargesRateResponseList(chargesRateResponseList);
		
		bookScheduleResponse.setTotalcharges(totalfreight);
		
		bookScheduleResponse.setTwentyFtCount(twentyFtCount);
		bookScheduleResponse.setFourtyFtCount(fourtyFtCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFtHcCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFiveFtCount);
		
		logger.info("bookScheduleResponse:: "+bookScheduleResponse);
		
		
		String filename = pdfService.generatePdf(bookScheduleResponse);
		
		logger.info("PDF file generated :: "+filename);
		
		//booking successful mail to customer with attachment 		
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(userEntity.get().getEmail());
		mailExceed.setSubject("Book My Cargo: Booking Reference Number <"+bookingReff+"> submitted");
					
		try {
				emailService.sendBookingSuccessMail(mailExceed,bookScheduleResponse ,filename);
		}catch(Exception e) {
				e.printStackTrace();
		}
		
		logger.info("Email Send Successfully with attachment :: "+filename);
		*/
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Booking submitted successfully!");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserBookingList(String userId,String shipmentType,String bookingStatus) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getUserBookingList method in UserServicesImpl*******************");
	
		long userid = Long.parseLong(userId);
		
		List<BookingDetailsEntity> bookingDetailsEntityList = bookingDetailsRepository.findByUseridAndStatusLikeAndCustomerisdeleted(userid,shipmentType,bookingStatus+"%","N");

		
		if (bookingDetailsEntityList == null) {
			throw new Exception("Error: No Data Found");
		}
		
		if (bookingDetailsEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
	
		List<UserBookScheduleResponse> userBookScheduleResponseList  = new ArrayList<UserBookScheduleResponse>();
		
		for (BookingDetailsEntity bookingDetailsEntity : bookingDetailsEntityList) {
			UserBookScheduleResponse userBookScheduleResponse = new UserBookScheduleResponse();
			
			userBookScheduleResponse.setId(bookingDetailsEntity.getId());
			userBookScheduleResponse.setBookingreff(bookingDetailsEntity.getBookingreff());
			
			long enquiryId = bookingDetailsEntity.getEnquiryid();			
			Optional<EnquiryEntity> enquiryEntity = enquiryRepository.findById(enquiryId);
			
			
			Optional<LocationEntity> orgEntity = locationRepository.findById(enquiryEntity.get().getOriginlocid());
			String origin = orgEntity.get().getLocationname();
			String origincode = orgEntity.get().getLocationcode();
			String originwithcode = origin +" ("+origincode+")";
					
			userBookScheduleResponse.setOrigin(origin);
			userBookScheduleResponse.setOrigincode(origincode);
			userBookScheduleResponse.setOriginwithcode(originwithcode);
			
			Optional<LocationEntity> destEntity = locationRepository.findById(enquiryEntity.get().getDestinationlocid());
			String destination = destEntity.get().getLocationname();
			String destinationcode = destEntity.get().getLocationcode();
			String destinationwithcode = destination+" ("+destinationcode+")";
			
			userBookScheduleResponse.setDestination(destination);
			userBookScheduleResponse.setDestinationcode(destinationcode);
			userBookScheduleResponse.setDestinationwithcode(destinationwithcode);
			
			/*
			CarrierEntity carrierEntity = carrierRepository.findByIdAndIsdeleted(bookingDetailsEntity.getCarrierid(),"N");
			String carriername = carrierEntity.getCarriername();
			userBookScheduleResponse.setCarriername(carriername);
			*/
			
			userBookScheduleResponse.setBookingdate(bookingDetailsEntity.getBookingdate());
			userBookScheduleResponse.setBookingstatus(bookingDetailsEntity.getBookingstatus());
			
			long scheduleid = bookingDetailsEntity.getScheduleid();
			
			TransDetailsEntity transDetailsEntity = transDetailsRepository.findByIdAndIsdeleted(scheduleid,"N");
			
			long userIdForw = transDetailsEntity.getForwarderid();
			Optional<UserEntity> userEntiForw = userRepository.findById(userIdForw);
					
			long compId = userEntiForw.get().getCompanyId();
			
            Optional<CompanyEntity> compEntity = companyRepository.findById(compId);
			 
            String forwarder = compEntity.get().getTradename();
			
			userBookScheduleResponse.setForwarder(forwarder);
			
			userBookScheduleResponse.setCutoffdate(transDetailsEntity.getCutoffdate());
			
			userBookScheduleResponseList.add(userBookScheduleResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userBookScheduleResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserBookingCountByStatus(String userId,String shipmentType) throws Exception {
		 baseResponse = new BaseResponse();
			
		logger.info("*****************getUserBookingCountByStatus method in UserServicesImpl*******************");
			
		long userid = Long.parseLong(userId);
		
		
		long count = bookingDetailsRepository.bookingDetailsEntityCoutByUserId(userid,shipmentType,"N"); //shipmentType filter remaining
		BookingCountByStatusResponse bookingCountByStatusResponse = null;
		
		if(count == 0) {
			bookingCountByStatusResponse = new BookingCountByStatusResponse(0,0,0,0);
		}else {
			bookingCountByStatusResponse = bookingDetailsRepository.bookingCountByUserIdAndCustomerisdeletedAndGroupByStatus(userid,shipmentType, "N");
			
			if (bookingCountByStatusResponse == null) {
				throw new Exception("Error: No Data Found");
			}
		}
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(bookingCountByStatusResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserBookingDetailsById(String bookingReff) throws Exception {
		baseResponse = new BaseResponse();
		
		BookingDetailsEntity bookingDetailsEntity = bookingDetailsRepository.findByBookingreffAndIsdeleted(bookingReff,"N");
		
		if (bookingDetailsEntity == null) {
			throw new Exception("Error: No Booking Details Found");
		}
				
		Long userId = bookingDetailsEntity.getUserid();
		String chargerateids = bookingDetailsEntity.getChargerateids();
        long enquiryId = bookingDetailsEntity.getEnquiryid();
        String shipmentType = bookingDetailsEntity.getShipmenttype();
		
		Optional<EnquiryEntity> enquiryEntity = enquiryRepository.findById(enquiryId);
						
		String cargoReadyDate = enquiryEntity.get().getCargoreadydate();
				
        String selectedFCL = "";
        String selectedLCL = "";
		
        if(shipmentType.equals("FCL")) {
        	
        	int twentyFtCount = enquiryEntity.get().getTwentyftcount();
    		int fourtyFtCount = enquiryEntity.get().getFourtyftcount();
    		int fourtyFtHcCount = enquiryEntity.get().getFourtyfthccount();
    		int fourtyFiveFtCount = enquiryEntity.get().getFourtyfiveftcount();
    		HashSet<String> hSetSelectedFCL = new HashSet<String>();
    		if(twentyFtCount != 0){
    		    String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
    		    hSetSelectedFCL.add(twentyFtCountStr);
    		}
    		if(fourtyFtCount != 0){
    		    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
    		    hSetSelectedFCL.add(fourtyFtCountStr);
    		}
    		if(fourtyFtHcCount != 0){
    		    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
    		    hSetSelectedFCL.add(fourtyFtHcCountStr);
    		}
    		if(fourtyFiveFtCount != 0){
    		    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' ";
    		    hSetSelectedFCL.add(fourtyFiveFtCountStr);
    		}
    		selectedFCL = String.join(",", hSetSelectedFCL);
        }else if(shipmentType.equals("LCL")) {
        	String lcltotalweight = enquiryEntity.get().getLcltotalweight();
		    String lclweightunit = enquiryEntity.get().getLclweightunit();
		    String lclvolume = enquiryEntity.get().getLclvolume();
		    String lclvolumeunit = enquiryEntity.get().getLclvolumeunit();
		    String lclnumberpackage = enquiryEntity.get().getLclnumberpackage();
		    String lclpackageunit = enquiryEntity.get().getLclpackageunit();
		    
		    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
        }
        
		UserEntity userEntity = userRepository.findByIdAndIsdeleted(userId,"N");
		
		Long companyId = userEntity.getCompanyId();
	
		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
		if (! companyEntity.isPresent()  ) {
			throw new Exception("Error: No Company Details Found");
		}
		String companyname = companyEntity.get().getTradename();
		String companyAddress = companyEntity.get().getBuildingnumber() +", "+companyEntity.get().getFloornumber()+", "+companyEntity.get().getBuildingname()+", "+companyEntity.get().getStreet();
		String companyNameWithAdd = companyname+", "+companyAddress;
		Long scheduleId = bookingDetailsEntity.getScheduleid();
			
		Optional<TransDetailsEntity> transDetailsEntity = transDetailsRepository.findById(scheduleId);
		
		if (transDetailsEntity == null ) {
			throw new Exception("Error: No Transportation Details Found");
		}
		
		Long originId = transDetailsEntity.get().getOriginid();		
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
		
		Long destinationId = transDetailsEntity.get().getDestinationid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();

		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
		
		String cutoffdate = transDetailsEntity.get().getCutoffdate();
		
		//charge rate calculation part
		logger.info("going to calculate charge rates");
		String chargeRateId [] = chargerateids.split(",");
		
		long incotermId = 0;
				
        List<ChargesRateResponse> chargesRateResponseList = new  ArrayList<ChargesRateResponse>();
		
		for(int i=0; i<chargeRateId.length;i++) {
			Long chargeId = Long.parseLong(chargeRateId[i]);
			
			ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
						
			ChargesRateEntity chargesRateEnti = chargesRateRepository.findByIdAndIsdeleted(chargeId,"N");
			
			incotermId = chargesRateEnti.getIncotermid();
			
			long chargesgroupingid = chargesRateEnti.getChargesgroupingid();
			ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
			String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
			chargesRateResponse.setChargesgrouping(chargesgrouping);
			
			long chargetypeid = chargesRateEnti.getChargetypeid();
			ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");
			String chargestype = chargesTypeEntity.getChargecodedescription();
			chargesRateResponse.setChargestype(chargestype);
			
			chargesRateResponse.setCurrency(chargesRateEnti.getCurrency());
			
			String basiscode = chargesRateEnti.getBasis();
			ChargeBasisEntity chargeBasisEntity = chargeBasisRepository.findByBasiscodeAndIsdeleted(basiscode,"N");						
			chargesRateResponse.setBasis(chargeBasisEntity.getBasis());
			
			chargesRateResponse.setQuantity(chargesRateEnti.getQuantity());
					
		    chargesRateResponse.setRate(chargesRateEnti.getRate());
		    chargesRateResponseList.add(chargesRateResponse);
		}
									
		IncotermEntity incotermEntity = incotermRepository.findByIdAndIsdeleted(incotermId,"N");
		String incoterm = incotermEntity.getDescription()+" ("+incotermEntity.getIncoterm()+")";
			
		BookScheduleResponse bookScheduleResponse = new BookScheduleResponse();
		bookScheduleResponse.setBookingreff(bookingReff);
		bookScheduleResponse.setBookedby(userEntity.getEmail());
		bookScheduleResponse.setCargoreadydate(cargoReadyDate);
		bookScheduleResponse.setDateofbooking(bookingDetailsEntity.getBookingdate());
		bookScheduleResponse.setBookingstatus(bookingDetailsEntity.getBookingstatus());
		bookScheduleResponse.setCutoffdate(cutoffdate);
		
		bookScheduleResponse.setBookingparty(companyNameWithAdd);
		bookScheduleResponse.setFclselected(selectedFCL);
		
		bookScheduleResponse.setBookingdate(bookingDetailsEntity.getBookingdate());
		bookScheduleResponse.setBookingupdateddate(bookingDetailsEntity.getBookingupdateddate());
		bookScheduleResponse.setOrigin(originWithCode);
		bookScheduleResponse.setOrigincode(origincode);
		bookScheduleResponse.setDestination(destinationWithCode);
		bookScheduleResponse.setDestinationcode(destinationcode);
		
		bookScheduleResponse.setIncoterm(incoterm);
		
		List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transDetailsEntity.get().getId(),"N");
		
		if(scheduleLegsEntityList == null) {
			throw new Exception("Error: No Schedule Legs Found");
		}

		List<ScheduleLegsResponse> scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
		
		for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
			ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
			
			scheduleLegsResponse.setOriginid(Long.toString(scheduleLegsEntity.getOriginid()));
			
			Optional<LocationEntity> originEnti=  locationRepository.findById(scheduleLegsEntity.getOriginid());
			String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
			scheduleLegsResponse.setOrigin(orgWithCode);
			
			scheduleLegsResponse.setDestinationid(scheduleLegsEntity.getDestinationid());
			Optional<LocationEntity> destEntity =  locationRepository.findById(scheduleLegsEntity.getDestinationid());
			String destWithCode = destinationEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
			scheduleLegsResponse.setDestination(destWithCode);
			
			scheduleLegsResponse.setMode(scheduleLegsEntity.getMode());
			
			String mode = scheduleLegsEntity.getMode();
			if(mode.equals("Sea")) {
				long carriId = scheduleLegsEntity.getCarrierid();
				Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
				String carriName = carriEntity.get().getCarriershortname();
				scheduleLegsResponse.setCarrier(carriName);
			}else {
				String carriName = scheduleLegsEntity.getCarrieroption();
				scheduleLegsResponse.setCarrier(carriName);
			}
			
			scheduleLegsResponse.setVessel(scheduleLegsEntity.getVessel());
			scheduleLegsResponse.setVoyage(scheduleLegsEntity.getVoyage());
			
			scheduleLegsResponse.setEtddate(scheduleLegsEntity.getEtddate());
			scheduleLegsResponse.setEtadate(scheduleLegsEntity.getEtadate());
			scheduleLegsResponse.setTransittime(scheduleLegsEntity.getTransittime());
			
			scheduleLegsResponseList.add(scheduleLegsResponse);			
		}
		
		bookScheduleResponse.setScheduleLegsResponse(scheduleLegsResponseList);		
		bookScheduleResponse.setChargesRateResponseList(chargesRateResponseList);
		
		logger.info("bookScheduleResponse:: "+bookScheduleResponse);
					
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(bookScheduleResponse);
		return baseResponse;
		
	}

	@Override
	public BaseResponse updateBookingStatus(String id,String userId, String bookingStatus) throws Exception {
		baseResponse = new BaseResponse();
	
		long bookingId = Long.parseLong(id);
		
		String bookingupdateddate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		
		BookingDetailsEntity bookingDtlEntity = bookingDetailsRepository.getOne(bookingId);
		String bookingReff = bookingDtlEntity.getBookingreff();
		bookingDtlEntity.setUpdateby(userId);
		bookingDtlEntity.setBookingupdateddate(bookingupdateddate);
		bookingDtlEntity.setBookingstatus(bookingStatus);
		bookingDetailsRepository.save(bookingDtlEntity);
		
		
        BookingDetailsEntity bookingDetailsEntity = bookingDetailsRepository.findByBookingreffAndIsdeleted(bookingReff,"N");
		
		if (bookingDetailsEntity == null) {
			throw new Exception("Error: No Booking Details Found");
		}
				
		Long userIdLong = Long.parseLong(userId);
		String chargerateids = bookingDetailsEntity.getChargerateids();
		long enquiryId = bookingDetailsEntity.getEnquiryid();
		String shipmentType = bookingDetailsEntity.getShipmenttype();
		
		Optional<EnquiryEntity> enquiryEntity = enquiryRepository.findById(enquiryId);
		
        String selectedFCL = "";
        String selectedLCL = "";
        
        int twentyFtCount = 0;
		int fourtyFtCount = 0;
		int fourtyFtHcCount = 0;
		int fourtyFiveFtCount = 0;
		
		String lcltotalweight = "";
	    String lclweightunit = "";
		String lclvolume = "";
	    String lclvolumeunit = "";
	    String lclnumberpackage = "";
	    String lclpackageunit = "";
        
		if(shipmentType.equals("FCL")) {
			
			twentyFtCount = enquiryEntity.get().getTwentyftcount();
			fourtyFtCount = enquiryEntity.get().getFourtyftcount();
			fourtyFtHcCount = enquiryEntity.get().getFourtyfthccount();
			fourtyFiveFtCount = enquiryEntity.get().getFourtyfiveftcount();
			HashSet<String> hSetSelectedFCL = new HashSet<String>();
			if(twentyFtCount != 0){
			    String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
			    hSetSelectedFCL.add(twentyFtCountStr);
			}
			if(fourtyFtCount != 0){
			    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
			    hSetSelectedFCL.add(fourtyFtCountStr);
			}
			if(fourtyFtHcCount != 0){
			    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
			    hSetSelectedFCL.add(fourtyFtHcCountStr);
			}
			if(fourtyFiveFtCount != 0){
			    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' ";
			    hSetSelectedFCL.add(fourtyFiveFtCountStr);
			}
			selectedFCL = String.join(",", hSetSelectedFCL);
		}else if(shipmentType.equals("LCL")) {
			lcltotalweight = enquiryEntity.get().getLcltotalweight();
		    lclweightunit = enquiryEntity.get().getLclweightunit();
			lclvolume = enquiryEntity.get().getLclvolume();
		    lclvolumeunit = enquiryEntity.get().getLclvolumeunit();
		    lclnumberpackage = enquiryEntity.get().getLclnumberpackage();
		    lclpackageunit = enquiryEntity.get().getLclpackageunit();
		    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
		}
		
		Optional<UserEntity> userEntity = userRepository.findById(userIdLong);
		
		Long companyId = userEntity.get().getCompanyId();
	
		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
		if (! companyEntity.isPresent()  ) {
			throw new Exception("Error: No Company Details Found");
		}
		String companyname = companyEntity.get().getTradename();
		String companyAddress = companyEntity.get().getBuildingnumber() +", "+companyEntity.get().getFloornumber()+", "+companyEntity.get().getBuildingname()+", "+companyEntity.get().getStreet();
		String companyNameWithAdd = companyname+", "+companyAddress;
		
		long scheduleid = bookingDetailsEntity.getScheduleid();			
		TransDetailsEntity transDetailsEntity = transDetailsRepository.findByIdAndIsdeleted(scheduleid,"N");
			
		
		if (transDetailsEntity == null ) {
			throw new Exception("Error: No Transportation Details Found");
		}
		
		Long originId = transDetailsEntity.getOriginid();		
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
		
		Long destinationId = transDetailsEntity.getDestinationid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
	
		String cutoffdate = transDetailsEntity.getCutoffdate();

		String chargeRateId [] = chargerateids.split(",");

        List<ChargesRateResponse> chargesRateResponseList = new  ArrayList<ChargesRateResponse>();
		
		for(int i=0; i<chargeRateId.length;i++) {
			Long chargeId = Long.parseLong(chargeRateId[i]);
			
			ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
						
			ChargesRateEntity chargesRateEnti = chargesRateRepository.findByIdAndIsdeleted(chargeId,"N");
			
			long chargesgroupingid = chargesRateEnti.getChargesgroupingid();
			ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
			String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
			chargesRateResponse.setChargesgrouping(chargesgrouping);
			
			long chargetypeid = chargesRateEnti.getChargetypeid();
			ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");
			String chargestype = chargesTypeEntity.getChargecodedescription();
			chargesRateResponse.setChargestype(chargestype);
			
			chargesRateResponse.setCurrency(chargesRateEnti.getCurrency());
			
			String basiscode = chargesRateEnti.getBasis();
			ChargeBasisEntity chargeBasisEntity = chargeBasisRepository.findByBasiscodeAndIsdeleted(basiscode,"N");						
			chargesRateResponse.setBasis(chargeBasisEntity.getBasis());
			
			chargesRateResponse.setQuantity(chargesRateEnti.getQuantity());
			
		    chargesRateResponse.setRate(chargesRateEnti.getRate());
		    
		    double rate = Double.parseDouble(chargesRateEnti.getRate());
		    float quntity = Float.parseFloat(chargesRateEnti.getQuantity());
		    double tAmount = rate * quntity;
		    double value =Double.parseDouble(new DecimalFormat("##.##").format(tAmount));
		    String totalamount = Double.toString(value);
		    chargesRateResponse.setTotalamount(totalamount);
		    
		    chargesRateResponseList.add(chargesRateResponse);
			 			
		}
		
		// To generate PDF
		BookScheduleResponse bookScheduleResponse = new BookScheduleResponse();
		bookScheduleResponse.setBookingreff(bookingReff);
		bookScheduleResponse.setBookedby(userEntity.get().getEmail());
		
		bookScheduleResponse.setDateofbooking(bookingDetailsEntity.getBookingdate());
		bookScheduleResponse.setBookingstatus(bookingStatus);
		bookScheduleResponse.setCutoffdate(cutoffdate);
		
		bookScheduleResponse.setBookingparty(companyNameWithAdd);
		
		bookScheduleResponse.setShipmenttype(shipmentType);
		
		bookScheduleResponse.setFclselected(selectedFCL);
		bookScheduleResponse.setLclselected(selectedLCL);
		
		bookScheduleResponse.setBookingdate(bookingDetailsEntity.getBookingdate());
		bookScheduleResponse.setBookingupdateddate(bookingupdateddate);
		bookScheduleResponse.setOrigin(originWithCode);
		bookScheduleResponse.setOrigincode(origincode);
		bookScheduleResponse.setDestination(destinationWithCode);
		bookScheduleResponse.setDestinationcode(destinationcode);
		
		List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transDetailsEntity.getId(),"N");
			
	    if(scheduleLegsEntityList == null) {
			throw new Exception("Error: No Schedule Legs Found");
		}

		List<ScheduleLegsResponse> scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
		
		for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
			ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
			
			scheduleLegsResponse.setOriginid(Long.toString(scheduleLegsEntity.getOriginid()));
			
			Optional<LocationEntity> originEnti=  locationRepository.findById(scheduleLegsEntity.getOriginid());
			String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
			scheduleLegsResponse.setOrigin(orgWithCode);
			
			scheduleLegsResponse.setDestinationid(scheduleLegsEntity.getDestinationid());
			Optional<LocationEntity> destEntity =  locationRepository.findById(scheduleLegsEntity.getDestinationid());
			String destWithCode = destinationEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
			scheduleLegsResponse.setDestination(destWithCode);
			
			scheduleLegsResponse.setMode(scheduleLegsEntity.getMode());
			
			String mode = scheduleLegsEntity.getMode();
			if(mode.equals("Sea")) {
				long carriId = scheduleLegsEntity.getCarrierid();
				Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
				String carriName = carriEntity.get().getCarriershortname();
				scheduleLegsResponse.setCarrier(carriName);
			}else {
				String carriName = scheduleLegsEntity.getCarrieroption();
				scheduleLegsResponse.setCarrier(carriName);
			}
			
			scheduleLegsResponse.setVessel(scheduleLegsEntity.getVessel());
			scheduleLegsResponse.setVoyage(scheduleLegsEntity.getVoyage());
			
			scheduleLegsResponse.setEtddate(scheduleLegsEntity.getEtddate());
			scheduleLegsResponse.setEtadate(scheduleLegsEntity.getEtadate());
			scheduleLegsResponse.setTransittime(scheduleLegsEntity.getTransittime());
			
			scheduleLegsResponseList.add(scheduleLegsResponse);						
		}
		
		bookScheduleResponse.setScheduleLegsResponse(scheduleLegsResponseList);
		bookScheduleResponse.setChargesRateResponseList(chargesRateResponseList);
				
		bookScheduleResponse.setTwentyFtCount(twentyFtCount);
		bookScheduleResponse.setFourtyFtCount(fourtyFtCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFtHcCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFiveFtCount);
		
		bookScheduleResponse.setLcltotalweight(lcltotalweight);
		bookScheduleResponse.setLclweightunit(lclweightunit);
		bookScheduleResponse.setLclvolume(lclvolume);
		bookScheduleResponse.setLclvolumeunit(lclvolumeunit);
		bookScheduleResponse.setLclnumberpackage(lclnumberpackage);
		bookScheduleResponse.setLclpackageunit(lclpackageunit);
		
		logger.info("bookScheduleResponse:: "+bookScheduleResponse);
		
        String filename = pdfService.generatePdf(bookScheduleResponse);
		
		logger.info("PDF file generated :: "+filename);
		
		//booking successful mail to customer with attachment 		
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(userEntity.get().getEmail());
		mailExceed.setSubject("Book My Cargo: "+shipmentType+" Booking Reference Number <"+bookingReff+"> Updated");
					
		try {
				emailService.sendBookingUpdateStatusMail(mailExceed,bookScheduleResponse ,filename);
		}catch(Exception e) {
				e.printStackTrace();
		}
		
		logger.info("Email Send Successfully with attachment :: "+filename);
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Status Updated successfully!");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserEnquiryList(String userId, String shipmentType,String enquiryStatus) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getUserEnquiryList method in UserServicesImpl*******************");
	
		long userid = Long.parseLong(userId);
		
		List<EnquiryEntity> enquiryEntityList = enquiryRepository.findByUseridAndStatusLikeAndIsdeleted(userid,shipmentType,enquiryStatus+"%","N");
		
		if (enquiryEntityList == null) {
			throw new Exception("Error: No Data Found");
		}
		
		if (enquiryEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
	
		List<EnquiryResponse> enquiryResponseList  = new ArrayList<EnquiryResponse>();
		
		for (EnquiryEntity enquiryEntity : enquiryEntityList) {
			EnquiryResponse enquiryResponse = new EnquiryResponse();
			
			String selectedFCL = "";
			
            if(shipmentType.equals("FCL")) {
            	
            	int twentyFtCount = enquiryEntity.getTwentyftcount();
    			int fourtyFtCount = enquiryEntity.getFourtyftcount();
    			int fourtyFtHcCount = enquiryEntity.getFourtyfthccount();
    			int fourtyFiveFtCount = enquiryEntity.getFourtyfiveftcount();
    			HashSet<String> hSetSelectedFCL = new HashSet<String>();	
    			if(twentyFtCount != 0){
    			    String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
    			    hSetSelectedFCL.add(twentyFtCountStr);
    			}
    			if(fourtyFtCount != 0){
    			    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
    			    hSetSelectedFCL.add(fourtyFtCountStr);
    			}
    			if(fourtyFtHcCount != 0){
    			    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
    			    hSetSelectedFCL.add(fourtyFtHcCountStr);
    			}
    			if(fourtyFiveFtCount != 0){
    			    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' "; 
    			    hSetSelectedFCL.add(fourtyFiveFtCountStr);
    			}
    			selectedFCL = String.join(",", hSetSelectedFCL);
			}else if(shipmentType.equals("LCL")){
				String lcltotalweight = enquiryEntity.getLcltotalweight();
			    String lclweightunit = enquiryEntity.getLclweightunit();
			    String lclvolume = enquiryEntity.getLclvolume();
			    String lclvolumeunit = enquiryEntity.getLclvolumeunit();
			    String lclnumberpackage = enquiryEntity.getLclnumberpackage();
			    String lclpackageunit = enquiryEntity.getLclpackageunit();
			    
			    enquiryResponse.setLcltotalweight(lcltotalweight);
			    enquiryResponse.setLclweightunit(lclweightunit);
			    enquiryResponse.setLclvolume(lclvolume);
			    enquiryResponse.setLclvolumeunit(lclvolumeunit);
			    enquiryResponse.setLclnumberpackage(lclnumberpackage);
			    enquiryResponse.setLclpackageunit(lclpackageunit);
			    
			}
			
			enquiryResponse.setId(enquiryEntity.getId());
			enquiryResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
			
			Long originId = enquiryEntity.getOriginlocid();					
			Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
			String origincode = originEntity.get().getLocationcode();
			String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
			
			enquiryResponse.setOrigin(originWithCode);
			enquiryResponse.setOrigincode(origincode);
			
			Long destinationId = enquiryEntity.getDestinationlocid();
			Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
			String destinationcode = destinationEntity.get().getLocationcode();
			String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
			
			enquiryResponse.setDestination(destinationWithCode);
			enquiryResponse.setDestinationcode(destinationcode);
			
			enquiryResponse.setCargoreadydate(enquiryEntity.getCargoreadydate());
			enquiryResponse.setCargocategory(enquiryEntity.getCargocategory());
			enquiryResponse.setShipmenttype(enquiryEntity.getShipmenttype());
			enquiryResponse.setSelectedfcl(selectedFCL);
			enquiryResponse.setSearchdate(enquiryEntity.getSearchdate());
			enquiryResponse.setStatus(enquiryEntity.getStatus());
			
			enquiryResponseList.add(enquiryResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserEnquiryCountByStatus(String userId,String shipmentType) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getUserEnquiryCountByStatus method in UserServicesImpl*******************");
			
		long userid = Long.parseLong(userId);
				
		long count = enquiryRepository.enquiryDetailsEntityCoutByUserId(userid,shipmentType,"N");
		EnquiryCountByStatusResponse enquiryCountByStatusResponse = null;
		
		if(count == 0) {
			enquiryCountByStatusResponse = new EnquiryCountByStatusResponse(0,0,0,0);
		}else {
			enquiryCountByStatusResponse = enquiryRepository.enquiryCountByUserIdAndIsdeletedAndGroupByStatus(userid,shipmentType,"N");
			
			if (enquiryCountByStatusResponse == null) {
				throw new Exception("Error: No Data Found");
			}
		}
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryCountByStatusResponse);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserProfileDetailsById(String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getUserProfileDetailsById method in UserServicesImpl*******************");
			
		long id = Long.parseLong(userId);
		
		Optional<UserEntity> userEntity = userRepository.findById(id);
		
		if(userEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		UserDetailsResponse userDetailsResponse = new UserDetailsResponse();
		userDetailsResponse.setId(userEntity.get().getId());
		userDetailsResponse.setEmail(userEntity.get().getEmail());
		userDetailsResponse.setIsemailverify(userEntity.get().getIsemailverify());
		userDetailsResponse.setEmailverifydate(userEntity.get().getEmailverifydate());
		userDetailsResponse.setIsmobileverify(userEntity.get().getIsmobileverify());
		userDetailsResponse.setMobileverifydate(userEntity.get().getMobileverifydate());
		userDetailsResponse.setMobilenumber(userEntity.get().getMobileno());
		
		String roleCode = userEntity.get().getRole();
	
		RoleEntity roleEntity = roleRepository.findByCode(roleCode);
		
		userDetailsResponse.setRole(roleEntity.getName());		
		String status = userEntity.get().getIsactive();
		String statusCap = TitleCaseConvertsionUtils.titleCaseConversion(status);
								
		userDetailsResponse.setStatus(statusCap);
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userDetailsResponse);
		return baseResponse;
		
	}

	@Override
	public BaseResponse updateUserMyProfile(UserDetailsRequest userDetailsRequest) throws Exception {
        baseResponse = new BaseResponse();
	    
		String mobilenumber = userDetailsRequest.getMobilenumber();
		long userId = Long.parseLong(userDetailsRequest.getUserid());
		mobilenumber = mobilenumber.replaceAll("\\s", "");
		
		UserEntity userEntiCheck = userRepository.findByMobileno(mobilenumber);
		if(userEntiCheck != null) {
			throw new Exception("Error: Mobile Number Already Exists!");
		}
				
		UserEntity userEntities  = userRepository.getOne(userId);	
		if(userEntities == null) {
			throw new Exception("Error: No Data Found!");
		}

		userEntities.setMobileno(mobilenumber);
		userEntities.setIsmobileverify("N");
		userRepository.save(userEntities);
		
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "My Profile Details Updated Successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse updateMyProfileResetPassword(UserDetailsRequest userDetailsRequest) throws Exception {
        logger.info("********************resetPassword Method in UserServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		long userid = Long.parseLong(userDetailsRequest.getUserid());
		String password = userDetailsRequest.getPassword();
		String newpassword = userDetailsRequest.getNewpassword();
			
		UserEntity userEntities  = userRepository.getOne(userid);
		
		if(userEntities == null) {
			throw new Exception("Error: Invalid Token!");
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
	public BaseResponse sendVerifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception {
        logger.info("********************sendVerifyMobileOtp Method in UserServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		long userid = Long.parseLong(userDetailsRequest.getUserid());
		String email = userDetailsRequest.getEmail();
		String mobilenumber = userDetailsRequest.getMobilenumber();
	    mobilenumber = mobilenumber.replaceAll("\\s", "");
		//delete exists entries from table
		verifyAccoutRepository.deleteByUserid(userid);
		
        VerifyAccountEntity verifyAccountEntity = new VerifyAccountEntity();
        
        verifyAccountEntity.setUserid(userid);
        verifyAccountEntity.setEmail(email);
        verifyAccountEntity.setMobilenumber(mobilenumber);
        verifyAccountEntity.setCreateby("system");
        
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
		
		verifyAccoutRepository.save(verifyAccountEntity);
		
		//send OTP SMS to customer
		smsService.sendSignupOTPMobile(verifymobiletokenString,mobilenumber);
		logger.info("Please check Mobile SMS for Verification OTP!");
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Please check Mobile SMS for Verification OTP!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
		
	}

	@Override
	public BaseResponse verifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception {
        logger.info("********************verifyMobileOtp Method in UserServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
	    long userId = Long.parseLong(userDetailsRequest.getUserid());
		String mobileVerifyOtp = userDetailsRequest.getMobileverifyotp();
		
		VerifyAccountEntity verifyAccountEntity = verifyAccoutRepository.findByVerifymobiletoken(mobileVerifyOtp);
			
		if(verifyAccountEntity == null) {
			throw new Exception("Error: Invalid OTP");
		}
		
		if(userId != verifyAccountEntity.getUserid()) {
			throw new Exception("Error: Invalid OTP");
		}
		
		String mobilenumber = verifyAccountEntity.getMobilenumber();
		
		String currentTimestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			
		String mobileOtpExpiryTime = verifyAccountEntity.getExpiredmobiletoken();
		
		SimpleDateFormat sdfM = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		logger.info("check OTP expiry: "+sdf.parse(currentTimestamp).before(sdfM.parse(mobileOtpExpiryTime)));
		
		if(! sdf.parse(currentTimestamp).before(sdfM.parse(mobileOtpExpiryTime))) {
			throw new Exception("Error: Mobile OTP has Expired");
		}
		
		String verifyDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		
		UserEntity user = userRepository.getOne(userId);
		user.setMobileno(mobilenumber);
		user.setIsmobileverify("Y");
		user.setMobileverifydate(verifyDate);

		userRepository.save(user);
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Mobile Number Verify successfully!");
				
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse resendVerifyMobileOtp(UserDetailsRequest userDetailsRequest) throws Exception {
        logger.info("********************resendVerifyMobileOtp Method in UserServicesImpl******************");
		
		baseResponse = new BaseResponse();
	    
		long userid = Long.parseLong(userDetailsRequest.getUserid());
		String email = userDetailsRequest.getEmail();
		
		
        VerifyAccountEntity verifyAccEntity = verifyAccoutRepository.findByUseridAndEmail(userid,email);
        
        if(verifyAccEntity == null) {
        	throw new Exception("Error: No Data Found");
        }
        
        long id = verifyAccEntity.getId();
        String mobilenumber = verifyAccEntity.getMobilenumber();
        
        VerifyAccountEntity verifyAccountEnti = verifyAccoutRepository.getOne(id);
        
		SimpleDateFormat currentDateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		int minutesToAdd = 5;
 
		Calendar date = Calendar.getInstance();
		logger.info("Initial Time: " + currentDateTime.format(date.getTime()));
		
        Calendar startTimes = date;
        startTimes.add(date.MINUTE, minutesToAdd);
		String newDateString = currentDateTime.format(startTimes.getTime());
		
		String expiredmobiletoken = newDateString;
		int verifymobiletoken = smsService.generateRandomMobileOTP();
		String verifymobiletokenString = Integer.toString(verifymobiletoken);
		
		verifyAccountEnti.setVerifymobiletoken(verifymobiletokenString);
		verifyAccountEnti.setExpiredmobiletoken(expiredmobiletoken);
		
		verifyAccoutRepository.save(verifyAccountEnti);
		
		//send OTP SMS to customer
		smsService.sendSignupOTPMobile(verifymobiletokenString,mobilenumber);
		logger.info("Please check Mobile SMS for resend Verification OTP!");
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Please check Mobile SMS for Verification OTP!");
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse updateEnquiryStatus(String id, String userId, String enquiryStatus) throws Exception {
		
		baseResponse = new BaseResponse();
		
		long enquiryId = Long.parseLong(id);
		
		EnquiryEntity enquiryEntity = enquiryRepository.getOne(enquiryId);
		enquiryEntity.setUpdateby(userId);
		enquiryEntity.setStatus(enquiryStatus);
		enquiryRepository.save(enquiryEntity);
		
		List<EnquiryForwarderEntity> enquiryForwarderEntityList = enquiryForwarderRepository.findByEnquiryidAndIsdeleted(enquiryId,"N");
		
		if(enquiryForwarderEntityList.size() != 0) {
			for(EnquiryForwarderEntity enquiryForwarderEntity : enquiryForwarderEntityList) {
				 long forwEnqid = enquiryForwarderEntity.getId();
				 
				 EnquiryForwarderEntity enquiryForwarderEnti = enquiryForwarderRepository.getOne(forwEnqid);
				 enquiryForwarderEnti.setUpdateby(userId);
				 enquiryForwarderEnti.setStatus(enquiryStatus);
				 enquiryForwarderEnti.setIsdeleted("Y");
				 enquiryForwarderRepository.save(enquiryForwarderEnti);
				
			}
		}
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Status Updated successfully!");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse getEnquiryAcceptedList(String userId, String enquiryId) throws Exception {
        baseResponse = new BaseResponse();
	
		long enquiryid = Long.parseLong(enquiryId);
		
		EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryid, "N");
		
		String enquiryreference = enquiryEntity.getEnquiryreference();
	
		List<EnquiryForwarderEntity> enquiryForwarderEntityList = enquiryForwarderRepository.findByEnquiryidAndStatusAndIsdeleted(enquiryid,"Accepted" ,"N");
		
		if(enquiryForwarderEntityList == null) {
			throw new Exception("Error: No Data Found");
		}	
		
        if(enquiryForwarderEntityList.size() == 0) {
        	throw new Exception("Error: No Data Found");
		}
		
        List<EnquiryForwarderAcceptedResponse> enquiryForwarderAcceptedResponseList = new ArrayList<EnquiryForwarderAcceptedResponse>();
        
        for(EnquiryForwarderEntity enquiryForwarderEntity : enquiryForwarderEntityList) {
        	       	
        	EnquiryForwarderAcceptedResponse enquiryForwarderAcceptedResponse = new EnquiryForwarderAcceptedResponse();
        	enquiryForwarderAcceptedResponse.setEnquiryreference(enquiryreference);
        	     	
        	long forwarderid = enquiryForwarderEntity.getForwarderid();
        	
        	Optional<UserEntity> userEntity = userRepository.findById(forwarderid);
    		
    		Long companyId = userEntity.get().getCompanyId();
    	
    		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
    		if (! companyEntity.isPresent()  ) {
    			throw new Exception("Error: No Company Details Found");
    		}
    		
    		String companyname = companyEntity.get().getTradename();
           	      	    		    	
            TransDetailsEntity transDetailsEntity = transDetailsRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderid,enquiryreference,"N");
			
			if (transDetailsEntity == null) {				
				throw new Exception("Error: No Data Found");
			} 
			
    		List<ChargesRateEntity> chargesRateList = chargesRateRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderid,enquiryreference,"N");   
				
    		if(chargesRateList == null || chargesRateList.size() == 0) {
    			throw new Exception("Error: No Data Found");
    		}
								
		    double freightchargestotal = 0;
		    double originchargestotal = 0;
		    double destinationchargestotal = 0;
		    double otherchargestotal = 0;
		    double totalcharges = 0;	
		    double usdTotalCharges = 0;
		    double inrTotalCharges = 0;
						
			String ftchargescurrency = "";
			String orgchargescurrency = "";			
			String destchargescurrency = "";
			String otherchargescurrency = "";
						
			for(ChargesRateEntity chargeRateEnt : chargesRateList) {
					
				long chargesgroupingid = chargeRateEnt.getChargesgroupingid();
				ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
				
				String chargesgroupingcode = chargesGroupingEntity.getChargesgroupingcode();
							
				double rates = Double.parseDouble(chargeRateEnt.getRate());
				double quantity = Double.parseDouble(chargeRateEnt.getQuantity());
				String currency = chargeRateEnt.getCurrency();
				
				if(chargesgroupingcode.equals("FC")) {
					//freight charges
					freightchargestotal = freightchargestotal + (rates * quantity);
					ftchargescurrency = currency;
				}else if(chargesgroupingcode.equals("OC")) {
					//origin charges
					originchargestotal = originchargestotal + (rates * quantity);
					orgchargescurrency = currency;
				}else if(chargesgroupingcode.equals("DC")) {
					//destination charges
					destinationchargestotal = destinationchargestotal + (rates * quantity);
					destchargescurrency = currency;
				}else{
					//other charges
					otherchargestotal = otherchargestotal + (rates * quantity);
					otherchargescurrency = currency;
				}
				
				if(currency.equals("USD")) {
					usdTotalCharges = usdTotalCharges + (rates * quantity);
				}else if(currency.equals("INR")) {
					inrTotalCharges = inrTotalCharges + (rates * quantity);
				}
																 				
			}
						
        	enquiryForwarderAcceptedResponse.setForwarderid(Long.toString(forwarderid)); 
        	enquiryForwarderAcceptedResponse.setForwarder(companyname);						
			enquiryForwarderAcceptedResponse.setFreightcharges(Double.toString(freightchargestotal));
			enquiryForwarderAcceptedResponse.setFtchargescurrency(ftchargescurrency);
			enquiryForwarderAcceptedResponse.setOrigincharges(Double.toString(originchargestotal));
			enquiryForwarderAcceptedResponse.setOrgchargescurrency(orgchargescurrency);
			enquiryForwarderAcceptedResponse.setDestinationcharges(Double.toString(destinationchargestotal));
			enquiryForwarderAcceptedResponse.setDestchargescurrency(destchargescurrency);
			enquiryForwarderAcceptedResponse.setOthercharges(Double.toString(otherchargestotal));
			enquiryForwarderAcceptedResponse.setOtherchargescurrency(otherchargescurrency);
						
			totalcharges = freightchargestotal + originchargestotal + destinationchargestotal + otherchargestotal;
			
			enquiryForwarderAcceptedResponse.setTotalcharges(Double.toString(totalcharges));
			enquiryForwarderAcceptedResponse.setUsdtotalcharges(Double.toString(usdTotalCharges));
			enquiryForwarderAcceptedResponse.setInrtotalcharges(Double.toString(inrTotalCharges));
			enquiryForwarderAcceptedResponseList.add(enquiryForwarderAcceptedResponse);
        }
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryForwarderAcceptedResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse bookScheduleFromEnquiry(BookScheduleRequest bookScheduleRequest) throws Exception {
		
        baseResponse = new BaseResponse();
				
		logger.info("*****************bookScheduleFromEnquiry method in UserServicesImpl*******************");
		
		String enquiryReference = bookScheduleRequest.getEnquiryreference();
		Long forwarderId = Long.parseLong(bookScheduleRequest.getForwarderid());
		Long userId = Long.parseLong(bookScheduleRequest.getUserId());
		
		
		EnquiryEntity enquiryEnti = enquiryRepository.findByEnquiryreferenceAndIsdeleted(enquiryReference, "N");
		if(enquiryEnti == null) {
			throw new Exception("Error: No Data Found");
		}
		
		long enquiryId = enquiryEnti.getId();		
		String shipmentType = enquiryEnti.getShipmenttype();
				
        String selectedFCL = "";
        String selectedLCL = "";
        
        int twentyFtCount = 0;
		int fourtyFtCount = 0;
		int fourtyFtHcCount = 0;
		int fourtyFiveFtCount = 0;
		
		String lcltotalweight = "";
	    String lclweightunit = "";
	    String lclvolume = "";
	    String lclvolumeunit = "";
	    String lclnumberpackage = "";
	    String lclpackageunit = "";
        
        if(shipmentType.equals("FCL")) {
        	
        	twentyFtCount = enquiryEnti.getTwentyftcount();
    		fourtyFtCount = enquiryEnti.getFourtyftcount();
    		fourtyFtHcCount = enquiryEnti.getFourtyfthccount();
    		fourtyFiveFtCount = enquiryEnti.getFourtyfiveftcount();
    		HashSet<String> hSetSelectedFCL = new HashSet<String>();
		    if(twentyFtCount != 0){
		        String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
		        hSetSelectedFCL.add(twentyFtCountStr);
		    }
		    if(fourtyFtCount != 0){
		        String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
		        hSetSelectedFCL.add(fourtyFtCountStr);
		    }
		    if(fourtyFtHcCount != 0){
		        String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
		        hSetSelectedFCL.add(fourtyFtHcCountStr);
		    }
		    if(fourtyFiveFtCount != 0){
		        String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' ";	
		        hSetSelectedFCL.add(fourtyFiveFtCountStr);
		    }
		    selectedFCL = String.join(",", hSetSelectedFCL);
        }else if(shipmentType.equals("LCL")){
			lcltotalweight = enquiryEnti.getLcltotalweight();
		    lclweightunit = enquiryEnti.getLclweightunit();
		    lclvolume = enquiryEnti.getLclvolume();
		    lclvolumeunit = enquiryEnti.getLclvolumeunit();
		    lclnumberpackage = enquiryEnti.getLclnumberpackage();
		    lclpackageunit = enquiryEnti.getLclpackageunit();
		    
		    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;		    		    
		}
		
		UserEntity userEntity = userRepository.findByIdAndIsdeleted(userId,"N");
		
		if(userEntity.getCompanyId() == null) {
			HashMap<String ,String>  userInfo = new HashMap<String ,String>();
			
			userInfo.put("msg", "Error: Please fill the company details first!");
			
			baseResponse.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			baseResponse.setRespData(userInfo);
			
			return baseResponse;
			
		}
		
		Long companyId = userEntity.getCompanyId();
	
		Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
		if (companyEntity == null ) {
			throw new Exception("Error: No Company Details Found");
		}
		String companyname = companyEntity.get().getTradename();
		String companyAddress = companyEntity.get().getBuildingnumber() +", "+companyEntity.get().getFloornumber()+", "+companyEntity.get().getBuildingname()+", "+companyEntity.get().getStreet();
		String companyNameWithAdd = companyname+", "+companyAddress;
		
		TransDetailsEntity transDetailsEntity = transDetailsRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderId,enquiryReference,"N");
		
		if (transDetailsEntity == null ) {
			throw new Exception("Error: No Transportation Details Found");
		}
		
		List<ChargesRateEntity> chRateList = chargesRateRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderId,enquiryReference,"N");   
		if (chRateList == null ) {
			throw new Exception("Error: No Transportation Rate Found");
		}
		
		String chargerateids = "";		
		for(ChargesRateEntity chRate : chRateList) {
			String chargeId = chRate.getId().toString();			
			chargerateids = chargerateids.concat(chargeId+",");
		}
		if(chargerateids.endsWith(",")) {
			 int index = chargerateids.lastIndexOf(",");
			 chargerateids =  chargerateids.substring(0, index);
		}	
		 
		Long scheduleId = transDetailsEntity.getId();
		
		Long originId = transDetailsEntity.getOriginid();		
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originCityCode = originEntity.get().getCitycode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
		
		Long destinationId = transDetailsEntity.getDestinationid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();
		String destinationCityCode = destinationEntity.get().getCitycode();
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
		
		String bookingReff = bookingReffGenerator.getBookingRefferenceNumber(companyname,originCityCode,destinationCityCode);
		
		String cutoffdate = transDetailsEntity.getCutoffdate();
		
		BookingDetailsEntity bookingDetailsEntity = new BookingDetailsEntity();
		
		bookingDetailsEntity.setCreateby("system");
		bookingDetailsEntity.setEnquiryid(enquiryId);
		bookingDetailsEntity.setBookingreff(bookingReff);
		bookingDetailsEntity.setUserid(userId);		
		bookingDetailsEntity.setForwarderid(forwarderId);
		bookingDetailsEntity.setScheduleid(scheduleId);				
		bookingDetailsEntity.setChargerateids(chargerateids);
		bookingDetailsEntity.setShipmenttype(shipmentType);
	
		String bookingdate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date()); 
		bookingDetailsEntity.setBookingdate(bookingdate);
		bookingDetailsEntity.setBookingupdateddate(bookingdate);
		bookingDetailsEntity.setBookingstatus(StatusUtils.REQUESTED);
		bookingDetailsEntity.setCustomerisdeleted("N");
		bookingDetailsEntity.setForwarderisdeleted("N");
		bookingDetailsEntity.setIsdeleted("N");
		bookingDetailsRepository.save(bookingDetailsEntity);
			
		//charge rate calculation part
		logger.info("going to calculate charge rates");
		String chargeRateId [] = chargerateids.split(",");
		
			
		List<ChargesRateResponse> chargesRateResponseList = new  ArrayList<ChargesRateResponse>();
		List<ChargesGroupingCurrencyResponse> chargesGroupingCurrencyResponseList = new ArrayList<ChargesGroupingCurrencyResponse>();
		List<ChargesGroupingCurrencyResponse> totalChargesList = new ArrayList<ChargesGroupingCurrencyResponse>();
		
		double tamountusd = 0;
		double tamountinr = 0;
		
		for(int i=0; i<chargeRateId.length;i++) {
			Long chargeId = Long.parseLong(chargeRateId[i]);
											
			ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
						
			ChargesRateEntity chargesRateEnti = chargesRateRepository.findByIdAndIsdeleted(chargeId,"N");
			
			long chargesgroupingid = chargesRateEnti.getChargesgroupingid();
			ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
			String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
			chargesRateResponse.setChargesgrouping(chargesgrouping);
			
			long chargetypeid = chargesRateEnti.getChargetypeid();
			ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");
			String chargestype = chargesTypeEntity.getChargecodedescription();
			chargesRateResponse.setChargestype(chargestype);
			
			String currency	= chargesRateEnti.getCurrency();
			chargesRateResponse.setCurrency(currency);
			
			String basiscode = chargesRateEnti.getBasis();
			ChargeBasisEntity chargeBasisEntity = chargeBasisRepository.findByBasiscodeAndIsdeleted(basiscode,"N");						
			chargesRateResponse.setBasis(chargeBasisEntity.getBasis());
			
			chargesRateResponse.setQuantity(chargesRateEnti.getQuantity());
			
		    chargesRateResponse.setRate(chargesRateEnti.getRate());
		    double rate = Double.parseDouble(chargesRateEnti.getRate());
		    float quntity = Float.parseFloat(chargesRateEnti.getQuantity());
		    double tAmount = rate * quntity;
		    double value = Double.parseDouble(new DecimalFormat("##.##").format(tAmount));
		    String totalamount = Double.toString(value);
		    chargesRateResponse.setTotalamount(totalamount);
		    chargesRateResponseList.add(chargesRateResponse);
		    
		    double totalChargesUsd = 0;
            double totalChargesInr = 0;
            
        	if(currency.equals("USD")){
        		totalChargesUsd =  value;
        		tamountusd = tamountusd + value;
            }else if(currency.equals("INR")){
            	totalChargesInr =  value;
            	tamountinr = tamountinr + value;
	        }
        	
        	final double tChargeUsd = totalChargesUsd;
            final double tChargeInr = totalChargesInr;
            
		    if (chargesGroupingCurrencyResponseList.stream().anyMatch(o -> o.getGroup().equals(chargesgrouping))) {
		    	totalChargesList = chargesGroupingCurrencyResponseList.stream() 
		    			.filter(n -> n.getGroup().equals(chargesgrouping))
		    			.map(n -> 	new	ChargesGroupingCurrencyResponse 
		    					(chargesgrouping,Double.toString(Double.parseDouble(n.getUsd()) + tChargeUsd),Double.toString(Double.parseDouble(n.getInr()) + tChargeInr))  				 	    		           		    		         
		    			).collect(Collectors.toList());		    		    	
          		       chargesGroupingCurrencyResponseList = totalChargesList;
          	  }else{
          		
          		  ChargesGroupingCurrencyResponse chargesGroupingCurrencyResponse = new ChargesGroupingCurrencyResponse();
          		  chargesGroupingCurrencyResponse.setGroup(chargesgrouping);
          		  chargesGroupingCurrencyResponse.setUsd(Double.toString(totalChargesUsd));
          		  chargesGroupingCurrencyResponse.setInr(Double.toString(totalChargesInr));
          		  chargesGroupingCurrencyResponseList.add(chargesGroupingCurrencyResponse); 	            	  			  	            	    		  	            	    		  	            	    
	          }
		    					    
		}
		
		// To generate PDF
		
		BookScheduleResponse bookScheduleResponse = new BookScheduleResponse();
		bookScheduleResponse.setBookingreff(bookingReff);
		bookScheduleResponse.setBookedby(userEntity.getEmail());
		
		bookScheduleResponse.setDateofbooking(bookingdate);
		bookScheduleResponse.setBookingstatus(bookingDetailsEntity.getBookingstatus());
		bookScheduleResponse.setCutoffdate(cutoffdate);
		
		bookScheduleResponse.setBookingparty(companyNameWithAdd);
		bookScheduleResponse.setShipmenttype(shipmentType);
		bookScheduleResponse.setFclselected(selectedFCL);
		bookScheduleResponse.setLclselected(selectedLCL);
		
		bookScheduleResponse.setBookingdate(bookingdate);
		bookScheduleResponse.setBookingupdateddate(bookingdate);
		
		bookScheduleResponse.setOrigin(originWithCode);
		bookScheduleResponse.setOrigincode(origincode);
		bookScheduleResponse.setDestination(destinationWithCode);
		bookScheduleResponse.setDestinationcode(destinationcode);
		
        List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(scheduleId,"N");
		
		if(scheduleLegsEntityList == null) {
			throw new Exception("Error: No Schedule Legs Found");
		}

		List<ScheduleLegsResponse> scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
				
		for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
			ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
			
			scheduleLegsResponse.setOriginid(Long.toString(scheduleLegsEntity.getOriginid()));
			
			Optional<LocationEntity> originEnti=  locationRepository.findById(scheduleLegsEntity.getOriginid());			
			String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
			scheduleLegsResponse.setOrigin(orgWithCode);
			
			scheduleLegsResponse.setDestinationid(scheduleLegsEntity.getDestinationid());
			Optional<LocationEntity> destEntity =  locationRepository.findById(scheduleLegsEntity.getDestinationid());			
			String destWithCode = destEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
			scheduleLegsResponse.setDestination(destWithCode);
			
			scheduleLegsResponse.setMode(scheduleLegsEntity.getMode());
			String mode = scheduleLegsEntity.getMode();
			if(mode.equals("Sea")) {
				long carriId = scheduleLegsEntity.getCarrierid();
				Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
				String carriName = carriEntity.get().getCarriershortname();
				scheduleLegsResponse.setCarrier(carriName);
			}else {
				String carriName = scheduleLegsEntity.getCarrieroption();
				scheduleLegsResponse.setCarrier(carriName);
			}
					
			scheduleLegsResponse.setVessel(scheduleLegsEntity.getVessel());
			scheduleLegsResponse.setVoyage(scheduleLegsEntity.getVoyage());
			
			scheduleLegsResponse.setEtddate(scheduleLegsEntity.getEtddate());
			scheduleLegsResponse.setEtadate(scheduleLegsEntity.getEtadate());
			scheduleLegsResponse.setTransittime(scheduleLegsEntity.getTransittime());
			
			scheduleLegsResponseList.add(scheduleLegsResponse);						
		}
		
		bookScheduleResponse.setScheduleLegsResponse(scheduleLegsResponseList);
		bookScheduleResponse.setChargesRateResponseList(chargesRateResponseList);
		bookScheduleResponse.setChargesGroupingCurrencyResponseList(chargesGroupingCurrencyResponseList);
		bookScheduleResponse.setTotalamountusd(Double.toString(tamountusd));
		bookScheduleResponse.setTotalamountinr(Double.toString(tamountinr));
		
		bookScheduleResponse.setTwentyFtCount(twentyFtCount);
		bookScheduleResponse.setFourtyFtCount(fourtyFtCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFtHcCount);
		bookScheduleResponse.setFourtyFtHcCount(fourtyFiveFtCount);
		
		bookScheduleResponse.setLcltotalweight(lcltotalweight);
		bookScheduleResponse.setLclweightunit(lclweightunit);
		bookScheduleResponse.setLclvolume(lclvolume);
		bookScheduleResponse.setLclvolumeunit(lclvolumeunit);
		bookScheduleResponse.setLclnumberpackage(lclnumberpackage);
		bookScheduleResponse.setLclpackageunit(lclpackageunit);
		
		logger.info("bookScheduleResponse:: "+bookScheduleResponse);
		
		
		String filename = pdfService.generatePdf(bookScheduleResponse);
		
		logger.info("PDF file generated :: "+filename);
		
		//booking successful mail to customer with attachment 		
		Mail mailExceed = new Mail();
		mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
		mailExceed.setTo(userEntity.getEmail());
		mailExceed.setSubject("Book My Cargo: "+shipmentType+" Booking Reference Number <"+bookingReff+"> submitted");
					
		try {
				emailService.sendBookingSuccessMail(mailExceed,bookScheduleResponse ,filename);
		}catch(Exception e) {
				e.printStackTrace();
		}
		
		logger.info("Email Send Successfully with attachment :: "+filename);
						
		EnquiryEntity enquiryEntity = enquiryRepository.getOne(enquiryId);
		enquiryEntity.setUpdateby(bookScheduleRequest.getUserId());
		enquiryEntity.setStatus("Booked");
		enquiryEntity.setIsdeleted("Y");
		enquiryRepository.save(enquiryEntity);		
				
		List<EnquiryForwarderEntity> enquiryForwarderEntityList = enquiryForwarderRepository.findByEnquiryidAndIsdeleted(enquiryId, "N");
		for(EnquiryForwarderEntity enquiryForwarderEntity : enquiryForwarderEntityList) {
			long forwEnquiryId = enquiryForwarderEntity.getId();
			long forwId = enquiryForwarderEntity.getForwarderid();
			
			EnquiryForwarderEntity enquiryForwarderEnti = enquiryForwarderRepository.getOne(forwEnquiryId);
			
			if(forwarderId == forwId) {
				enquiryForwarderEnti.setUpdateby(bookScheduleRequest.getUserId());
				enquiryForwarderEnti.setStatus("Booked");	
				enquiryForwarderEnti.setIsdeleted("Y");
				enquiryForwarderRepository.save(enquiryForwarderEnti);
			}else {
				
				enquiryForwarderEnti.setUpdateby(bookScheduleRequest.getUserId());
				enquiryForwarderEnti.setStatus("Rejected");			
				enquiryForwarderRepository.save(enquiryForwarderEnti);
			}
			
		}
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		
		userInfo.put("msg", "Booking submitted successfully!");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	
	}

	/*
	@Override
	public BaseResponse getEnquiryScheduleChargesDetailsByEnquiryReff(String enquiryReff,String forwarderid) throws Exception {
		 baseResponse = new BaseResponse();
			
			logger.info("*****************getEnquiryScheduleChargesDetailsByEnquiryReff method in UserServicesImpl*******************");
			
			EnquiryEntity enquiryEntity = enquiryRepository.findByEnquiryreferenceAndIsdeleted(enquiryReff, "N");
			if (enquiryEntity == null) {
				throw new Exception("Error: No Data Found");
			}
			String enquiryreference = enquiryEntity.getEnquiryreference();
			String shipmentType = enquiryEntity.getShipmenttype();
			
			long forwId = Long.parseLong(forwarderid);
			long originlocid = enquiryEntity.getOriginlocid();
			long destinationlocid = enquiryEntity.getDestinationlocid();
			
			String selectedFCL = "";
            String selectedLCL = "";
	        
			if(shipmentType.equals("FCL")) {
				
				int twentyFtCount = enquiryEntity.getTwentyftcount();
				int fourtyFtCount = enquiryEntity.getFourtyftcount();
				int fourtyFtHcCount = enquiryEntity.getFourtyfthccount();
				int fourtyFiveFtCount = enquiryEntity.getFourtyfiveftcount();
				HashSet<String> hSetSelectedFCL = new HashSet<String>();
				if(twentyFtCount != 0){
				    String twentyFtCountStr = Integer.toString(twentyFtCount) + " x 20' ";
				    hSetSelectedFCL.add(twentyFtCountStr);
				}
				if(fourtyFtCount != 0){
				    String fourtyFtCountStr = Integer.toString(fourtyFtCount) + " x 40' ";
				    hSetSelectedFCL.add(fourtyFtCountStr);
				}
				if(fourtyFtHcCount != 0){
				    String fourtyFtHcCountStr = Integer.toString(fourtyFtHcCount) + " x 40' HC ";
				    hSetSelectedFCL.add(fourtyFtHcCountStr);
				}
				if(fourtyFiveFtCount != 0){
				    String fourtyFiveFtCountStr = Integer.toString(fourtyFiveFtCount) + " x 45' ";	
				    hSetSelectedFCL.add(fourtyFiveFtCountStr);
				}
				selectedFCL = String.join(",", hSetSelectedFCL);
			}else if(shipmentType.equals("LCL")) {
				String lcltotalweight = enquiryEntity.getLcltotalweight();
			    String lclweightunit = enquiryEntity.getLclweightunit();
				String lclvolume = enquiryEntity.getLclvolume();
			    String lclvolumeunit = enquiryEntity.getLclvolumeunit();
			    String lclnumberpackage = enquiryEntity.getLclnumberpackage();
			    String lclpackageunit = enquiryEntity.getLclpackageunit();
			    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
			}
			
		    EnquiryScheduleChargesDetailsResponse enqScheduleChargesDetailsResponse = new EnquiryScheduleChargesDetailsResponse();
			
		    enqScheduleChargesDetailsResponse.setFclselected(selectedFCL);
		    enqScheduleChargesDetailsResponse.setLclselected(selectedLCL);
		    
		    enqScheduleChargesDetailsResponse.setId(enquiryEntity.getId());
		    enqScheduleChargesDetailsResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
		    
		    Optional<LocationEntity> originEnti=  locationRepository.findById(originlocid);
			String orgCityCode = originEnti.get().getCitycode();
			String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
		   
			enqScheduleChargesDetailsResponse.setOrigin(orgWithCode);
			enqScheduleChargesDetailsResponse.setOrigincode(orgCityCode);
			
			Optional<LocationEntity> destEnti =  locationRepository.findById(destinationlocid);
			String destCityCode = destEnti.get().getCitycode();
			String destWithCode = destEnti.get().getLocationname()+" ("+destEnti.get().getLocationcode()+")";
			
			enqScheduleChargesDetailsResponse.setDestination(destWithCode);
			enqScheduleChargesDetailsResponse.setDestinationcode(destCityCode);
			
			enqScheduleChargesDetailsResponse.setCargocategory(enquiryEntity.getCargocategory());
			enqScheduleChargesDetailsResponse.setShipmenttype(enquiryEntity.getShipmenttype());
			enqScheduleChargesDetailsResponse.setStatus(enquiryEntity.getStatus());
						
			TransDetailsEntity transDetailsEntity = transDetailsRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwId,enquiryreference,"N");
			
			if (transDetailsEntity == null) {				
				List<ScheduleLegsResponse> scheduleLegsResponseList = null;
				List<ChargesRateResponse> chargesRateResponseList = null;
				enqScheduleChargesDetailsResponse.setScheduleLegsResponseList(scheduleLegsResponseList);
				enqScheduleChargesDetailsResponse.setChargesRateResponseList(chargesRateResponseList);
				baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
				baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				baseResponse.setRespData(enqScheduleChargesDetailsResponse);
				return baseResponse;
			}
			
			long transId = transDetailsEntity.getId();
			enqScheduleChargesDetailsResponse.setScheduletype(transDetailsEntity.getScheduletype());
			enqScheduleChargesDetailsResponse.setNumberoflegs(transDetailsEntity.getNumberoflegs());
			
			List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transId,"N");
			
			if(scheduleLegsEntityList == null) {
				throw new Exception("Error: No Schedule Legs Found");
			}

			List<ScheduleLegsResponse> scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
			
			for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
				ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
				
				scheduleLegsResponse.setOriginid(Long.toString(scheduleLegsEntity.getOriginid()));				
				Optional<LocationEntity> originEntity =  locationRepository.findById(scheduleLegsEntity.getOriginid());
				String legsorgWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
				scheduleLegsResponse.setOrigin(legsorgWithCode);
				
				scheduleLegsResponse.setDestinationid(scheduleLegsEntity.getDestinationid());
				Optional<LocationEntity> destEntity =  locationRepository.findById(scheduleLegsEntity.getDestinationid());
				String legsdestWithCode = destEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
				scheduleLegsResponse.setDestination(legsdestWithCode);
				
				scheduleLegsResponse.setMode(scheduleLegsEntity.getMode());
				String mode = scheduleLegsEntity.getMode();								
				if(mode.equals("Sea")) {
					long carriId = scheduleLegsEntity.getCarrierid();
					Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
					String carriName = carriEntity.get().getCarriershortname();
					scheduleLegsResponse.setCarrier(carriName);
				}else {
					String carriName = scheduleLegsEntity.getCarrieroption();
					scheduleLegsResponse.setCarrier(carriName);
				}
				
				scheduleLegsResponse.setVessel(scheduleLegsEntity.getVessel());
				scheduleLegsResponse.setVoyage(scheduleLegsEntity.getVoyage());
				
				scheduleLegsResponse.setEtddate(scheduleLegsEntity.getEtddate());
				scheduleLegsResponse.setEtadate(scheduleLegsEntity.getEtadate());
				scheduleLegsResponse.setTransittime(scheduleLegsEntity.getTransittime());
				
				scheduleLegsResponseList.add(scheduleLegsResponse);						
			}
				
			enqScheduleChargesDetailsResponse.setScheduleLegsResponseList(scheduleLegsResponseList);
		
			List<ChargesRateEntity> chargesRateList = chargesRateRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwId,enquiryreference,"N");
					
		    List<ChargesRateResponse> chargesRateResponseList = null;
		    
		    long incotermId = 0;
		    
			if(chargesRateList != null && chargesRateList.size() != 0) {
				
				chargesRateResponseList = new  ArrayList<ChargesRateResponse>();			

				String chargerateids = ""; 
				   
				for(ChargesRateEntity chargeRateEnt : chargesRateList) {
					
					ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
					
					long chargesgroupingid = chargeRateEnt.getChargesgroupingid();
					ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
					String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
					chargesRateResponse.setChargesgrouping(chargesgrouping);
					
					long chargetypeid = chargeRateEnt.getChargetypeid();
					ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");					
					String chargestype = chargesTypeEntity.getChargecodedescription();
					chargesRateResponse.setChargestype(chargestype);
					
					chargesRateResponse.setCurrency(chargeRateEnt.getCurrency());
					
					String basiscode = chargeRateEnt.getBasis();
					ChargeBasisEntity chargeBasisEntity = chargeBasisRepository.findByBasiscodeAndIsdeleted(basiscode,"N");						
					chargesRateResponse.setBasis(chargeBasisEntity.getBasis());
					
					chargesRateResponse.setQuantity(chargeRateEnt.getQuantity());
																
					String chargeId = chargeRateEnt.getId().toString();					
					chargerateids = chargerateids.concat(chargeId+",");

				    chargesRateResponse.setRate(chargeRateEnt.getRate());
				    chargesRateResponseList.add(chargesRateResponse);
				}//end for loop
								
				if(chargerateids.endsWith(",")) {
					 int index = chargerateids.lastIndexOf(",");
					 chargerateids =  chargerateids.substring(0, index);
				}				   
				enqScheduleChargesDetailsResponse.setChargerateids(chargerateids);
				enqScheduleChargesDetailsResponse.setChargesRateResponseList(chargesRateResponseList);
				
			}else {
				enqScheduleChargesDetailsResponse.setChargesRateResponseList(chargesRateResponseList);
			}
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(enqScheduleChargesDetailsResponse);
			return baseResponse;
	}
*/
	
	@Override
	public BaseResponse getForwarderEnquiryStatusByReference(String userId, String enquiryReference) throws Exception {
		
		baseResponse = new BaseResponse();
		
		List<EnquiryForwarderEntity> enquiryForwarderEntityList = enquiryForwarderRepository.findByEnquiryreference(enquiryReference);
		
		if(enquiryForwarderEntityList == null) {
			throw new Exception("Error: No Data Found");
		}	
		
        if(enquiryForwarderEntityList.size() == 0) {
        	throw new Exception("Error: No Data Found");
		}
        
        List<EnquiryForwarderResponse> enquiryForwarderResponseList = new ArrayList<EnquiryForwarderResponse>();
        
        for(EnquiryForwarderEntity enquiryForwarderEntity : enquiryForwarderEntityList) {
        	
        	EnquiryForwarderResponse enquiryForwarderResponse = new EnquiryForwarderResponse();
        	
        	enquiryForwarderResponse.setId(enquiryForwarderEntity.getId());
        	enquiryForwarderResponse.setEnquiryreference(enquiryForwarderEntity.getEnquiryreference());
        	
        	long forwarderId = enquiryForwarderEntity.getForwarderid();
        	UserEntity userEntity = userRepository.findByIdAndIsdeleted(forwarderId,"N");
        	long compId = userEntity.getCompanyId();
        	CompanyEntity compEntity = companyRepository.findByIdAndIsdeleted(compId,"N");	
            String forwarder = compEntity.getTradename();            
        	enquiryForwarderResponse.setForwarder(forwarder);   
        	String status = enquiryForwarderEntity.getStatus();
        	enquiryForwarderResponse.setStatus(status);
        	
        	String updatedDate = "";
        	if(status.equals("Requested")) {
        		updatedDate = new SimpleDateFormat("dd-MMM-yyyy").format(enquiryForwarderEntity.getCreatedate());
        	}else {
        		if(enquiryForwarderEntity.getUpdatedate() != null) {
        			updatedDate = new SimpleDateFormat("dd-MMM-yyyy").format(enquiryForwarderEntity.getUpdatedate());
        		}
        	}
        	enquiryForwarderResponse.setUpdateddate(updatedDate);
        	
        	enquiryForwarderResponseList.add(enquiryForwarderResponse);
        }
        
        baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryForwarderResponseList);
		return baseResponse;
		
	}

	@Override
	public BaseResponse deleteUserEnquiryById(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		long enquiryId = Long.parseLong(id);
				
		EnquiryEntity enquiryEntity = enquiryRepository.getOne(enquiryId);
		enquiryEntity.setUpdateby(userId);
		enquiryEntity.setStatus("Deleted");
		enquiryEntity.setIsdeleted("Y");
		enquiryRepository.save(enquiryEntity);
				
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Enquiry deleted successfully!");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteUserBookingById(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		long bookingId = Long.parseLong(id);
		
        BookingDetailsEntity bookingDetailsEntity = bookingDetailsRepository.getOne(bookingId);
        bookingDetailsEntity.setUpdateby(userId);
        bookingDetailsEntity.setCustomerisdeleted("Y");	
		bookingDetailsRepository.save(bookingDetailsEntity);
		
				
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Booking deleted successfully!");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse getUserEnquiryDetailsById(String enquiryId) throws Exception {
        baseResponse = new BaseResponse();
		
		long enqId = Long.parseLong(enquiryId);
		
		logger.info("*****************getUserEnquiryDetailsById method in UserServicesImpl*******************");		
		
		EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enqId,"N");
		
		if (enquiryEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		String shipmentType = enquiryEntity.getShipmenttype();
		
		SearchEnquiryResponse searchEnquiryResponse = new SearchEnquiryResponse();
				
        if(shipmentType.equals("FCL")) {
            	
            int twentyFtCount = enquiryEntity.getTwentyftcount();
    		int fourtyFtCount = enquiryEntity.getFourtyftcount();
    		int fourtyFtHcCount = enquiryEntity.getFourtyfthccount();
    		int fourtyFiveFtCount = enquiryEntity.getFourtyfiveftcount();
    		
    		searchEnquiryResponse.setTwentyFtCount(twentyFtCount);
    		searchEnquiryResponse.setFourtyFtCount(fourtyFtCount);
    		searchEnquiryResponse.setFourtyFtHcCount(fourtyFtHcCount);
    		searchEnquiryResponse.setFourtyFiveFtCount(fourtyFiveFtCount);
    		
		}else if(shipmentType.equals("LCL")){
			String lcltotalweight = enquiryEntity.getLcltotalweight();
		    String lclweightunit = enquiryEntity.getLclweightunit();
		    String lclvolume = enquiryEntity.getLclvolume();
		    String lclvolumeunit = enquiryEntity.getLclvolumeunit();
		    String lclnumberpackage = enquiryEntity.getLclnumberpackage();
		    String lclpackageunit = enquiryEntity.getLclpackageunit();
			    
		    searchEnquiryResponse.setLcltotalweight(lcltotalweight);
		    searchEnquiryResponse.setLclweightunit(lclweightunit);
		    searchEnquiryResponse.setLclvolume(lclvolume);
		    searchEnquiryResponse.setLclvolumeunit(lclvolumeunit);
		    searchEnquiryResponse.setLclnumberpackage(lclnumberpackage);
		    searchEnquiryResponse.setLclpackageunit(lclpackageunit);
			    
		}
					
        searchEnquiryResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
			
		Long originId = enquiryEntity.getOriginlocid();					
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);		
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
			
		searchEnquiryResponse.setOrigin(originWithCode);
			
		Long destinationId = enquiryEntity.getDestinationlocid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);		
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
			
		searchEnquiryResponse.setDestination(destinationWithCode);
			
		searchEnquiryResponse.setCargoreadydate(enquiryEntity.getCargoreadydate());
		searchEnquiryResponse.setCargocategory(enquiryEntity.getCargocategory());
		searchEnquiryResponse.setShipmenttype(enquiryEntity.getShipmenttype());	
		searchEnquiryResponse.setCommodity(enquiryEntity.getCommodity());
		searchEnquiryResponse.setImco(enquiryEntity.getImco());
		searchEnquiryResponse.setTemprange(enquiryEntity.getTemprange());
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(searchEnquiryResponse);
		return baseResponse;
		
	}

}
