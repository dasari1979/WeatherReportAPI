package com.weather.api.report.weatherreport.exceptions;

import org.springframework.http.HttpStatus;

public class PostalCodeNotFoundException extends Exception{

	
	private static final long serialVersionUID = 1L;

	public PostalCodeNotFoundException(String message, HttpStatus badRequest) {
        super(message);
    }
	public PostalCodeNotFoundException(String message) {
        super(message);
    }
}
