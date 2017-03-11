package com.imzy.help.sqlmonitor;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import com.imzy.exception.HBatisException;

/**
 * SQL�ļ�������
 * @author yangzhang7
 *
 */
public class SqllMonitor {

	/** ��ɨ��·��*/
	private String[] basePackages;

	public void setBasePackages(String[] basePackages) {
		this.basePackages = basePackages;
	}

	/** xml�ļ���¼ʱ������ļ�·����ʱ�����*/
	private Map<String, Long> xmlStamp = new HashMap<String, Long>();

	public void init() {
		// ��xml��������
		monitorSql();
		// �����߳�ʵʱ����Sql
		ScannerThread monitor = new ScannerThread(xmlStamp);
		Thread thread = new Thread(monitor);
		thread.start();
	}

	private void monitorSql() {
		for (String basePackage : basePackages) {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
					+ ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.xml";

			try {
				// ��ȡ·���µ�����class�ļ�
				Resource[] resources = resourcePatternResolver.getResources(pattern);
				for (Resource resource : resources) {
					File file = resource.getFile();
					String absolutePath = file.getAbsolutePath();
					long stamp = file.lastModified();
					xmlStamp.put(absolutePath, stamp);
				}
			} catch (Exception e) {
				throw new HBatisException(e);
			}

		}
	}

}
