package com.my.goldmanager.service;

import java.security.Key;

import com.my.goldmanager.service.entity.KeyInfo;

/**
 * Service interface providing secret Keys for Authentication
 */
public interface AuthKeyInfoService {

	/**
	 * Returns a {@link KeyInfo} for specified username
	 * 
	 * @param username
	 * @return
	 */
	KeyInfo getKeyInfoForUserName(String username);

	/**
	 * Returns a {@link Key} for specified KeyId
	 * 
	 * @param keyId
	 * @return
	 */
	Key getKeyforKeyId(String keyId);

	/**
	 * Remove KeyInfo for user
	 * @param username
	 */
	void removeKeyInfoForUserName(String username);
	
	/**
	 * Remove all KeyInfos
	 */
	void clearAll();

}
