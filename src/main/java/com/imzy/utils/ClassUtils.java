package com.imzy.utils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ClassUtils {

	/**
	 * 获取方法的返回值泛型
	 * @param method
	 * @return
	 */
	public static Class<?> getGenericReturnClass(Method method) {
		Type type = method.getGenericReturnType();
		if (type instanceof ParameterizedType) {
			return (Class<?>) ((ParameterizedType) type).getActualTypeArguments()[0];// 强制转型为带参数的泛型类型，
		} else {
			return (Class<?>) type;
		}

	}

	/**
	 * 判断是否为list或者array
	 * @param type
	 * @return
	 */
	public static boolean judgeListOrArray(Class<?> type) {
		if (type.isArray() || List.class.isAssignableFrom(type) || Set.class.isAssignableFrom(type)
				|| Collection.class.isAssignableFrom(type)) {
			return true;
		} else {
			return false;
		}
	}
}
