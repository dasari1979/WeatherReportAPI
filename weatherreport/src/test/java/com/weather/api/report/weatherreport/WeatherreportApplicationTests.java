package com.weather.api.report.weatherreport;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.weather.api.report.weatherreport.dtos.LoginData;
import com.weather.api.report.weatherreport.dtos.RegisterUser;
import com.weather.api.report.weatherreport.entity.UserCredentials;
import com.weather.api.report.weatherreport.entity.WeatherReport;
import com.weather.api.report.weatherreport.exceptions.InvalidAPIKeyException;
import com.weather.api.report.weatherreport.exceptions.InvalidDataException;
import com.weather.api.report.weatherreport.exceptions.PostalCodeNotFoundException;
import com.weather.api.report.weatherreport.exceptions.ResourceNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherAPIKeyNotFoundException;
import com.weather.api.report.weatherreport.exceptions.WeatherNotFoundException;
import com.weather.api.report.weatherreport.gentoken.GenerateToken;
import com.weather.api.report.weatherreport.repository.UserRepository;
import com.weather.api.report.weatherreport.repository.WeatherReportRepository;
import com.weather.api.report.weatherreport.service.AuthenticationService;
import com.weather.api.report.weatherreport.service.WeatherReportService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(SpringRunner.class)
@SpringBootTest
public class WeatherreportApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(WeatherreportApplicationTests.class);

	@Autowired(required = true)
	private WeatherReportRepository weatherReportRepository;

	@Autowired
	private WeatherReportService weatherReportService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private GenerateToken generateToken;

	@Autowired
	UserRepository userRepository;

	private String uniqueUid = UUID.randomUUID().toString();

	@Test
	public void registerUserTest() {

		RegisterUser registerUser = new RegisterUser();
		registerUser.setUsername(uniqueUid);
		registerUser.setPassword(uniqueUid);
		authenticationService.registerUser(registerUser);
		// Checking
		assertNotNull(userRepository.findByUsername(uniqueUid));
	}

	@Test
	public void tokenGenerateTest() throws InvalidDataException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		LoginData loginData = new LoginData();
		List<UserCredentials> allRecords = userRepository.findAll();
		UserCredentials userCredentials = allRecords.stream().findAny().get();
		loginData.setUsername(userCredentials.getUsername());
		loginData.setPassword(userCredentials.getUsername());

		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
				loginData.getUsername(), loginData.getPassword());
		String accessToken = "";
		try {
			Authentication authUser = authenticationManager.authenticate(usernamePassword);
			logger.info("User authenticated...");
			accessToken = generateToken.generateAccessToken((UserCredentials) authUser.getPrincipal());
			logger.info("Token Generated....");

			// Checking
			assertNotNull(accessToken);

			if (accessToken.isEmpty())
				throw new InvalidDataException("Token not generated and Gernerate Token");
		} catch (AuthenticationException e) {
			throw new InvalidDataException("Token is expired or Invalid Token or Gernerate Token");
		}
	}

	@Test
	public void saveWeatherReportTest()
			throws WeatherNotFoundException, JSONException, IOException, WeatherAPIKeyNotFoundException,
			InvalidAPIKeyException, ResourceNotFoundException, PostalCodeNotFoundException {

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
		weatherReportService.saveWeatherData(weatherReport);
		logger.info(" Saved...");

		// Checking
		List<WeatherReport> totalReport = weatherReportRepository.findAll();
		Assertions.assertThat(
				totalReport.stream().filter(x -> x.getPostalCode().equals("560100")).findFirst().isPresent());
	}

	@Test
	public void getAllWeatherReportTest() {

		List<WeatherReport> totalReport = weatherReportRepository.findAll();
		totalReport.stream().forEach(report -> {
			logger.info("Report details " + report.getName() + " " + report.getPostalCode() + " "
					+ report.getTemperature() + " " + report.getHumidity());
		});
		// Checking
		Assertions.assertThat(totalReport.stream().findAny().get().getUser()).isEqualTo("Max");
	}
}
