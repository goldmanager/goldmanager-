package com.my.goldmanager.config;

import java.net.http.HttpClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({ "default", "dev" })
public class HttpClientConfig {

	@Bean
	public HttpClient.Builder httpClientBuilder() {
		return HttpClient.newBuilder();
	}
}
