package com.imzy.xml.node.parser.sql;

import com.imzy.xml.node.parser.PlaceholderParser;

public class SqlPlaceholderParser extends PlaceholderParser {

	@Override
	public String parser(String text) {
		return "?";
	}

}
