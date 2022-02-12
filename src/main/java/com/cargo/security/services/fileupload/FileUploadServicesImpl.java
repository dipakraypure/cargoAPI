package com.cargo.security.services.fileupload;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.constants.MaxiconColumnUtils;
import com.cargo.load.request.CarrierFileConvertFormRequest;
import com.cargo.load.request.CommonRateTemplateRequest;
import com.cargo.load.request.UpdateRateTemplateRecordRequest;
import com.cargo.load.response.BaseResponse;
import com.cargo.load.response.CommonRateTemplateResponse;
import com.cargo.load.response.RatePreviewListResponse;
import com.cargo.models.CarrierEntity;
import com.cargo.models.ChargesGroupingEntity;
import com.cargo.models.ChargesRateEntity;
import com.cargo.models.LocationEntity;
import com.cargo.models.UploadRateHistoryEntity;
import com.cargo.models.UploadRateTemporaryEntity;
import com.cargo.repository.CarrierRepository;
import com.cargo.repository.ChargeGroupingRepository;
import com.cargo.repository.ChargesRateRepository;
import com.cargo.repository.LocationRepository;
import com.cargo.repository.UploadRateHistoryRepository;
import com.cargo.repository.UploadRateTemporaryRepository;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.utils.StringsUtils;

@Service
public class FileUploadServicesImpl implements FileUploadServices{
	
	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	ChargesRateRepository chargesRateRepository;
	
	
	@Autowired
	ChargeGroupingRepository chargeGroupingRepository;
	
	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	LocationRepository locationRepository;
	
	@Autowired
	UploadRateHistoryRepository uploadRateHistoryRepository;
	
	@Autowired
	UploadRateTemporaryRepository uploadRateTemporaryRepository;
	
	
	BaseResponse baseResponse	= null;

	private static final Logger logger = LoggerFactory.getLogger(FileUploadServicesImpl.class);

	
	private static Workbook wb;
	private static Sheet sh;
	private static FileInputStream fis;
	private static FileOutputStream fos;
	private static Row row;
	private static Cell cell;
	
	
	@Override
	public BaseResponse submitFileUpload(MultipartFile file) throws Exception {
	
		String fileName=null;
		InputStream fis = null;
		TreeMap<Integer, String[]> hm = null;
		try {  
				fis = file.getInputStream();
		        fileName=file.getOriginalFilename();
					
					
		        TreeMap<Integer, String[]> rowArray = new TreeMap<Integer, String[]>();
		        BufferedReader br = null;
		        // System.out.println("File : " + fileName);
		        try {

			      br = new BufferedReader(new InputStreamReader(fis, "UTF-8"));
			      String line = null;

			      int i = 0;
			      while ((line = br.readLine()) != null) {
				    String[] arr = line.split(",");
				    // System.out.println(Arrays.toString(arr));
				    rowArray.put(i++, arr);
				    
			      }
			      logger.info(" :: Read successfully");
			      
			      if (br != null)
				     br.close();
		        } catch (Exception e) {
			       logger.error("IO Generic Exception occurs " + e.getMessage(), e);
		        }
			
		        hm=rowArray;
					
		        for (int i = 1; i < hm.size(); i++) {
						
		        	String[] arry = hm.get(i);
					
	        	}
					
			
		} catch (Exception e) {
			
			throw new Exception(e);
				
		}
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData("uploadData");
		return baseResponse;
		
	}


	@Override
	public BaseResponse testSubmitFileUpload(MultipartFile file) throws Exception {
		
		baseResponse = new BaseResponse();
		
		logger.info("********************testSubmitFileUpload Method in FileUploadServicesImpl******************");
		
		fis = (FileInputStream) file.getInputStream();
		//fis = new FileInputStream("./testdata.xlsx");
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		
		int noOfRows = sh.getLastRowNum();
		
		System.out.println(noOfRows);
		
		List<CarrierEntity> carrierEntityList = new ArrayList<CarrierEntity>();
		
		for(int i=1;i<=noOfRows;i++) {
			
			logger.info("**************** "+ i +" **************************");
			
			String carrierShortName = (sh.getRow(i).getCell(0) == null) ? "" : sh.getRow(i).getCell(0).toString();
			logger.info("carriername abbreviated: "+carrierShortName); 
			 
			String carrierName = (sh.getRow(i).getCell(1) == null) ? "" : sh.getRow(i).getCell(1).toString();
			logger.info("Carrier name: "+carrierName);
			
			String scacCode = (sh.getRow(i).getCell(2) == null) ? "" : sh.getRow(i).getCell(2).toString();			
			logger.info("SCAC code: " + scacCode);
			
		    String website = (sh.getRow(i).getCell(3) == null) ? "" : sh.getRow(i).getCell(3).toString();
		    logger.info("website: "+website);
		    
		    String logo = (sh.getRow(i).getCell(4) == null)? "" : sh.getRow(i).getCell(4).toString();
		    logger.info("logo: "+logo);

		    CarrierEntity carrierEntity = new CarrierEntity();
		    
		    carrierEntity.setCreateby("system");
		    carrierEntity.setCarriershortname(carrierShortName);
		    carrierEntity.setCarriername(carrierName);
		    carrierEntity.setScaccode(scacCode);
		    carrierEntity.setWebsite(website);
		    carrierEntity.setLogopath(logo);
		    carrierEntity.setIsdeleted("N");
			    
		    carrierEntityList.add(carrierEntity);
		    
		    logger.info("******************************************");
		}
		
		if(carrierEntityList.size() != 0) {
			carrierRepository.saveAll(carrierEntityList);
		}
		
        HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Excel File data Uploaded successfully!");
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		return baseResponse;
	}
	
	@Override
	public BaseResponse convertUploadedFileMaxiconToTemplate(CarrierFileConvertFormRequest carrierFileConvertFormRequest,MultipartFile file) throws Exception {
		
		String userId = carrierFileConvertFormRequest.getUserId();
	    
		String shipmentType = carrierFileConvertFormRequest.getShipmentType();
		Long forwarderId = Long.parseLong(carrierFileConvertFormRequest.getForwarderCha());
		
		Long carrierId = Long.parseLong(carrierFileConvertFormRequest.getCarrier());
		Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
		String carrierName = carrierEntity.get().getCarriershortname();
		String carrierNameWithScacCode = carrierEntity.get().getCarriername()+" ("+carrierEntity.get().getScaccode()+")";
		
		if(!file.getOriginalFilename().endsWith(".xlsx")) {	
			String filename = fileStorageService.storeUnformattedFile(userId,carrierFileConvertFormRequest,file);
			throw new Exception("Error: Invalid carrier file!");	
		}
		
		String uploadFilename = file.getOriginalFilename().replace(".xlsx", "");
		
		Long chargeTypeId = Long.parseLong(carrierFileConvertFormRequest.getChargeType());
		
		Optional<ChargesGroupingEntity> chargesGroupingEntity = chargeGroupingRepository.findById(chargeTypeId);
		String chargeGrouping = chargesGroupingEntity.get().getChargesgrouping();
	    String chargeGroupingCode = chargesGroupingEntity.get().getChargesgroupingcode();
		
		baseResponse = new BaseResponse();
		
		logger.info("********************convertUploadedFileMaxiconToTemplate() Method in FileUploadServicesImpl******************");
		
		fis = (FileInputStream) file.getInputStream();
	
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		
		if(sh == null) {			
			throw new Exception("Error: Invalid carrier file!");
		}
		
		int noOfRows = sh.getLastRowNum();
		
		logger.info("No of Rows: noOfRows= "+noOfRows);
		
		List<CommonRateTemplateRequest> commonRateTemplateRequestList = new ArrayList<CommonRateTemplateRequest>();

		String origin = (sh.getRow(4).getCell(0) == null) ? "" : sh.getRow(4).getCell(0).toString().trim();
		if(origin.startsWith("EX.")){
			origin = origin.replace("EX.", "");
		}else {
			throw new Exception("Error: Invalid carrier file!");
		}
		
		List<LocationEntity> originEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(origin+"%","N");
		 
		if(originEntityList.size() == 0) {
				throw new Exception("Error: Origin Not Found in master");
		}
		 
		String originLocId = originEntityList.get(0).getId().toString();
		
		int count=1;
		for(int i=7;i<=noOfRows;i++) {
			
			CommonRateTemplateRequest commonRateTemplate = new CommonRateTemplateRequest();
			
			logger.info("**************** "+ count +" **************************");
			
			String pod = (sh.getRow(i).getCell(0) == null) ? "" : sh.getRow(i).getCell(0).toString().trim();
			logger.info("POD: "+pod); 
			 
			String twentyFT = (sh.getRow(i).getCell(1) == null) ? "" : sh.getRow(i).getCell(1).toString().trim();
			logger.info("Twenty Fit Ocean freight: "+twentyFT);
			
			String fourtyFT = (sh.getRow(i).getCell(2) == null) ? "" : sh.getRow(i).getCell(2).toString().trim();			
			logger.info("Fourty Fit Ocean freight: " + fourtyFT);
			
		    String routing = (sh.getRow(i).getCell(3) == null) ? "" : sh.getRow(i).getCell(3).toString().trim();
		    logger.info("Routing: "+routing);
		    
		    String transit = (sh.getRow(i).getCell(4) == null)? "" : sh.getRow(i).getCell(4).toString().trim();
		    logger.info("Transit: "+transit);
		    
		    String frequency = (sh.getRow(i).getCell(5) == null)? "" : sh.getRow(i).getCell(5).toString().trim();
		    logger.info("Frequency: "+frequency);

		    logger.info("**************************************************");
		    logger.info("Going to set data into common template pojo class");
		    		   
		  //  List<LocationEntity> destinationEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(pod+"%","N");
		    
		    commonRateTemplate.setOrigin(origin);
	    	commonRateTemplate.setOriginlocid(originLocId);
		    commonRateTemplate.setForwarderid(Long.toString(forwarderId));
		    commonRateTemplate.setShipmenttype(shipmentType);
		    commonRateTemplate.setCarrierid(carrierFileConvertFormRequest.getCarrier());
		    commonRateTemplate.setChargesgroupingid(chargeGrouping);
		    commonRateTemplate.setChargesgroupingcode(chargeGroupingCode);
		    
		    
		    commonRateTemplate.setValiddatefrom(carrierFileConvertFormRequest.getValidDateFrom());
		    commonRateTemplate.setValiddateto(carrierFileConvertFormRequest.getValidDateTo());
		    commonRateTemplate.setRouting(routing);
		    commonRateTemplate.setTransittime(transit);
		    commonRateTemplate.setCurrency("USD");
		    commonRateTemplate.setBasis("Per Container");
		    commonRateTemplate.setQuantity("1");
		    
		    commonRateTemplate.setChargetype("Ocean Freight - 20");
		    commonRateTemplate.setChargetypecode("OF20FT");
		    commonRateTemplate.setRate(twentyFT);
		     
		    List<LocationEntity> destinationEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(pod+"%","N");
		    
		    if( destinationEntityList.size() != 0) {
		    	    	
		    	String destinationLocId = destinationEntityList.get(0).getId().toString();    	
			    commonRateTemplate.setDestination(pod);
			    commonRateTemplate.setDestinationlocid(destinationLocId);
			    commonRateTemplate.setErrorflag("N");    		    
				    	 
		    }else {
		    	logger.info("SKIP Record ==> destination: "+pod+" not found in master");
		    	
                String destinationLocId = "NA";

			    commonRateTemplate.setDestination(pod);
			    commonRateTemplate.setDestinationlocid(destinationLocId);
			    commonRateTemplate.setErrorflag("Y");	            
		    }
		    
		    CommonRateTemplateRequest commonRateTemplateFourtyFT = (CommonRateTemplateRequest)commonRateTemplate.clone();
        	commonRateTemplateFourtyFT.setChargetype("Ocean Freight - 40");
        	commonRateTemplateFourtyFT.setChargetypecode("OF40FT");
        	commonRateTemplateFourtyFT.setRate(fourtyFT);
		    
		    if(twentyFT != null && !twentyFT.equals("") && !twentyFT.equals("-") && !twentyFT.equals("NA")) {
            	commonRateTemplateRequestList.add(commonRateTemplate);
		    }
            
            // for 40		
            if(fourtyFT != null && !fourtyFT.equals("") && !fourtyFT.equals("-") && !fourtyFT.equals("NA")) {
            	commonRateTemplateRequestList.add(commonRateTemplateFourtyFT);
            }
		   
		   
		    count++;
		}
		
		logger.info("No of size() :: commonRateTemplateRequestList= "+commonRateTemplateRequestList.size());
		
		String filename = fileStorageService.generateMaxiconDataToCommonTemplate(userId,commonRateTemplateRequestList,carrierName,uploadFilename);
		
		
        HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Excel File Converted successfully!");
        excelInfo.put("carrierid", carrierFileConvertFormRequest.getCarrier());
        excelInfo.put("carriername", carrierNameWithScacCode);
        excelInfo.put("chargegroupingid", carrierFileConvertFormRequest.getChargeType());
        excelInfo.put("chargegrouping", chargeGrouping);
        excelInfo.put("filename", filename);
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		return baseResponse;
	}
	
   // convert RCL to template
	@Override
	public BaseResponse convertUploadedFileRclToTemplate(CarrierFileConvertFormRequest carrierFileConvertFormRequest,
			MultipartFile file) throws Exception {
		
		String userId = carrierFileConvertFormRequest.getUserId();
		
		String shipmentType = carrierFileConvertFormRequest.getShipmentType();
		Long forwarderId = Long.parseLong(carrierFileConvertFormRequest.getForwarderCha());
		
        Long carrierId = Long.parseLong(carrierFileConvertFormRequest.getCarrier());
		
		Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
		
		String carrierName = carrierEntity.get().getCarriershortname();
		String carrierNameWithScacCode = carrierEntity.get().getCarriername()+" ("+carrierEntity.get().getScaccode()+")";
		
		if(!file.getOriginalFilename().endsWith(".xlsx")) {
			String filename = fileStorageService.storeUnformattedFile(userId,carrierFileConvertFormRequest,file);
			throw new Exception("Error: Invalid carrier file!");	
		}
		
		String uploadFilename = file.getOriginalFilename().replace(".xlsx", "");
		
		
		
        Long chargeTypeId = Long.parseLong(carrierFileConvertFormRequest.getChargeType());
		
		Optional<ChargesGroupingEntity> chargesGroupingEntity = chargeGroupingRepository.findById(chargeTypeId);
		String chargeGrouping = chargesGroupingEntity.get().getChargesgrouping();
	    String chargeGroupingCode = chargesGroupingEntity.get().getChargesgroupingcode();
	    
		baseResponse = new BaseResponse();
		
		logger.info("********************convertUploadedFileRclToTemplate() Method in FileUploadServicesImpl******************");
		
		fis = (FileInputStream) file.getInputStream();
	
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		

		
		if(sh == null) {
			throw new Exception("Error: Invalid carrier file!");
		}
		
		
		int noOfRows = sh.getLastRowNum();
		
		logger.info("No of Rows: noOfRows= "+noOfRows);
		
		List<CommonRateTemplateRequest> commonRateTemplateRequestList = new ArrayList<CommonRateTemplateRequest>();

		String origin = "Nhava Sheva";
		
		
		List<LocationEntity> originEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(origin+"%","N");
		 
		if(originEntityList.size() == 0) {
				throw new Exception("Error: Origin Not Found in master");
		}
		 
		String originLocId = originEntityList.get(0).getId().toString();
		
		int count=1;
		for(int i=5;i<=noOfRows;i++) {
			
			if (isRowEmpty(sh.getRow(i))) {
				logger.info("blank row found");
			}else {
				
				CommonRateTemplateRequest commonRateTemplate = new CommonRateTemplateRequest();
				
				logger.info("**************** "+ count +" **************************");
				
				
				String pod = (sh.getRow(i).getCell(2) == null) ? "" : sh.getRow(i).getCell(2).toString().trim();
				logger.info("POD: "+pod); 
				 
				String routing = (sh.getRow(i).getCell(3) == null) ? "" : sh.getRow(i).getCell(3).toString().trim();
			    logger.info("Routing: "+routing);
			    
			    String transit = (sh.getRow(i).getCell(4) == null)? "" : sh.getRow(i).getCell(4).toString().trim();
			    logger.info("Transit: "+transit);
			    
				String twentyFT = (sh.getRow(i).getCell(5) == null) ? "" : sh.getRow(i).getCell(5).toString().trim();
				logger.info("Twenty Fit Ocean freight: "+twentyFT);
				
				String fourtyFT = (sh.getRow(i).getCell(6) == null) ? "" : sh.getRow(i).getCell(6).toString().trim();			
				logger.info("Fourty Fit Ocean freight: " + fourtyFT);
				

			    logger.info("**************************************************");
			    logger.info("Going to set data into common template pojo class");
			    
			    commonRateTemplate.setOrigin(origin);
		    	commonRateTemplate.setOriginlocid(originLocId);
		    	commonRateTemplate.setOriginlocid(originLocId);
			    commonRateTemplate.setForwarderid(Long.toString(forwarderId));
			    commonRateTemplate.setCarrierid(carrierFileConvertFormRequest.getCarrier());
			    commonRateTemplate.setChargesgroupingid(chargeGrouping);
			    commonRateTemplate.setChargesgroupingcode(chargeGroupingCode);
			    
			    
			    commonRateTemplate.setValiddatefrom(carrierFileConvertFormRequest.getValidDateFrom());
			    commonRateTemplate.setValiddateto(carrierFileConvertFormRequest.getValidDateTo());
			    commonRateTemplate.setRouting(routing);
			    commonRateTemplate.setTransittime(transit);
			    commonRateTemplate.setCurrency("USD");
			    commonRateTemplate.setBasis("Per Container");
			    commonRateTemplate.setQuantity("1");
			    
			    commonRateTemplate.setChargetype("Ocean Freight - 20");
			    commonRateTemplate.setChargetypecode("OF20FT");
			    commonRateTemplate.setRate(twentyFT);
			     
			    List<LocationEntity> destinationEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(pod+"%","N");
			    
			    if( destinationEntityList.size() != 0) {
			    	    	
			    	String destinationLocId = destinationEntityList.get(0).getId().toString();    	
				    commonRateTemplate.setDestination(pod);
				    commonRateTemplate.setDestinationlocid(destinationLocId);
				    commonRateTemplate.setErrorflag("N");    		    
					    	 
			    }else {
			    	logger.info("SKIP Record ==> destination: "+pod+" not found in master");
			    	
	                String destinationLocId = "NA";

				    commonRateTemplate.setDestination(pod);
				    commonRateTemplate.setDestinationlocid(destinationLocId);
				    commonRateTemplate.setErrorflag("Y");	            
			    }
			    
			    CommonRateTemplateRequest commonRateTemplateFourtyFT = (CommonRateTemplateRequest)commonRateTemplate.clone();
	        	commonRateTemplateFourtyFT.setChargetype("Ocean Freight - 40");
	        	commonRateTemplateFourtyFT.setChargetypecode("OF40FT");
	        	commonRateTemplateFourtyFT.setRate(fourtyFT);
			    
			    if(twentyFT != null && !twentyFT.equals("") && !twentyFT.equals("-") && !twentyFT.equals("NA")) {
	            	commonRateTemplateRequestList.add(commonRateTemplate);
			    }
	            
	            // for 40		
	            if(fourtyFT != null && !fourtyFT.equals("") && !fourtyFT.equals("-") && !fourtyFT.equals("NA")) {
	            	commonRateTemplateRequestList.add(commonRateTemplateFourtyFT);
	            }
				
			    
			    
			}
			
		    count++;
		}
		
		logger.info("No of size() :: commonRateTemplateRequestList= "+commonRateTemplateRequestList.size());
		
		String filename = fileStorageService.generateMaxiconDataToCommonTemplate(userId,commonRateTemplateRequestList,carrierName,uploadFilename);
		
		
        HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Excel File Converted successfully!");
        excelInfo.put("carrierid", carrierFileConvertFormRequest.getCarrier());
        excelInfo.put("carriername", carrierNameWithScacCode);
        excelInfo.put("chargegroupingid", carrierFileConvertFormRequest.getChargeType());
        excelInfo.put("chargegrouping", chargeGrouping);
        excelInfo.put("filename", filename);
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		return baseResponse;
	}
	

	@Override
	public BaseResponse uploadTemplateDataRateMaster(String userId, String carrierName,String chargeType, String templateName)throws Exception {
		baseResponse = new BaseResponse();
		
		Long chargeGroupingId = Long.parseLong(chargeType);
		ChargesGroupingEntity chargesGroupingEntity = chargeGroupingRepository.findByIdAndIsdeleted(chargeGroupingId,"N");
		
		if (chargesGroupingEntity == null ) {
			throw new Exception("Error: Charge Type Grouping Not Found");
		}
		
		String carrierIdReq = carrierName;
		String chargesGroupingCodeReq = chargesGroupingEntity.getChargesgroupingcode();
		
				
		List<UploadRateTemporaryEntity> uploadRateTemporaryEntityList = uploadRateTemporaryRepository.findByCarrierIdAndChargesGroupingIdAndErrorStatus(carrierIdReq,chargesGroupingCodeReq,"N");
		
		List<ChargesRateEntity> chargesRateEntityList = new ArrayList<ChargesRateEntity>();
		
		Long carrierid = null;		
		String validdatefrom = null;
		String validdateto = null;
		Long forwarderchaid = null;
		String shipmentType = null;
		
		for(UploadRateTemporaryEntity uploadRateTemporaryEntity  : uploadRateTemporaryEntityList) {
			
			
			ChargesRateEntity chargesRateEntity = new ChargesRateEntity();
			
			String origin = uploadRateTemporaryEntity.getOrigin();
			String originlocid = uploadRateTemporaryEntity.getOriginlocid();
			String destination = uploadRateTemporaryEntity.getDestination();
			String destinationlocid = uploadRateTemporaryEntity.getDestinationlocid();
			
			long forwarderid = Long.parseLong(uploadRateTemporaryEntity.getForwarderid());
			String shipmenttype = uploadRateTemporaryEntity.getShipmenttype();
			forwarderchaid = forwarderid;
			shipmentType = shipmenttype;
			
			String carrierId = uploadRateTemporaryEntity.getCarrierid();
			carrierid = Long.parseLong(carrierId);
			String chargesGrouping = uploadRateTemporaryEntity.getChargesgrouping();
			
			String chargesGroupingCode = uploadRateTemporaryEntity.getChargesgroupingcode();
			
			String chargesType = uploadRateTemporaryEntity.getChargestype();
			String chargesTypeCode = uploadRateTemporaryEntity.getChargestypecode();
			String cargoCategory = uploadRateTemporaryEntity.getCargocategory();
			
			String validDateFrom =  uploadRateTemporaryEntity.getValiddatefrom();
			validdatefrom = validDateFrom;
			String validDateTo = uploadRateTemporaryEntity.getValiddateto();
			validdateto = validDateTo;
			
			String routing = uploadRateTemporaryEntity.getRouting();
			String transitTime = uploadRateTemporaryEntity.getTransittime();
			String currency = uploadRateTemporaryEntity.getCurrency();
			String basis = uploadRateTemporaryEntity.getBasis();
			String quantity = uploadRateTemporaryEntity.getQuantity();
			String rate = uploadRateTemporaryEntity.getRate();
			String errorStatus = uploadRateTemporaryEntity.getErrorstatus();
			
			String isdeleted = "N";
			
			chargesRateEntity.setCreateby(userId);
			chargesRateEntity.setOrigin(origin);
			chargesRateEntity.setOriginlocid(Long.parseLong(originlocid));
			chargesRateEntity.setDestination(destination);
			chargesRateEntity.setDestinationlocid(Long.parseLong(destinationlocid));
			chargesRateEntity.setCarrierid(Long.parseLong(carrierId));
			
			chargesRateEntity.setForwarderid(forwarderid);
			chargesRateEntity.setShipmenttype(shipmenttype);		
			
			chargesRateEntity.setChargesgroupingid(chargeGroupingId);
			
			chargesRateEntity.setChargetype(chargesType);
			chargesRateEntity.setChargetypecode(chargesTypeCode);
			
			chargesRateEntity.setCargocategory(cargoCategory);
			chargesRateEntity.setValiddatefrom(validDateFrom);
			chargesRateEntity.setValiddateto(validDateTo);
			
			chargesRateEntity.setRouting(routing);
			chargesRateEntity.setTransittime(transitTime);
			chargesRateEntity.setCurrency(currency);
			chargesRateEntity.setBasis(basis);
			chargesRateEntity.setQuantity(quantity);
			chargesRateEntity.setRate(rate);
			
			
			chargesRateEntity.setIsdeleted(isdeleted);
						
			if(!rate.equals("")) {
				chargesRateEntityList.add(chargesRateEntity);
				
			}

			
		}
				
		if(chargesRateEntityList.size() != 0) {
			//chargesRateRepository.saveAll(chargesRateEntityList);
			
		   
			Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierid);			
			String selectCarrierName = carrierEntity.get().getCarriershortname();
			
			String filename = fileStorageService.updateCommonTemplate(userId,uploadRateTemporaryEntityList,selectCarrierName,templateName);
			
			String recordscount = Integer.toString(chargesRateEntityList.size());
			UploadRateHistoryEntity uploadRateHistory = new UploadRateHistoryEntity();
		    uploadRateHistory.setCreateby("system");
		
		    uploadRateHistory.setForwarderid(forwarderchaid);
		    uploadRateHistory.setShipmenttype(shipmentType);
		    
	    	uploadRateHistory.setCarrierid(carrierid);
		    uploadRateHistory.setChargetypeid(chargeGroupingId);
		    uploadRateHistory.setValiddatefrom(validdatefrom);
		    uploadRateHistory.setValiddateto(validdateto);
		    uploadRateHistory.setRecordscount(recordscount);
		    uploadRateHistory.setFilename(templateName);
		    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		    uploadRateHistory.setUploadeddate(timeStamp);
		    uploadRateHistory.setStatus("Uploaded");
		
            uploadRateHistory.setIsdeleted("N");
		
            uploadRateHistoryRepository.save(uploadRateHistory);
		}
		
		

		HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Template data Uploaded into masater successfully!");
        excelInfo.put("carriername", carrierName);
        excelInfo.put("filename", templateName);
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		
		
		return baseResponse;
		
	}


	@Override
	public BaseResponse convertPdfToExcel(MultipartFile file)throws Exception {
		
		baseResponse = new BaseResponse();
		
		//pDFToExcelService.pdfToExcelConvertion(file);
		
		HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "PDF File Converted into excel successfully!");
       
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		
		
		return baseResponse;
	}


	@Override
	public BaseResponse uploadDirectCarrierRateTemplateIntoMaster(
			CarrierFileConvertFormRequest carrierFileConvertFormRequest, MultipartFile file) throws Exception {
        baseResponse = new BaseResponse();
		
		HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Template File uploaded successfully!!!");
       
		
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		
		
		return baseResponse;
	
	}


	@Override
	public BaseResponse uploadOriginDestinationDataFileToMaster(MultipartFile file) throws Exception {
        baseResponse = new BaseResponse();
		
		logger.info("********************uploadOriginDestinationDataFileToMaster Method in FileUploadServicesImpl******************");
		
		fis = (FileInputStream) file.getInputStream();
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("UN_Locations");
		
		int noOfRows = sh.getLastRowNum();
		
		System.out.println(noOfRows);
		
		List<LocationEntity> locationList = new ArrayList<LocationEntity>();
		
		for(int i=1;i<=noOfRows;i++) {
			
			logger.info("**************** "+ i +" **************************");
			
			String locationcode = (sh.getRow(i).getCell(0) == null) ? "" : sh.getRow(i).getCell(0).toString().trim();
			String countrycode = (sh.getRow(i).getCell(1) == null) ? "" : sh.getRow(i).getCell(1).toString().trim();
			String countryname = (sh.getRow(i).getCell(2) == null) ? "" : sh.getRow(i).getCell(2).toString().trim();			
		    String citycode = (sh.getRow(i).getCell(3) == null) ? "" : sh.getRow(i).getCell(3).toString().trim();
		    String locationname = (sh.getRow(i).getCell(4) == null)? "" : sh.getRow(i).getCell(4).toString().trim();
		    
		    logger.info("locationcode: "+locationcode+" | countrycode: "+countrycode+" | countryname: "+countryname+" | citycode: "+citycode+" | locationname: "+locationname); 
		    
		    LocationEntity locationEntity = new LocationEntity();
		    
		    locationEntity.setCreateby("system");
		    
		    locationEntity.setLocationcode(locationcode);
		    locationEntity.setCountrycode(countrycode);
		    locationEntity.setCountryname(countryname);
		    locationEntity.setCitycode(citycode);
		    locationEntity.setLocationname(locationname);
		    
		    locationEntity.setIsdeleted("N");
			    
		    locationList.add(locationEntity);
		    
		    logger.info("entity added into list :: list size: "+locationList.size());
		    logger.info("******************************************");
		}
		
		 
		 
		if(locationList.size() != 0) {
			//locationRepository.saveAll(locationList);
		}
		
		logger.info("Data uploaded into master successfully");
		
        HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Origin,Destination Location Excel File data Uploaded successfully!");
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		return baseResponse;
	}
	
	private static boolean isRowEmpty(Row row) {
		boolean isEmpty = true;
		DataFormatter dataFormatter = new DataFormatter();

		if (row != null) {
			for (Cell cell : row) {
				if (dataFormatter.formatCellValue(cell).trim().length() > 0) {
					isEmpty = false;
					break;
				}
			}
		}

		return isEmpty;
	}


	@Override
	public BaseResponse getTemplateDataRateForPreview(Resource resource, String userId, String carrierName,String templateName) throws Exception {
        baseResponse = new BaseResponse();
		
		File file =  resource.getFile();				  
		fis = new FileInputStream(file);
		
		wb = WorkbookFactory.create(fis);
		sh = wb.getSheet("Sheet1");
		
		int noOfRows = sh.getLastRowNum();
			
		logger.info("No of Rows: noOfRows= "+noOfRows);
		
		RatePreviewListResponse ratePreviewListResponse = new RatePreviewListResponse();
		
		List<CommonRateTemplateResponse> commonRateTemplateRespList = new ArrayList<CommonRateTemplateResponse>();
		List<CommonRateTemplateResponse> commonRateTemplateRespErrorList = new ArrayList<CommonRateTemplateResponse>();
		
		List<UploadRateTemporaryEntity> uploadRateTemporaryEntityList = uploadRateTemporaryRepository.findAll();
		
		int count=1;
		for(UploadRateTemporaryEntity uploadRateTemporaryEntity : uploadRateTemporaryEntityList) {
			//int i=1;i<=noOfRows;i++
			logger.info("********************"+count+"******************");
			
			CommonRateTemplateResponse commonRateTemplateResponse = new CommonRateTemplateResponse();
			
			/*
			String serialnumber = (sh.getRow(i).getCell(0) == null) ? "" : sh.getRow(i).getCell(0).toString().trim();
			logger.info("serialnumber: "+serialnumber);
			
			String origin = (sh.getRow(i).getCell(1) == null) ? "" : sh.getRow(i).getCell(1).toString().trim();
			String originlocid = (sh.getRow(i).getCell(2) == null) ? "" : sh.getRow(i).getCell(2).toString().trim();
			String destination = (sh.getRow(i).getCell(3) == null) ? "" : sh.getRow(i).getCell(3).toString().trim();
			String destinationlocid = (sh.getRow(i).getCell(4) == null) ? "" : sh.getRow(i).getCell(4).toString().trim();
			String carrierId = (sh.getRow(i).getCell(5) == null) ? "" : sh.getRow(i).getCell(5).toString().trim();
			
			String chargesGrouping = (sh.getRow(i).getCell(6) == null) ? "" : sh.getRow(i).getCell(6).toString().trim();
			
			String chargesGroupingCode = (sh.getRow(i).getCell(7) == null) ? "" : sh.getRow(i).getCell(7).toString().trim();
			
			String chargesType = (sh.getRow(i).getCell(8) == null) ? "" : sh.getRow(i).getCell(8).toString().trim();
			String chargesTypeCode = (sh.getRow(i).getCell(9) == null) ? "" : sh.getRow(i).getCell(9).toString().trim();
			String cargoCategory = (sh.getRow(i).getCell(10) == null) ? "" : sh.getRow(i).getCell(10).toString().trim();
			
			String validDateFrom =  (sh.getRow(i).getCell(11) == null) ? "" : sh.getRow(i).getCell(11).toString().trim();
			
			String validDateTo = (sh.getRow(i).getCell(12) == null) ? "" : sh.getRow(i).getCell(12).toString().trim();
			
			
			String routing = (sh.getRow(i).getCell(13) == null) ? "" : sh.getRow(i).getCell(13).toString().trim();
			String transitTime = (sh.getRow(i).getCell(14) == null) ? "" : sh.getRow(i).getCell(14).toString().trim();
			String currency = (sh.getRow(i).getCell(15) == null) ? "" : sh.getRow(i).getCell(15).toString().trim();
			String basis = (sh.getRow(i).getCell(16) == null) ? "" : sh.getRow(i).getCell(16).toString().trim();
			String quantity = (sh.getRow(i).getCell(17) == null) ? "" : sh.getRow(i).getCell(17).toString().trim();
			String rate = (sh.getRow(i).getCell(18) == null) ? "" : sh.getRow(i).getCell(18).toString().trim();
			String errorStatus = (sh.getRow(i).getCell(19) == null) ? "" : sh.getRow(i).getCell(19).toString().trim();
			
			*/
			
			commonRateTemplateResponse.setId(uploadRateTemporaryEntity.getId());
			commonRateTemplateResponse.setSerialnumber(uploadRateTemporaryEntity.getSerialnumber());
			commonRateTemplateResponse.setOrigin(uploadRateTemporaryEntity.getOrigin());
			commonRateTemplateResponse.setOriginlocid(uploadRateTemporaryEntity.getOriginlocid());
			commonRateTemplateResponse.setDestination(uploadRateTemporaryEntity.getDestination());
			commonRateTemplateResponse.setDestinationlocid(uploadRateTemporaryEntity.getDestinationlocid());
			commonRateTemplateResponse.setCarrierid(uploadRateTemporaryEntity.getCarrierid());
			
			commonRateTemplateResponse.setChargesgroupingid(uploadRateTemporaryEntity.getChargesgrouping());
			commonRateTemplateResponse.setChargesgroupingcode(uploadRateTemporaryEntity.getChargesgroupingcode());
			
			commonRateTemplateResponse.setChargetype(uploadRateTemporaryEntity.getChargestype());
			commonRateTemplateResponse.setChargetypecode(uploadRateTemporaryEntity.getChargestypecode());
			
			commonRateTemplateResponse.setCargocategory(uploadRateTemporaryEntity.getCargocategory());
			commonRateTemplateResponse.setValiddatefrom(uploadRateTemporaryEntity.getValiddatefrom());
			commonRateTemplateResponse.setValiddateto(uploadRateTemporaryEntity.getValiddateto());
			
			commonRateTemplateResponse.setRouting(uploadRateTemporaryEntity.getRouting());
			commonRateTemplateResponse.setTransittime(uploadRateTemporaryEntity.getTransittime());
			commonRateTemplateResponse.setCurrency(uploadRateTemporaryEntity.getCurrency());
			commonRateTemplateResponse.setBasis(uploadRateTemporaryEntity.getBasis());
			commonRateTemplateResponse.setQuantity(uploadRateTemporaryEntity.getQuantity());
			commonRateTemplateResponse.setRate(uploadRateTemporaryEntity.getRate());
			commonRateTemplateResponse.setErrorflag(uploadRateTemporaryEntity.getErrorstatus());
			
						
			if(!uploadRateTemporaryEntity.getRate().equals("")) {
				if(uploadRateTemporaryEntity.getErrorstatus().equals("Y")) {
					commonRateTemplateRespErrorList.add(commonRateTemplateResponse);
				}else {
					commonRateTemplateRespList.add(commonRateTemplateResponse);
				}
				
			}
			
			
			count++;
			
		}
		
		ratePreviewListResponse.setCarriername(carrierName);
		ratePreviewListResponse.setTemplatename(templateName);
		ratePreviewListResponse.setCommonRateTemplateRespList(commonRateTemplateRespList);
		ratePreviewListResponse.setCommonRateTemplateRespErrorList(commonRateTemplateRespErrorList);
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(ratePreviewListResponse);
		
		
		return baseResponse;
	}	
	
	//use on excel template 
    public BaseResponse updateTemplateRateFileRecord(Resource resource,UpdateRateTemplateRecordRequest updateRateTemplateRecordRequest) throws Exception {
		
        baseResponse = new BaseResponse();
		
    
        
		File file =  resource.getFile();				  
		fis = new FileInputStream(file);
		
		
		 XSSFWorkbook workbook = new XSSFWorkbook(fis); 
		sh = wb.getSheet("Sheet1");
		
		Cell cell = null;
		
		int noOfRows = sh.getLastRowNum();
			
		logger.info("No of Rows: noOfRows= "+noOfRows);
	
		
		int count=1;
		for(int i=1;i<=noOfRows;i++) {
			
			logger.info("********************"+count+"******************");
			
			
			String serialnumber = (sh.getRow(i).getCell(0) == null) ? "" : sh.getRow(i).getCell(0).toString().trim();
			logger.info("serialnumber: "+serialnumber);
			
			if(serialnumber.equals(updateRateTemplateRecordRequest.getSerialNumber())) {
				logger.info("serialnumber match: "+serialnumber);
				
				String pod = updateRateTemplateRecordRequest.getDestination();
				List<LocationEntity> destinationEntityList = locationRepository.findByLocationnameLikeWithIgnoreCaseAndIsdeleted(pod+"%","N");
			    
				if(destinationEntityList.size() == 0) {
					throw new Exception("Error: Destination Not Found in master");
			    }
				
			    if( destinationEntityList.size() != 0) {
			    	    	
			    	String destinationLocId = destinationEntityList.get(0).getId().toString();    	
			    	
			    	
			    	cell = sh.getRow(i).getCell(3);
			        
			    	cell.setCellValue(pod);
			        				    
					
					cell = sh.getRow(i).getCell(4);
			        cell.setCellValue(destinationLocId);
			        
					
					cell = sh.getRow(i).getCell(19);
			        cell.setCellValue("N");
					
			        
			        fis.close(); 
			         
		            FileOutputStream outFile =new FileOutputStream(file);
		            workbook.write(outFile);
		            workbook.close();
		            outFile.close();
			        
					break;
			    }
			    
				
				
			}
			

			count++;
			
		}
		
		
		HashMap<String ,String>  excelInfo = new HashMap<String ,String>();
        excelInfo.put("msg", "Record Updated Seccessfully!");
	
        
		baseResponse.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
		baseResponse.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
		baseResponse.setRespData(excelInfo);
		
		
		return baseResponse;
	}
	
}