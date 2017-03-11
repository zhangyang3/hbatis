package com.imzy.utils;

import java.net.URISyntaxException;

public class PathUtils {

	public static String getRealPath() {
		String path = null;
		try {
			path = PathUtils.class.getClassLoader().getResource("").toURI().getPath();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return path;
	}

	public static String getPath(String path) {
		return getRealPath() + path;
	}

}
