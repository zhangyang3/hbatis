package com.imzy.xml.node.parser.common;

import org.dom4j.Element;

import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.IncludeNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.parser.Parsable;

public class IncludeParser implements Parsable<IncludeNode> {

	public IncludeNode parse(String text, Node parent) {
		IncludeNode includeNode = new IncludeNode();
		includeNode.setXml(text);

		Element rootElement = XmlUtils.getRootElement(text);
		includeNode.setRefid(Dom4jUtils.getAttribute(rootElement, "refid"));

		// 将节点放入树中
		includeNode.setPT(parent);
		return includeNode;
	}

}
