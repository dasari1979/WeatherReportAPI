package com.weather.api.report.weatherreport.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.weather.api.report.weatherreport.dtos.RegisterUser;
import com.weather.api.report.weatherreport.entity.UserCredentials;
import com.weather.api.report.weatherreport.exceptions.InvalidJwtException;
import com.weather.api.report.weatherreport.repository.UserRepository;

@Service
public class AuthenticationService implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userName = userRepository.findByUsername(username);
		return userName;
	}

	public UserDetails registerUser(RegisterUser data) throws InvalidJwtException {

		if (userRepository.findByUsername(data.getUsername()) != null) {
			throw new InvalidJwtException("Username already exists");
		}

		String encryptedPassword = new BCryptPasswordEncoder().encode(data.getPassword());

		UserCredentials newUser = new UserCredentials(data.getUsername(), encryptedPassword);

		return userRepository.save(newUser);

	}
}
