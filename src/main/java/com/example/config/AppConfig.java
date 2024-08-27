package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class AppConfig {

	private String jwtSecret;
	private long jwtRefreshExpirationMs;
	
}
