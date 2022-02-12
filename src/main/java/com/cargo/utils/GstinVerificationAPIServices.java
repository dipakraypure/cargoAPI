package com.cargo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.models.CompanyEntity;
import com.cargo.repository.CompanyRepository;

@Service
public class GstinVerificationAPIServices {
	
	private static final Logger logger = LoggerFactory.getLogger(GstinVerificationAPIServices.class);

	@Autowired
	CompanyRepository companyRepository;
	
	
	public static CompanyEntity getGstinDetailsByGstinNumber(String gstinnumber)throws Exception {
		     
		CompanyEntity companyEntity = null;
		
		if(gstinnumber!= null) {
		  
		    /******* TAXPRO GSP API CALL TO GET GSTIN DETAILS******/
		    
		    InputStream inputStream;
			try {
				//absortio => 27AAAPJ7100N1ZS
				String url = "https://gstapi.charteredinfo.com/commonapi/v1.1/search?aspid=1653291921&password=BookMyCargo@2021&action=TP&Gstin="+gstinnumber;
				inputStream = new URL(url).openStream();
			
			    JSONParser jsonParser = new JSONParser();
			    JSONObject jsonObject;
			    
			    companyEntity = new CompanyEntity();
			    
				try {
					
					jsonObject = (JSONObject)jsonParser.parse(new InputStreamReader(inputStream, "UTF-8"));
					String taxpayertype = (String)jsonObject.get("dty");
					

					String gstinstatus = (String)jsonObject.get("sts");
					
					if(gstinstatus.equals("Cancelled") || gstinstatus.equals("Inactive")) {
						throw new Exception("Error: This GSTIN number is "+gstinstatus);
					}
					
					String gstinnum = (String)jsonObject.get("gstin");
					
					logger.info("gstinnumber from taxpro API: "+gstinnum);
					
					String statejurisdictioncode = (String)jsonObject.get("stjCd");
					String statejurisdiction = (String)jsonObject.get("stj");

					String legalname = (String)jsonObject.get("lgnm");	
					String tradename = (String)jsonObject.get("tradeNam"); 
					
					String centrejurisdiction = (String)jsonObject.get("ctj");
					String registrationdate = (String)jsonObject.get("rgdt");
					
					String lastupdate = (String)jsonObject.get("lstupdt");
					String lastupdatedate = "";   
				    if(lastupdate != null) {
				    	
				    Date dateInput = null;
					try {
						dateInput = new SimpleDateFormat("dd/MM/yyyy").parse(lastupdate);
					} catch (java.text.ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					
					SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
					
					lastupdatedate = formatter.format(dateInput);
					logger.info("lastupdatedate: "+lastupdatedate);
					
				    }
					
					JSONObject jsonObjPradr = (JSONObject) jsonObject.get("pradr");
					JSONObject jsonObjAddr = (JSONObject) jsonObjPradr.get("addr");
					
				    String buildingname = (String)jsonObjAddr.get("bnm");
				    String buildingnumber = (String)jsonObjAddr.get("bno");
					String floornumber =  (String)jsonObjAddr.get("flno");
					String street =  (String)jsonObjAddr.get("st");
					String location = (String)jsonObjAddr.get("loc");
					String destination = (String)jsonObjAddr.get("dst");	
					String city = (String)jsonObjAddr.get("city");
					String pincode = (String)jsonObjAddr.get("pncd");
					String statecode = (String)jsonObjAddr.get("stcd");
					
					companyEntity.setGstinnumber(gstinnum);
					companyEntity.setStatejurisdictioncode(statejurisdictioncode);
					companyEntity.setStatejurisdiction(TitleCaseConvertsionUtils.titleCaseConversion(statejurisdiction));
					companyEntity.setLegalname(TitleCaseConvertsionUtils.titleCaseConversion(legalname));
					companyEntity.setTradename(TitleCaseConvertsionUtils.titleCaseConversion(tradename));
					companyEntity.setBuildingname(TitleCaseConvertsionUtils.titleCaseConversion(buildingname));
					companyEntity.setBuildingnumber(TitleCaseConvertsionUtils.titleCaseConversion(buildingnumber));
					companyEntity.setFloornumber(TitleCaseConvertsionUtils.titleCaseConversion(floornumber));
					companyEntity.setStreet(TitleCaseConvertsionUtils.titleCaseConversion(street));
					companyEntity.setLocation(TitleCaseConvertsionUtils.titleCaseConversion(location));
					companyEntity.setDestination(TitleCaseConvertsionUtils.titleCaseConversion(destination));
					companyEntity.setCity(city);
					companyEntity.setPincode(pincode);
					companyEntity.setTaxpayertype(TitleCaseConvertsionUtils.titleCaseConversion(taxpayertype));
					companyEntity.setCentrejurisdiction(centrejurisdiction);
					companyEntity.setGstinstatus(TitleCaseConvertsionUtils.titleCaseConversion(gstinstatus));
					companyEntity.setRegistrationdate(registrationdate);
					companyEntity.setLastupdatedate(lastupdatedate);
					companyEntity.setStatecode(statecode);
				
				    companyEntity.setCreateby("system");
					companyEntity.setIsdeleted("N");
					    
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
									
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    
		    /***********END**************/
		  
		}
		
		return companyEntity;
	}
}
