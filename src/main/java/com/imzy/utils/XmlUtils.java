package com.imzy.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XmlUtils {
	private static SAXReader reader = new SAXReader();

	/**
	 * ��textתΪdom4j��ʽ
	 * @param text
	 * @return
	 */
	public static Element getRootElement(String text) {
		Element sqlElement = null;

		try {
			Document doc = reader.read(new ByteArrayInputStream(text.getBytes()));
			sqlElement = doc.getRootElement();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return sqlElement;
	}

	/**
	 * ��ȡxml�ļ�
	 * @param filePath
	 * @return
	 */
	public static String readXml(String filePath) {
		String text = StringUtils.EMPTY;
		try {
			text = FileUtils.readFileToString(new File(PathUtils.getPath(filePath)));
			text = text.replaceAll("\\s+", " ");
		} catch (IOException e) {
		}
		return text;
	}

	/**
	 * ����ȫ·����ȡxml
	 * @param absolutePath
	 * @return
	 */
	public static String readXmlByAbsolutePath(String absolutePath) {
		String text = StringUtils.EMPTY;
		try {
			text = FileUtils.readFileToString(new File(absolutePath));
			text = text.replaceAll("\\s+", " ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;
	}
}
