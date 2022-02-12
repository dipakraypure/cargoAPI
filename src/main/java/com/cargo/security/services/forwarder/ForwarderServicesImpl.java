package com.cargo.security.services.forwarder;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.load.request.AddChargesTypeRequest;
import com.cargo.load.request.AddScheduleChargeRequest;
import com.cargo.load.request.AddScheduleLegsRequest;
import com.cargo.load.request.AddScheduleRequest;
import com.cargo.load.request.ConfigureAlertRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.BookScheduleResponse;
import com.cargo.load.response.BookingCountByStatusResponse;
import com.cargo.load.response.ChargesGroupingCurrencyResponse;
import com.cargo.load.response.ChargesRateResponse;
import com.cargo.load.response.ConfiguredLocationResponse;
import com.cargo.load.response.EnquiryCountByStatusResponse;
import com.cargo.load.response.EnquiryResponse;
import com.cargo.load.response.EnquiryScheduleChargesDetailsResponse;
import com.cargo.load.response.EnquiryScheduleChargeResponse;
import com.cargo.load.response.LocationResponse;
import com.cargo.load.response.ScheduleLegsResponse;
import com.cargo.load.response.TransDetailsResponse;
import com.cargo.load.response.UserBookScheduleResponse;
import com.cargo.mail.EmailService;
import com.cargo.mail.Mail;
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
import com.cargo.models.IncotermEntity;
import com.cargo.models.LocationEntity;
import com.cargo.models.ScheduleLegsEntity;
import com.cargo.models.TransDetailsEntity;
import com.cargo.models.UserEntity;
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
import com.cargo.repository.IncotermRepository;
import com.cargo.repository.LocationRepository;
import com.cargo.repository.ScheduleLegsRepository;
import com.cargo.repository.TransDetailsRepository;
import com.cargo.repository.UserRepository;
import com.cargo.template.response.ForwarderQuotationResponse;
import com.cargo.utils.FileStorageUtility;
import com.cargo.utils.StringsUtils;
import com.cargo.utils.TitleCaseConvertsionUtils;

@Service
public class ForwarderServicesImpl implements ForwarderServices{

	private static final Logger logger = LoggerFactory.getLogger(ForwarderServicesImpl.class);
	
	BaseResponse baseResponse	= null;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired 
	FileStorageUtility fileStorageUtility;
	
	@Autowired
	BookingDetailsRepository bookingDetailsRepository;
	
	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	ChargeGroupingRepository chargeGroupingRepository;
	
	@Autowired
	ChargesRateRepository chargesRateRepository;
	
	@Autowired
	ChargesTypeRepository chargesTypeRepository;
	
	@Autowired
	TransDetailsRepository transDetailsRepository;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	EnquiryRepository enquiryRepository;
	
	@Autowired
	EnquiryForwarderRepository enquiryForwarderRepository;
	
	@Autowired
	ScheduleLegsRepository scheduleLegsRepository;
	
	@Autowired
	IncotermRepository incotermRepository;
	
	@Autowired
	ConfigureAlertRepository configureAlertRepository;
	
	@Autowired
	PdfService pdfService;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ChargeBasisRepository chargeBasisRepository;
	
	@Override
	public BaseResponse getForwarderBookingCountByStatus(String userId,String shipmentType)throws Exception {
		
			 baseResponse = new BaseResponse();
				
			logger.info("*****************getForwarderBookingCountByStatus method in ForwarderServicesImpl*******************");
				
			long userid = Long.parseLong(userId);
			
			
			long count = bookingDetailsRepository.bookingDetailsEntityCoutByForwarderId(userid,shipmentType,"N");
			BookingCountByStatusResponse bookingCountByStatusResponse = null;
			
			if(count == 0) {
				bookingCountByStatusResponse = new BookingCountByStatusResponse(0,0,0,0);
			}else {
				bookingCountByStatusResponse = bookingDetailsRepository.bookingCountByForwarderIdAndForwarderisdeletedAndGroupByStatus(userid,shipmentType,"N");
				
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
	public BaseResponse getForwarderBookingList(String userId,String shipmentType,String bookingStatus) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderBookingList method in ForwarderServicesImpl*******************");
	
		long userid = Long.parseLong(userId);
		
		List<BookingDetailsEntity> bookingDetailsEntityList = bookingDetailsRepository.findByForwarderidAndStatusLikeAndForwarderisdeleted(userid,shipmentType,bookingStatus+"%","N");
		
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
			
			userBookScheduleResponse.setBookingdate(bookingDetailsEntity.getBookingdate());
			userBookScheduleResponse.setBookingstatus(bookingDetailsEntity.getBookingstatus());
			
			long scheduleid = bookingDetailsEntity.getScheduleid();			
			TransDetailsEntity transDetailsEntity = transDetailsRepository.findByIdAndIsdeleted(scheduleid,"N");
			
			
			long userIdForw = bookingDetailsEntity.getUserid();
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
        
		UserEntity userEntity = userRepository.findByIdAndIsdeleted(userIdLong,"N");
		
		Long companyId = userEntity.getCompanyId();
	
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
		    double value =Double.parseDouble(new DecimalFormat("##.##").format(tAmount));
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
	public BaseResponse getForwarderBookingDetailsById(String bookingReff) throws Exception {
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
		    enquiryEntity.get().getLclnumberpackage();
		    enquiryEntity.get().getLclpackageunit();
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
	public BaseResponse getForwarderEnquiryCountByStatus(String userId,String shipmentType) throws Exception {
		baseResponse = new BaseResponse();
		
		logger.info("*****************getUserEnquiryCountByStatus method in UserServicesImpl*******************");
			
		long userid = Long.parseLong(userId);
				
		long count = enquiryForwarderRepository.enquiryDetailsEntityCoutByForwarderId(userid,shipmentType,"N");
		EnquiryCountByStatusResponse enquiryCountByStatusResponse = null;
		
		if(count == 0) {
			enquiryCountByStatusResponse = new EnquiryCountByStatusResponse(0,0,0,0);
		}else {
			enquiryCountByStatusResponse = enquiryForwarderRepository.enquiryCountByForwarderIdAndIsdeletedAndGroupByStatus(userid,shipmentType, "N");
			
			if(enquiryCountByStatusResponse == null) {
				throw new Exception("Error: No Data Found");
			}
		}
			
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryCountByStatusResponse);
		return baseResponse;
		
	}
	
	@Override
	public BaseResponse getForwarderEnquiryList(String userId,String shipmentType, String enquiryStatus) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderEnquiryList method in ForwarderServicesImpl*******************");
	
		long userid = Long.parseLong(userId);
		
		List<EnquiryForwarderEntity> enquiryForwarderEntityList = enquiryForwarderRepository.findByForwarderidAndStatusLikeAndIsdeleted(userid,shipmentType,enquiryStatus+"%","N");

		if (enquiryForwarderEntityList == null) {
			throw new Exception("Error: No Data Found");
		}
		
		if (enquiryForwarderEntityList.size() == 0) {
			throw new Exception("Error: No Data Found");
		}
	
		List<EnquiryResponse> enquiryResponseList  = new ArrayList<EnquiryResponse>();
		
		for (EnquiryForwarderEntity enquiryForwarderEntity : enquiryForwarderEntityList) {
			EnquiryResponse enquiryResponse = new EnquiryResponse();
			
			enquiryResponse.setId(enquiryForwarderEntity.getId());
			
			long enquiryid = enquiryForwarderEntity.getEnquiryid();
			
			EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryid,"N");
			 
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
			}else if(shipmentType.equals("LCL")){
				String lcltotalweight = enquiryEntity.getLcltotalweight();
			    String lclweightunit = enquiryEntity.getLclweightunit();
			    String lclvolume = enquiryEntity.getLclvolume();
			    String lclvolumeunit = enquiryEntity.getLclvolumeunit();
			    String lclnumberpackage = enquiryEntity.getLclnumberpackage();
			    String lclpackageunit = enquiryEntity.getLclpackageunit();
			    
			    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
			    
			    enquiryResponse.setLcltotalweight(lcltotalweight);
			    enquiryResponse.setLclweightunit(lclweightunit);
			    enquiryResponse.setLclvolume(lclvolume);
			    enquiryResponse.setLclvolumeunit(lclvolumeunit);
			    enquiryResponse.setLclnumberpackage(lclnumberpackage);
			    enquiryResponse.setLclpackageunit(lclpackageunit);
			    
			}
						
			enquiryResponse.setSelectedfcl(selectedFCL);
			enquiryResponse.setSelectedlcl(selectedLCL);
			enquiryResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
			
			Long originId = enquiryForwarderEntity.getOriginlocid();					
			Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
			String origincode = originEntity.get().getLocationcode();			
			String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
			
			enquiryResponse.setOrigin(originWithCode);
			enquiryResponse.setOrigincode(origincode);
			
			Long destinationId = enquiryForwarderEntity.getDestinationlocid();
			Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
			String destinationcode = destinationEntity.get().getLocationcode();			
			String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
			
			enquiryResponse.setDestination(destinationWithCode);
			enquiryResponse.setDestinationcode(destinationcode);
			
			enquiryResponse.setCargoreadydate(enquiryForwarderEntity.getCargoreadydate());
			enquiryResponse.setCargocategory(enquiryEntity.getCargocategory());
			enquiryResponse.setShipmenttype(enquiryEntity.getShipmenttype());
			enquiryResponse.setSearchdate(enquiryEntity.getSearchdate());
			enquiryResponse.setStatus(enquiryForwarderEntity.getStatus());
			enquiryResponse.setIsscheduleupload(enquiryForwarderEntity.getIsscheduleupload());
			enquiryResponse.setIschargesupload(enquiryForwarderEntity.getIschargesupload());
			
			long eqUserId = enquiryEntity.getUserid();
			
			Optional<UserEntity> userEntiForw = userRepository.findById(eqUserId);
			
            long compId = userEntiForw.get().getCompanyId();
			
            Optional<CompanyEntity> compEntity = companyRepository.findById(compId);
			
            String customer = compEntity.get().getTradename();
			
            enquiryResponse.setCustomer(customer);
            
			enquiryResponseList.add(enquiryResponse);
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryResponseList);
		return baseResponse;
	}

	@Override
	public BaseResponse addSchedule(AddScheduleRequest addScheduleRequest, String userid) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************addSchedule method in ForwarderServicesImpl*******************");
	
		
		if(addScheduleRequest == null) {
			throw new Exception("Error: No Request Data Found");
		}
		
		String enquiryReference = addScheduleRequest.getEnquiryreference();
		long enquiryId = Long.parseLong(addScheduleRequest.getEnquiryid());
		long userId = Long.parseLong(userid);
		String origin = addScheduleRequest.getOrigin();
		String destination = addScheduleRequest.getDestination();
		String scheduletype = addScheduleRequest.getScheduletype();
	    String legscount = addScheduleRequest.getLegscount();
	    String cutoffdate = addScheduleRequest.getCutoffdate();
		
	   
		if(!origin.contains("(") || !origin.contains(")")){
			throw new Exception("Error: No Matching Data Found");
		}
		
		if(!destination.contains("(") || !destination.contains(")")) {
			throw new Exception("Error: No Matching Data Found");
		}
		
		List<AddScheduleLegsRequest> addScheduleLegsRequestList = addScheduleRequest.getAddScheduleLegsRequest();
		
		if(addScheduleLegsRequestList == null) {
			throw new Exception("Error: No Request Data Found");
		}
		
		if(addScheduleLegsRequestList.size() == 0) {
			throw new Exception("Error: No Request Data Found");
		}
		
		String newOrigin = origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")).trim();
		String newDestination = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")")).trim();
		
		
		LocationEntity originEnti = locationRepository.findByLocationcodeAndIsdeleted(newOrigin,"N");
		long originId = originEnti.getId();
		
		LocationEntity destEnti = locationRepository.findByLocationcodeAndIsdeleted(newDestination,"N");
		long destinationId = destEnti.getId();
		
		TransDetailsEntity transDetailsEnti = transDetailsRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(userId,enquiryReference,"N");
		long transid = 0;
		if(transDetailsEnti == null) {			
			TransDetailsEntity transDetailsEntity = new TransDetailsEntity();
			transDetailsEntity.setCreateby("system");
			transDetailsEntity.setEnquiryreference(enquiryReference);
			transDetailsEntity.setOriginid(originId);
			transDetailsEntity.setDestinationid(destinationId);
			transDetailsEntity.setScheduletype(scheduletype);
			transDetailsEntity.setNumberoflegs(legscount);
			
			String currentTimestamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
			transDetailsEntity.setCargoreadydate(currentTimestamp);
			transDetailsEntity.setCutoffdate(cutoffdate);
			
			transDetailsEntity.setForwarderid(userId);
			transDetailsEntity.setIsactive("N");
			transDetailsEntity.setIsdeleted("N");		
			transDetailsRepository.save(transDetailsEntity);
			transid = transDetailsEntity.getId();
			
		}else {
			long id = transDetailsEnti.getId();
			int status = scheduleLegsRepository.deleteAllByTransId(id);
			transid = id;
			TransDetailsEntity transDetailsEntity = transDetailsRepository.getOne(id);			
			transDetailsEntity.setScheduletype(scheduletype);
			transDetailsEntity.setNumberoflegs(legscount);					
			transDetailsEntity.setCutoffdate(cutoffdate);				
			transDetailsRepository.save(transDetailsEntity);
		}
				
		for(AddScheduleLegsRequest addScheduleLegsRequest : addScheduleLegsRequestList) {
			
			String originInner = addScheduleLegsRequest.getOrigin();
			String destinationInner = addScheduleLegsRequest.getDestination();
			String mode = addScheduleLegsRequest.getMode();
						
			String vessel = addScheduleLegsRequest.getVessel();
		    String voyage = addScheduleLegsRequest.getVoyage();
		    String etddate = addScheduleLegsRequest.getEtddate();
			String etadate = addScheduleLegsRequest.getEtadate();
			String transittime = addScheduleLegsRequest.getTransittime();
		    		
			if(!originInner.contains("(") || !originInner.contains(")")){
				throw new Exception("Error: No Matching Data Found");
			}
			
			if(!destinationInner.contains("(") || !destinationInner.contains(")")) {
				throw new Exception("Error: No Matching Data Found");
			}
			
			String newInnerOrigin = originInner.substring(originInner.indexOf("(") + 1, originInner.indexOf(")")).trim();
			String newInnerDestination = destinationInner.substring(destinationInner.indexOf("(") + 1, destinationInner.indexOf(")")).trim();
			
			
			LocationEntity originInnerEnti = locationRepository.findByLocationcodeAndIsdeleted(newInnerOrigin,"N");
			long originInnerId = originInnerEnti.getId();
			
			LocationEntity destInnerEnti = locationRepository.findByLocationcodeAndIsdeleted(newInnerDestination,"N");
			long destinationInnerId = destInnerEnti.getId();
			
			ScheduleLegsEntity scheduleLegsEntity = new ScheduleLegsEntity();
					
			scheduleLegsEntity.setCreateby("system");
			scheduleLegsEntity.setTransid(transid);
			scheduleLegsEntity.setOriginid(originInnerId);
			scheduleLegsEntity.setDestinationid(destinationInnerId);
			scheduleLegsEntity.setMode(mode);
			
			if(mode.equals("Sea")) {
				scheduleLegsEntity.setCarrierid(Long.parseLong(addScheduleLegsRequest.getCarrier()));
			}else {
				scheduleLegsEntity.setCarrieroption(TitleCaseConvertsionUtils.titleCaseConversion(addScheduleLegsRequest.getCarrier()));
			}
			
			scheduleLegsEntity.setVessel(TitleCaseConvertsionUtils.titleCaseConversion(vessel));
			scheduleLegsEntity.setVoyage(TitleCaseConvertsionUtils.titleCaseConversion(voyage));
			scheduleLegsEntity.setEtddate(etddate);
			scheduleLegsEntity.setEtadate(etadate);
			scheduleLegsEntity.setTransittime(transittime);
			
			scheduleLegsEntity.setIsdeleted("N");
			
			scheduleLegsRepository.save(scheduleLegsEntity);
		}	
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.getOne(enquiryId);
		enquiryForwarderEntity.setUpdateby("system");
		enquiryForwarderEntity.setIsscheduleupload("Y");
		enquiryForwarderRepository.save(enquiryForwarderEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
	    info.put("msg", "Schedule Added successfully!");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
		
	}
 
	@Override
	public BaseResponse addScheduleCharge(AddScheduleChargeRequest addScheduleChargeRequest, String userid) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************addScheduleCharge method in ForwarderServicesImpl*******************");
		
		if(addScheduleChargeRequest == null) {
			throw new Exception("Error: No Request Data Found");
		}
		
		List<AddChargesTypeRequest> addChargesTypeRequestList = addScheduleChargeRequest.getAddChargesTypeRequest();
		
		if(addChargesTypeRequestList == null) {
			throw new Exception("Error: No Request Data Found");
		}
		
		if(addChargesTypeRequestList.size() == 0) {
			throw new Exception("Error: No Request Data Found");
		}
				
		String enquiryReference = addScheduleChargeRequest.getEnquiryreference();
		long enquiryId = Long.parseLong(addScheduleChargeRequest.getEnquiryid());
		long userId = Long.parseLong(userid);
		String origin = addScheduleChargeRequest.getOrigin();
		String destination = addScheduleChargeRequest.getDestination();		
		String validdatefrom = addScheduleChargeRequest.getValiddatefrom();
		String validdateto = addScheduleChargeRequest.getValiddateto();
		String incoterm = addScheduleChargeRequest.getIncoterm();
		long incotermid = Long.parseLong(incoterm);
		String remark = addScheduleChargeRequest.getRemark();	
		
		String newOrigin = origin.substring(origin.indexOf("(") + 1, origin.indexOf(")")).trim();
		String newDestination = destination.substring(destination.indexOf("(") + 1, destination.indexOf(")")).trim();
		
		LocationEntity originEnti = locationRepository.findByLocationcodeAndIsdeleted(newOrigin,"N");
		long originId = originEnti.getId();
		
		LocationEntity destEnti = locationRepository.findByLocationcodeAndIsdeleted(newDestination,"N");
		long destinationId = destEnti.getId();
		
		List<ChargesRateEntity> chargesRateEntiList = chargesRateRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(userId,enquiryReference,"N");
		
		if(chargesRateEntiList.size() != 0) {						
			int status = chargesRateRepository.deleteAllByEnquiryReference(enquiryReference);
		}
		
		/*
		List<ChargesRateEntity> chargesRateEntityList = chargesRateRepository.findByOriginlocidAndDestinationlocidAndForwarderidAndIsdeleted(originId,destinationId,userId,"N");
		if(chargesRateEntityList != null && chargesRateEntityList.size() != 0) {
			for(ChargesRateEntity chargesRateEnti : chargesRateEntityList) {
				ChargesRateEntity chargesRateEntityUpdate = chargesRateRepository.getOne(chargesRateEnti.getId());
				chargesRateEntityUpdate.setUpdateby("system");
				chargesRateEntityUpdate.setIsdeleted("Y");
				chargesRateRepository.save(chargesRateEntityUpdate);
			}
		}
		*/
		
		for(AddChargesTypeRequest addChargesTypeRequest : addChargesTypeRequestList) {
			ChargesRateEntity chargesRateEntity = new ChargesRateEntity();	
			chargesRateEntity.setCreateby("system");
			
			chargesRateEntity.setEnquiryreference(enquiryReference);
			chargesRateEntity.setOrigin(origin);
			chargesRateEntity.setOriginlocid(originId);
			chargesRateEntity.setDestination(destination);
			chargesRateEntity.setDestinationlocid(destinationId);
			chargesRateEntity.setForwarderid(userId);
			//chargesRateEntity.setCarrierid(carrierid);
			long chargegroupingid = Long.parseLong(addChargesTypeRequest.getChargegrouping());
			chargesRateEntity.setChargesgroupingid(chargegroupingid);
			
			long chargetypeid = Long.parseLong(addChargesTypeRequest.getChargetype());
			chargesRateEntity.setChargetypeid(chargetypeid);
			
			chargesRateEntity.setIncotermid(incotermid);
			chargesRateEntity.setRemark(remark);			
			
			chargesRateEntity.setValiddatefrom(validdatefrom);
			chargesRateEntity.setValiddateto(validdateto);
			chargesRateEntity.setCurrency(addChargesTypeRequest.getCurrency());
			chargesRateEntity.setBasis(addChargesTypeRequest.getBasis());
			chargesRateEntity.setQuantity(addChargesTypeRequest.getQuantity());
			chargesRateEntity.setRate(addChargesTypeRequest.getRate());
			
			chargesRateEntity.setIsactive("N");
			chargesRateEntity.setIsdeleted("N");
			
			chargesRateRepository.save(chargesRateEntity); 
			
		}
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.getOne(enquiryId);
		enquiryForwarderEntity.setUpdateby("system");
		enquiryForwarderEntity.setIschargesupload("Y");
		enquiryForwarderRepository.save(enquiryForwarderEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		
	    info.put("msg", "Charges Added successfully!");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse updateEnquiryStatus(String id, String userId, String enquiryStatus) throws Exception {
        baseResponse = new BaseResponse();
		
		long forwEnqId = Long.parseLong(id);
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.getOne(forwEnqId);	
		long enquiryId = enquiryForwarderEntity.getEnquiryid();
		String enquiryReference = enquiryForwarderEntity.getEnquiryreference();
		enquiryForwarderEntity.setUpdateby(userId);
		enquiryForwarderEntity.setStatus(enquiryStatus);
		enquiryForwarderRepository.save(enquiryForwarderEntity);
		
		if(enquiryStatus.equals("Accepted")) {
	
	    	EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryId, "N");
		    if(enquiryEntity != null) {
			    EnquiryEntity enquiryEnti = enquiryRepository.getOne(enquiryId);
			    enquiryEnti.setUpdateby(userId);
			    enquiryEnti.setStatus(enquiryStatus);
			    enquiryRepository.save(enquiryEnti);

			    long ieId = enquiryEntity.getUserid();
			    
			    UserEntity userEnti = userRepository.findByIdAndIsdeleted(ieId, "N");
			    String userEmailId = userEnti.getEmail();
			    
			    long originlocid = enquiryEntity.getOriginlocid();
			    long destinationlocid = enquiryEntity.getDestinationlocid();
			    String shipmentType = enquiryEntity.getShipmenttype();
		
				String enqSearchDate = enquiryEntity.getSearchdate();
				String cargoReadyDate = enquiryEntity.getCargoreadydate();
			    
				String selectedFCL = "";
				String selectedLCL = "";
				String lcltotalweight = "";
			    String lclweightunit = "";
			    String lclvolume = "";
			    String lclvolumeunit = "";
			    String lclnumberpackage = "";
			    String lclpackageunit = "";
			    
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
					lcltotalweight = enquiryEntity.getLcltotalweight();
				    lclweightunit = enquiryEntity.getLclweightunit();
				    lclvolume = enquiryEntity.getLclvolume();
				    lclvolumeunit = enquiryEntity.getLclvolumeunit();
				    lclnumberpackage = enquiryEntity.getLclnumberpackage();
				    lclpackageunit = enquiryEntity.getLclpackageunit();			
				    
				    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
				}
			   				
				Optional<LocationEntity> originEnti=  locationRepository.findById(originlocid);		
				String orgWithCode = originEnti.get().getLocationname()+" ("+originEnti.get().getLocationcode()+")";
				
				Optional<LocationEntity> destEnti =  locationRepository.findById(destinationlocid);				
				String destWithCode = destEnti.get().getLocationname()+" ("+destEnti.get().getLocationcode()+")";
				
			    long forwId = Long.parseLong(userId);
			    UserEntity forwDetails = userRepository.findByIdAndIsdeleted(forwId, "N");
			    long companyId = forwDetails.getCompanyId();
			    Optional<CompanyEntity> companyEntity = companyRepository.findById(companyId);
				if (companyEntity == null ) {
					throw new Exception("Error: No Company Details Found");
				}
			    String quotationFrom = companyEntity.get().getTradename();
				String quotationDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
				String quotationStatus = enquiryStatus;
			    		    
			    //Forwarder Accept Enquiry and submited quotation
				Mail mailExceed = new Mail();
				mailExceed.setFrom("Book My Cargo<BookMyCargoNow@gmail.com>");
				mailExceed.setTo(userEmailId);
				mailExceed.setSubject("Book My Cargo: "+shipmentType+" Enquiry Reference Number <"+enquiryReference+"> Accepted");
				
				ForwarderQuotationResponse forwarderQuotationResponse = new ForwarderQuotationResponse();
				forwarderQuotationResponse.setAcceptedDate(quotationDate);
				forwarderQuotationResponse.setOrigin(orgWithCode);
				forwarderQuotationResponse.setDestination(destWithCode);
				forwarderQuotationResponse.setSelectedFcl(selectedFCL);
				forwarderQuotationResponse.setSelectedLcl(selectedLCL);
				forwarderQuotationResponse.setShipmentType(shipmentType);
				
				forwarderQuotationResponse.setEnquiryReference(enquiryReference);
				forwarderQuotationResponse.setQuotationFrom(quotationFrom);
				forwarderQuotationResponse.setQuotationDate(quotationDate);
				forwarderQuotationResponse.setQuotationStatus(quotationStatus);
				forwarderQuotationResponse.setEnqSearchDate(enqSearchDate);
				forwarderQuotationResponse.setCargoReadyDate(cargoReadyDate);
				forwarderQuotationResponse.setLcltotalweight(lcltotalweight);
				forwarderQuotationResponse.setLclweightunit(lclweightunit);
				forwarderQuotationResponse.setLclvolume(lclvolume);
				forwarderQuotationResponse.setLclvolumeunit(lclvolumeunit);
				forwarderQuotationResponse.setLclnumberpackage(lclnumberpackage);
				forwarderQuotationResponse.setLclpackageunit(lclpackageunit);
				
				try {
					emailService.sendForwarderEnquiryAcceptedMail(mailExceed, forwarderQuotationResponse);
					logger.info("Email Send Successfully to customer");
				}catch(Exception e) {
					e.printStackTrace();
				}
				
		    }
		}else if(enquiryStatus.equals("Cancelled")) {	
			
			long count = enquiryForwarderRepository.enquiryCountByEnquiryReference(enquiryReference);			
			if(count == 1) {
				EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryId, "N");
			    if(enquiryEntity != null) {
				    EnquiryEntity enquiryEnti = enquiryRepository.getOne(enquiryId);
				    enquiryEnti.setUpdateby(userId);
				    enquiryEnti.setStatus("Rejected");
				    enquiryRepository.save(enquiryEnti);
			    }
			}else if(count > 1) {
				boolean flag = true;
				List<EnquiryForwarderEntity> enquiryForwarderEntiList = enquiryForwarderRepository.findByEnquiryreference(enquiryReference);
				
				for(EnquiryForwarderEntity enqForwarderEntity : enquiryForwarderEntiList) {
					String status = enqForwarderEntity.getStatus();					
					if(!status.equals("Cancelled")) {
						flag = false;
						break;
					}
				}				
				if(flag) {
					EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryId, "N");
				    if(enquiryEntity != null) {
					    EnquiryEntity enquiryEnti = enquiryRepository.getOne(enquiryId);
					    enquiryEnti.setUpdateby(userId);
					    enquiryEnti.setStatus("Rejected");
					    enquiryRepository.save(enquiryEnti);
				    }
				}
				
			}
		}   
		
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Status Updated successfully!");		
		
		logger.info("Status Updated successfully");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse getForwarderEnquiryScheduleChargesById(String forwEnqId,String userId) throws Exception {
	        baseResponse = new BaseResponse();
		
			logger.info("*****************getForwarderEnquiryScheduleChargesById method in ForwarderServicesImpl*******************");
		
			long forwEnquiryId = Long.parseLong(forwEnqId);
			
			EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.findByIdAndIsdeleted(forwEnquiryId,"N");

			if (enquiryForwarderEntity == null) {
				throw new Exception("Error: No Data Found");
			}
		
			long enquiryId = enquiryForwarderEntity.getEnquiryid();
			long originlocid = enquiryForwarderEntity.getOriginlocid();
			long destinationlocid = enquiryForwarderEntity.getDestinationlocid();
			long forwarderid = Long.parseLong(userId);		
			
			EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryId, "N");
			if (enquiryEntity == null) {
				throw new Exception("Error: No Data Found");
			}
			
			String enquiryreference = enquiryEntity.getEnquiryreference();
			String shipmentType = enquiryEntity.getShipmenttype();
						
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
			    enquiryEntity.getLclnumberpackage();
			    enquiryEntity.getLclpackageunit();			   
			    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
			}
									
		    EnquiryScheduleChargesDetailsResponse enqScheduleChargesDetailsResponse = new EnquiryScheduleChargesDetailsResponse();
			
		    enqScheduleChargesDetailsResponse.setFclselected(selectedFCL);
		    enqScheduleChargesDetailsResponse.setLclselected(selectedLCL);
		    
		    enqScheduleChargesDetailsResponse.setId(enquiryEntity.getId());
		    enqScheduleChargesDetailsResponse.setEnquiryreference(enquiryreference);
		    
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
			
			TransDetailsEntity transDetailsEntity = transDetailsRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderid,enquiryreference,"N");
			List<ScheduleLegsResponse> scheduleLegsResponseList = null;	
						
			if (transDetailsEntity == null) {												
				enqScheduleChargesDetailsResponse.setScheduleLegsResponseList(scheduleLegsResponseList);								
			}else {
				long transId = transDetailsEntity.getId();
				enqScheduleChargesDetailsResponse.setScheduletype(transDetailsEntity.getScheduletype());
				enqScheduleChargesDetailsResponse.setNumberoflegs(transDetailsEntity.getNumberoflegs());
				
				List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transId,"N");
				
				if(scheduleLegsEntityList == null) {
					throw new Exception("Error: No Schedule Legs Found");
				}

				scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
				
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
			}

			List<ChargesRateEntity> chargesRateList = chargesRateRepository.findByForwarderidAndEnquiryreferenceAndIsdeleted(forwarderid,enquiryreference,"N");			
		    List<ChargesRateResponse> chargesRateResponseList = null;
		   
		    long incotermId = 0;
		    		    
			if(chargesRateList == null && chargesRateList.size() == 0) {
				
				enqScheduleChargesDetailsResponse.setChargesRateResponseList(chargesRateResponseList);
				enqScheduleChargesDetailsResponse.setIncoterm("");
				
			}else {
				
				chargesRateResponseList = new  ArrayList<ChargesRateResponse>();			
				
				String chargerateids = ""; 
				   
				for(ChargesRateEntity chargeRateEnt : chargesRateList) {
					incotermId = chargeRateEnt.getIncotermid();
					
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
				    double rate = Double.parseDouble(chargeRateEnt.getRate());
				    float quntity = Float.parseFloat(chargeRateEnt.getQuantity());
				    double tAmount = rate * quntity;
				    double value =Double.parseDouble(new DecimalFormat("##.##").format(tAmount));
				    String totalamount = Double.toString(value);
				    chargesRateResponse.setTotalamount(totalamount);
				    chargesRateResponseList.add(chargesRateResponse);
				}//end for loop
							
				if(chargerateids.endsWith(",")) {
					 int index = chargerateids.lastIndexOf(",");
					 chargerateids =  chargerateids.substring(0, index);
				}
				enqScheduleChargesDetailsResponse.setChargerateids(chargerateids);								  
			    enqScheduleChargesDetailsResponse.setChargesRateResponseList(chargesRateResponseList);
				
			    String incoterm = "";			    
			    if(incotermId != 0) {
			    	IncotermEntity incotermEntity = incotermRepository.findByIdAndIsdeleted(incotermId,"N");
					incoterm = incotermEntity.getDescription()+" ("+incotermEntity.getIncoterm()+")";
			    }				
				enqScheduleChargesDetailsResponse.setIncoterm(incoterm);
			}
			
			baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
			baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
			baseResponse.setRespData(enqScheduleChargesDetailsResponse);
			return baseResponse;
	}

	@Override
	public BaseResponse deleteForwarderEnquiryById(String id,String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		long forwEnqId = Long.parseLong(id);
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.getOne(forwEnqId);	
		enquiryForwarderEntity.setUpdateby(userId);
		enquiryForwarderEntity.setStatus("Deleted");
		enquiryForwarderEntity.setIsdeleted("Y");
		enquiryForwarderRepository.save(enquiryForwarderEntity);
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Enquiry deleted successfully!");		
		
		logger.info("Enquiry deleted successfully");
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse deleteForwarderBookingById(String id, String userId) throws Exception {
        baseResponse = new BaseResponse();
		
		long bookingId = Long.parseLong(id);
		
        BookingDetailsEntity bookingDetailsEntity = bookingDetailsRepository.getOne(bookingId);
        bookingDetailsEntity.setUpdateby(userId);
        bookingDetailsEntity.setForwarderisdeleted("Y");	
		bookingDetailsRepository.save(bookingDetailsEntity);
		
				
		HashMap<String ,String>  userInfo = new HashMap<String ,String>();
		userInfo.put("msg", "Booking deleted successfully!");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(userInfo);
		return baseResponse;
	}

	@Override
	public BaseResponse addOriginLocationAlert(ConfigureAlertRequest configureAlertRequest) throws Exception {
        baseResponse = new BaseResponse();
		
		long userId = Long.parseLong(configureAlertRequest.getUserid());
		String ids = configureAlertRequest.getIds();
		
		String[] newStrId = ids.split(",");	          	        
        List<String> newListParts = Arrays.asList(newStrId);	          
       
        HashSet<String> hsetFromString = new HashSet<String>( newListParts );          
        logger.info("New HashSet contains: " + hsetFromString);
        
		ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(userId,"N");
		
		if(configureAlertEntity != null) {
			String existIds = configureAlertEntity.getOriginlocids();
			
			if(existIds != null) {
				String[] existStrId = existIds.split(",");	          	        
		        List<String> existListParts = Arrays.asList(existStrId);
		        hsetFromString.addAll(existListParts);
			}
			  
	        String newIds = String.join(",", hsetFromString);
	        
	        logger.info("After add New HashSet contains:"+newIds);
			
			ConfigureAlertEntity configureAlertEnti = configureAlertRepository.getOne(configureAlertEntity.getId());
			configureAlertEnti.setOriginlocids(newIds);
			configureAlertEnti.setUpdateby("system");
			configureAlertRepository.save(configureAlertEnti);
			
		}else {
			ConfigureAlertEntity configureAlertEnti = new ConfigureAlertEntity();
			configureAlertEnti.setCreateby("system");
			configureAlertEnti.setUserid(userId);
			configureAlertEnti.setOriginlocids(ids);
			configureAlertEnti.setIsdeleted("N");
			configureAlertRepository.save(configureAlertEnti);
		}
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Alerts configuration updated as per details selected");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse addDestinationLocationAlert(ConfigureAlertRequest configureAlertRequest) throws Exception {
        baseResponse = new BaseResponse();
		
		long userId = Long.parseLong(configureAlertRequest.getUserid());
		String ids = configureAlertRequest.getIds();
		
		String[] newStrId = ids.split(",");	          	        
        List<String> newListParts = Arrays.asList(newStrId);	          
       
        HashSet<String> hsetFromString = new HashSet<String>( newListParts );          
        logger.info("New HashSet contains: " + hsetFromString);
        
		ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(userId,"N");
		
		if(configureAlertEntity != null) {
			String existIds = configureAlertEntity.getDestinationlocids();
			
			if(existIds != null) {
				String[] existStrId = existIds.split(",");	          	        
		        List<String> existListParts = Arrays.asList(existStrId);
		        hsetFromString.addAll(existListParts);
			}
			  
	        String newIds = String.join(",", hsetFromString);
	        
	        logger.info("After add New HashSet contains:"+newIds);
			
			ConfigureAlertEntity configureAlertEnti = configureAlertRepository.getOne(configureAlertEntity.getId());
			configureAlertEnti.setDestinationlocids(newIds);
			configureAlertEnti.setUpdateby("system");
			configureAlertRepository.save(configureAlertEnti);
			
		}else {
			ConfigureAlertEntity configureAlertEnti = new ConfigureAlertEntity();
			configureAlertEnti.setCreateby("system");
			configureAlertEnti.setUserid(userId);
			configureAlertEnti.setDestinationlocids(ids);
			configureAlertEnti.setIsdeleted("N");
			configureAlertRepository.save(configureAlertEnti);
		}
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Alerts configuration updated as per details selected");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getAllConfiguredLocationByUserId(String userId) throws Exception {
		baseResponse = new BaseResponse();
		
		long userid = Long.parseLong(userId);
		
		ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(userid,"N");
		
		if(configureAlertEntity == null) {
			throw new Exception("Error: No Configure Data Found");
		}
		
		String originlocids = configureAlertEntity.getOriginlocids();
		String destinationlocids = configureAlertEntity.getDestinationlocids();
		
		ConfiguredLocationResponse configuredLocationResponse = new ConfiguredLocationResponse();
		
		List<LocationResponse> originLocationResponse = null;
		List<LocationResponse> destinationLocationResponse = null;
				
		if(originlocids != null) {
			String[] strIds = originlocids.split(",");
			originLocationResponse = new ArrayList<LocationResponse>();
			for(String newStrId : strIds){
               long id = Long.parseLong(newStrId);              
               LocationEntity locationEntity = locationRepository.findByIdAndIsdeleted(id,"N");
               
   			   String locationcode = locationEntity.getLocationcode();
   			   String locationname = locationEntity.getLocationname();
   			   String countrycode = locationEntity.getCountrycode();
   			   String countryname = locationEntity.getCountryname();
   			   
   			   LocationResponse newResponse = new LocationResponse();
			   newResponse.setId(locationEntity.getId());
			   newResponse.setLocationcode(locationcode);
			   newResponse.setLocationname(locationname);
			   newResponse.setCountrycode(countrycode);
			   newResponse.setCountryname(countryname); 		
			   
			   originLocationResponse.add(newResponse);
    			   
			}			
		}
		
        if(destinationlocids != null) {
        	String[] strIds = destinationlocids.split(",");
        	destinationLocationResponse = new ArrayList<LocationResponse>();
			for(String newStrId : strIds){
               long id = Long.parseLong(newStrId);              
               LocationEntity locationEntity = locationRepository.findByIdAndIsdeleted(id,"N");
               
   			   String locationcode = locationEntity.getLocationcode();
   			   String locationname = locationEntity.getLocationname();
   			   String countrycode = locationEntity.getCountrycode();
   			   String countryname = locationEntity.getCountryname();
   			   
   			   LocationResponse newResponse = new LocationResponse();
			   newResponse.setId(locationEntity.getId());
			   newResponse.setLocationcode(locationcode);
			   newResponse.setLocationname(locationname);
			   newResponse.setCountrycode(countrycode);
			   newResponse.setCountryname(countryname);
			    			   
			   destinationLocationResponse.add(newResponse);
		    }
        }
               
        configuredLocationResponse.setOriginLocationResponse(originLocationResponse);
        configuredLocationResponse.setDestinationLocationResponse(destinationLocationResponse);
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(configuredLocationResponse);
		return baseResponse;
	
	}

	@Override
	public BaseResponse removeOriginLocationAlert(ConfigureAlertRequest configureAlertRequest) throws Exception {
        baseResponse = new BaseResponse();
		
		long userId = Long.parseLong(configureAlertRequest.getUserid());
		String ids = configureAlertRequest.getIds();
		
		String[] newStrId = ids.split(",");	          	        
        List<String> newListParts = Arrays.asList(newStrId);	// list of ids to remove from existing          
              
		ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(userId,"N");
		
		if(configureAlertEntity != null) {
			String existIds = configureAlertEntity.getOriginlocids();
		
		    String[] existStrId = existIds.split(",");	          	        
		    List<String> existListParts = Arrays.asList(existStrId);
		    HashSet<String> hsetFromString = new HashSet<String>( existListParts );          
	        logger.info("existing HashSet contains: " + hsetFromString);
		    
	        //For Each Loop for iterating ArrayList
	        for (String id : newListParts)
	        {
	        	hsetFromString.remove(id);
	        }
	        	        
	        String newIds = String.join(",", hsetFromString);
	        
	        logger.info("After remove New HashSet contains:"+newIds);
			
			ConfigureAlertEntity configureAlertEnti = configureAlertRepository.getOne(configureAlertEntity.getId());
			configureAlertEnti.setOriginlocids(newIds);
			configureAlertEnti.setUpdateby("system");
			configureAlertRepository.save(configureAlertEnti);
			
		}
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Alerts configuration updated as per details removed");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse removeDestinationLocationAlert(ConfigureAlertRequest configureAlertRequest) throws Exception {
        baseResponse = new BaseResponse();
		
		long userId = Long.parseLong(configureAlertRequest.getUserid());
		String ids = configureAlertRequest.getIds();
		
		String[] newStrId = ids.split(",");	          	        
        List<String> newListParts = Arrays.asList(newStrId);	// list of ids to remove from existing          
              
		ConfigureAlertEntity configureAlertEntity = configureAlertRepository.findByUseridAndIsdeleted(userId,"N");
		
		if(configureAlertEntity != null) {
			String existIds = configureAlertEntity.getDestinationlocids();
		
		    String[] existStrId = existIds.split(",");	          	        
		    List<String> existListParts = Arrays.asList(existStrId);
		    HashSet<String> hsetFromString = new HashSet<String>( existListParts );          
	        logger.info("existing HashSet contains: " + hsetFromString);
		    
	        //For Each Loop for iterating ArrayList
	        for (String id : newListParts)
	        {
	        	hsetFromString.remove(id);
	        }
	        	        
	        String newIds = String.join(",", hsetFromString);
	        
	        logger.info("After remove New HashSet contains:"+newIds);
			
			ConfigureAlertEntity configureAlertEnti = configureAlertRepository.getOne(configureAlertEntity.getId());
			configureAlertEnti.setDestinationlocids(newIds);
			configureAlertEnti.setUpdateby("system");
			configureAlertRepository.save(configureAlertEnti);
			
		}
		
		HashMap<String ,String>  info = new HashMap<String ,String>();
		info.put("msg", "Alerts configuration updated as per details removed");		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(info);
		return baseResponse;
	}

	@Override
	public BaseResponse getForwarderEnquiryScheduleById(String id) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderEnquiryList method in ForwarderServicesImpl*******************");
	
		long eid = Long.parseLong(id);
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.findByIdAndIsdeleted(eid,"N");

		if (enquiryForwarderEntity == null) {
			throw new Exception("Error: No Data Found");
		}
			
		EnquiryScheduleChargeResponse enquiryScheduleResponse = new EnquiryScheduleChargeResponse();
			
		enquiryScheduleResponse.setId(enquiryForwarderEntity.getId());
			
		long enquiryid = enquiryForwarderEntity.getEnquiryid();
			
		EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryid,"N");
			
		String enquiryReference = enquiryEntity.getEnquiryreference();
		
		enquiryScheduleResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
			
		Long originId = enquiryForwarderEntity.getOriginlocid();					
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
			
		enquiryScheduleResponse.setOrigin(originWithCode);
		enquiryScheduleResponse.setOrigincode(origincode);
			
		Long destinationId = enquiryForwarderEntity.getDestinationlocid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
			
		enquiryScheduleResponse.setDestination(destinationWithCode);
		enquiryScheduleResponse.setDestinationcode(destinationcode);
			
		enquiryScheduleResponse.setCargoreadydate(enquiryForwarderEntity.getCargoreadydate());
		enquiryScheduleResponse.setShipmenttype(enquiryEntity.getShipmenttype());
		enquiryScheduleResponse.setSearchdate(enquiryEntity.getSearchdate());
				
	    String lcltotalweight = enquiryEntity.getLcltotalweight();
		String lclweightunit = enquiryEntity.getLclweightunit();
		String lclvolume = enquiryEntity.getLclvolume();
		String lclvolumeunit = enquiryEntity.getLclvolumeunit();
		String lclnumberpackage = enquiryEntity.getLclnumberpackage();
		String lclpackageunit = enquiryEntity.getLclpackageunit();
		    
		enquiryScheduleResponse.setLcltotalweight(lcltotalweight);
		enquiryScheduleResponse.setLclweightunit(lclweightunit);
		enquiryScheduleResponse.setLclvolume(lclvolume);
		enquiryScheduleResponse.setLclvolumeunit(lclvolumeunit);
		enquiryScheduleResponse.setLclnumberpackage(lclnumberpackage);
		enquiryScheduleResponse.setLclpackageunit(lclpackageunit);
		
		enquiryScheduleResponse.setStatus(enquiryForwarderEntity.getStatus());
		
		TransDetailsEntity transDetailsEntity = transDetailsRepository.findByEnquiryreferenceAndIsdeleted(enquiryReference, "N");
		
		TransDetailsResponse transDetailsResponse = null;
		
		if(transDetailsEntity != null) {
			transDetailsResponse = new TransDetailsResponse();
			
			long transId = transDetailsEntity.getId();			
			String scheduleType = transDetailsEntity.getScheduletype();
		    String numberOfLegs = transDetailsEntity.getNumberoflegs();
		    String cutOffDate = transDetailsEntity.getCutoffdate();
		    String cargoReadyDate = transDetailsEntity.getCargoreadydate();
		    
		    transDetailsResponse.setId(Long.toString(transId));
		    transDetailsResponse.setScheduleType(scheduleType);
		    transDetailsResponse.setNumberOfLegs(numberOfLegs);
		    transDetailsResponse.setCutOffDate(cutOffDate);
		    transDetailsResponse.setCargoReadyDate(cargoReadyDate);
		    
		    List<ScheduleLegsEntity> scheduleLegsEntityList = scheduleLegsRepository.findByTransidAndIsdeleted(transId, "N");
		    List<ScheduleLegsResponse> scheduleLegsResponseList = null;
 		    if(scheduleLegsEntityList != null) {
 		    	scheduleLegsResponseList = new ArrayList<ScheduleLegsResponse>();
 		    	 		    	
 		    	for(ScheduleLegsEntity scheduleLegsEntity : scheduleLegsEntityList) {
 		    		ScheduleLegsResponse scheduleLegsResponse = new ScheduleLegsResponse();
 		    		
 		    		long scheduleLegsId = scheduleLegsEntity.getId();
 		    		long orgId = scheduleLegsEntity.getOriginid();
 		    		long destId = scheduleLegsEntity.getDestinationid();
 		    		String mode = scheduleLegsEntity.getMode();
 		    				    							
 		   		    Optional<LocationEntity> orgEntity =  locationRepository.findById(orgId); 		   		
 		   		    String orgWithCode = orgEntity.get().getLocationname()+" ("+orgEntity.get().getLocationcode()+")";
 		   		
 		   		    Optional<LocationEntity> destEntity =  locationRepository.findById(destId);		   	
 		   		    String destWithCode = destEntity.get().getLocationname()+" ("+destEntity.get().getLocationcode()+")";
 		   					    		
 		    		scheduleLegsResponse.setId(scheduleLegsId);
 		    		scheduleLegsResponse.setOrigin(orgWithCode);
 		    		scheduleLegsResponse.setDestination(destWithCode);
 		    		scheduleLegsResponse.setMode(mode);
 		    		if(mode.equals("Sea")) {
 						long carriId = scheduleLegsEntity.getCarrierid();
 						Optional<CarrierEntity>  carriEntity = carrierRepository.findById(carriId);
 						String carriName = carriEntity.get().getCarriershortname();
 						scheduleLegsResponse.setCarrierid(Long.toString(carriId));
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
 		    	
 		    }
 		   transDetailsResponse.setScheduleLegsResponseList(scheduleLegsResponseList);
 		    
		}
		
		enquiryScheduleResponse.setTransDetailsResponse(transDetailsResponse);
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryScheduleResponse);
		return baseResponse;
	}
	
	@Override
	public BaseResponse getForwarderEnquiryChargesById(String id) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("*****************getForwarderEnquiryChargesById method in ForwarderServicesImpl*******************");
	
		long eid = Long.parseLong(id);
		
		EnquiryForwarderEntity enquiryForwarderEntity = enquiryForwarderRepository.findByIdAndIsdeleted(eid,"N");

		if (enquiryForwarderEntity == null) {
			throw new Exception("Error: No Data Found");
		}
		
		EnquiryScheduleChargeResponse enquiryChargeResponse = new EnquiryScheduleChargeResponse();
			
		enquiryChargeResponse.setId(enquiryForwarderEntity.getId());
			
		long enquiryid = enquiryForwarderEntity.getEnquiryid();
			
		EnquiryEntity enquiryEntity = enquiryRepository.findByIdAndIsdeleted(enquiryid,"N");
		String enquiryReference = enquiryEntity.getEnquiryreference();
		String shipmentType = enquiryEntity.getShipmenttype();
		
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
		}else if(shipmentType.equals("LCL")){
			String lcltotalweight = enquiryEntity.getLcltotalweight();
		    String lclweightunit = enquiryEntity.getLclweightunit();
		    String lclvolume = enquiryEntity.getLclvolume();
		    String lclvolumeunit = enquiryEntity.getLclvolumeunit();
		    String lclnumberpackage = enquiryEntity.getLclnumberpackage();
		    String lclpackageunit = enquiryEntity.getLclpackageunit();
		    
		    selectedLCL = "Weight "+lcltotalweight+" "+lclweightunit+" , Volume "+lclvolume+" "+lclvolumeunit+" , Packages "+lclnumberpackage;
		    
		    enquiryChargeResponse.setLcltotalweight(lcltotalweight);
		    enquiryChargeResponse.setLclweightunit(lclweightunit);
		    enquiryChargeResponse.setLclvolume(lclvolume);
		    enquiryChargeResponse.setLclvolumeunit(lclvolumeunit);
		    enquiryChargeResponse.setLclnumberpackage(lclnumberpackage);
		    enquiryChargeResponse.setLclpackageunit(lclpackageunit);
		    
		}
					
		enquiryChargeResponse.setSelectedfcl(selectedFCL);
		enquiryChargeResponse.setSelectedlcl(selectedLCL);
			
		enquiryChargeResponse.setEnquiryreference(enquiryEntity.getEnquiryreference());
			
		Long originId = enquiryForwarderEntity.getOriginlocid();					
		Optional<LocationEntity> originEntity =  locationRepository.findById(originId);
		String origincode = originEntity.get().getLocationcode();
		String originWithCode = originEntity.get().getLocationname()+" ("+originEntity.get().getLocationcode()+")";
			
		enquiryChargeResponse.setOrigin(originWithCode);
		enquiryChargeResponse.setOrigincode(origincode);
			
		Long destinationId = enquiryForwarderEntity.getDestinationlocid();
		Optional<LocationEntity> destinationEntity =  locationRepository.findById(destinationId);
		String destinationcode = destinationEntity.get().getLocationcode();
		String destinationWithCode = destinationEntity.get().getLocationname()+" ("+destinationEntity.get().getLocationcode()+")";
			
		enquiryChargeResponse.setDestination(destinationWithCode);
		enquiryChargeResponse.setDestinationcode(destinationcode);
			
		enquiryChargeResponse.setCargoreadydate(enquiryForwarderEntity.getCargoreadydate());
		enquiryChargeResponse.setShipmenttype(enquiryEntity.getShipmenttype());
		enquiryChargeResponse.setSearchdate(enquiryEntity.getSearchdate());
				
	    String lcltotalweight = enquiryEntity.getLcltotalweight();
		String lclweightunit = enquiryEntity.getLclweightunit();
		String lclvolume = enquiryEntity.getLclvolume();
		String lclvolumeunit = enquiryEntity.getLclvolumeunit();
		String lclnumberpackage = enquiryEntity.getLclnumberpackage();
		String lclpackageunit = enquiryEntity.getLclpackageunit();
		    
		enquiryChargeResponse.setLcltotalweight(lcltotalweight);
		enquiryChargeResponse.setLclweightunit(lclweightunit);
		enquiryChargeResponse.setLclvolume(lclvolume);
		enquiryChargeResponse.setLclvolumeunit(lclvolumeunit);
		enquiryChargeResponse.setLclnumberpackage(lclnumberpackage);
		enquiryChargeResponse.setLclpackageunit(lclpackageunit);
		
		enquiryChargeResponse.setStatus(enquiryForwarderEntity.getStatus());
		
		List<ChargesRateEntity> chargesRateEntityList = chargesRateRepository.findByEnquiryreferenceAndIsdeleted(enquiryReference, "N");
	
		List<ChargesRateResponse> chargesRateResponseList = null;
		if(chargesRateEntityList != null) {
			chargesRateResponseList = new  ArrayList<ChargesRateResponse>();
			
			for(ChargesRateEntity chargesRateEntity : chargesRateEntityList) {
				Long chargeId = chargesRateEntity.getId();
				
				ChargesRateResponse chargesRateResponse = new ChargesRateResponse();
							
				ChargesRateEntity chargesRateEnti = chargesRateRepository.findByIdAndIsdeleted(chargeId,"N");
				long incotermId = chargesRateEnti.getIncotermid();
				chargesRateResponse.setIncotermid(incotermId);
				
				long chargesgroupingid = chargesRateEnti.getChargesgroupingid();
				ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargesgroupingid, "N");
				String chargesgrouping = chargesGroupingEntity.getChargesgrouping();			
				chargesRateResponse.setChargesgroupingid(chargesgroupingid);
				chargesRateResponse.setChargesgrouping(chargesgrouping);
				
				long chargetypeid = chargesRateEnti.getChargetypeid();
				ChargesTypeEntity chargesTypeEntity = chargesTypeRepository.findByIdAndIsdeleted(chargetypeid,"N");
				String chargestype = chargesTypeEntity.getChargecodedescription();
				chargesRateResponse.setChargestypeid(chargetypeid);
				chargesRateResponse.setChargestype(chargestype);
				chargesRateResponse.setRemark(chargesRateEnti.getRemark());
				chargesRateResponse.setValiddatefrom(chargesRateEnti.getValiddatefrom());
				chargesRateResponse.setValiddateto(chargesRateEnti.getValiddateto());
				
				chargesRateResponse.setCurrency(chargesRateEnti.getCurrency());
				
				String basiscode = chargesRateEnti.getBasis();
				ChargeBasisEntity chargeBasisEntity = chargeBasisRepository.findByBasiscodeAndIsdeleted(basiscode,"N");	
				chargesRateResponse.setBasiscode(basiscode);
				chargesRateResponse.setBasis(chargeBasisEntity.getBasis());
				
				chargesRateResponse.setQuantity(chargesRateEnti.getQuantity());
		
			    chargesRateResponse.setRate(chargesRateEnti.getRate());
			    chargesRateResponseList.add(chargesRateResponse);			 			
			}
		}
		
		enquiryChargeResponse.setChargesRateResponseList(chargesRateResponseList);
		
		
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(enquiryChargeResponse);
		return baseResponse;
		
	}
}
