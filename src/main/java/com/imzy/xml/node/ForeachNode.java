package com.imzy.xml.node;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * foreach节点
 * @author yangzhang7
 *
 */
public class ForeachNode extends OperationNode {

	/** 集合*/
	private String collection;

	/** 当前变量*/
	private String item;

	/** 分隔符*/
	private String sperator;

	/** 前缀*/
	private String prefix;

	/** 后缀*/
	private String suffix;

	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getSperator() {
		return sperator;
	}

	public void setSperator(String sperator) {
		this.sperator = sperator;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
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
