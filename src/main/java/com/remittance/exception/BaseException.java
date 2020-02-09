package com.remittance.exception;

public class BaseException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode = null;
	
	public BaseException() {
		super();
	}
	public BaseException(String message) {
		super(message);
	}
	public BaseException(String errCode, String message,Throwable cause) {
		super(message, cause);  
		this.errorCode =errCode;
	}
	public BaseException(String errCode, String message) {
		super(message);
		this.errorCode =errCode;
	}
	public String getErrorCode(){
		return errorCode;
	}
	public void setErrorCode(String errCode){
		this.errorCode =errCode;
	}
}