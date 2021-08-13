package com.ibm.mediaservice.exception;

public class MediaNotFoundException extends RuntimeException{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MediaNotFoundException(String message){
		 super(message);
	 }
}
