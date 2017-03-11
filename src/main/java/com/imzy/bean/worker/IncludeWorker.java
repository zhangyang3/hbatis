package com.imzy.bean.worker;

import java.util.List;
import java.util.Map;

import com.imzy.bean.SqlBindle;
import com.imzy.context.TreeContext;
import com.imzy.exception.HBatisException;
import com.imzy.xml.node.IncludeNode;
import com.imzy.xml.node.Node;
import com.imzy.xml.node.SegmentNode;
import com.imzy.xml.node.TextNode;
import com.imzy.xml.structure.TreeNode;

public class IncludeWorker implements Worker<IncludeNode> {

	public String sql(IncludeNode includeNode, Map<String, Object> inValueMap, List<Object> args)
			throws HBatisException {
		SqlBindle includeSqlBindle = getIncludeSqlBindle(includeNode, inValueMap);

		args.addAll(includeSqlBindle.getArgs());

		return includeSqlBindle.getSql();
	}

	/**
	 * 获取item的sql
	 * @param foreachNode
	 * @return
	 * @throws HBatisException 
	 */
	public SqlBindle getIncludeSqlBindle(IncludeNode includeNode, Map<String, Object> inValueMap)
			throws HBatisException {
		// refid
		String refId = includeNode.getRefid();

		TreeNode<Node> treeNode = TreeContext.getTreeNode(includeNode.getTreeId());
		List<TreeNode<SegmentNode>> segmentTreeNodes = treeNode.getChildListByType(SegmentNode.class);
		for (TreeNode<SegmentNode> segmentTreeNode : segmentTreeNodes) {
			SegmentNode segmentNode = segmentTreeNode.getData();
			// id
			String id = segmentNode.getId();
			if (refId.equals(id)) {
				List<TreeNode<TextNode>> textTreeNode = segmentTreeNode.getChildListByType(TextNode.class);
				if (textTreeNode.get(0) == null) {
					throw new HBatisException("segment里未填值");
				}
				TextNode textNode = textTreeNode.get(0).getData();
				return SqlBindle.text(textNode, inValueMap);
			}
		}

		throw new HBatisException("segment未找到");
	}

}
