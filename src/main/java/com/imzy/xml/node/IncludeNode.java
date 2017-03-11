package com.imzy.xml.node;

import org.apache.commons.lang.builder.ToStringBuilder;

public class IncludeNode extends XmlNode {

	/** ¹ØÁªÆ¬¶Îid*/
	private String refid;

	public String getRefid() {
		return refid;
	}

	public void setRefid(String refid) {
		this.refid = refid;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
