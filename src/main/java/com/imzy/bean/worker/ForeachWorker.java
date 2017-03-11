package com.imzy.bean.worker;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.xml.node.DmlNode;
import com.imzy.xml.node.ForeachNode;
import com.imzy.xml.node.LangType;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.Placeholder;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.structure.TreeNode;

import ognl.Ognl;
import ognl.OgnlException;

public class ForeachWorker implements Worker<ForeachNode> {

	public String sql(ForeachNode foreachNode, Map<String, Object> inValueMap, List<Object> args)
			throws HBatisException {
		String collection = foreachNode.getCollection();

		Object value = null;
		try {
			value = Ognl.getValue(collection, inValueMap);
			if (value == null) {
				throw new HBatisException(collection + "没有对应的值");
			}
		} catch (OgnlException e) {
			e.printStackTrace();
		}

		String itemSql = getItemSql(foreachNode, args, value);

		StringBuilder sb = new StringBuilder();
		sb.append(foreachNode.getPrefix());
		sb.append(itemSql);
		sb.append(foreachNode.getSuffix());

		return sb.toString();
	}

	/**
	 * 获取item的sql
	 * @param foreachNode
	 * @param args 
	 * @param value 
	 * @return
	 */
	public String getItemSql(ForeachNode foreachNode, List<Object> args, Object value) {
		String sperator = foreachNode.getSperator();

		TreeNode<Node> treeNode = TreeContext.getTreeNode(foreachNode.getTreeId());
		TreeNode<Node> foreachTreeNode = treeNode.findTreeNodeById(foreachNode.getSelfId());
		List<TreeNode<Node>> childList = foreachTreeNode.getChildList();
		TextNode textNode = (TextNode) childList.get(0).getData();

		// 获取dml节点的类型
		DmlNode parentNodeData = foreachTreeNode.getParentNodeDataByType(DmlNode.class);

		StringBuilder sb = new StringBuilder();
		String sql = textNode.getInnerXml();
		List<Placeholder> placeholders = textNode.getPlaceholders();
		for (Placeholder placeholder : placeholders) {
			if (LangType.HQL.equals(parentNodeData.getLangType())) {
				sql = sql.replace(placeholder.getBefore(),
						placeholder.getAfter() + "_" + foreachNode.getCollection().replaceAll("\\.", "")
								+ UUID.randomUUID().toString().substring(0, 4));
			} else {
				sql = sql.replace(placeholder.getBefore(), placeholder.getAfter());
			}
		}
		if (parentNodeData.getLangType().equals(LangType.SQL)) {
			if (value.getClass().isArray()) {
				Object[] l = (Object[]) value;
				for (Object o : l) {
					args.add(o);
				}
			} else {
				Collection<?> l = (Collection<?>) value;
				for (Object o : l) {
					args.add(o);
				}
			}
			for (int i = 0; i < args.size(); i++) {
				if (0 == i) {
					sb.append(sql);
				} else {
					sb.append(sperator).append(sql);

				}
			}

		} else if (parentNodeData.getLangType().equals(LangType.HQL)) {
			args.add(value);
			sb.append(sql);
		}

		return sb.toString();
	}

}
