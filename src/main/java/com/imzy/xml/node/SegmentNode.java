package com.imzy.xml.node;

import org.apache.commons.lang.builder.ToStringBuilder;

public class SegmentNode extends XmlNode {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
