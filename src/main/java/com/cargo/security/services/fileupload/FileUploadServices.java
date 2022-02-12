package com.cargo.security.services.fileupload;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.cargo.load.request.CarrierFileConvertFormRequest;
import com.cargo.load.request.UpdateRateTemplateRecordRequest;
import com.cargo.load.response.BaseResponse;

public interface FileUploadServices {

	BaseResponse submitFileUpload(MultipartFile file) throws Exception;

	//testing purpose
	BaseResponse testSubmitFileUpload(MultipartFile file)throws Exception;

	BaseResponse convertUploadedFileMaxiconToTemplate(CarrierFileConvertFormRequest carrierFileConvertFormRequest,
			MultipartFile file) throws Exception;

	BaseResponse uploadTemplateDataRateMaster(String userId,String carrierName,String chargeType,String templateName)throws Exception;

	//testing purpose
	BaseResponse convertPdfToExcel(MultipartFile file)throws Exception;

	BaseResponse uploadDirectCarrierRateTemplateIntoMaster(CarrierFileConvertFormRequest carrierFileConvertFormRequest,
			MultipartFile file)throws Exception;

	BaseResponse uploadOriginDestinationDataFileToMaster(MultipartFile file) throws Exception;

	BaseResponse convertUploadedFileRclToTemplate(CarrierFileConvertFormRequest carrierFileConvertFormRequest,
			MultipartFile file)throws Exception;

	BaseResponse getTemplateDataRateForPreview(Resource resource, String userId, String carrierName,
			String templateName)throws Exception;

	BaseResponse updateTemplateRateFileRecord(Resource resource,UpdateRateTemplateRecordRequest updateRateTemplateRecordRequest) throws Exception;
	
}
