package com.weather.api.report.weatherreport.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weather.api.report.weatherreport.dtos.LoginData;
import com.weather.api.report.weatherreport.dtos.RegisterUser;
import com.weather.api.report.weatherreport.entity.UserCredentials;
import com.weather.api.report.weatherreport.exceptions.InvalidDataException;
import com.weather.api.report.weatherreport.gentoken.GenerateToken;
import com.weather.api.report.weatherreport.service.AuthenticationService;

@RestController
@RequestMapping("/app/auth")
public class AuthenticationController {

	private final static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private AuthenticationService authenticationService;
	@Autowired
	private GenerateToken generateToken;

	@PostMapping("/register")
	public ResponseEntity<?> signUp(@RequestBody @Valid RegisterUser data) {
		logger.info("Registering User...");
		authenticationService.registerUser(data);
		return ResponseEntity.status(HttpStatus.CREATED).build();

	}

	@PostMapping("/login")
	public ResponseEntity<String> signIn(@RequestBody @Valid LoginData data) throws InvalidDataException {
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(data.getUsername(),
				data.getPassword());
		String accessToken = "";
		try {
		Authentication authUser = authenticationManager.authenticate(usernamePassword);
		logger.info("User authenticated...");
		accessToken = generateToken.generateAccessToken((UserCredentials) authUser.getPrincipal());
		logger.info("Token Generated....");
		if(accessToken.isEmpty())
			throw new InvalidDataException("Token not generated and Gernerate Token");
		}catch (AuthenticationException e) {
			throw new InvalidDataException("Token is expired or Invalid Token or Gernerate Token");
		}
		return ResponseEntity.ok("Token Generated successfully " + accessToken);
	}

}
