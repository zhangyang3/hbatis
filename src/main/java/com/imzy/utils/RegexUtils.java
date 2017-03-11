package com.imzy.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {

	public interface Method {
		String method(String str);
	}

	public static List<String> getGroups(String regex, String content) {
		return getGroups(regex, content, null);
	}

	public static List<String> getGroups(String regex, String content, Method method) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String group = m.group(0);
			if (method != null) {
				group = method.method(group);
			}
			result.add(group);
		}
		return result;
	}
}
