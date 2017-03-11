package com.imzy.xml.node.parser.common;

import java.util.List;

import org.dom4j.Element;

import com.imzy.context.ParserContext;
import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SegmentNode;
import com.imzy.xml.node.parser.Parsable;

public class SegmentParser implements Parsable<SegmentNode> {

	public SegmentNode parse(String text, Node parent) {
		SegmentNode segmentNode = new SegmentNode();
		segmentNode.setXml(text);

		Element rootElement = XmlUtils.getRootElement(text);
		segmentNode.setId(Dom4jUtils.getAttribute(rootElement, "id"));
		// 将节点放入树中
		segmentNode.setPT(parent);

		List<String> analyseElements = Dom4jUtils.analyseElement(rootElement);
		for (String analyseElement : analyseElements) {
			Parsable<?> parser = ParserContext.getTextParser(segmentNode);
			parser.parse(analyseElement, segmentNode);
		}

		return segmentNode;
	}

}
