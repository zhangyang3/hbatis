package com.imzy.xml.node;

public enum LangType {

	HQL("hql"), SQL("sql");

	private String name;

	LangType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
