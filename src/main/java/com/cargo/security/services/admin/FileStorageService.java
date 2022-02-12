package com.cargo.security.services.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.constants.MaxiconColumnUtils;
import com.cargo.exception.FileStorageException;
import com.cargo.load.request.CarrierFileConvertFormRequest;
import com.cargo.load.request.CommonRateTemplateRequest;
import com.cargo.models.CarrierEntity;
import com.cargo.models.FileStorageProperties;
import com.cargo.models.UploadRateHistoryEntity;
import com.cargo.models.UploadRateTemporaryEntity;
import com.cargo.repository.CarrierRepository;
import com.cargo.repository.UploadRateHistoryRepository;
import com.cargo.repository.UploadRateTemporaryRepository;
import com.cargo.utils.UploadPathContUtils;


@Service
public class FileStorageService{
	private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);
	
	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	@Autowired
	CarrierRepository carrierRepository;
	
	@Autowired
	UploadRateHistoryRepository uploadRateHistoryRepository;
	
	@Autowired
	UploadRateTemporaryRepository uploadRateTemporaryRepository;
	
	@Autowired
	public FileStorageService(FileStorageProperties fileStorageProperties) {
		this.fileBaseLocation = fileStorageProperties.getUploadDir();
	}
	
	public final static String fileExtension = ".xlsx";
	public final static  String ratesDirPath = UploadPathContUtils.FILE_RATES_DIR;
	public final static String bookingDirPath = UploadPathContUtils.FILE_BOOKING_DIR;
	
 public String generateMaxiconDataToCommonTemplate(String userId,List<CommonRateTemplateRequest> commonRateTemplateRequestList,String fileSubPath,String uploadFilename) {
		 	
	    //declare file name to be create   
	    //String filename = "C:\\Balance.xlsx";
	    String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	    String filename = uploadFilename+"_"+timeStamp+""+fileExtension;
		
	    try   
		 {   
			 //creating an instance of HSSFWorkbook class  
		   //HSSFWorkbook workbook = new HSSFWorkbook();  
		   Workbook workbook = new XSSFWorkbook();
		   
		    XSSFFont font = (XSSFFont) workbook.createFont();
			font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
			font.setFontHeightInPoints((short) 12);
			font.setColor(IndexedColors.BLACK.getIndex());
			
			
			XSSFCellStyle topHeaderStyle = (XSSFCellStyle) workbook.createCellStyle();
			topHeaderStyle.setFont(font);
			topHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
			topHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			topHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			topHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			topHeaderStyle.setBorderLeft(BorderStyle.THIN);
			topHeaderStyle.setBorderRight(BorderStyle.THIN);
			topHeaderStyle.setBorderTop(BorderStyle.THIN);
			topHeaderStyle.setBorderBottom(BorderStyle.THIN);
			topHeaderStyle.setWrapText(true);
			
			XSSFCellStyle cellBorderStyle = (XSSFCellStyle) workbook.createCellStyle();
			cellBorderStyle.setBorderLeft(BorderStyle.THIN);
			cellBorderStyle.setBorderRight(BorderStyle.THIN);
			cellBorderStyle.setBorderTop(BorderStyle.THIN);
			cellBorderStyle.setBorderBottom(BorderStyle.THIN);
			cellBorderStyle.setAlignment(HorizontalAlignment.CENTER);
			cellBorderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		   
		   //invoking creatSheet() method and passing the name of the sheet to be created   
		   //HSSFSheet sheet = workbook.createSheet("Sheet1");  
		   Sheet sheet1 = workbook.createSheet("Sheet1");
		   //creating the 0th row using the createRow() method   
		   Row rowhead = sheet1.createRow((short)0);  
		   //creating cell by using the createCell() method and setting the values to the cell by using the setCellValue() method  
		   rowhead.createCell(0).setCellValue(MaxiconColumnUtils.CL_SR_NO);  
		   rowhead.createCell(1).setCellValue(MaxiconColumnUtils.CL_ORIGIN);  
		   rowhead.createCell(2).setCellValue(MaxiconColumnUtils.CL_ORIGIN_LOC_ID);
		   rowhead.createCell(3).setCellValue(MaxiconColumnUtils.CL_DESTINATION);  
		   rowhead.createCell(4).setCellValue(MaxiconColumnUtils.CL_DESTINATION_LOC_ID);
		   rowhead.createCell(5).setCellValue(MaxiconColumnUtils.CL_FORWARDER_CHA_ID);
		   rowhead.createCell(6).setCellValue(MaxiconColumnUtils.CL_SHIPMENT_TYPE);
		   rowhead.createCell(7).setCellValue(MaxiconColumnUtils.CL_CARRIER);  
		   rowhead.createCell(8).setCellValue(MaxiconColumnUtils.CL_CHARGES_GROUPING);  
		   rowhead.createCell(9).setCellValue(MaxiconColumnUtils.CL_CHARGES_GROUPING_CODE); 
		   rowhead.createCell(10).setCellValue(MaxiconColumnUtils.CL_CHARGES_TYPE); 
		   rowhead.createCell(11).setCellValue(MaxiconColumnUtils.CL_CHARGES_TYPE_CODE);
		   rowhead.createCell(12).setCellValue(MaxiconColumnUtils.CL_CARGO_CATEGORY);
		   rowhead.createCell(13).setCellValue(MaxiconColumnUtils.CL_VALID_DATE_FROM);
		   rowhead.createCell(14).setCellValue(MaxiconColumnUtils.CL_VALID_DATE_TO);
		   rowhead.createCell(15).setCellValue(MaxiconColumnUtils.CL_ROUTING);
		   rowhead.createCell(16).setCellValue(MaxiconColumnUtils.CL_TRANSIT_TIME);
		   rowhead.createCell(17).setCellValue(MaxiconColumnUtils.CL_CURRENCY);
		   rowhead.createCell(18).setCellValue(MaxiconColumnUtils.CL_BASIS);
		   rowhead.createCell(19).setCellValue(MaxiconColumnUtils.CL_QUANTITY);
		   rowhead.createCell(20).setCellValue(MaxiconColumnUtils.CL_RATE);
		   rowhead.createCell(21).setCellValue(MaxiconColumnUtils.CL_ERROR_STATUS);
		   
		   int count = 1;
		   
		   List<UploadRateTemporaryEntity> uploadRateTemporaryEntityList = new ArrayList<UploadRateTemporaryEntity>();
		   
		   for(CommonRateTemplateRequest commonRateTemplateRequest : commonRateTemplateRequestList) {
			   
			   UploadRateTemporaryEntity uploadRateTemporaryEntity = new UploadRateTemporaryEntity();
			   
			   Row row = sheet1.createRow((short) count );  
			   
			   row.createCell(0).setCellValue(Integer.toString(count));  
			   row.createCell(1).setCellValue(commonRateTemplateRequest.getOrigin());
			   row.createCell(2).setCellValue(commonRateTemplateRequest.getOriginlocid());
			   row.createCell(3).setCellValue(commonRateTemplateRequest.getDestination());  
			   row.createCell(4).setCellValue(commonRateTemplateRequest.getDestinationlocid());
			   row.createCell(5).setCellValue(commonRateTemplateRequest.getForwarderid()); 
			   row.createCell(6).setCellValue(commonRateTemplateRequest.getShipmenttype());
			   row.createCell(7).setCellValue(commonRateTemplateRequest.getCarrierid());  
			   row.createCell(8).setCellValue(commonRateTemplateRequest.getChargesgroupingid());  
			   row.createCell(9).setCellValue(commonRateTemplateRequest.getChargesgroupingcode()); 
			   row.createCell(10).setCellValue(commonRateTemplateRequest.getChargetype()); 
			   row.createCell(11).setCellValue(commonRateTemplateRequest.getChargetypecode());
			   row.createCell(12).setCellValue(commonRateTemplateRequest.getCargocategory());
			   row.createCell(13).setCellValue(commonRateTemplateRequest.getValiddatefrom());
			   row.createCell(14).setCellValue(commonRateTemplateRequest.getValiddateto());
			   row.createCell(15).setCellValue(commonRateTemplateRequest.getRouting());
			   row.createCell(16).setCellValue(commonRateTemplateRequest.getTransittime());
			   row.createCell(17).setCellValue(commonRateTemplateRequest.getCurrency());
			   row.createCell(18).setCellValue(commonRateTemplateRequest.getBasis());
			   row.createCell(19).setCellValue(commonRateTemplateRequest.getQuantity());
			   row.createCell(20).setCellValue(commonRateTemplateRequest.getRate());
			   row.createCell(21).setCellValue(commonRateTemplateRequest.getErrorflag());
			   
			   uploadRateTemporaryEntity.setCreateby("system");
			   uploadRateTemporaryEntity.setUserid(userId);
			   uploadRateTemporaryEntity.setSerialnumber(Integer.toString(count));
			   uploadRateTemporaryEntity.setOrigin(commonRateTemplateRequest.getOrigin());
			   uploadRateTemporaryEntity.setOriginlocid(commonRateTemplateRequest.getOriginlocid());
			   uploadRateTemporaryEntity.setDestination(commonRateTemplateRequest.getDestination());
			   uploadRateTemporaryEntity.setDestinationlocid(commonRateTemplateRequest.getDestinationlocid());
			  
			   uploadRateTemporaryEntity.setForwarderid(commonRateTemplateRequest.getForwarderid());
			   uploadRateTemporaryEntity.setShipmenttype(commonRateTemplateRequest.getShipmenttype());
			   
			   uploadRateTemporaryEntity.setCarrierid(commonRateTemplateRequest.getCarrierid());
			   uploadRateTemporaryEntity.setChargesgrouping(commonRateTemplateRequest.getChargesgroupingid());
			   uploadRateTemporaryEntity.setChargesgroupingcode(commonRateTemplateRequest.getChargesgroupingcode());
			   uploadRateTemporaryEntity.setChargestype(commonRateTemplateRequest.getChargetype());
			   uploadRateTemporaryEntity.setChargestypecode(commonRateTemplateRequest.getChargetypecode());
			   uploadRateTemporaryEntity.setCargocategory(commonRateTemplateRequest.getCargocategory());
			   uploadRateTemporaryEntity.setValiddatefrom(commonRateTemplateRequest.getValiddatefrom());
			   uploadRateTemporaryEntity.setValiddateto(commonRateTemplateRequest.getValiddateto());
			   uploadRateTemporaryEntity.setRouting(commonRateTemplateRequest.getRouting());
			   uploadRateTemporaryEntity.setTransittime(commonRateTemplateRequest.getTransittime());
			   uploadRateTemporaryEntity.setCurrency(commonRateTemplateRequest.getCurrency());
			   uploadRateTemporaryEntity.setBasis(commonRateTemplateRequest.getBasis());
			   uploadRateTemporaryEntity.setQuantity(commonRateTemplateRequest.getQuantity());
			   uploadRateTemporaryEntity.setRate(commonRateTemplateRequest.getRate());
			   uploadRateTemporaryEntity.setErrorstatus(commonRateTemplateRequest.getErrorflag());
			   uploadRateTemporaryEntity.setIsdeleted("N");
			   
			   uploadRateTemporaryEntityList.add(uploadRateTemporaryEntity);
			   
			   count ++;
		   }
		   	   
		   String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+fileSubPath;
		   this.fileStorageLocation = Paths.get(newPath)
				   .toAbsolutePath().normalize();
		   
		   Files.createDirectories(this.fileStorageLocation);
		  
		   
		   String fileFullPath = newPath+"/"+filename;
		   
		   File existingFile = new File(fileFullPath);

		   if (existingFile.exists() && existingFile.isFile()) {
		       existingFile.delete();
		       
		     }

		   //store into temporory table
		   uploadRateTemporaryRepository.deleteAll();
		   uploadRateTemporaryRepository.saveAll(uploadRateTemporaryEntityList);
		   
		   FileOutputStream fileOut = new FileOutputStream(fileFullPath);  
		   workbook.write(fileOut);  
		   //closing the Stream  
		   fileOut.close();  
		   //closing the workbook  
		   workbook.close();  
		   //prints the message on the console  
		   logger.info("Excel file has been converted successfully.");  
		 }   
		 catch (Exception e)   
		 {  
		    e.printStackTrace();  
		 }
		 
		 return filename;
	 }
 
 
     public Resource loadFile(String carrierName,String fileName) throws FileNotFoundException {
        
        String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carrierName;
       
        try {
        	
           this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();	
           Path file = this.fileStorageLocation.resolve(fileName).normalize();       
           Resource resource = new UrlResource(file.toUri());

           if (resource.exists() || resource.isReadable()) {
              return resource;
           } 
           else {
        	   resource = null;
        	   return resource;
           }
         } 
         catch (MalformedURLException e) {
              throw new FileNotFoundException("Error: Could not download file");
         }           
     }
     
     public void deleteFile(String userId,String carrierName,String fileName) throws FileNotFoundException {
  
            String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carrierName;
         
            this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
		   
		   //Files.createDirectories(this.fileStorageLocation);
		   
		    String fileFullPath = newPath+"/"+fileName;
		    File existingFile = new File(fileFullPath);

		    if (existingFile.exists() && existingFile.isFile()) {
		        existingFile.delete();
		    } 
		    else {
		        throw new FileNotFoundException("Error: Could not find file");
		    }           
      }


	public File loadFileFullPathWithName(String fileName, String fileSubDir) throws FileNotFoundException {
		 
		   String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+fileSubDir;
         
           this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
           
		    String fileFullPath = newPath+"/"+fileName;
		    File existingFile = new File(fileFullPath);

		    if (existingFile.exists() && existingFile.isFile()) {
		       return existingFile;
		    } 
		    else {
		        throw new FileNotFoundException("Error: Could not find file");
		    }  
	}
	
	public File loadBookingFileFullPathWithName(String fileName, String fileBookingDir) throws FileNotFoundException {
		String newPath = this.fileBaseLocation+"/"+fileBookingDir;
	      
        this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
     
		    String fileFullPath = newPath+"/"+fileName;
		    File existingFile = new File(fileFullPath);

		    if (existingFile.exists() && existingFile.isFile()) {
		       return existingFile;
		    } 
		    else {
		        throw new FileNotFoundException("Error: Could not find file");
		    }  
	}


	public String updateCommonTemplate(String userId, List<UploadRateTemporaryEntity> uploadRateTemporaryEntityList,String carrierName, String templateName) {
		 
	    try   
		 {   
			 //creating an instance of HSSFWorkbook class  
		   //HSSFWorkbook workbook = new HSSFWorkbook();  
		   Workbook workbook = new XSSFWorkbook();
		   
		    XSSFFont font = (XSSFFont) workbook.createFont();
			font.setFontName(XSSFFont.DEFAULT_FONT_NAME);
			font.setFontHeightInPoints((short) 12);
			font.setColor(IndexedColors.BLACK.getIndex());
			
			
			XSSFCellStyle topHeaderStyle = (XSSFCellStyle) workbook.createCellStyle();
			topHeaderStyle.setFont(font);
			topHeaderStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
			topHeaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			topHeaderStyle.setAlignment(HorizontalAlignment.CENTER);
			topHeaderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			topHeaderStyle.setBorderLeft(BorderStyle.THIN);
			topHeaderStyle.setBorderRight(BorderStyle.THIN);
			topHeaderStyle.setBorderTop(BorderStyle.THIN);
			topHeaderStyle.setBorderBottom(BorderStyle.THIN);
			topHeaderStyle.setWrapText(true);
			
			XSSFCellStyle cellBorderStyle = (XSSFCellStyle) workbook.createCellStyle();
			cellBorderStyle.setBorderLeft(BorderStyle.THIN);
			cellBorderStyle.setBorderRight(BorderStyle.THIN);
			cellBorderStyle.setBorderTop(BorderStyle.THIN);
			cellBorderStyle.setBorderBottom(BorderStyle.THIN);
			cellBorderStyle.setAlignment(HorizontalAlignment.CENTER);
			cellBorderStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		   
		   //invoking creatSheet() method and passing the name of the sheet to be created   
		   //HSSFSheet sheet = workbook.createSheet("Sheet1");  
		   Sheet sheet1 = workbook.createSheet("Sheet1");
		   //creating the 0th row using the createRow() method   
		   Row rowhead = sheet1.createRow((short)0);  
		   //creating cell by using the createCell() method and setting the values to the cell by using the setCellValue() method  
		   rowhead.createCell(0).setCellValue(MaxiconColumnUtils.CL_SR_NO);  
		   rowhead.createCell(1).setCellValue(MaxiconColumnUtils.CL_ORIGIN);  
		   rowhead.createCell(2).setCellValue(MaxiconColumnUtils.CL_ORIGIN_LOC_ID);
		   rowhead.createCell(3).setCellValue(MaxiconColumnUtils.CL_DESTINATION);  
		   rowhead.createCell(4).setCellValue(MaxiconColumnUtils.CL_DESTINATION_LOC_ID);
		   
		   rowhead.createCell(5).setCellValue(MaxiconColumnUtils.CL_FORWARDER_CHA_ID);  
		   rowhead.createCell(6).setCellValue(MaxiconColumnUtils.CL_SHIPMENT_TYPE);
		   
		   rowhead.createCell(7).setCellValue(MaxiconColumnUtils.CL_CARRIER);  
		   rowhead.createCell(8).setCellValue(MaxiconColumnUtils.CL_CHARGES_GROUPING);  
		   rowhead.createCell(9).setCellValue(MaxiconColumnUtils.CL_CHARGES_GROUPING_CODE); 
		   rowhead.createCell(10).setCellValue(MaxiconColumnUtils.CL_CHARGES_TYPE); 
		   rowhead.createCell(11).setCellValue(MaxiconColumnUtils.CL_CHARGES_TYPE_CODE);
		   rowhead.createCell(12).setCellValue(MaxiconColumnUtils.CL_CARGO_CATEGORY);
		   rowhead.createCell(13).setCellValue(MaxiconColumnUtils.CL_VALID_DATE_FROM);
		   rowhead.createCell(14).setCellValue(MaxiconColumnUtils.CL_VALID_DATE_TO);
		   rowhead.createCell(15).setCellValue(MaxiconColumnUtils.CL_ROUTING);
		   rowhead.createCell(16).setCellValue(MaxiconColumnUtils.CL_TRANSIT_TIME);
		   rowhead.createCell(17).setCellValue(MaxiconColumnUtils.CL_CURRENCY);
		   rowhead.createCell(18).setCellValue(MaxiconColumnUtils.CL_BASIS);
		   rowhead.createCell(19).setCellValue(MaxiconColumnUtils.CL_QUANTITY);
		   rowhead.createCell(20).setCellValue(MaxiconColumnUtils.CL_RATE);
		   rowhead.createCell(21).setCellValue(MaxiconColumnUtils.CL_ERROR_STATUS);
		   
		   int count = 1;
		   
		   
		   for(UploadRateTemporaryEntity uploadRateTemporaryEntity : uploadRateTemporaryEntityList) {
			      
			   Row row = sheet1.createRow((short) count );  
			   
			   row.createCell(0).setCellValue(Integer.toString(count));  
			   row.createCell(1).setCellValue(uploadRateTemporaryEntity.getOrigin());
			   row.createCell(2).setCellValue(uploadRateTemporaryEntity.getOriginlocid());
			   row.createCell(3).setCellValue(uploadRateTemporaryEntity.getDestination());  
			   row.createCell(4).setCellValue(uploadRateTemporaryEntity.getDestinationlocid());
			   
			   row.createCell(5).setCellValue(uploadRateTemporaryEntity.getForwarderid());  
			   row.createCell(6).setCellValue(uploadRateTemporaryEntity.getShipmenttype());
			   
			   row.createCell(7).setCellValue(uploadRateTemporaryEntity.getCarrierid());  
			   row.createCell(8).setCellValue(uploadRateTemporaryEntity.getChargesgrouping()); 
			   row.createCell(9).setCellValue(uploadRateTemporaryEntity.getChargesgroupingcode()); 
			   row.createCell(10).setCellValue(uploadRateTemporaryEntity.getChargestype()); 
			   row.createCell(11).setCellValue(uploadRateTemporaryEntity.getChargestypecode());
			   row.createCell(12).setCellValue(uploadRateTemporaryEntity.getCargocategory());
			   row.createCell(13).setCellValue(uploadRateTemporaryEntity.getValiddatefrom());
			   row.createCell(14).setCellValue(uploadRateTemporaryEntity.getValiddateto());
			   row.createCell(15).setCellValue(uploadRateTemporaryEntity.getRouting());
			   row.createCell(16).setCellValue(uploadRateTemporaryEntity.getTransittime());
			   row.createCell(17).setCellValue(uploadRateTemporaryEntity.getCurrency());
			   row.createCell(18).setCellValue(uploadRateTemporaryEntity.getBasis());
			   row.createCell(19).setCellValue(uploadRateTemporaryEntity.getQuantity());
			   row.createCell(20).setCellValue(uploadRateTemporaryEntity.getRate());
			   row.createCell(21).setCellValue(uploadRateTemporaryEntity.getErrorstatus());
			   			   
			   count ++;
		   }
		   	   
		   String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carrierName;
		   this.fileStorageLocation = Paths.get(newPath)
				   .toAbsolutePath().normalize();
		   
		   Files.createDirectories(this.fileStorageLocation);
		  
		   
		   String fileFullPath = newPath+"/"+templateName;
		   
		   File existingFile = new File(fileFullPath);

		   if (existingFile.exists() && existingFile.isFile()) {
		       existingFile.delete();
		       
		     }
		   
		   FileOutputStream fileOut = new FileOutputStream(fileFullPath);  
		   workbook.write(fileOut);  
		   //closing the Stream  
		   fileOut.close();  
		   //closing the workbook  
		   workbook.close();  
		   //prints the message on the console  
		   logger.info("Excel file has been updated successfully.");  
		 }   
		 catch (Exception e)   
		 {  
		    e.printStackTrace();  
		 }
		 
		 return templateName;
	}


	public String storeUnformattedFile(String userId, CarrierFileConvertFormRequest carrierFileConvertFormRequest, MultipartFile file) {
		 
		 Long carrierId = Long.parseLong(carrierFileConvertFormRequest.getCarrier());
		 Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
		 String carrierName = carrierEntity.get().getCarriershortname();
		 
		 String templateName = file.getOriginalFilename();
		   
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	     
		 String timeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
		    
	     fileName = timeStamp+"_"+fileName; 
	     
	     String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carrierName;
	     
	     //String fileDirWithFileName = ratesDirPath+"/"+carrierName+"/"+fileName;
	        
	     try {
	            // Check if the file's name contains invalid characters
	            if (fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + newPath);
	            } 
	            
	            this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();

	            //Files.createDirectories(this.fileStorageLocation.resolve(carrierName));
	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileName);	            
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	     
			
			UploadRateHistoryEntity uploadRateHistory = new UploadRateHistoryEntity();
			
			String recordscount = "NA";
			Long chargeTypeId = Long.parseLong(carrierFileConvertFormRequest.getChargeType());
			
		    uploadRateHistory.setCreateby("system");		
		    uploadRateHistory.setForwarderid(Long.parseLong(carrierFileConvertFormRequest.getForwarderCha()));
		    uploadRateHistory.setShipmenttype(carrierFileConvertFormRequest.getShipmentType());
	    	uploadRateHistory.setCarrierid(carrierId);
		    uploadRateHistory.setChargetypeid(chargeTypeId);
		    uploadRateHistory.setValiddatefrom(carrierFileConvertFormRequest.getValidDateFrom());
		    uploadRateHistory.setValiddateto(carrierFileConvertFormRequest.getValidDateTo());
		    uploadRateHistory.setRecordscount(recordscount);
		    uploadRateHistory.setFilename(fileName);
		    String uploadedDate = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").format(new Date());
		    uploadRateHistory.setUploadeddate(uploadedDate);
		    uploadRateHistory.setStatus("Pending");
            uploadRateHistory.setIsdeleted("N");
            uploadRateHistoryRepository.save(uploadRateHistory);
		 
		 return templateName;
	}


	public void deleteFileWithoutException(String userId, String carrierName, String filename) throws FileNotFoundException{
		 String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carrierName;
         
         this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();
		   
		   //Files.createDirectories(this.fileStorageLocation);
		   
		    String fileFullPath =  this.fileStorageLocation+"/"+filename;
		    File existingFile = new File(fileFullPath);

		    if (existingFile.exists() && existingFile.isFile()) {
		        existingFile.delete();
		    } 
		    else {
		        logger.info("File Not found in location");
		    }      
	}


	public Resource loadRateFile(String carrierName, String fileName) throws FileNotFoundException {
		Long carrierId = Long.parseLong(carrierName);
 		Optional<CarrierEntity> carrierEntity = carrierRepository.findById(carrierId);
 		String carriername = carrierEntity.get().getCarriershortname();
 		
        String newPath = this.fileBaseLocation+"/"+ratesDirPath+"/"+carriername;
       
        try {
        	
           this.fileStorageLocation = Paths.get(newPath).toAbsolutePath().normalize();	
           Path file = this.fileStorageLocation.resolve(fileName).normalize();       
           Resource resource = new UrlResource(file.toUri());

           if (resource.exists() || resource.isReadable()) {
              return resource;
           } 
           else {
        	   resource = null;
        	   return resource;
           }
         } 
         catch (MalformedURLException e) {
              throw new FileNotFoundException("Error: Could not download file");
         }       
	}



	
     
}
