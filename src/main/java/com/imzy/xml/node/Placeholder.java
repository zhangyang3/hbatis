package com.imzy.xml.node;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.imzy.utils.RegexUtils;
import com.imzy.xml.node.parser.PlaceholderParser;

/**
 * 占位符
 * @author yangzhang7
 *
 */
public class Placeholder {

	/** 原始字符串*/
	private String before;

	/** 属性名称*/
	private String attributeName;

	/** 替换后字符串*/
	private String after;

	public String getBefore() {
		return before;
	}

	public void setBefore(String before) {
		this.before = before;
	}

	public String getAfter() {
		return after;
	}

	public void setAfter(String after) {
		this.after = after;
	}

	public String getAttributeName() {
		return attributeName;
	}

	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public static List<Placeholder> getPlaceholderList(String text, PlaceholderParser method) {
		List<Placeholder> placeholders = new ArrayList<Placeholder>();
		List<String> groups = RegexUtils.getGroups("#\\{\\s*[\\w\\.]+\\s*\\}", text);
		for (String before : groups) {
			Placeholder placeholder = new Placeholder();
			placeholder.setBefore(before);
			placeholder.setAttributeName(before.substring(before.indexOf("{") + 1, before.lastIndexOf("}")).trim());
			String after = method.parser(placeholder.getAttributeName());
			placeholder.setAfter(after);
			placeholders.add(placeholder);
		}
		return placeholders;
	}

}
