package com.weather.api.report.weatherreport.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.weather.api.report.weatherreport.entity.WeatherReport;

@Repository
public interface WeatherReportRepository extends JpaRepository<WeatherReport, Long>{

	@Query("SELECT op FROM WeatherReport op WHERE op.postalCode = :zipCode")
    List<WeatherReport> findBypostalCode(@Param("zipCode") String zipCode); 
	@Query("SELECT op FROM WeatherReport op WHERE op.user = :user")
    List<WeatherReport> findBymax(@Param("user") String user); 
}
