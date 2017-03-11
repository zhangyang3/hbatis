package com.imzy.help.sqlmonitor;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.imzy.bean.builder.AnnotationBuilder;
import com.imzy.context.ParserContext;
import com.imzy.utils.PathUtils;
import com.imzy.utils.XmlUtils;
import com.imzy.xml.node.parser.SqlParser;

public class ScannerThread implements Runnable {

	/** xml�ļ���¼ʱ������ļ�·����ʱ�����*/
	private Map<String, Long> xmlStamp;

	public ScannerThread(Map<String, Long> xmlStamp) {
		this.xmlStamp = xmlStamp;
	}

	public void run() {
		SqlParser sqlPaser = (SqlParser) ParserContext.getCommonParserByQName("sql");

		while (true) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (Entry<String, Long> entry : xmlStamp.entrySet()) {
				File f = new File(entry.getKey());
				if (f.lastModified() != entry.getValue()) {
					xmlStamp.put(entry.getKey(), f.lastModified());
					System.out.println(entry.getKey() + "�����仯");
					// ���¼���xml��sql
					String readXml = XmlUtils.readXmlByAbsolutePath(entry.getKey());
					sqlPaser.parse(readXml);
					// ���¼���annotation��sql
					reloadAnnotationSql(entry);
				}
			}
		}
	}

	/**
	 * ���¼���annotation��sql
	 * @param entry
	 */
	private void reloadAnnotationSql(Entry<String, Long> entry) {
		AnnotationBuilder annotationBuilder = new AnnotationBuilder();

		String realPath = PathUtils.getRealPath().replaceAll("\\\\", "/");
		String key = entry.getKey().replaceAll("\\\\", "/");
		if (!key.startsWith("/")) {
			key = "/" + key;
		}

		String className = StringUtils.substringAfter(key, realPath).replaceAll("/", ".");
		className = StringUtils.substringBefore(className, ".xml");

		annotationBuilder.build(className);
	}
}
