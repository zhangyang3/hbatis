package com.imzy.xml.node.parser.common;

import com.imzy.xml.node.DeleteNode;
import com.imzy.xml.node.parser.DmlParser;

/**
 * delete������
 * @author yangzhang7
 *
 */
public class DeleteParser extends DmlParser<DeleteNode> {

	public DeleteParser() {
		super(DeleteNode.class);
	}

}
