package com.imzy.xml.node;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.imzy.context.TreeContext;

public class SqlNode extends XmlNode {
	/** 命名空间：与dao的全类名对应*/
	private String namespace;
	/** sql类型*/
	private LangType langType = LangType.SQL;

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

	public String getNamespace() {
		return namespace;
	}

	public SqlNode(String namespace) {
		this.namespace = namespace;
		this.setTreeId(namespace);
		this.setSelfId(namespace);

		TreeContext.putTreeNode(this);
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
