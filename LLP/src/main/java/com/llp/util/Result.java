package com.llp.util;

public class Result {

	private Boolean executionSuccessful;
	private String successCode;
	private String errorCode;
	private String message;
	private Object returnValue;
	
	public Result()  {
		executionSuccessful = true;
	}
	
	public void clean() {
		executionSuccessful = true;
		successCode = null;
		errorCode = null;
		message = null;
		returnValue = null;
	}
	
	public Boolean isExecutionSuccessful() {
		return executionSuccessful;
	}
	public void setExecutionSuccessful(Boolean executionSuccessful) {
		this.executionSuccessful = executionSuccessful;
	}
	public String getSuccessCode() {
		return successCode;
	}
	public void setSuccessCode(String successCode) {
		this.successCode = successCode;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getReturnValue() {
		return returnValue;
	}
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
}
