package com.cargo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class RandomKeyServices {
	private static final Logger logger = LoggerFactory.getLogger(RandomKeyServices.class);
	
	public String randomPassword() {
		logger.info("Generating Password");

	    String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%&?{}*";
	    StringBuilder builder = new StringBuilder();

	    int count = 8;

	    while (count-- != 0) {
	        int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
	        builder.append(ALPHA_NUMERIC_STRING.charAt(character));
	    }
	    
	    return builder.toString();

	}
}
