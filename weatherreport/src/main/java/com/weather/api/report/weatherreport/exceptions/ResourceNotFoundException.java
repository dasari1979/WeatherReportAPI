package com.weather.api.report.weatherreport.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends Exception {


	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message, HttpStatus badRequest) {
        super(message);
    }
	public ResourceNotFoundException(String message) {
        super(message);
    }
}
