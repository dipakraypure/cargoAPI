package com.cargo.utils;

import java.util.stream.Stream;

public class TitleCaseConvertsionUtils {

	 public static String titleCaseConversion(String inputString) {
		 
		    if (inputString == null) {
	            return null;
	        }
	        if (inputString.isEmpty()) {
	            return "";
	        }
	 
	        if (inputString.length() == 1) {
	            return inputString.toUpperCase();
	        }
	 
	        StringBuffer resultPlaceHolder = new StringBuffer(inputString.length());
	 
	        Stream.of(inputString.split(" ")).forEach(stringPart -> {
	            char[] charArray = stringPart.toLowerCase().toCharArray();
	            charArray[0] = Character.toUpperCase(charArray[0]);
	            resultPlaceHolder.append(new String(charArray)).append(" ");
	        });
	 
	        
	        return resultPlaceHolder.toString().trim();
	    }
}
