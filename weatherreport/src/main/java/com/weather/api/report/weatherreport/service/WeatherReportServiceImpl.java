package com.weather.api.report.weatherreport.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.weather.api.report.weatherreport.countrycodes.CountryCodes;
import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.exceptions.InvalidAPIKeyException;
import com.weather.api.report.weatherreport.exceptions.PostalCodeNotFoundException;
import com.weather.api.report.weatherreport.exceptions.ResourceNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherAPIKeyNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherNotFoundException;
import com.weather.api.report.weatherreport.repository.WeatherReportRepository;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
@Slf4j
public class WeatherReportServiceImpl implements WeatherReportService{

	@Autowired
	WeatherReportRepository weatherReportRepository;
	
	@Value("${weatherreport.api.key}")
	String apikey;
	
	@Override
	public List<WeatherReport> fetchWheatherData() {
		
		return (List<WeatherReport>) weatherReportRepository.findAll();
	}


	@Override
	public List<WeatherReport> findBypostalCodeOruser(String postalCode,String max) throws ResourceNotFoundException {
		List<WeatherReport> records = new ArrayList<>();
		
		if(postalCode != null)
		records = weatherReportRepository.findBypostalCode(postalCode);
		else if(max != null)
		records = weatherReportRepository.findBymax(max);
		return records;
	}

	@Override
	public WeatherReport saveWeatherData(WeatherReport weatherReport) throws JSONException, IOException, WeatherAPIKeyNotFoundException, WeatherNotFoundException, InvalidAPIKeyException, ResourceNotFoundException, PostalCodeNotFoundException   {

			String pinCode = String.valueOf(weatherReport.getPostalCode());
			String countryCode = findCountryCode(pinCode);
			String jsonString = getWeatherDataCity(pinCode, countryCode);
			
			if(jsonString.isEmpty())
				throw new WeatherNotFoundException("Weather Not found");
			
			JSONObject obj = new JSONObject(jsonString.trim());
			setWeatherParameters(obj,weatherReport);

		return weatherReportRepository.save(weatherReport);
	}


	private WeatherReport setWeatherParameters(JSONObject obj,WeatherReport weatherReport) throws JSONException {
		String name = obj.getString("name").toString();
		double temperature = obj.getJSONObject("main").getDouble("temp");
		double tempMax = obj.getJSONObject("main").getDouble("temp_max");
		double tempMin = obj.getJSONObject("main").getDouble("temp_min");
		String weather = obj.getJSONArray("weather").getJSONObject(0).getString("main");
		String weatherDesc = obj.getJSONArray("weather").getJSONObject(0).getString("description");
		double humidity = obj.getJSONObject("main").getInt("humidity");
		double pressure = obj.getJSONObject("main").getInt("pressure");
		
		weatherReport.setName(name);
		weatherReport.setHumidity(humidity);
		weatherReport.setPressure(pressure);
		weatherReport.setTemperature(temperature);
		weatherReport.setTempMax(tempMax);
		weatherReport.setTempMin(tempMin);
		weatherReport.setWeather(weather);
		weatherReport.setWeatherDesc(weatherDesc);
		weatherReport.setActive(true);
		
		return weatherReport;

	}


	@Override
	public String findCountryCode(String postalCode) throws IOException, JSONException, ResourceNotFoundException, PostalCodeNotFoundException {
		return jsonParsePostalCode(postalCode);
	}

	private String jsonParsePostalCode(String postalCode) throws IOException, JSONException, ResourceNotFoundException, PostalCodeNotFoundException {
		 CountryCodes codes = new CountryCodes();
		 String cuntryName = codes.getCountyName(postalCode);
		return codes.getCountryCode(cuntryName);
	}


	@Override
	public String getWeatherDataCity(String pinCode, String countryCode) throws IOException, WeatherAPIKeyNotFoundException, InvalidAPIKeyException {

		return connectAPICity(pinCode, countryCode);
	}
	
	private String connectAPICity(String zipCode, String countryCode) throws IOException, WeatherAPIKeyNotFoundException, InvalidAPIKeyException {
		
		if(apikey.isEmpty())
		throw new WeatherAPIKeyNotFoundException("API KEY Not found");
			
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
				.url("https://api.openweathermap.org/data/2.5/weather?zip="+zipCode+","+countryCode+"&appid="+apikey)
				.get()
				.addHeader("Content-Type", "application/json")
				.addHeader("Accept", "application/json")
				.build();
		return getResponse(client, request);
		
	}
	private String getResponse(OkHttpClient client, Request request) throws IOException, InvalidAPIKeyException {
		
		Response response = client.newCall(request).execute();
		
		String responseBody = response.body().string();

		if(!response.isSuccessful() || responseBody.contains("Invalid API key") || response.code() == 401)
		throw new InvalidAPIKeyException("Invalid API key or Bad Request");
		return responseBody;
		
	}
}
