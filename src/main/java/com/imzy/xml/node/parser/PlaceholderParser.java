package com.imzy.xml.node.parser;

import com.imzy.xml.node.Node;

/**
 * ռλ���������ӿ�
 * @author yangzhang7
 *
 */
public abstract class PlaceholderParser implements Parsable<String> {

	public abstract String parser(String text);

	public String parse(String text, Node parent) {
		return null;
	}

}
