package com.imzy.bean.worker;

import java.util.List;
import java.util.Map;

import com.imzy.exception.HBatisException;

/**
 * sql工作器
 * @author yangzhang7
 *
 * @param <T>
 */
public interface Worker<T> {

	/**
	 * 获取sql
	 * @param textNode 当前节点
	 * @param inValueMap 参数map
	 * @param args 参数值
	 * @return
	 * @throws HBatisException
	 */
	String sql(T textNode, Map<String, Object> inValueMap, List<Object> args) throws HBatisException;

}
