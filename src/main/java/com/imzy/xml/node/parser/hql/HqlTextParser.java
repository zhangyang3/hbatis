package com.imzy.xml.node.parser.hql;

import java.util.List;

import com.imzy.xml.node.Node;
import com.imzy.xml.node.Placeholder;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.node.parser.PlaceholderParser;
import com.imzy.xml.node.parser.TextParser;

/**
 * text��������hqlʵ��
 * @author yangzhang7
 *
 */
public class HqlTextParser implements TextParser {

	private PlaceholderParser hqlPlaceholderParser = new HqlPlaceholderParser();

	public TextNode parse(String text, Node parent) {
		TextNode textNode = new TextNode();
		textNode.setXml(text);

		List<Placeholder> placeholders = Placeholder.getPlaceholderList(textNode.getInnerXml(), hqlPlaceholderParser);
		textNode.setPlaceholders(placeholders);

		// ���ڵ��������
		textNode.setPT(parent);
		return textNode;
	}

}
