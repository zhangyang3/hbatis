package com.imzy.config;

/**
 *
 * @author zy
 *
 */
public class Version {

	public static String MAJOR = "0";
	public static String SECONDARY = "0";
	public static String MINOR = "1";

	public static String getVersion() {
		return MAJOR + "." + SECONDARY + "." + MINOR;
	}

}
