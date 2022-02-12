package com.cargo.security.services.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.repository.UploadOfferRepository;

@Service
public class CampaignCodeGenerator {

	private static final Logger logger = LoggerFactory.getLogger(CampaignCodeGenerator.class);
	
	@Autowired
	UploadOfferRepository uploadOfferRepository;
	
public String getCampaignCode(String originCityCode, String destinationCityCode) {
		
		String campaignCode = "";
		
		String orgName = originCityCode.toUpperCase();
		String destName = destinationCityCode.toUpperCase();
		String srNo = "";
		
		long  recordsCount = uploadOfferRepository.uploadOfferEntityCout() + 1;
		
		srNo = Long.toString(recordsCount);
		
		campaignCode = campaignCode.concat(orgName).concat(destName).concat(srNo);
		
		logger.info("generated campaignCode: "+campaignCode);
		
		return campaignCode;
	}
}
