package com.imzy.context;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;

import com.imzy.bean.DaoProxyFactory;

public class DaoContext {

	/** dao与代理工厂的关系*/
	private static Map<Class<?>, DaoProxyFactory<?>> impProxyFactoryMap = new HashMap<Class<?>, DaoProxyFactory<?>>();

	public static <T> void addImpProxyFactory(Class<T> clazz, SessionFactory sessionFactory) {
		if (!impProxyFactoryMap.containsKey(clazz)) {
			impProxyFactoryMap.put(clazz, new DaoProxyFactory<T>(clazz, sessionFactory));
		} else {
			System.out.println(clazz + " exists");
		}
	}

	public static <T> T getDao(Class<T> clazz) {
		DaoProxyFactory<T> mapperProxyFactory = getImpProxyFactory(clazz);
		if (null != mapperProxyFactory) {
			T newInstance = mapperProxyFactory.newInstance();
			return newInstance;
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> DaoProxyFactory<T> getImpProxyFactory(Class<T> clazz) {
		return (DaoProxyFactory<T>) impProxyFactoryMap.get(clazz);
	}

	private DaoContext() {
	}

}
