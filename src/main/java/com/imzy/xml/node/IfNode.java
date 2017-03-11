package com.imzy.xml.node;

import org.apache.commons.lang.builder.ToStringBuilder;

public class IfNode extends OperationNode {

	/** ÅÐ¶ÏÌõ¼þ*/
	private String test;

	public String getTest() {
		return test.trim();
	}

	public void setTest(String test) {
		this.test = test.replace('\'', '"');
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public String operate() {
		return null;
	}
}
