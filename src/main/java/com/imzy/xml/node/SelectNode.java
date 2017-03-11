package com.imzy.xml.node;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * select½Úµã
 * @author yangzhang7
 *
 */
public class SelectNode extends DmlNode {

	public SelectNode(DmlNode dmlNode) {
		try {
			BeanUtils.copyProperties(this, dmlNode);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	public SelectNode() {

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
