package com.imzy.xml.node.parser.sql;

import java.util.List;

import com.imzy.xml.node.Node;
import com.imzy.xml.node.Placeholder;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.node.parser.PlaceholderParser;
import com.imzy.xml.node.parser.TextParser;

/**
 * text��������sqlʵ��
 * @author yangzhang7
 *
 */
public class SqlTextParser implements TextParser {
	private PlaceholderParser sqlPlaceholderParser = new SqlPlaceholderParser();

	public TextNode parse(String text, Node parent) {
		TextNode textNode = new TextNode();
		textNode.setXml(text);

		List<Placeholder> placeholders = Placeholder.getPlaceholderList(textNode.getInnerXml(), sqlPlaceholderParser);
		textNode.setPlaceholders(placeholders);

		// ���ڵ��������
		textNode.setPT(parent);

		return textNode;
	}

}
