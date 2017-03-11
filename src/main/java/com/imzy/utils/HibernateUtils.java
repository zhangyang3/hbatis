package com.imzy.utils;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.util.Assert;

import com.imzy.bean.Page;
import com.imzy.bean.SqlBindle;
import com.imzy.utils.RegexUtils.Method;

public class HibernateUtils {

	/**
	 * 创建sqlquery
	 * @param session
	 * @param queryString  sql内容
	 * @param values  sql参数
	 * @return
	 */
	public static SQLQuery createSqlQuery(Session session, String queryString, List<Object> values) {
		Assert.hasText(queryString, "queryString不能为空");
		SQLQuery query = session.createSQLQuery(queryString);
		if (values != null) {
			for (int i = 0; i < values.size(); ++i) {
				query.setParameter(i, values.get(i));
			}
		}

		return query;
	}

	public static Query createQuery(Session session, String queryString, List<Object> values) {
		Assert.hasText(queryString, "queryString不能为空");

		// 得到所有的占位符名称
		List<String> names = RegexUtils.getGroups(":\\w+\\s*", queryString, new Method() {
			public String method(String str) {
				return str.trim().substring(1);
			}
		});

		Query query = session.createQuery(queryString);

		if (values != null) {
			for (int i = 0; i < values.size(); ++i) {
				String name = names.get(i);
				Object value = values.get(i);
				if (ClassUtils.judgeListOrArray(value.getClass())) {
					if (value instanceof Collection<?>) {
						query.setParameterList(name, (Collection<?>) values.get(i));
					} else if (value instanceof Object[]) {
						query.setParameterList(name, (Object[]) values.get(i));
					}
				} else {
					query.setParameter(name, values.get(i));
				}
			}
		}

		return query;
	}

	/**
	 * 设置分页
	 * @param sqlBindle 
	 * @param currentSession 
	 * @param object
	 * @param returnGenericType 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setSqlQueryPage(Session currentSession, SqlBindle sqlBindle, Page page,
			Class<?> returnGenericType) {

		// 获取分页数据
		SQLQuery pageQuery = HibernateUtils.createSqlQuery(currentSession, sqlBindle.getSql(), sqlBindle.getArgs());
		List list = pageQuery.setFirstResult((page.getCurrentPageNo() - 1) * page.getPageSize())
				.setMaxResults(page.getPageSize())
				.setResultTransformer(new AliasToBeanResultTransformer(returnGenericType)).list();
		page.setResult(list);

		// 求总数
		if (page.isAutoCount()) {
			String countHql = "select 1 from " + StringUtils.substringAfter(sqlBindle.getSql(), "from");
			countHql = StringUtils.substringBefore(countHql, "order by");
			countHql = "select count(1) from(" + countHql + ") a";
			SQLQuery countQuery = HibernateUtils.createSqlQuery(currentSession, countHql, sqlBindle.getArgs());
			BigInteger count = (BigInteger) countQuery.uniqueResult();
			page.setTotalCount(Long.valueOf(count.toString()));
		}

	}

	/**
	 * 设置分页
	 * @param sqlBindle 
	 * @param currentSession 
	 * @param object
	 * @param returnGenericType 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setQueryPage(Session currentSession, SqlBindle sqlBindle, Page page,
			Class<?> returnGenericType) {

		// 获取分页数据
		Query pageQuery = HibernateUtils.createQuery(currentSession, sqlBindle.getSql(), sqlBindle.getArgs());
		List list = pageQuery.setFirstResult((page.getCurrentPageNo() - 1) * page.getPageSize())
				.setMaxResults(page.getPageSize()).list();
		page.setResult(list);

		// 求总数
		if (page.isAutoCount()) {
			String countHql = "select count(1) from " + StringUtils.substringAfter(sqlBindle.getSql(), "from");
			countHql = StringUtils.substringBefore(countHql, "order by");
			Query countQuery = HibernateUtils.createQuery(currentSession, countHql, sqlBindle.getArgs());
			Long count = (Long) countQuery.uniqueResult();
			page.setTotalCount(count);
		}

	}
}
