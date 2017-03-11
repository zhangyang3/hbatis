package com.imzy.xml.node.parser.hql;

import com.imzy.xml.node.parser.PlaceholderParser;

public class HqlPlaceholderParser extends PlaceholderParser {

	@Override
	public String parser(String text) {
		return ":" + text.replaceAll("\\.", "");
	}

}
