package com.imzy.xml.node.parser;

import java.util.List;

import org.dom4j.Element;

import com.imzy.context.ParserContext;
import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SqlNode;

public class SqlParser implements Parsable<SqlNode> {

	public SqlNode parse(String text, Node parent) {

		Element rootElement = XmlUtils.getRootElement(text);
		SqlNode sqlNode = new SqlNode(Dom4jUtils.getAttribute(rootElement, "namespace"));
		sqlNode.setXml(text);
		sqlNode.setLangType(Dom4jUtils.getAttribute(rootElement, "lang"));

		List<String> nodeElements = Dom4jUtils.analyseElement(rootElement);
		for (String nodeElement : nodeElements) {
			Element re = XmlUtils.getRootElement(nodeElement);
			Parsable<?> parser = ParserContext.getCommonParserByQName(re.getQName().getName());
			if (!(parser instanceof TextParser) && null != parser) {
				parser.parse(nodeElement, sqlNode);
			}
		}

		return sqlNode;
	}

	public SqlNode parse(String text) {
		return parse(text, null);
	}

}
