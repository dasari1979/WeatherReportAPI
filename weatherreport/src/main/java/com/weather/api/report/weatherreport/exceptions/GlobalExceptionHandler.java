package com.weather.api.report.weatherreport.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public Map<String, String> handleException(MethodArgumentNotValidException ex) {

		Map<String, String> errorMap = new HashMap<>();

		ex.getBindingResult().getFieldErrors().forEach(error -> {

			errorMap.put(error.getField(), error.getDefaultMessage());
		});

		return errorMap;
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<String> handleAllExceptions(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + ex.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = NullPointerException.class)
	public ResponseEntity<String> handleNullPointerException(NullPointerException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad Input OR Not able to find Record OR User not found");
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = PostalCodeNotFoundException.class)
	public ResponseEntity<String> handlePostalCodNotFound(PostalCodeNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please provide valid PostalCode...");
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = WeatherNotFoundException.class)
	public ResponseEntity<String> handleWeatherNotFoundException(WeatherNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Weather report not found : " + ex.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = ResourceNotFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = InvalidDataException.class)
	public ResponseEntity<String> handleInvalidDataException(InvalidDataException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid data provided: " + ex.getMessage());
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = WeatherAPIKeyNotFoundException.class)
	public ResponseEntity<String> handleInvalidDataException(WeatherAPIKeyNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Please provide API KEY from https://api.openweathermap.org/");
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(value = InvalidAPIKeyException.class)
	public ResponseEntity<String> handleInvalidAPIKeyException(InvalidAPIKeyException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Please provide valid API KEY from https://api.openweathermap.org/");
	}
}
