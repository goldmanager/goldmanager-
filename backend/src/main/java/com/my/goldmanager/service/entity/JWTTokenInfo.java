package com.my.goldmanager.service.entity;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

public class JWTTokenInfo {
	@Getter
	@Setter
	private String token;
	@Getter
	@Setter
	private Date refreshAfter;
	@Getter
	@Setter
	private Date epiresOn;
}
