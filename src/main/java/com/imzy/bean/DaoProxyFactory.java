package com.imzy.bean;

import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;

/**
 * imp的代理工厂
 * @author yangzhang7
 *
 */
public class DaoProxyFactory<T> {

	private Class<T> target;

	private SessionFactory sessionFactory;

	public DaoProxyFactory(Class<T> target, SessionFactory sessionFactory) {
		this.target = target;
		this.sessionFactory = sessionFactory;
	}

	@SuppressWarnings("unchecked")
	public T newInstance() {
		DaoProxy<T> daoProxy = new DaoProxy<T>(target, sessionFactory);
		// 创建并返回一个动态代理
		return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[] { target }, daoProxy);
	}
}
