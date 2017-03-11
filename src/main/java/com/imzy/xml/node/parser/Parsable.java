package com.imzy.xml.node.parser;

import com.imzy.xml.node.Node;

public interface Parsable<T> {

	/**
	 * ����xml����װ�ɶ���
	 * @param text
	 * @param parent
	 * @return
	 */
	T parse(String text, Node parent);
}
