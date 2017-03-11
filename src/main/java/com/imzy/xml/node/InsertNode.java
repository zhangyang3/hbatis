package com.imzy.xml.node;

import org.apache.commons.lang.builder.ToStringBuilder;

public class InsertNode extends DmlNode {
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
