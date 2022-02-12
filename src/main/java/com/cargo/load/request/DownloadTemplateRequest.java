package com.cargo.load.request;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class DownloadTemplateRequest {
	
	 private String userId;
     private String carrier;
     private String templateName;
    
}
