package com.my.goldmanager.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.my.goldmanager.service.entity.KeyInfo;

import io.jsonwebtoken.Jwts;

@Service
public class AuthenticationService {
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthKeyInfoService authKeyInfoService;

	public String getJWTToken(String username, String password) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(username, password));

		User user = (User) authentication.getPrincipal();
		KeyInfo keyInfo = authKeyInfoService.getKeyInfoForUserName(user.getUsername());

		return Jwts.builder().header().add("keyId", keyInfo.getKeyId()).and().subject(user.getUsername())
				.issuedAt(new Date()).expiration(new Date(System.currentTimeMillis() + 86400000))
				.signWith(keyInfo.getKey()).compact();

	}

	public void logout(String username) {
		authKeyInfoService.removeKeyInfoForUserName(username);
	}

	public void logoutAll() {
		authKeyInfoService.clearAll();
	}

}
