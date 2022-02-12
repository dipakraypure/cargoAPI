package com.cargo.pdf;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.text.StyleConstants.ColorConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cargo.load.response.BookScheduleResponse;
import com.cargo.load.response.ChargesRateResponse;
import com.cargo.load.response.ScheduleLegsResponse;
import com.cargo.models.FileStorageProperties;
import com.cargo.utils.UploadPathContUtils;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.WebColors;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.property.VerticalAlignment;

@Service
public class PdfService {
	private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

	private Path fileStorageLocation;
	
	private final String fileBaseLocation;
	
	@Autowired
	public PdfService(FileStorageProperties fileStorageProperties) {
		this.fileBaseLocation = fileStorageProperties.getUploadDir();
	}
	
	public final static String fileExtension = ".pdf";
	
	String imageFile = "C:/itext/logo.png"; 
	
	// private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,Font.BOLD);
	// private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL, BaseColor.RED);
	// private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,Font.BOLD);
	 //private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.BOLD);
	 //private static Font smallNorm = new Font(Font.FontFamily.TIMES_ROMAN, 12,Font.NORMAL);
	    
	public String generatePdf(BookScheduleResponse bookScheduleResponse) throws Exception {
		
		
		String filename = bookScheduleResponse.getBookingreff().concat(fileExtension);
		
		logger.info("filename: "+filename);
		
		String newPath = this.fileBaseLocation+"/"+UploadPathContUtils.FILE_BOOKING_DIR;
		   this.fileStorageLocation = Paths.get(newPath)
				   .toAbsolutePath().normalize();
		 
		logger.info("file newPath: "+newPath);
		   
		Files.createDirectories(this.fileStorageLocation);
		
		String dest = newPath+"/"+filename;
		
		logger.info("file dest: "+dest);
		
		PdfWriter writer = new PdfWriter(dest); 
		PdfDocument pdfDocument = new PdfDocument(writer);		
		Document layoutDocument = new Document(pdfDocument, PageSize.A4);

		//PdfService.addHeader(layoutDocument,pdfDocument);
		
		//PdfService.addTitle(layoutDocument);
		
		ImageData data = ImageDataFactory.create(imageFile);
		Image img = new Image(data); 
		img.setHeight(1f);
		//img.scaleAbsolute(70f, 50f);
		img.scaleAbsolute(142f, 71f);
		
		// Creating a table
		float [] pointColumnWidths = {150f, 370f};
		Table table = new Table(pointColumnWidths);
		

		// Populating row 1 and adding it to the table
		Cell cell1 = new Cell();
		cell1.setBorder(Border.NO_BORDER);
		cell1.add(img);
		table.addCell(cell1);

		Cell cell2 = new Cell();
		cell2.setPaddingTop(35);
		cell2.setBorder(Border.NO_BORDER);
		
		Color color = WebColors.getRGBColor("#161a47");
		Paragraph para = new Paragraph("BOOKING DETAILS");
		para.setPaddingTop(5);
		para.setPaddingLeft(55);
		para.setPaddingBottom(5);
		para.setBold();
		//para.setTextAlignment(TextAlignment.CENTER);
		para.setFontColor(Color.WHITE);
		para.setBackgroundColor(color);
		cell2.add(para);
		table.addCell(cell2);
		
		
		layoutDocument.add(table);
		
		
		String bookingReff = "Booking Ref.: "+ bookScheduleResponse.getBookingreff();
		PdfService.addBookingRefference(layoutDocument,bookingReff);
		
		PdfService.addBookingByAndDateAndStatus(layoutDocument,bookScheduleResponse);
		
		String bookingParty = bookScheduleResponse.getBookingparty();
		PdfService.addBookingParty(layoutDocument,bookingParty);
		
		PdfService.addBookingDetails(layoutDocument,bookScheduleResponse);
		
		PdfService.addBookingDetailsLegs(layoutDocument,bookScheduleResponse);
		
		PdfService.addBookingRequirement(layoutDocument,bookScheduleResponse);
		
		PdfService.addChargesSummary(layoutDocument,bookScheduleResponse);
	
		PdfService.addDestReqInfo(layoutDocument,bookScheduleResponse);
		
		PdfService.addTermsAndConditionHeader(layoutDocument);
		
		
		
		//PdfService.addFooter(layoutDocument,pdfDocument);
		
	
		
		layoutDocument.close();
		
		return filename;
	}

	

	private static void addHeader(Document layoutDocument, PdfDocument pdfDocument) {
		Paragraph header = new Paragraph("Copy")
                .setFontSize(8)
                .setFontColor(Color.RED);

        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            Rectangle pageSize = pdfDocument.getPage(i).getPageSize();
            float x = pageSize.getWidth() / 2;
            float y = pageSize.getTop() - 20;
            layoutDocument.showTextAligned(header, x, y, i, TextAlignment.LEFT, VerticalAlignment.BOTTOM, 0);
        }
		
	}

	private static void addFooter(Document layoutDocument, PdfDocument pdfDocument) {
		
		layoutDocument.add(new Paragraph(""));
		layoutDocument.add(new Paragraph(""));
		
		//layoutDocument.add(new Paragraph(" Login | Edit Booking | View Charges | Contact Us").setBorder(Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		layoutDocument.add(new Paragraph("Copyright 2021 BookMyCargo.online All rights reserved.").setBorder(Border.NO_BORDER).setFontSize(8).setTextAlignment(TextAlignment.CENTER));
		
	}



	public static void addTitle(Document layoutDocument) {
		layoutDocument.add(new Paragraph("BOOKING DETAILS").setBold().setUnderline().setTextAlignment(TextAlignment.CENTER));
		logger.info("pdf title added ");
	}
	
	private static void addBookingRefference(Document layoutDocument,String bookingReff) {
		
		layoutDocument.add(new Paragraph(""));
		
		layoutDocument.add(new Paragraph(bookingReff).setFontSize(10).setBold().setTextAlignment(TextAlignment.RIGHT));
		logger.info("pdf booking refference added ");
	}

	private static void addBookingByAndDateAndStatus(Document layoutDocument,BookScheduleResponse bookScheduleResponse) {
		
		layoutDocument.add(new Paragraph(""));
		
		String bookedBy = bookScheduleResponse.getBookedby();
		String bookingDate= bookScheduleResponse.getBookingdate();
		String bookinStatus = bookScheduleResponse.getBookingstatus();
		String bookingupdateddate = bookScheduleResponse.getBookingupdateddate();
		
		// Creating a table object 
	    float [] pointColumnWidths = {173.3F,173.3F,173.3F}; 
		Table table = new Table(pointColumnWidths); 
		table.setFontSize(8);
		
				
		// headers
	    table.addCell(new Paragraph("Booked By")).setBackgroundColor(Color.LIGHT_GRAY).setBold();
	    table.addCell(new Paragraph("Date of Booking")).setBackgroundColor(Color.LIGHT_GRAY).setBold();
	    table.addCell(new Paragraph("Booking Status")).setBackgroundColor(Color.LIGHT_GRAY).setBold();
	    layoutDocument.add(table);
	    
	    String bookingStatWithDate = bookinStatus+" ("+bookingupdateddate+")";
	    
	    Table table1 = new Table(pointColumnWidths); 
		table1.setFontSize(8);
		table1.addCell(new Paragraph(bookedBy));
		table1.addCell(new Paragraph(bookingDate));
		table1.addCell(new Paragraph(bookingStatWithDate));
	  
	    layoutDocument.add(table1);
	    
	    logger.info("pdf booking by,date,status added ");
	}
	private static void addBookingParty(Document layoutDocument, String bookingParty) {
		
		layoutDocument.add(new Paragraph(""));
		layoutDocument.add(new Paragraph(""));
		
		// Creating a table object 
		float [] pointColumnWidths = {520F}; 
		Table table = new Table(pointColumnWidths); 
		table.setFontSize(8);
		 // headers
	    table.addCell(new Paragraph("Booking Party")).setBackgroundColor(Color.LIGHT_GRAY).setBold();
	    layoutDocument.add(table);
	    
	    Table table1 = new Table(pointColumnWidths); 
		table1.setFontSize(8);
	    table1.addCell(new Paragraph(bookingParty));
	
	    layoutDocument.add(table1);
	    logger.info("pdf party name added ");
	}

	
	private static void addBookingDetails(Document layoutDocument, BookScheduleResponse bookScheduleResponse) {
		
		
		layoutDocument.add(new Paragraph(""));
		layoutDocument.add(new Paragraph(""));
		
		// Creating a table object 
		float [] pointColumnWidths = {520F}; 
		Table table = new Table(pointColumnWidths); 
				
		// headers
	    table.addCell(new Paragraph("Schedule Details").setFontSize(10).setBold()).setBackgroundColor(Color.LIGHT_GRAY);
		layoutDocument.add(table);
		layoutDocument.add(new Paragraph(""));
		
		String date = bookScheduleResponse.getDateofbooking();
		String origin= bookScheduleResponse.getOrigin();		
		String destination = bookScheduleResponse.getDestination();
		String cutOffDate = bookScheduleResponse.getCutoffdate();	
				
		// Creating a table object 
	    float [] pointColumnWidths1 = {130F,130F,130F,130F}; 
		Table table1 = new Table(pointColumnWidths1); 
		table1.setFontSize(8);		
		// headers
	    table1.addCell(new Paragraph("Booking Date").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Origin").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Destination").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Cut-off Date").setBold().setTextAlignment(TextAlignment.CENTER));
	   
	 
		table1.addCell(new Paragraph(date));
		table1.addCell(new Paragraph(origin));		
		table1.addCell(new Paragraph(destination));
		table1.addCell(new Paragraph(cutOffDate));		    
			    
	    layoutDocument.add(table1);
	    logger.info("pdf booking details added ");
		
	}
	
	private static void addBookingDetailsLegs(Document layoutDocument, BookScheduleResponse bookScheduleResponse) throws Exception {
		
		layoutDocument.add(new Paragraph(""));

		if( bookScheduleResponse.getScheduleLegsResponse().size() == 0) {
			throw new Exception("Error: No Schedule Legs Found");
		}
		
        if( bookScheduleResponse.getScheduleLegsResponse() == null) {
        	throw new Exception("Error: No Schedule Legs Found");
		}
		
        List<ScheduleLegsResponse> scheduleLegsResponseList =  bookScheduleResponse.getScheduleLegsResponse();
       
        // Creating a table object 
	    float [] pointColumnWidths1 = {57.77F,57.77F,57.77F,57.77F,57.77F,57.77F,57.77F,57.77F,57.77F}; 
		Table table1 = new Table(pointColumnWidths1); 
		table1.setFontSize(8);		
		// headers
	    table1.addCell(new Paragraph("Origin").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Destination").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Mode").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Carrier").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Vessel Name").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Voyage Name").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("Transit Time").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("ETD").setBold().setTextAlignment(TextAlignment.CENTER));
	    table1.addCell(new Paragraph("ETA").setBold().setTextAlignment(TextAlignment.CENTER));
	    
        for(ScheduleLegsResponse scheduleLegsResponse : scheduleLegsResponseList) {
        	    String origin = scheduleLegsResponse.getOrigin();
        		String destination = scheduleLegsResponse.getDestination();
        		String mode = scheduleLegsResponse.getMode();
        		String carrier = scheduleLegsResponse.getCarrier();
        		String vessel = scheduleLegsResponse.getVessel();
        		String voyage = scheduleLegsResponse.getVoyage();
        		String transittime = scheduleLegsResponse.getTransittime()+" Days";
        		String etddate = scheduleLegsResponse.getEtddate();
        		String etadate = scheduleLegsResponse.getEtadate();
        		
            	    
        		table1.addCell(new Paragraph(origin));
        		table1.addCell(new Paragraph(destination));
        		table1.addCell(new Paragraph(mode));
        		table1.addCell(new Paragraph(carrier));
        		table1.addCell(new Paragraph(vessel));
        		table1.addCell(new Paragraph(voyage));
        		table1.addCell(new Paragraph(transittime));
        		table1.addCell(new Paragraph(etddate));
        		table1.addCell(new Paragraph(etadate));
        			          			    
        	    layoutDocument.add(table1);
        }
			
	    logger.info("pdf booking details legs added ");
		
	}
	
	private static void addBookingRequirement(Document layoutDocument, BookScheduleResponse bookScheduleResponse) {
		
		layoutDocument.add(new Paragraph(""));
		layoutDocument.add(new Paragraph(""));
		
		// Creating a table object 
		float [] pointColumnWidths = {520F}; 
		Table table = new Table(pointColumnWidths); 
				
		// headers
	    table.addCell(new Paragraph("Booking Requirement").setFontSize(10).setBold()).setBackgroundColor(Color.LIGHT_GRAY);
		layoutDocument.add(table);
		layoutDocument.add(new Paragraph(""));
		
		String shipmentType = bookScheduleResponse.getShipmenttype();
		
		if(shipmentType.equals("FCL")) {
			
			String twentyFtCount = Integer.toString(bookScheduleResponse.getTwentyFtCount());
			String fourtyFtCount= Integer.toString(bookScheduleResponse.getFourtyFtCount());
			String fourtyFtHcCount =  Integer.toString(bookScheduleResponse.getFourtyFtHcCount());
			String fourtyFiveFtCount =  Integer.toString(bookScheduleResponse.getFourtyFiveFtCount());
			
			// Creating a table object 
			float [] pointColumnWidths1 = {173.3F,86.67F,86.67F,86.67F,86.67F};  
			Table table1 = new Table(pointColumnWidths1); 
			table1.setFontSize(8);		
			// headers
		    table1.addCell(new Paragraph("Service").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("20").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("40").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("40 HC").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("45 HC").setBold().setTextAlignment(TextAlignment.CENTER));
		    		   	
			table1.addCell(new Paragraph(shipmentType).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(twentyFtCount).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(fourtyFtCount).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(fourtyFtHcCount).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(fourtyFiveFtCount).setTextAlignment(TextAlignment.CENTER));
			
			layoutDocument.add(table1);
		    logger.info("pdf booking requirement added ");
		    
		}else if(shipmentType.equals("LCL")) {
			
			String lcltotalweight = bookScheduleResponse.getLcltotalweight();
			String lclweightunit = bookScheduleResponse.getLclweightunit();
			String lclvolume = bookScheduleResponse.getLclvolume();
			String lclvolumeunit = bookScheduleResponse.getLclvolumeunit();
			String lclnumberpackage = bookScheduleResponse.getLclnumberpackage();
			String lclpackageunit = bookScheduleResponse.getLclpackageunit();
			
			// Creating a table object 
			float [] pointColumnWidths1 = {173.3F,173.3F,173.3F};  
			Table table1 = new Table(pointColumnWidths1); 
			table1.setFontSize(8);		
			// headers
		    table1.addCell(new Paragraph("Service").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("Weight (Kgs)").setBold().setTextAlignment(TextAlignment.CENTER));
		    table1.addCell(new Paragraph("Volume (CBM)").setBold().setTextAlignment(TextAlignment.CENTER));
		    
			table1.addCell(new Paragraph(shipmentType).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(lcltotalweight).setTextAlignment(TextAlignment.CENTER));
			table1.addCell(new Paragraph(lclvolume).setTextAlignment(TextAlignment.CENTER));
			
			layoutDocument.add(table1);
		    logger.info("pdf booking requirement added ");
		}
			   
	}
	
private static void addChargesSummary(Document layoutDocument, BookScheduleResponse bookScheduleResponse) {
		
	    List<ChargesRateResponse> chargesRateResponseList =  bookScheduleResponse.getChargesRateResponseList();
	
        layoutDocument.add(new Paragraph(""));
        layoutDocument.add(new Paragraph(""));
		
		// Creating a table object 
		float [] pointColumnWidths = {520F}; 
		Table table = new Table(pointColumnWidths); 
				
		// headers
	    table.addCell(new Paragraph("Charges Summary").setFontSize(10).setBold()).setBackgroundColor(Color.LIGHT_GRAY);
	    layoutDocument.add(table);
	    
	    layoutDocument.add(new Paragraph(""));
	    
	    // Creating a table object 
	    float [] pointColumnWidths1 = {74.28F,74.28F,74.28F,74.28F,74.28F,74.28F,74.28F}; 
	 	Table table1 = new Table(pointColumnWidths1); 
	 	table1.setFontSize(8);	
	 	
	 	table1.addCell(new Paragraph("Charge Grouping").setBold().setTextAlignment(TextAlignment.CENTER));
	 	table1.addCell(new Paragraph("Charge Type").setBold().setTextAlignment(TextAlignment.CENTER));
	 	table1.addCell(new Paragraph("Currency").setBold().setTextAlignment(TextAlignment.CENTER));	 	
	 	table1.addCell(new Paragraph("Rate").setBold().setTextAlignment(TextAlignment.CENTER));
	 	table1.addCell(new Paragraph("Basis").setBold().setTextAlignment(TextAlignment.CENTER));
	 	table1.addCell(new Paragraph("Quantity").setBold().setTextAlignment(TextAlignment.CENTER));
	 	table1.addCell(new Paragraph("Total").setBold().setTextAlignment(TextAlignment.CENTER));
	 	
	 	for(ChargesRateResponse chargesRateResponse : chargesRateResponseList) {
	 		
	 		String chargesGrouping = chargesRateResponse.getChargesgrouping();
	 		String chargestype = chargesRateResponse.getChargestype();
	 		String currency = chargesRateResponse.getCurrency();
	 		String rate = chargesRateResponse.getRate();
	 		String basis = chargesRateResponse.getBasis();
	 		String quantity = chargesRateResponse.getQuantity();
	 		String totalamount = chargesRateResponse.getTotalamount();
	 			 		
	 	    table1.addCell(new Paragraph(chargesGrouping));
	 	    table1.addCell(new Paragraph(chargestype));
	 	    table1.addCell(new Paragraph(currency).setTextAlignment(TextAlignment.CENTER));	 	   
	 	    table1.addCell(new Paragraph(rate).setTextAlignment(TextAlignment.RIGHT));
	 	    table1.addCell(new Paragraph(basis).setTextAlignment(TextAlignment.CENTER));
	 	    table1.addCell(new Paragraph(quantity).setTextAlignment(TextAlignment.CENTER));
	 	    table1.addCell(new Paragraph(totalamount).setTextAlignment(TextAlignment.RIGHT));
	 	}
	 		
	 	table1.addCell(new Paragraph("Total Charges").setBold());
	 	table1.addCell(new Paragraph(""));
	 	table1.addCell(new Paragraph("USD").setBold().setTextAlignment(TextAlignment.CENTER));
	 	String totalCharges = "";
	 	table1.addCell(new Paragraph(""));
	 	table1.addCell(new Paragraph(""));
	 	table1.addCell(new Paragraph(""));
	 	table1.addCell(new Paragraph(totalCharges).setBold().setTextAlignment(TextAlignment.RIGHT));
	 	
	 	layoutDocument.add(table1);
	 	logger.info("pdf charges summary added ");
	    
	    
	}
	
    
    private static void addDestReqInfo(Document layoutDocument, BookScheduleResponse bookScheduleResponse) {
	   
 	    layoutDocument.add(new Paragraph(""));
    	layoutDocument.add(new Paragraph(""));
	
    	// Creating a table object 
	    float [] pointColumnWidths = {520F}; 
	    Table table = new Table(pointColumnWidths); 
    	table.setFontSize(8);
	    // headers
        table.addCell(new Paragraph("Destination Requirements Information").setFontSize(10).setBold()).setBackgroundColor(Color.LIGHT_GRAY);
        layoutDocument.add(table);
    
        Table table1 = new Table(pointColumnWidths); 
	    table1.setFontSize(8);
	    String orgCodeAndDestCode = bookScheduleResponse.getOrigincode()+"  "+bookScheduleResponse.getDestinationcode();
        table1.addCell(new Paragraph(orgCodeAndDestCode));

        layoutDocument.add(table1);
        logger.info("pdf destination req added ");
   }
    
    private static void addTermsAndConditionHeader(Document layoutDocument) {
		
       
        layoutDocument.add(new Paragraph(""));
        layoutDocument.add(new Paragraph(""));
		
		// Creating a table object 
		float [] pointColumnWidths = {520F}; 
		Table table = new Table(pointColumnWidths); 
		
		// headers		
	    table.addCell(new Paragraph("Terms & Conditions").setFontSize(10).setBold()).setBackgroundColor(Color.LIGHT_GRAY);
	    layoutDocument.add(table);
	    layoutDocument.add(new Paragraph("• For more information about your booking, please click here").setBorder(Border.NO_BORDER).setFontSize(8));
	    layoutDocument.add(new Paragraph("• To read our conditions of carriage as per Indian regulations, please click here").setBorder(Border.NO_BORDER).setFontSize(8));

	    logger.info("pdf terms and condition added ");
	}
	
	
}
