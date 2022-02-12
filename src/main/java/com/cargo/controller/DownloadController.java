package com.cargo.controller;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cargo.load.response.BaseResponse;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.utils.StringsUtils;

import io.swagger.annotations.Api;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/download")
@Api(value = "/api/download", tags = "Download Management")
public class DownloadController {
	private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

	BaseResponse response = null;
	
	@Autowired
	FileStorageService fileStorageService;
	
	//check template exists or not 
	@GetMapping(value = "/checkFileExists")
	public ResponseEntity<Object> checkFileExists(@RequestParam("userId") String userId,@RequestParam("carrierName")String carrierName,@RequestParam("templateName")String templateName ) {
		logger.info("********************DownloadController checkFileExists()******************");
		response = new BaseResponse();
				
		try {
			
			 Resource resource = fileStorageService.loadFile(carrierName,templateName);
			
			 HashMap<String ,String>  info = new HashMap<String ,String>();
			 if(resource != null) {
				
				 info.put("msg", "File Exists");
			        
				 response.setRespCode(StringsUtils.Response.SUCCESS_RESP_CODE);
				 response.setRespMessage(StringsUtils.Response.SUCCESS_RESP_MSG);
				 response.setRespData(info);
		 
			 }else {
				 info.put("msg", "Error: File not found!");
			        
				 response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
				 response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
				 response.setRespData(info);
			 }					
			 
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
	
	@GetMapping(value = "/downloadTemplate")
	public ResponseEntity<Object> downloadCarrierRateTemplate(@RequestParam("userId") String userId,@RequestParam("carrierName")String carrierName,@RequestParam("templateName")String templateName ) {
		logger.info("********************DownloadController downloadCarrierRateTemplate()******************");
			response = new BaseResponse();
		
		try {

			Resource resource = fileStorageService.loadFile(carrierName,templateName);
			
				logger.info("resource file name: "+resource.getFilename());
				ResponseEntity respEntity = null;
				
				try {
					
					respEntity = ResponseEntity.ok()
		                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
		                    .body(resource);
					
				}catch(Exception e) {
					logger.error("error : ",e);
				}
				return respEntity;
	            /*
	             * return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").body(resource);
	             */
			
		}
		catch (Exception e) {
			
			logger.error(e.getMessage());
			
			response.setRespCode(StringsUtils.Response.FAILURE_RESP_CODE);
			response.setRespMessage(StringsUtils.Response.FAILURE_RESP_MSG);
			response.setRespData(e.getMessage());
			
			return ResponseEntity.badRequest().body(response);
			
		}
	}
	
	
}
