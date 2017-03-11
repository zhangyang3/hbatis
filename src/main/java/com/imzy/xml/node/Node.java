package com.imzy.xml.node;

import java.util.UUID;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.imzy.context.TreeContext;

public class Node {

	/** treeId*/
	private String treeId;

	/** parentId*/
	private String parentId;

	/** selfId*/
	private String selfId = UUID.randomUUID().toString().replaceAll("-", "");

	public String getTreeId() {
		return treeId;
	}

	public void setTreeId(String treeId) {
		this.treeId = treeId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getSelfId() {
		return selfId;
	}

	public void setSelfId(String selfId) {
		this.selfId = selfId;
	}

	/**
	 * …Ë÷√treeId∫ÕparentId
	 * @param node
	 */
	public void setPT(Node node) {
		this.treeId = node.getTreeId();
		this.parentId = node.getSelfId();
		TreeContext.putTreeNode(this);

	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
