package com.my.goldmanager.config;


public class SystemEnvUtil {
	/**
	 * Pstatic class
	 */
	private SystemEnvUtil() {
		
	}
	/**
	 * Reads the specified variable
	 * @param name
	 * @return
	 */
	public static String readVariable(String name) {
		if (System.getenv(name) != null) {
			return System.getenv(name);
		}
		return System.getProperty(name);
	}
}
