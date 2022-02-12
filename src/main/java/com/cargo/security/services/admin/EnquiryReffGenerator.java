package com.cargo.security.services.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.repository.EnquiryRepository;

@Service
public class EnquiryReffGenerator {

	private static final Logger logger = LoggerFactory.getLogger(BookingReffGenerator.class);
	
	@Autowired
	EnquiryRepository enquiryRepository;
	
    public String getEnquiryRefferenceNumber(String originCityCode, String destinationCityCode) {
		
		String enquiryReff = "";
		
		String orgName = originCityCode.toUpperCase();
		String destName = destinationCityCode.toUpperCase();
		String srNo = "";
		
		long  recordsCount = enquiryRepository.enquiryDetailsEntityCout() + 1;
		
		srNo = Long.toString(recordsCount);
		
		enquiryReff = enquiryReff.concat(orgName).concat(destName).concat("_E").concat(srNo);
		
		logger.info("generated enquiryReff Number: "+enquiryReff);
		
		return enquiryReff;
	}
}
