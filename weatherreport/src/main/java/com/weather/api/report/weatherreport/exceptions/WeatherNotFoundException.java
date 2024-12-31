package com.weather.api.report.weatherreport.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherNotFoundException extends Exception {
	private static final Logger logger = LoggerFactory.getLogger(WeatherNotFoundException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public WeatherNotFoundException() {}
	public WeatherNotFoundException(String msg)
	{

		super(msg);
		logger.error("Weather not found "+msg);
	}
}