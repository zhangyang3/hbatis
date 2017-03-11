package com.imzy.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.hibernate.SessionFactory;

public class DaoProxy<T> implements InvocationHandler {

	private Class<T> target;

	private SessionFactory sessionFactory;

	public DaoProxy(Class<T> target, SessionFactory sessionFactory) {
		this.target = target;
		this.sessionFactory = sessionFactory;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (Object.class.equals(method.getDeclaringClass())) {
			return method.invoke(target, args);
		} else {
			DaoMethod daoMethod = new DaoMethod(method, args, sessionFactory);
			return daoMethod.execute();
		}
	}

}
