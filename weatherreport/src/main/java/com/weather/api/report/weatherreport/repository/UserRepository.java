package com.weather.api.report.weatherreport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import com.weather.api.report.weatherreport.entity.UserCredentials;

@Repository
public interface UserRepository extends JpaRepository<UserCredentials, Long> {
	UserDetails findByUsername(String login);
}
