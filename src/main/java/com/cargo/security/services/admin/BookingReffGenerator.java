package com.cargo.security.services.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.models.BookingDetailsEntity;
import com.cargo.repository.BookingDetailsRepository;


@Service
public class BookingReffGenerator {
	private static final Logger logger = LoggerFactory.getLogger(BookingReffGenerator.class);
	
	@Autowired
	BookingDetailsRepository bookingDetailsRepository;
	
	public String getBookingRefferenceNumber(String companyname, String originCityCode, String destinationCityCode) {
		
		String bookingReff = "";
		
		String compName = companyname.substring(0, 3).toUpperCase();
		String orgName = originCityCode.toUpperCase();
		String destName = destinationCityCode.toUpperCase();
		String srNo = "";
		
		long  recordsCount = bookingDetailsRepository.bookingDetailsEntityCout() + 1;
		
		srNo = Long.toString(recordsCount);
		
		bookingReff = bookingReff.concat(compName).concat(orgName).concat(destName).concat(srNo);
		
		logger.info("generated bookingReff Number: "+bookingReff);
		
		return bookingReff;
	}
}
