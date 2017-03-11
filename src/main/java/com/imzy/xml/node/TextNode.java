package com.imzy.xml.node;

import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

public class TextNode extends XmlNode {

	/** sql中的占位符*/
	private List<Placeholder> placeholders;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public List<Placeholder> getPlaceholders() {
		return placeholders;
	}

	public void setPlaceholders(List<Placeholder> placeholders) {
		this.placeholders = placeholders;
	}

}
