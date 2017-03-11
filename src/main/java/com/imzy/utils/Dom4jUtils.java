package com.imzy.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

/**
 * dom4j工具
 * @author yangzhang7
 *
 */
public class Dom4jUtils {

	private static final String TEXT_LEFT = "<text>";
	private static final String TEXT_RIGHT = "</text>";

	public static List<String> analyseElement(Element rootElement) {
		List<String> list = new ArrayList<String>();

		Iterator<?> nodeIterator = rootElement.nodeIterator();
		while (nodeIterator.hasNext()) {
			Object next = nodeIterator.next();
			if (next instanceof DefaultText) {
				DefaultText defaultText = (DefaultText) next;
				if (StringUtils.isNotBlank(defaultText.getText())) {
					list.add(TEXT_LEFT + defaultText.getText().trim() + TEXT_RIGHT);
				}
			} else if (next instanceof DefaultElement) {
				DefaultElement defaultElement = (DefaultElement) next;
				list.add(defaultElement.asXML().trim());

			}
		}

		return list;
	}

	/**
	 * 获取属性值
	 * @param element
	 * @param attributeName  属性名称
	 * @return
	 */
	public static String getAttribute(Element element, String attributeName) {
		String value = element.attributeValue(attributeName);
		return StringUtils.isNotBlank(value) ? value.trim() : null;
	}
}
