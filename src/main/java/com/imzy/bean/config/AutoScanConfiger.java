package com.imzy.bean.config;

import java.lang.annotation.Annotation;
import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;

import com.imzy.annotation.Dao;
import com.imzy.bean.builder.AnnotationBuilder;
import com.imzy.bean.builder.XmlBuilder;
import com.imzy.config.Version;
import com.imzy.context.DaoContext;
import com.imzy.context.ParserContext;
import com.imzy.context.TreeContext;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SqlNode;
import com.imzy.xml.structure.TreeNode;

public class AutoScanConfiger implements BeanDefinitionRegistryPostProcessor {
	/** 数据源*/
	private SessionFactory sessionFactory;

	/** 包扫描路径*/
	private String[] basePackages;

	/** Dao接口上的注解类*/
	private Class<? extends Annotation> annotationClass = Dao.class;

	public void setAnnotationClass(Class<? extends Annotation> annotationClass) {
		this.annotationClass = annotationClass;
	}

	public AutoScanConfiger() {
		System.out.println("hbatis 版本：" + Version.getVersion());
		// 初始化ParserContext
		ParserContext.getParserContext().init();
	}

	/**
	 * 是否含有注解
	 * @param className
	 * @return
	 */
	private boolean hasAnnotation(String className) {
		try {
			Class<?> forName = Class.forName(className);
			Annotation annotation = forName.getAnnotation(annotationClass);
			return annotation != null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 扫描包
	 * @param packagesToScan
	 */
	private void scanPackages(String[] basePackages) {
		XmlBuilder xmlBuilder = new XmlBuilder();
		AnnotationBuilder annotationBuilder = new AnnotationBuilder();

		for (String basePackage : basePackages) {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";

			try {
				// 获取路径下的所有class文件
				Resource[] resources = resourcePatternResolver.getResources(pattern);
				MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resourcePatternResolver);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						MetadataReader reader = readerFactory.getMetadataReader(resource);
						String className = reader.getClassMetadata().getClassName();
						if (hasAnnotation(className)) {
							// 先解析xml
							xmlBuilder.build(className);
							// 再解析annotation（顺序不可乱）
							annotationBuilder.build(className);
						}
					}
				}
			} catch (Exception e) {
			}
		}

	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
		// 扫描路径设置好，立刻扫描
		scanPackages(this.basePackages);
	}

	/**
	 * 初始化，构建dao的代理工厂
	 * xml中可以不配置这个方法
	 */
	public void init() {
		List<TreeNode<Node>> allTree = TreeContext.getAllTree();
		for (TreeNode<Node> tree : allTree) {
			SqlNode sqlNode = (SqlNode) tree.getData();
			String namespace = sqlNode.getNamespace();
			try {
				Class<?> clazz = Class.forName(namespace);
				DaoContext.addImpProxyFactory(clazz, sessionFactory);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		ClassPathDaoScanner scanner = new ClassPathDaoScanner(registry, this.sessionFactory);
		scanner.addIncludeFilter(new AnnotationTypeFilter(annotationClass));
		scanner.scan(basePackages);
	}

}
