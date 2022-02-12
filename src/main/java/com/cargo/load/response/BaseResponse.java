package com.cargo.load.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BaseResponse {
	public int respCode;
	private String respMessage;
	private Object respData;
}
