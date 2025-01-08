package com.weather.api.report.weatherreport.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.exceptions.InvalidAPIKeyException;
import com.weather.api.report.weatherreport.exceptions.PostalCodeNotFoundException;
import com.weather.api.report.weatherreport.exceptions.ResourceNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherAPIKeyNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherNotFoundException;
import com.weather.api.report.weatherreport.service.WeatherReportService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/app")
@Slf4j
public class WeatherReportController {
	
	private final static Logger logger = LoggerFactory.getLogger(WeatherReportController.class);
	
	@Autowired
	private WeatherReportService weatherReportService;
	
	/**
	 * This method is called when a POST request is made
	 * URL: http://localhost:9090/app/weather
     * Purpose: save an WeatherReport entity
     * @param postalCode - Request body is an WeatherReport entity
	 * @param user - - Request body is an WeatherReport entity
     * @return Saved WeatherReport entity
	 * @throws ResourceNotFoundException 
	 */
	@GetMapping("/history/data")
	@Cacheable(value="WeatherReport")
	public ResponseEntity<List<WeatherReport>> fetchWeatherDataByPostalCode(@RequestParam(name = "postalcode",required = false) String postalCode,@RequestParam(name = "user",required = false) String user) throws ResourceNotFoundException {
		logger.info("Fetching weather history for "+postalCode+" postalcode");
		List<WeatherReport> records = weatherReportService.findBypostalCodeOruser(postalCode,user);
		if(!records.isEmpty())
		return ResponseEntity.ok(records);
		else
		return ResponseEntity.status(HttpStatus.NO_CONTENT) 
                .body(records);
	}
	/**
	 * This method is called when a GET request is made
	 * URL: http://localhost:8000/app/history
	 * Purpose: Fetch the weather report based on postalCode or user
	 * @param postalCode Request body is an WeatherReport entity
	 * @param user Request body is an WeatherReport entity
	 * @param model contains the data of the application
	 * @return Fetch weather data to html
	 * @throws ResourceNotFoundException
	 */
	@GetMapping("/history")
	@Cacheable(value="WeatherReport")
	public String fetchWeatherDataByPostalCodeORUser(@RequestParam(name = "postalcode",required = false) String postalCode,@RequestParam(name = "user",required = false) String user,Model model) throws ResourceNotFoundException {
		logger.info("Fetching weather history...");

		List<WeatherReport> records = weatherReportService.findBypostalCodeOruser(postalCode,user);
		model.addAttribute("weatherReport", records);
		if(records.isEmpty())
			model.addAttribute("weatherReport", postalCode);
		 if(!records.isEmpty())
			return "history";
		else 
		    return "error";
	}
	
	/**
	 * 
	 * @param weatherReport
	 * @return
	 * @throws IOException
	 * @throws JSONException
	 * @throws WeatherAPIKeyNotFoundException 
	 * @throws InvalidAPIKeyException 
	 * @throws ResourceNotFoundException 
	 * @throws PostalCodeNotFoundException 
	 */
	@PostMapping("/weather")
	public ResponseEntity<WeatherReport> saveWeatherData(@RequestBody @Valid WeatherReport weatherReport) throws WeatherNotFoundException, JSONException, IOException, WeatherAPIKeyNotFoundException, InvalidAPIKeyException, ResourceNotFoundException, PostalCodeNotFoundException{
		    logger.info("Inserting weather data...");
		    WeatherReport report = weatherReportService.saveWeatherData(weatherReport);
		    if(report.getTemperature()!=0)
			return ResponseEntity.ok().body(report);
		    else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST) 
	                .body(report);

	}
}
