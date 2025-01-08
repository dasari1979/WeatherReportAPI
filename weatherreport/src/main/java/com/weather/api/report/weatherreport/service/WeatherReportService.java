package com.weather.api.report.weatherreport.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.exceptions.InvalidAPIKeyException;
import com.weather.api.report.weatherreport.exceptions.PostalCodeNotFoundException;
import com.weather.api.report.weatherreport.exceptions.ResourceNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherAPIKeyNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherNotFoundException;

public interface WeatherReportService {

	List<WeatherReport> fetchWheatherData();
	String getWeatherDataCity(String city, String country) throws IOException,WeatherAPIKeyNotFoundException, InvalidAPIKeyException;
	WeatherReport saveWeatherData(WeatherReport weatherReport) throws WeatherNotFoundException, JSONException, IOException, WeatherAPIKeyNotFoundException, InvalidAPIKeyException, ResourceNotFoundException, PostalCodeNotFoundException;
	String findCountryCode(String postalCode) throws IOException, JSONException, ResourceNotFoundException, PostalCodeNotFoundException;
	List<WeatherReport> findBypostalCodeOruser(String postalCode,String max) throws ResourceNotFoundException;
}
