package com.imzy.xml.node;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * dml节点
 * @author yangzhang7
 *
 */
public class DmlNode extends XmlNode {

	private String inType;

	private Class<?> inTypeClass;

	/** sql类型*/
	private LangType langType = null;

	public void setLangType(String langType) {
		if (StringUtils.isNotBlank(langType)) {
			this.langType = LangType.valueOf(langType.toUpperCase());
		}
	}

	public void setLangType(LangType langType) {
		this.langType = langType;
	}

	public LangType getLangType() {
		return langType;
	}

	public String getInType() {
		return inType;
	}

	public void setInType(String inType) {
		this.inType = inType;
		if (StringUtils.isNotBlank(inType)) {
			setInTypeClass(this.inType);
		}
	}

	public Class<?> getInTypeClass() {
		return inTypeClass;
	}

	public void setInTypeClass(Class<?> inTypeClass) {
		this.inTypeClass = inTypeClass;
	}

	public void setInTypeClass(String inType) {
		try {
			this.inTypeClass = Class.forName(inType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
