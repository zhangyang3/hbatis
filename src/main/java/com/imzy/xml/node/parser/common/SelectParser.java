package com.imzy.xml.node.parser.common;

import com.imzy.xml.node.SelectNode;
import com.imzy.xml.node.parser.DmlParser;

/**
 * select������
 * @author yangzhang7
 *
 */
public class SelectParser extends DmlParser<SelectNode> {

	public SelectParser() {
		super(SelectNode.class);
	}

}
