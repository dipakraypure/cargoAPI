package com.cargo.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import com.cargo.models.UserEntity;

public class RandomNumberString {

	
public static String getTeanderRandomNumberString(UserEntity userEntity, String requestType,long todayRequestNumber) throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yy");
		DecimalFormat mFormat= new DecimalFormat("00");
		
		GetCharactersOfString	charactersOfString	= new GetCharactersOfString();
		
		String formattedDate = df.format(Calendar.getInstance().getTime());
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.get(Calendar.DATE);
		calendar.get(Calendar.MONTH);
		

		String requestDate = mFormat.format(Double.valueOf(calendar.get(Calendar.MONTH)+1))+mFormat.format(Double.valueOf(calendar.get(Calendar.DATE)))+formattedDate ;
		
	    // It will generate 5 digit random Number.
	    // from 0 to 99999
	    Random rnd = new Random();
	    int number = rnd.nextInt(99999);
	    
	    
	    System.out.println(String.format("%04d",number)); 

	    // this will convert any number sequence into 5 character.
	    
	    
	    String todayRequestCount = String.format("%04d",todayRequestNumber+1);
		
		String randomNumberString = charactersOfString.getSubString(userEntity.getEmail(), 0, 4)+"-"+ charactersOfString.getSubString(requestType, 0,3)+"-"+todayRequestCount +"-"+requestDate ;
		
	    return randomNumberString.toUpperCase();
	}
	
	public static String getFourDigitRandomNumberString() throws ParseException {
		
		DateFormat df = new SimpleDateFormat("yy");
		DecimalFormat mFormat= new DecimalFormat("00");
		
		
		String formattedDate = df.format(Calendar.getInstance().getTime());
		
		Calendar calendar = Calendar.getInstance();
		
		calendar.get(Calendar.DATE);
		calendar.get(Calendar.MONTH);
		

		String requestDate = mFormat.format(Double.valueOf(calendar.get(Calendar.MONTH)+1))+mFormat.format(Double.valueOf(calendar.get(Calendar.DATE)))+formattedDate ;
		
	    // It will generate 5 digit random Number.
	    // from 0 to 99999
	    Random rnd = new Random();
	    int number = rnd.nextInt(999);
	    
	    System.out.println(String.format("%03d",number)); 

	    // this will convert any number sequence into 5 character.
	    
		String randomNumberString = String.format("%03d",number)+requestDate ;
		
	    return randomNumberString;
	}
}
