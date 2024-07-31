package com.my.goldmanager.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.my.goldmanager.config.filter.UIWebFilter;

@Configuration
@Profile({ "default", "dev" })
public class UiPathWebConfig {
	@Bean
	public FilterRegistrationBean<UIWebFilter> spaWebFilter() {
		FilterRegistrationBean<UIWebFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new UIWebFilter());
		registrationBean.addUrlPatterns("/*");
		return registrationBean;
	}
}
