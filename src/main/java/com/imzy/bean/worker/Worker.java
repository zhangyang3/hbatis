package com.imzy.bean.worker;

import java.util.List;
import java.util.Map;

import com.imzy.exception.HBatisException;

/**
 * sql������
 * @author yangzhang7
 *
 * @param <T>
 */
public interface Worker<T> {

	/**
	 * ��ȡsql
	 * @param textNode ��ǰ�ڵ�
	 * @param inValueMap ����map
	 * @param args ����ֵ
	 * @return
	 * @throws HBatisException
	 */
	String sql(T textNode, Map<String, Object> inValueMap, List<Object> args) throws HBatisException;

}
