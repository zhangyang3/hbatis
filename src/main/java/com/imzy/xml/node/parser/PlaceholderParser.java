package com.imzy.xml.node.parser;

import com.imzy.xml.node.Node;

/**
 * 占位符解析器接口
 * @author yangzhang7
 *
 */
public abstract class PlaceholderParser implements Parsable<String> {

	public abstract String parser(String text);

	public String parse(String text, Node parent) {
		return null;
	}

}
