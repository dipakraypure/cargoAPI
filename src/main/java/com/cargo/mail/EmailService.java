package com.cargo.mail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Map;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.cargo.load.response.BookScheduleResponse;
import com.cargo.load.response.FeedbackResponse;
import com.cargo.security.services.admin.FileStorageService;
import com.cargo.template.response.ForwarderQuotationResponse;
import com.cargo.utils.UploadPathContUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	@Autowired
	FileStorageService fileStorageService;
	
	private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
	
	public int generateRandomEmailOTP(){
		
		Random rand = new Random();
		int resRandom = rand.nextInt((999999 - 100) + 1) + 10;
		logger.info("Random OTP for Email verification: "+resRandom);
		return resRandom;
			
	}

	
	public void sendSignupSuccessMail(Mail mailExceed, SignupEmailMapper mailBody)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
        
        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-signup-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}
	
	public void sendBookingSuccessMail(Mail mailExceed, BookScheduleResponse mailBody,String fileName)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        
        		
        File fileToAttach = fileStorageService.loadBookingFileFullPathWithName(fileName,UploadPathContUtils.FILE_BOOKING_DIR);
        
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
                
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
        helper.addAttachment(fileName, fileToAttach);
        
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-booking-done-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendForgotPasswordMail(Mail mailExceed, SignupEmailMapper mailBody) throws MessagingException, IOException{
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
        
        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-forgot-password-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendResetPasswordMail(Mail mailExceed, SignupEmailMapper mailBody)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
        
        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-reset-password-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendSignupSuccessToAdminMail(Mail mailExceed, SignupEmailMapper mailBody)throws MessagingException, IOException  {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-signup-to-admin-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendSignupOTPMail(Mail mailExceed, SignupEmailMapper mailBody)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-signup-otp-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendBookingUpdateStatusMail(Mail mailExceed, BookScheduleResponse mailBody,String fileName)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        
        		
        File fileToAttach = fileStorageService.loadBookingFileFullPathWithName(fileName,UploadPathContUtils.FILE_BOOKING_DIR);
        
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
                
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
        helper.addAttachment(fileName, fileToAttach);
        
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-booking-update-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}

	public void sendForwarderEnquiryMail(Mail mailExceed, ForwarderEnquiryMailRequest mailBody)throws MessagingException, IOException {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-forwarder-enquiry-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}


	public void sendForwarderEnquiryAcceptedMail(Mail mailExceed,ForwarderQuotationResponse mailBody)throws MessagingException, IOException  {
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-forwarder-enquiry-accept-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
	}


	public void sendFeedbackToAdminMail(Mail mailExceed, FeedbackResponse mailBody)throws MessagingException, IOException {
		
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
        
        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-feedback-to-admin-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}


	public void sendFeedbackReplyToUserMail(Mail mailExceed, FeedbackResponse mailBody)throws MessagingException, IOException {
		
		MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());
        

        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Object> map = oMapper.convertValue(mailBody, Map.class);
        
        //helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));
        //FileSystemResource file = new FileSystemResource(new File(fileToAttach));
   
        Context context = new Context();
        context.setVariables(map);
       
        String templateName="email-feedback-reply-to-user-template";
        
        String html = templateEngine.process(templateName, context);

        helper.setTo(mailExceed.getTo());
        helper.setText(html, true);
        helper.setSubject(mailExceed.getSubject());
        helper.setFrom(mailExceed.getFrom());

        emailSender.send(message);
		
	}
	
}
