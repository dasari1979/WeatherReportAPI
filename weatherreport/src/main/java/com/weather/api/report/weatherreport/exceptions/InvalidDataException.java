package com.weather.api.report.weatherreport.exceptions;

public class InvalidDataException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidDataException(String message) {
        super(message);
    }
}
