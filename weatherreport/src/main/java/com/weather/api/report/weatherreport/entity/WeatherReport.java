package com.weather.api.report.weatherreport.entity;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name ="WeatherReport")
@AllArgsConstructor(staticName ="build")
@NoArgsConstructor
@Data
@Component
public class WeatherReport {

	private static final Double ABSOLUTE_TEMPERATURE_CONSTANT = 273.15;
	@Transient
	private DecimalFormat df;
	
	public WeatherReport() {
		this.df = new DecimalFormat("#.00");
		this.df.setRoundingMode(RoundingMode.CEILING);
	}
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID",nullable = false)
    private Integer id;
    @Column(name = "NAME")
    private String name;
    @Column(name = "USER")
    @NotNull(message = "user should not be null")
    @NotEmpty(message = "user should not be empty")
    private String user;
    @NotNull(message = "postalCode should not be null")
    @NotEmpty(message = "postalCode should not be empty")
    @Column(name = "POSTALCODE",length = 6)
    @Pattern(regexp = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$", message = "Invalid postalCode")
	private String postalCode;
    @Column(name = "TEMPERATURE")
	private double temperature;
    @Column(name = "WEATHER")
	private String weather;
    @Column(name = "WEATHERDESC")
	private String weatherDesc;
    @Column(name = "TEMPERATURE_MIN")
	private double tempMin;
    @Column(name = "TEMPERATURE_MAX")
	private double tempMax;
    @Column(name = "PRESSURE")
	private double pressure;
    @Column(name = "HUMIDITY")
	private double humidity;
    @CreationTimestamp
    @Column(updatable = false, name = "CREATED_AT")
    private Date createdAt;
    @Column(name = "ACTIVE")
    private Boolean isActive = false;

	public Boolean isActive() {
		return isActive;
	}
	public void setActive(Boolean isActive) {
		this.isActive = isActive;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public double getTemperature() {
		return temperature;
	}
	public void setTemperature(double temperature) {
		this.temperature = Double.parseDouble(String.format("%.2f", temperature - ABSOLUTE_TEMPERATURE_CONSTANT));
	}
	public String getWeather() {
		return weather;
	}
	public void setWeather(String weather) {
		this.weather = weather;
	}
	public String getWeatherDesc() {
		return weatherDesc;
	}
	public void setWeatherDesc(String weatherDesc) {
		this.weatherDesc = weatherDesc;
	}
	public double getTempMin() {
		return tempMin;
	}
	public void setTempMin(double tempMin) {
		this.tempMin = Double.parseDouble(String.format("%.2f", tempMin - ABSOLUTE_TEMPERATURE_CONSTANT));
	}
	public double getTempMax() {
		return tempMax;
	}
	public void setTempMax(double tempMax) {
		this.tempMax = Double.parseDouble(String.format("%.2f", tempMax - ABSOLUTE_TEMPERATURE_CONSTANT));
	}
	public double getPressure() {
		return pressure;
	}
	public void setPressure(double pressure) {
		this.pressure = pressure;
	}
	public double getHumidity() {
		return humidity;
	}
	public void setHumidity(double humidity) {
		this.humidity = humidity;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}
