package com.imzy.bean.config;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;

import com.imzy.bean.DaoFactoryBean;

/**
 * dao的扫描器，将DaoFctoryBean交给spring管理
 * @author yangzhang7
 *
 */
public class ClassPathDaoScanner extends ClassPathBeanDefinitionScanner {

	private SessionFactory sessionFactory;

	public ClassPathDaoScanner(BeanDefinitionRegistry registry, SessionFactory sessionFactory) {
		super(registry, false);
		this.sessionFactory = sessionFactory;
	}

	@Override
	public Set<BeanDefinitionHolder> doScan(String... basePackages) {

		Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);

		if (beanDefinitions.isEmpty()) {
		} else {
			for (BeanDefinitionHolder holder : beanDefinitions) {
				GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
				definition.getPropertyValues().add("clazz", definition.getBeanClassName());
				definition.getPropertyValues().add("sessionFactory", this.sessionFactory);
				definition.setBeanClass(DaoFactoryBean.class);
				definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
			}
		}

		return beanDefinitions;
	}

	@Override
	protected boolean isCandidateComponent(AnnotatedBeanDefinition beanDefinition) {
		return (beanDefinition.getMetadata().isInterface() && beanDefinition.getMetadata().isIndependent());
	}

	@Override
	protected boolean checkCandidate(String beanName, BeanDefinition beanDefinition) throws IllegalStateException {
		if (super.checkCandidate(beanName, beanDefinition)) {
			return true;
		} else {
			return false;
		}
	}

}
