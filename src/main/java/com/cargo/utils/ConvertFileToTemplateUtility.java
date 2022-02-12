package com.cargo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cargo.load.request.CommonRateTemplateRequest;



public class ConvertFileToTemplateUtility {
	private static final Logger logger = LoggerFactory.getLogger(ConvertFileToTemplateUtility.class);
	
	public final static String fileExtension = ".xlsx";
	
	 public static void generateMaxiconDataToCommonTemplate(List<CommonRateTemplateRequest> commonRateTemplateRequestList) {
		 
		 
		 try   
		 {  
		   //declare file name to be create   
		   //String filename = "C:\\Balance.xlsx";
		   String fileSubPath = "maxicon";
		   String filename = "template"+fileExtension;
		   String fileFullPath = fileSubPath+"/"+filename;
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
		   rowhead.createCell(0).setCellValue("SR_NO");  
		   rowhead.createCell(1).setCellValue("ORIGIN");  
		   rowhead.createCell(2).setCellValue("DESTINATION");  
		   rowhead.createCell(3).setCellValue("CARRIER");  
		   rowhead.createCell(4).setCellValue("CHARGES_GROUPING");  
		   rowhead.createCell(5).setCellValue("CHARGES_TYPE"); 
		   rowhead.createCell(6).setCellValue("CHARGES_TYPE_CODE");
		   rowhead.createCell(7).setCellValue("CARGO_CATEGORY");
		   rowhead.createCell(8).setCellValue("VALID_DATE_FROM");
		   rowhead.createCell(9).setCellValue("VALID_DATE_TO");
		   rowhead.createCell(10).setCellValue("ROUTING");
		   rowhead.createCell(11).setCellValue("TRANSIT_TIME");
		   rowhead.createCell(12).setCellValue("CURRENCY");
		   rowhead.createCell(13).setCellValue("BASIS");
		   rowhead.createCell(14).setCellValue("QUANTITY");
		   rowhead.createCell(15).setCellValue("RATE");
		    
		   int count = 1;
		   
		   for(CommonRateTemplateRequest commonRateTemplateRequest : commonRateTemplateRequestList) {
			   Row row = sheet1.createRow((short) count );  
			   
			   row.createCell(0).setCellValue(count);  
			   row.createCell(1).setCellValue(commonRateTemplateRequest.getOrigin());
			   row.createCell(2).setCellValue(commonRateTemplateRequest.getDestination());  
			   row.createCell(3).setCellValue(commonRateTemplateRequest.getCarrierid());  
			   row.createCell(4).setCellValue(commonRateTemplateRequest.getChargesgroupingid());  
			   row.createCell(5).setCellValue(commonRateTemplateRequest.getChargetype()); 
			   row.createCell(6).setCellValue(commonRateTemplateRequest.getChargetypecode());
			   row.createCell(7).setCellValue(commonRateTemplateRequest.getCargocategory());
			   row.createCell(8).setCellValue(commonRateTemplateRequest.getValiddatefrom());
			   row.createCell(9).setCellValue(commonRateTemplateRequest.getValiddateto());
			   row.createCell(10).setCellValue(commonRateTemplateRequest.getRouting());
			   row.createCell(11).setCellValue(commonRateTemplateRequest.getTransittime());
			   row.createCell(12).setCellValue(commonRateTemplateRequest.getCurrency());
			   row.createCell(13).setCellValue(commonRateTemplateRequest.getBasis());
			   row.createCell(14).setCellValue(commonRateTemplateRequest.getQuantity());
			   row.createCell(15).setCellValue(commonRateTemplateRequest.getRate());
			   
			   count ++;
		   }
		   
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
		   logger.info("Excel file has been converted successfully.");  
		 }   
		 catch (Exception e)   
		 {  
		    e.printStackTrace();  
		 }
	 }
	 
	 //create excel file using another code
	 public static void genMaxiconDataToCommonTemplate(List<CommonRateTemplateRequest> commonRateTemplateRequestList) {
		    
		    String headerArray[];
			String ColumnArray[];
			String column="SR_NO.,ORIGIN,DESTINATION,CARRIER,CHARGES_GROUPING,CHARGES_TYPE,CHARGES_TYPE_CODE,CARGO_CATEGORY,VALID_DATE_FROM,VALID_DATE_TO,ROUTING,TRANSIT_TIME,CURRENCY,BASIS,QUANTITY,RATE";
			String header="S.No.,ORIGIN,DESTINATION,CARRIER,CHARGES_GROUPING,CHARGES_TYPE,CHARGES_TYPE_CODE,CARGO_CATEGORY,VALID_DATE_FROM,VALID_DATE_TO,ROUTING,TRANSIT_TIME,CURRENCY,BASIS,QUANTITY,RATE";
			
			headerArray= header.split(",");
			ColumnArray=column.split(",");
			
			
			String fileName="maxicon_template";
     		     		
     		try {
    			
				SXSSFWorkbook workbook = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE/* 100 */);
				
				Sheet sheet = workbook.createSheet("Sheet1");
			
				createSheet(commonRateTemplateRequestList,headerArray,ColumnArray,fileName,workbook,sheet);
				
			}
			catch(Exception e){
				e.printStackTrace();
				
			}
		}
	 
	 public static void createSheet(List<CommonRateTemplateRequest>  map,String [] headerName, String [] columName ,String sheetName,SXSSFWorkbook workbook,Sheet sheet) throws FileNotFoundException {
			int rowCount = 0;
		
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
		
		
		//XSSFRow rowhead = sheet.createRow(rowCount);
		Row row = sheet.createRow(rowCount);
		rowCount++;
		for(int j=0;j<headerName.length;j++) {
			Cell cell = row.createCell(j);
			cell.setCellValue(headerName[j]);
			cell.setCellStyle(topHeaderStyle);	
		}
				
			//Row row = sheet.createRow(rowCount);
			int count = 1;			
			for (int i = 0 ; i < map.size(); i++) {
							
					row = sheet.createRow(rowCount++);
					 
					row.createCell(0).setCellValue(count);
					row.getCell(0).setCellStyle(cellBorderStyle);
					
					row.createCell(1).setCellValue(map.get(i).getOrigin());
					row.getCell(1).setCellStyle(cellBorderStyle);
					
					row.createCell(2).setCellValue(map.get(i).getDestination());
					row.getCell(2).setCellStyle(cellBorderStyle);
					
					
					row.createCell(3).setCellValue(map.get(i).getCarrierid());
					row.getCell(3).setCellStyle(cellBorderStyle);
					
					row.createCell(4).setCellValue(map.get(i).getChargesgroupingid());
					row.getCell(4).setCellStyle(cellBorderStyle);
					
					row.createCell(5).setCellValue(map.get(i).getChargetype());
					row.getCell(5).setCellStyle(cellBorderStyle);
								
					row.createCell(6).setCellValue(map.get(i).getChargetypecode());
					row.getCell(6).setCellStyle(cellBorderStyle);
					
					row.createCell(7).setCellValue(map.get(i).getCargocategory());
					row.getCell(7).setCellStyle(cellBorderStyle);
					
					row.createCell(8).setCellValue(map.get(i).getValiddatefrom());
					row.getCell(8).setCellStyle(cellBorderStyle);
					
					row.createCell(9).setCellValue(map.get(i).getValiddateto());
					row.getCell(9).setCellStyle(cellBorderStyle);
					
					row.createCell(10).setCellValue(map.get(i).getRouting());
					row.getCell(10).setCellStyle(cellBorderStyle);
				
					row.createCell(11).setCellValue(map.get(i).getTransittime());
					row.getCell(11).setCellStyle(cellBorderStyle);
					   
					row.createCell(12).setCellValue(map.get(i).getCurrency());
					row.getCell(12).setCellStyle(cellBorderStyle);
					   
					row.createCell(13).setCellValue(map.get(i).getBasis());
					row.getCell(13).setCellStyle(cellBorderStyle);
					   
					row.createCell(14).setCellValue(map.get(i).getQuantity());
					row.getCell(14).setCellStyle(cellBorderStyle);
					   
					row.createCell(15).setCellValue(map.get(i).getRate());
					row.getCell(15).setCellStyle(cellBorderStyle);
			
					count++;
				}
				
				
		}
			

}
	 