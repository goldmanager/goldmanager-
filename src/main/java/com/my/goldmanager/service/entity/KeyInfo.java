package com.my.goldmanager.service.entity;

import java.security.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * KeyInfo containing Key, username & KeyId
 */
@NoArgsConstructor
@AllArgsConstructor
public class KeyInfo {
	@Getter
	@Setter
	private Key key;
	@Getter
	@Setter
	private String username;
	@Getter
	@Setter
	private String keyId;
}
