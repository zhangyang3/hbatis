package com.imzy.bean.builder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.imzy.annotation.dml.Delete;
import com.imzy.annotation.dml.Insert;
import com.imzy.annotation.dml.Select;
import com.imzy.annotation.dml.Update;
import com.imzy.context.ParserContext;
import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.xml.node.LangType;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.parser.Parsable;
import com.imzy.xml.node.parser.SqlParser;
import com.imzy.xml.structure.TreeNode;

public class AnnotationBuilder implements Build {

	private static final String SELECT = "select";
	private static final String UPDATE = "update";
	private static final String INSERT = "insert";
	private static final String DELETE = "delete";

	private String aroundSqlQname(String text, String className) {
		StringBuilder sb = new StringBuilder();
		sb.append("<sql namespace=\"").append(className).append("\">");
		sb.append(text);
		sb.append("</sql>");
		return sb.toString();
	}

	private String aroundDmlQname(String text, String methodName, String qname, LangType langType) {
		StringBuilder sb = new StringBuilder();
		sb.append("<").append(qname).append(" id=\"").append(methodName).append("\" lang=\"").append(langType.getName())
				.append("\">");
		sb.append(text);
		sb.append("</").append(qname).append(">");
		return sb.toString();
	}

	public void build(String className) {
		try {
			Class<?> clazz = Class.forName(className);

			Method[] methods = clazz.getDeclaredMethods();
			for (Method method : methods) {
				Annotation[] annotations = method.getAnnotations();
				String methodName = method.getName();
				for (Annotation annotation : annotations) {
					if (annotation instanceof Select) {
						parseAnnotation(className, methodName, annotation, SELECT);
					} else if (annotation instanceof Update) {
						parseAnnotation(className, methodName, annotation, UPDATE);
					} else if (annotation instanceof Insert) {
						parseAnnotation(className, methodName, annotation, INSERT);
					} else if (annotation instanceof Delete) {
						parseAnnotation(className, methodName, annotation, DELETE);
					}
				}
			}

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析annotation中的sql
	 * @param className 全类名或treeid
	 * @param methodName 方法名
	 * @param annotation
	 * @param qname dml类型
	 */
	private void parseAnnotation(String className, String methodName, Annotation annotation, String qname) {
		String value = null;
		LangType langType = null;
		if (SELECT.equals(qname)) {
			value = ((Select) annotation).value();
			langType = ((Select) annotation).type();
		} else if (UPDATE.equals(qname)) {
			value = ((Update) annotation).value();
			langType = ((Update) annotation).type();
		} else if (INSERT.equals(qname)) {
			value = ((Insert) annotation).value();
			langType = ((Insert) annotation).type();
		} else if (DELETE.equals(qname)) {
			value = ((Delete) annotation).value();
			langType = ((Delete) annotation).type();
		}
		if (value == null) {
			throw new HBatisException(className + "." + methodName + "的" + qname + "注解值不能为空");
		}

		TreeNode<Node> treeNode = TreeContext.getTreeNode(className);
		if (treeNode != null) {
			TreeNode<Node> methodTreeNode = treeNode.findTreeNodeById(methodName);
			if (null != methodTreeNode) {
				System.out.println(className + "." + methodName + "已在xml中定义，annotiaon中定义的sql不会覆盖");
			} else {
				Parsable<?> parser = ParserContext.getCommonParserByQName(qname);
				parser.parse(aroundDmlQname(value, methodName, qname, langType), treeNode.getData());
			}
		} else {
			String aroundSqlQname = aroundSqlQname(aroundDmlQname(value, methodName, qname, langType), className);
			SqlParser sqlPaser = (SqlParser) ParserContext.getCommonParserByQName("sql");
			sqlPaser.parse(aroundSqlQname);
		}
	}

}
