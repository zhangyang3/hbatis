package com.imzy.xml.node.parser.common;

import java.util.List;

import org.dom4j.Element;

import com.imzy.context.ParserContext;
import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.ForeachNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.parser.Parsable;

/**
 * foreach解析器接口
 * @author yangzhang7
 *
 */
public class ForeachParser implements Parsable<ForeachNode> {

	public ForeachNode parse(String text, Node parent) {

		ForeachNode foreachNode = new ForeachNode();
		foreachNode.setXml(text);

		Element rootElement = XmlUtils.getRootElement(text);
		foreachNode.setCollection(Dom4jUtils.getAttribute(rootElement, "collection"));
		foreachNode.setItem(Dom4jUtils.getAttribute(rootElement, "item"));
		foreachNode.setSperator(Dom4jUtils.getAttribute(rootElement, "sperator"));
		foreachNode.setPrefix(Dom4jUtils.getAttribute(rootElement, "prefix"));
		foreachNode.setSuffix(Dom4jUtils.getAttribute(rootElement, "suffix"));
		// 将节点放入树中
		foreachNode.setPT(parent);

		List<String> analyseElements = Dom4jUtils.analyseElement(rootElement);
		for (String analyseElement : analyseElements) {
			Parsable<?> parser = ParserContext.getTextParser(foreachNode);
			parser.parse(analyseElement, foreachNode);
		}

		return foreachNode;
	}

}
