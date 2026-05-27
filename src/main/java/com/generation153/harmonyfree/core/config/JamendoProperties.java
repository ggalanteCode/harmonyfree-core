package com.generation153.harmonyfree.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "jamendo")
@Getter
@Setter
public class JamendoProperties {
	
	private String clientId;
    private String baseUrl;

}
