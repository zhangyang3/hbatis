package com.imzy.xml.node.parser;

import java.util.List;

import org.dom4j.Element;

import com.imzy.context.ParserContext;
import com.imzy.utils.Dom4jUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.DmlNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SqlNode;

/**
 * dml解析器
 * @author yangzhang7
 *
 */
public class DmlParser<T extends DmlNode> implements Parsable<T> {
	private Class<T> clazz;

	public DmlParser(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T parse(String text, Node parent) {
		T specialDmlNode = null;
		try {
			specialDmlNode = clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		// 设置属性值
		specialDmlNode.setXml(text);
		Element rootElement = XmlUtils.getRootElement(text);
		specialDmlNode.setId(Dom4jUtils.getAttribute(rootElement, "id"));
		specialDmlNode.setSelfId(specialDmlNode.getId());
		specialDmlNode.setInType(Dom4jUtils.getAttribute(rootElement, "intype"));
		specialDmlNode.setLangType(Dom4jUtils.getAttribute(rootElement, "lang"));
		// 将节点放入树中
		specialDmlNode.setPT(parent);

		String langType = getLangType(specialDmlNode, (SqlNode) parent);

		List<String> analyseElements = Dom4jUtils.analyseElement(rootElement);
		for (String analyseElement : analyseElements) {
			Element re = XmlUtils.getRootElement(analyseElement);
			Parsable<?> parser = ParserContext.getParserByQNameAndLang(re.getQName().getName(), langType);
			parser.parse(analyseElement, specialDmlNode);
		}

		return specialDmlNode;
	}

	/**
	 * 获取langtype
	 * @param specialDmlNode
	 * @param parent
	 * @return
	 */
	private String getLangType(T specialDmlNode, SqlNode parent) {
		if (null == specialDmlNode.getLangType()) {
			String langType = parent.getLangType().getName();
			specialDmlNode.setLangType(langType);
		}

		return specialDmlNode.getLangType().getName();
	}
}
