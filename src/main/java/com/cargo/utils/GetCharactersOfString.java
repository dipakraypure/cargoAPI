package com.cargo.utils;

import java.text.ParseException;

public class GetCharactersOfString {

	public String getSubString(String inputString ,int substringStart ,int substringEnd ) throws ParseException {
		 String firstFourChars = "";  
		 
		 if (inputString.length() > substringEnd) 
		 {
		     firstFourChars = inputString.substring(substringStart, substringEnd);
		 } 
		
		 firstFourChars = inputString;
		 
		 return firstFourChars;
	}
}
