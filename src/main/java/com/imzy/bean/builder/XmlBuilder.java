package com.imzy.bean.builder;

import org.apache.commons.lang.StringUtils;

import com.imzy.context.ParserContext;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.parser.SqlParser;

public class XmlBuilder implements Build {

	public void build(String className) {
		SqlParser sqlPaser = (SqlParser) ParserContext.getCommonParserByQName("sql");
		String readXml = XmlUtils.readXml(className.replaceAll("\\.", "/") + ".xml");
		if (StringUtils.isNotBlank(readXml)) {
			sqlPaser.parse(readXml);
		} else {
			System.out.println(className.replaceAll("\\.", "/") + ".xml文件不存在");
		}
	}

}
