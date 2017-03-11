package com.imzy.bean.worker;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.imzy.bean.SqlBindle;
import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.xml.node.IfNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.structure.TreeNode;

import ognl.Ognl;
import ognl.OgnlException;

public class IfWorker implements Worker<IfNode> {

	public String sql(IfNode ifNode, Map<String, Object> inValueMap, List<Object> args) throws HBatisException {
		String test = ifNode.getTest();

		try {
			Object expression = Ognl.parseExpression(test);
			Boolean value = (Boolean) Ognl.getValue(expression, inValueMap);
			if (Boolean.TRUE.equals(value)) {
				SqlBindle ifSqlBindle = getIfSqlBindle(ifNode, inValueMap);
				args.addAll(ifSqlBindle.getArgs());
				return ifSqlBindle.getSql();
			}
		} catch (OgnlException e) {
			e.printStackTrace();
		}

		return StringUtils.EMPTY;
	}

	public SqlBindle getIfSqlBindle(IfNode ifNode, Map<String, Object> inValueMap) throws HBatisException {

		TreeNode<Node> treeNode = TreeContext.getTreeNode(ifNode.getTreeId());
		TreeNode<Node> ifTreeNode = treeNode.findTreeNodeById(ifNode.getSelfId());

		List<TreeNode<TextNode>> textTreeNode = ifTreeNode.getChildListByType(TextNode.class);
		if (textTreeNode.get(0) == null) {
			throw new HBatisException("if¿ÔŒ¥ÃÓ÷µ");
		}
		TextNode textNode = textTreeNode.get(0).getData();
		return SqlBindle.text(textNode, inValueMap);
	}
}
