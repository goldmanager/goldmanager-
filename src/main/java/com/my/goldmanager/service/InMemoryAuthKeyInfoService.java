package com.my.goldmanager.service;

import java.security.Key;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.my.goldmanager.service.entity.KeyInfo;

import io.jsonwebtoken.Jwts;

@Service
public class InMemoryAuthKeyInfoService implements AuthKeyInfoService {
	private final ConcurrentHashMap<String, Key> secretKeys = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<String, String> userKeyIDMap = new ConcurrentHashMap<>();

	@Override
	public synchronized KeyInfo getKeyInfoForUserName(String username) {
		synchronized (secretKeys) {

			String keyId = userKeyIDMap.get(username);
			if (keyId == null) {
				keyId = UUID.randomUUID().toString();
				userKeyIDMap.put(username, keyId);

			}
			Key secretKey = secretKeys.get(keyId);

			if (secretKey == null) {
				secretKey = Jwts.SIG.HS256.key().build();
				secretKeys.put(keyId, secretKey);
			}

			return new KeyInfo(secretKey, username, keyId);
		}

	}

	@Override
	public Key getKeyforKeyId(String keyId) {

		return secretKeys.get(keyId);
	}

	@Override
	public synchronized void removeKeyInfoForUserName(String username) {
		String keyId = userKeyIDMap.get(username);
		if (keyId != null) {
			secretKeys.remove(keyId);
		}
		userKeyIDMap.remove(username);

	}

	@Override
	public void clearAll() {
		secretKeys.clear();
		userKeyIDMap.clear();

	}

}
