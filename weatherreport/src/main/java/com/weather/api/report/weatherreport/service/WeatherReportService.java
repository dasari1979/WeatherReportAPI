package com.weather.api.report.weatherreport.service;

import java.io.IOException;
import java.util.List;

import org.json.JSONException;

import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.exceptions.ResourceNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherAPIKeyNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherNotFoundException;

public interface WeatherReportService {

	List<WeatherReport> fetchWheatherData();
	String getWeatherDataCity(String city, String country) throws IOException,WeatherAPIKeyNotFoundException;
	WeatherReport saveWeatherData(WeatherReport weatherReport) throws WeatherNotFoundException, JSONException, IOException, WeatherAPIKeyNotFoundException;
	String findCountryCode(String postalCode) throws IOException, JSONException;
	List<WeatherReport> findBypostalCodeOruser(String postalCode,String max) throws ResourceNotFoundException;
}
