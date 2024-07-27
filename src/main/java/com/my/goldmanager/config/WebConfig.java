package com.my.goldmanager.config;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	private static final String ALLOWEDORIGINS = "ALLOWEDORIGINS";
	private static final String[] allowedOriginsDefault = { "http://localhost:8081",
			"http://localhost.localdomain:8081" };

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		String allowedOrigins = SystemEnvUtil.readVariable(ALLOWEDORIGINS);
		String[] allowedOriginsArray;
		if (allowedOrigins != null && !allowedOrigins.isBlank()) {
			logger.info("allow origins {}", allowedOrigins);
			allowedOriginsArray = List.of(allowedOrigins.split("[,]")).stream().map(s -> s.trim())
					.collect(Collectors.toList()).toArray(new String[] {});
		} else {
			allowedOriginsArray = allowedOriginsDefault;
		}
		registry.addMapping("/**").allowedOrigins(allowedOriginsArray)
				.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS").allowedHeaders("*").allowCredentials(true);
	}
}
