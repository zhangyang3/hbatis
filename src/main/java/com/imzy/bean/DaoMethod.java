package com.imzy.bean;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.AliasToBeanResultTransformer;

import com.imzy.annotation.Param;
import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.utils.ClassUtils;
import com.imzy.utils.HibernateUtils;
import com.imzy.xml.node.DmlNode;
import com.imzy.xml.node.LangType;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SelectNode;
import com.imzy.xml.structure.TreeNode;

/**
 * dao�ķ�������
 * @author yangzhang7
 *
 */
public class DaoMethod {

	/** ���صķ���*/
	private Method method;

	/** �������������ֵ��ϵӳ��*/
	private Map<String, Object> inValueMap;

	private SessionFactory sessionFactory;

	public DaoMethod(Method method, Object[] args, SessionFactory sessionFactory) throws HBatisException {
		this.method = method;
		this.inValueMap = buildInValueMap(this.method, args);
		this.sessionFactory = sessionFactory;
	}

	/**
	 * ����ķ���
	 * @return
	 * @throws HBatisException
	 */
	public Object execute() throws HBatisException {
		Class<?> returnType = method.getReturnType();
		Class<?> returnGenericType = ClassUtils.getGenericReturnClass(method);
		String methodName = method.getName();
		String className = method.getDeclaringClass().getName();

		TreeNode<Node> methodTreeNode = TreeContext.getTreeNode(className).findTreeNodeById(methodName);
		SqlBindle sqlBindle = SqlBindle.getSqlBindle(className, methodName, this.inValueMap);
		Session currentSession = getSession();

		DmlNode dmlNode = (DmlNode) methodTreeNode.getData();
		if (dmlNode instanceof SelectNode) {
			if (Void.class.equals(returnType)) {
				throw new HBatisException("select��䲻�ܷ���void");
			}
			List<?> reuslt = null;
			Page<?> page = (Page<?>) this.inValueMap.get("_page");
			if (LangType.SQL.equals(sqlBindle.getLangType())) {
				if (null != page) {
					HibernateUtils.setSqlQueryPage(currentSession, sqlBindle, page, returnGenericType);
					reuslt = page.getResult();
				} else {
					SQLQuery sqlQuery = HibernateUtils.createSqlQuery(currentSession, sqlBindle.getSql(),
							sqlBindle.getArgs());
					reuslt = sqlQuery.setResultTransformer(new AliasToBeanResultTransformer(returnGenericType)).list();
				}
			} else if (LangType.HQL.equals(sqlBindle.getLangType())) {
				if (null != page) {
					HibernateUtils.setQueryPage(currentSession, sqlBindle, page, returnGenericType);
					reuslt = page.getResult();
				} else {
					Query query = HibernateUtils.createQuery(currentSession, sqlBindle.getSql(), sqlBindle.getArgs());
					reuslt = query.list();
				}
			}
			if (ClassUtils.judgeListOrArray(returnType)) {
				return reuslt;
			} else {
				return reuslt == null ? null : reuslt.get(0);
			}
		} else {
			SQLQuery sqlQuery = HibernateUtils.createSqlQuery(currentSession, sqlBindle.getSql(), sqlBindle.getArgs());
			sqlQuery.executeUpdate();
		}

		return null;
	}

	/**
	 * ��ȡsession
	 * @return
	 */
	public Session getSession() {
		try {
			return this.sessionFactory.getCurrentSession();
		} catch (Exception e) {
		}
		return this.sessionFactory.openSession();
	}

	/**
	 * �����������������ֵ��ϵ
	 * @param method  ����
	 * @param inValue  �������ֵ
	 * @return  �������������ֵ��ϵӳ��
	 * @throws HBatisException
	 */
	private Map<String, Object> buildInValueMap(Method method, Object[] inValue) throws HBatisException {
		// ����ע���б�
		List<String> annotationValues = getAnnotationValues(method);
		List<String> paramNames = new ArrayList<String>();
		Map<String, Object> inValueMap = new HashMap<String, Object>();

		// ������������
		Class<?>[] parameterTypes = method.getParameterTypes();

		if (parameterTypes.length == 1) {
			Class<?> parameterType = parameterTypes[0];
			if (annotationValues.size() == 0) {
				if (parameterType.equals(String.class)) {
					paramNames.add("_str");
				} else if (parameterType.equals(int.class) || parameterType.equals(Integer.class)
						|| parameterType.equals(long.class) || parameterType.equals(Long.class)
						|| parameterType.equals(double.class) || parameterType.equals(Double.class)) {
					paramNames.add("_num");

				} else if (ClassUtils.judgeListOrArray(parameterType)) {
					paramNames.add("_list");
				} else if (parameterType.equals(Page.class)) {
					paramNames.add("_page");
				} else {
					paramNames.add("_obj");
				}
			} else {
				paramNames.addAll(annotationValues);
			}
		} else if (parameterTypes.length > 1) {
			if (parameterTypes.length != annotationValues.size()) {
				throw new HBatisException(method.getName() + "�����д��ڲ���δ���Paramע��");
			} else {
				paramNames.addAll(annotationValues);
			}
		}

		for (int i = 0; i < paramNames.size(); i++) {
			String paramName = paramNames.get(i);
			inValueMap.put(paramName, inValue[i]);
		}
		return inValueMap;
	}

	/**
	 * ��ȡ����������ע��ֵ
	 * @param method
	 * @return
	 */
	private List<String> getAnnotationValues(Method method) {
		List<String> values = new ArrayList<String>();

		Annotation[][] parameterAnnotations = method.getParameterAnnotations();
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length; i++) {
			if (parameterTypes[i].equals(Page.class)) {
				values.add("_page");
			} else {
				Annotation[] annotations = parameterAnnotations[i];

				boolean paramExists = false;
				for (int k = 0; k < annotations.length; k++) {
					Annotation annotation = annotations[k];
					if (annotation instanceof Param) {
						values.add(((Param) annotation).value());
						paramExists = true;
						break;
					}
				}
				if (!paramExists && parameterTypes.length > 1) {
					throw new HBatisException("��" + (i + 1) + "������ȱ��Paramע��");
				}
			}
		}

		return values;
	}

}
