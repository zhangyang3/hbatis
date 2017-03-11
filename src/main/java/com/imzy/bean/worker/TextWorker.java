package com.imzy.bean.worker;

import java.util.List;
import java.util.Map;

import com.imzy.exception.HBatisException;
import com.imzy.xml.node.Placeholder;
import com.imzy.xml.node.TextNode;

import ognl.Ognl;
import ognl.OgnlException;

public class TextWorker implements Worker<TextNode> {

	public String sql(TextNode textNode, Map<String, Object> inValueMap, List<Object> args) throws HBatisException {
		String sql = textNode.getInnerXml();
		List<Placeholder> placeholders = textNode.getPlaceholders();
		for (Placeholder placeholder : placeholders) {
			sql = sql.replace(placeholder.getBefore(), placeholder.getAfter());
			addArg(args, inValueMap, placeholder);
		}
		return sql;
	}

	private void addArg(List<Object> args, Map<String, Object> inValueMap, Placeholder placeholder) {
		Object value = null;
		try {
			value = Ognl.getValue(placeholder.getAttributeName(), inValueMap);
		} catch (OgnlException e) {
			e.printStackTrace();
		}
		args.add(value);
	}

}
