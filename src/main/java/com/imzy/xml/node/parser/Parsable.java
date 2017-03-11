package com.imzy.xml.node.parser;

import com.imzy.xml.node.Node;

public interface Parsable<T> {

	/**
	 * 解析xml，封装成对象
	 * @param text
	 * @param parent
	 * @return
	 */
	T parse(String text, Node parent);
}
