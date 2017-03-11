package com.imzy.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imzy.xml.node.Node;
import com.imzy.xml.structure.TreeNode;

public class TreeContext {
	private static Map<String, TreeNode<Node>> sqlNodeTrees = new HashMap<String, TreeNode<Node>>();

	public static void putTreeNode(Node node) {
		// ��ʼ��sqlNode��ʱ�򣬽��������tree��
		TreeNode<Node> treeNode = new TreeNode<Node>(node.getSelfId());
		treeNode.setData(node);
		if (node.getTreeId().equals(node.getSelfId())) {// ���ڵ�
			sqlNodeTrees.put(treeNode.getSelfId(), treeNode);
		} else {
			TreeNode<Node> tree = sqlNodeTrees.get(node.getTreeId());
			TreeNode<Node> parent = tree.findTreeNodeById(node.getParentId());
			parent.addChildNode(treeNode);
		}
	}

	public static TreeNode<Node> getTreeNode(String treeId) {
		return sqlNodeTrees.get(treeId);
	}

	public static List<TreeNode<Node>> getAllTree() {
		List<TreeNode<Node>> list = new ArrayList<TreeNode<Node>>();
		list.addAll(sqlNodeTrees.values());
		return list;
	}

}
