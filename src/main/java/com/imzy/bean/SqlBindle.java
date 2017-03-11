package com.imzy.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.imzy.bean.worker.ForeachWorker;
import com.imzy.bean.worker.IfWorker;
import com.imzy.bean.worker.IncludeWorker;
import com.imzy.bean.worker.TextWorker;
import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.xml.node.DmlNode;
import com.imzy.xml.node.ForeachNode;
import com.imzy.xml.node.IfNode;
import com.imzy.xml.node.IncludeNode;
import com.imzy.xml.node.LangType;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.structure.TreeNode;

public class SqlBindle {

	/**
	 * 获取sqlBindle
	 * @param className 类名
	 * @param methodName 方法名
	 * @param inValueMap  入参map
	 * @return
	 * @throws HBatisException 
	 */
	public static SqlBindle getSqlBindle(String className, String methodName, Map<String, Object> inValueMap)
			throws HBatisException {
		SqlBindle sqlBindle = new SqlBindle();
		TreeNode<Node> methodTreeNode = TreeContext.getTreeNode(className).findTreeNodeById(methodName);
		DmlNode dmlNode = (DmlNode) methodTreeNode.getData();
		sqlBindle.setLangType(dmlNode.getLangType());
		List<TreeNode<Node>> childList = methodTreeNode.getChildList();

		for (TreeNode<Node> node : childList) {
			Node data = node.getData();
			if (data instanceof TextNode) {
				TextNode textNode = (TextNode) data;
				sqlBindle.merge(text(textNode, inValueMap));
			} else if (data instanceof ForeachNode) {
				ForeachNode foreachNode = (ForeachNode) data;
				sqlBindle.merge(foreach(foreachNode, inValueMap));
			} else if (data instanceof IfNode) {
				IfNode foreachNode = (IfNode) data;
				sqlBindle.merge(if_(foreachNode, inValueMap));
			} else if (data instanceof IncludeNode) {
				IncludeNode includeNode = (IncludeNode) data;
				sqlBindle.merge(include(includeNode, inValueMap));
			}
		}

		return sqlBindle;
	}

	public static SqlBindle text(TextNode textNode, Map<String, Object> inValueMap) throws HBatisException {
		SqlBindle sqlBindle = new SqlBindle();
		TextWorker textWorker = new TextWorker();
		sqlBindle.setSql(textWorker.sql(textNode, inValueMap, sqlBindle.getArgs()));
		return sqlBindle;
	}

	public static SqlBindle foreach(ForeachNode foreachNode, Map<String, Object> inValueMap) throws HBatisException {
		SqlBindle sqlBindle = new SqlBindle();
		ForeachWorker foreachWorker = new ForeachWorker();
		sqlBindle.setSql(foreachWorker.sql(foreachNode, inValueMap, sqlBindle.getArgs()));
		return sqlBindle;
	}

	public static SqlBindle if_(IfNode ifNode, Map<String, Object> inValueMap) throws HBatisException {
		SqlBindle sqlBindle = new SqlBindle();
		IfWorker ifWorker = new IfWorker();
		sqlBindle.setSql(ifWorker.sql(ifNode, inValueMap, sqlBindle.getArgs()));
		return sqlBindle;
	}

	public static SqlBindle include(IncludeNode includeNode, Map<String, Object> inValueMap) throws HBatisException {
		SqlBindle sqlBindle = new SqlBindle();
		IncludeWorker includeWorker = new IncludeWorker();
		sqlBindle.setSql(includeWorker.sql(includeNode, inValueMap, sqlBindle.getArgs()));
		return sqlBindle;
	}

	/** 参数*/
	private List<Object> args = new ArrayList<Object>();
	/** sql语句*/
	private String sql = StringUtils.EMPTY;

	private LangType langType;

	public LangType getLangType() {
		return langType;
	}

	public void setLangType(LangType langType) {
		this.langType = langType;
	}

	public List<Object> getArgs() {
		return args;
	}

	public String getSql() {
		return sql.trim();
	}

	public void setArgs(List<Object> args) {
		this.args = args;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	/**
	 * sqlbindle合并
	 * @param sqlBindle
	 */
	public void merge(SqlBindle sqlBindle) {
		this.sql = this.sql + " " + sqlBindle.getSql();
		this.args.addAll(sqlBindle.getArgs());
	}

}
