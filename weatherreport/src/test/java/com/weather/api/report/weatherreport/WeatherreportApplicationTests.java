package com.weather.api.report.weatherreport;

import java.util.Date;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.repository.WeatherReportRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherreportApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(WeatherreportApplicationTests.class);
	
	@Autowired(required=true)
	private WeatherReportRepository weatherReportRepository ;
	
	
	@Test
	public void saveWeatherReportTest() {
		
		WeatherReport weatherReport = new WeatherReport();
		weatherReport.setId(34);
        Date date = new Date(); 
		weatherReport.setCreatedAt(date);
		weatherReport.setHumidity(93);
		weatherReport.setName("Test");
		weatherReport.setPostalCode("560100");
		weatherReport.setPressure(1015);
		weatherReport.setTempMax(28.15);
		weatherReport.setTemperature(27.22);
		weatherReport.setTempMin(25.22);
		weatherReport.setUser("Max");
		weatherReport.setWeather("Clouds");
		weatherReport.setWeatherDesc("Clear sky");
        
		weatherReportRepository.save(weatherReport);
		logger.info(" Saved...");
		// Checking
		 Assertions.assertThat(weatherReport.getPostalCode()).isEqualTo("560100");
	}
	
	@Test
	public void getAllWeatherReport() {
		
		List<WeatherReport> totalReport = weatherReportRepository.findAll();
		totalReport.stream().forEach(report -> {
			logger.info("Report details "+report.getName()+" "+report.getPostalCode()+" "+report.getTemperature()+" "+report.getHumidity());
		});		
		// Checking
		Assertions.assertThat(totalReport.stream().findAny().get().getUser()).isEqualTo("Max");
	}
}
