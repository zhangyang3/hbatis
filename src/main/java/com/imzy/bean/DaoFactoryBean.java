package com.imzy.bean;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.FactoryBean;

public class DaoFactoryBean<T> implements FactoryBean<T> {

	private Class<T> clazz;
	private SessionFactory sessionFactory;

	public Class<T> getClazz() {
		return clazz;
	}

	public void setClazz(Class<T> clazz) {
		this.clazz = clazz;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public T getObject() throws Exception {
		return new DaoProxyFactory<T>(clazz, sessionFactory).newInstance();
	}

	public Class<?> getObjectType() {
		return this.clazz;
	}

	public boolean isSingleton() {
		return true;
	}

}
