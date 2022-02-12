package com.cargo.load.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RatePreviewListResponse {

	private String carriername;
	private String templatename;
	
	List<CommonRateTemplateResponse> commonRateTemplateRespList;
	List<CommonRateTemplateResponse> commonRateTemplateRespErrorList;
}
