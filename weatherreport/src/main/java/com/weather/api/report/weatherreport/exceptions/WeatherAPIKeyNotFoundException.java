package com.weather.api.report.weatherreport.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherAPIKeyNotFoundException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger logger = LoggerFactory.getLogger(WeatherAPIKeyNotFoundException.class);
	/**
	 * 
	 */
	public WeatherAPIKeyNotFoundException() {}
	public WeatherAPIKeyNotFoundException(String msg)
	{
		logger.error("Weather not found "+msg);
	}
}
