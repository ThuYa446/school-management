package com.astarel.school.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class ApiErrorResponse extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String errorCode;
	String message;
	
	public ApiErrorResponse(String errorCode,String message)
	{
		super();
		this.errorCode = errorCode;
		this.message = message;
	}
}
