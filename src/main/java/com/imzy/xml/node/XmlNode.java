package com.imzy.xml.node;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * xml节点
 * @author yangzhang7
 *
 */
public abstract class XmlNode extends Node {

	/** xml的id值*/
	private String id;

	/** 节点内容（不包括自身）*/
	private String innerXml;

	/** 节点内容（包括自身）*/
	private String xml;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInnerXml() {
		return innerXml;
	}

	/**
	 * 获取节点中间的内容
	 * @param text
	 * @return
	 */
	private String getMiddleContent(String text) {
		if (text.indexOf(">") <= text.lastIndexOf("<")) {
			return text.substring(text.indexOf(">") + 1, text.lastIndexOf("<"));
		} else {
			return StringUtils.EMPTY;
		}
	}

	public String getXml() {
		return xml;
	}

	public void setInnerXml(String innerXml) {
		this.innerXml = innerXml;
	}

	public void setXml(String xml) {
		this.xml = xml.trim();
		this.setInnerXml(getMiddleContent(this.xml).trim());
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
