package com.imzy.xml.node.parser.common;

import java.util.List;

import org.dom4j.Element;

import com.imzy.context.ParserContext;
import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.IfNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.parser.Parsable;

public class IfParser implements Parsable<IfNode> {

	public IfNode parse(String text, Node parent) {
		IfNode ifNode = new IfNode();
		ifNode.setXml(text);

		Element rootElement = XmlUtils.getRootElement(text);
		ifNode.setTest(Dom4jUtils.getAttribute(rootElement, "test"));
		// 将节点放入树中
		ifNode.setPT(parent);

		List<String> analyseElements = Dom4jUtils.analyseElement(rootElement);
		for (String analyseElement : analyseElements) {
			Parsable<?> parser = ParserContext.getTextParser(ifNode);
			parser.parse(analyseElement, ifNode);
		}

		return ifNode;
	}

}
