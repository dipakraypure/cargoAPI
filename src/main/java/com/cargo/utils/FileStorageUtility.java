package com.cargo.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.exception.FileStorageException;
import com.cargo.models.FileStorageProperties;


@Service
public class FileStorageUtility {
	
	 @Value("${file.upload-dir}")
     private String path;
	
	 private final Path fileStorageLocation;
	 
	 @Autowired
	 public FileStorageUtility(FileStorageProperties fileStorageProperties) {
	        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
	            .toAbsolutePath().normalize();

	        try {
	            Files.createDirectories(this.fileStorageLocation);
	        } catch (Exception ex) {
	            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
	        }
	 }
	 
	 public String storeFile(MultipartFile file,String fileType,String fileCompanyDir,String userId) {
	        
	        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	        
	        fileName = userId+"_"+fileType+"_"+fileName; 
	        String fileCompanyDirWithFilename = fileCompanyDir+"/"+fileName;
	        
	        try {
	            // Check if the file's name contains invalid characters
	            if (fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	            }

	            Files.createDirectories(this.fileStorageLocation.resolve(fileCompanyDir));	          
	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileCompanyDirWithFilename);
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	        
	        return fileName;        
	    }
	 
	 public boolean deleteExistsFileFromDrirectory(String fileType,String fileDir,String userId) {
		    String files;
		    boolean flag = false;
		    String newPath = path+"/"+fileDir;
		    File folder = new File(newPath);
		    File[] listOfFiles = folder.listFiles();

		    for (File checkedFile : listOfFiles){
		        if (checkedFile.isFile()){
		                files = checkedFile.getName();
		                System.out.println(files);
		                if (files.startsWith(userId+"_"+fileType)){
		                	if(checkedFile.delete()) {
		                		 System.out.println("======file found and deleted====="+files);
		                		 flag = true;
		                	}	                   
		            }
		        }
		    }
			return flag;
	 }

	public String storeOfferImageFile(MultipartFile file, String fileOfferDir, String offerName, String userId) {
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
	     
		    String timeStamp = new SimpleDateFormat("dd-MMM-yyyy").format(new Date());
		    
	        fileName = timeStamp+"_"+fileName; 
	        
 
	        String fileOfferDirWithFileName = fileOfferDir+"/"+fileName;
	        
	        try {
	            // Check if the file's name contains invalid characters
	            if (fileName.contains("..")) {
	                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileOfferDirWithFileName);
	            }

	           
	            Files.createDirectories(this.fileStorageLocation.resolve(fileOfferDir));
	            // Copy file to the target location (Replacing existing file with the same name)
	            Path targetLocation = this.fileStorageLocation.resolve(fileOfferDirWithFileName);	            
	            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

	        } catch (IOException ex) {
	            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
	        }
	        
	        return fileName;  
	}

	public String getUploadDirFileFullPathLocation(String filename,String fileOfferDir) {
		
		String fullPathWithname = fileOfferDir+"/"+filename;
		String fullpath = "";
		
		 // Check if the file's name contains invalid characters
		if (filename.contains("..")) {
		    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fullPathWithname);
		}

		// Copy file to the target location (Replacing existing file with the same name)
		Path targetLocation = this.fileStorageLocation.resolve(fullPathWithname);
		fullpath = targetLocation.toString();
		
		return fullpath;
	}
}
