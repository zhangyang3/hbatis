package com.imzy.bean;

import java.lang.reflect.Proxy;

import org.hibernate.SessionFactory;

/**
 * imp�Ĵ�����
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
		// ����������һ����̬����
		return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class[] { target }, daoProxy);
	}
}
