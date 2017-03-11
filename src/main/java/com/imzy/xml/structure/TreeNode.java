package com.imzy.xml.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * ���ڵ�
 * @author yangzhang7
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class TreeNode<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	private String parentId;
	private String selfId;
	protected T data;
	protected TreeNode<T> parentNode;
	protected List<TreeNode<T>> childList = new ArrayList<TreeNode<T>>();

	public TreeNode(String selfId) {
		this.selfId = selfId;
	}

	/**
	 * �Ƿ���Ҷ�ӽڵ�
	 * @return
	 */
	public boolean isLeaf() {
		if (childList.isEmpty()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * ����һ��child�ڵ㵽��ǰ�ڵ��� 
	 * @param treeNode
	 */
	public void addChildNode(TreeNode<T> treeNode) {
		treeNode.setParentNode(this);
		treeNode.setParentId(this.selfId);
		childList.add(treeNode);
	}

	/**
	 * ���ص�ǰ�ڵ�ĸ����ڵ㼯��
	 * @return
	 */
	public List<TreeNode<T>> getElders() {
		List<TreeNode<T>> elderList = new ArrayList<TreeNode<T>>();
		TreeNode<T> parentNode = this.parentNode;
		if (parentNode != null) {
			elderList.add(parentNode);
			elderList.addAll(parentNode.getElders());
		}
		return elderList;
	}

	public List<TreeNode<?>> getEldersByTypes(Class<?>... clazzs) {
		List<TreeNode<?>> elderList = new ArrayList<TreeNode<?>>();
		TreeNode<T> parentNode = this.parentNode;
		if (parentNode != null) {
			for (Class<?> clazz : clazzs) {
				if (clazz.isAssignableFrom(parentNode.getData().getClass())) {
					elderList.add(parentNode);
				}
			}
			elderList.addAll(parentNode.getEldersByTypes(clazzs));
		}
		return elderList;
	}

	/**
	 * ���ص�ǰ�ڵ��������
	 * @return
	 */
	public List<TreeNode<T>> getJuniors() {
		List<TreeNode<T>> juniorList = new ArrayList<TreeNode<T>>();
		List<TreeNode<T>> childList = this.childList;
		if (childList != null) {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				TreeNode<T> junior = childList.get(i);
				juniorList.add(junior);
				juniorList.addAll(junior.getJuniors());
			}
		}
		return juniorList;
	}

	/**
	 * ���ص�ǰ�ڵ�ĺ��Ӽ���
	 * @return
	 */
	public List<TreeNode<T>> getChildList() {
		return childList;
	}

	/**
	 * ɾ���ڵ�����������
	 */
	public void deleteNode() {
		TreeNode<T> parentNode = this.parentNode;
		String selfId = this.selfId;

		if (parentNode != null) {
			parentNode.deleteChildNode(selfId);
		}
	}

	/**
	 * ɾ����ǰ�ڵ��ĳ���ӽڵ�
	 * @param childId
	 */
	public void deleteChildNode(String childId) {
		List<TreeNode<T>> childList = this.childList;
		int childNumber = childList.size();
		for (int i = 0; i < childNumber; i++) {
			TreeNode<T> child = childList.get(i);
			if (child.getSelfId().equals(childId)) {
				childList.remove(i);
				return;
			}
		}
	}

	/**
	 * ��̬�Ĳ���һ���µĽڵ㵽��ǰ����
	 * @param treeNode
	 * @return
	 */
	public boolean insertJuniorNode(TreeNode<T> treeNode) {
		String juniorParentId = treeNode.getParentId();
		if (this.parentId.equals(juniorParentId)) {
			addChildNode(treeNode);
			return true;
		} else {
			List<TreeNode<T>> childList = this.childList;
			int childNumber = childList.size();
			boolean insertFlag;

			for (int i = 0; i < childNumber; i++) {
				TreeNode<T> childNode = childList.get(i);
				insertFlag = childNode.insertJuniorNode(treeNode);
				if (insertFlag == true)
					return true;
			}
			return false;
		}
	}

	/**
	 * �ҵ�һ������ĳ���ڵ�
	 * @param id
	 * @return
	 */
	public TreeNode<T> findTreeNodeById(String id) {
		if (this.selfId.equals(id))
			return this;
		if (this.childList.isEmpty() || this.childList == null) {
			return null;
		} else {
			int childNumber = this.childList.size();
			for (int i = 0; i < childNumber; i++) {
				TreeNode<T> child = this.childList.get(i);
				TreeNode<T> resultNode = child.findTreeNodeById(id);
				if (resultNode != null) {
					return resultNode;
				}
			}
			return null;
		}
	}

	/**
	 * ����һ��������α���
	 */
	public void traverse() {
		if (StringUtils.isBlank(this.selfId))
			return;
		print(this.selfId);
		if (this.childList == null || this.childList.isEmpty())
			return;
		int childNumber = this.childList.size();
		for (int i = 0; i < childNumber; i++) {
			TreeNode<T> child = this.childList.get(i);
			child.traverse();
		}
	}

	public void print(String content) {
		System.out.println(content);
	}

	public void setChildList(List<TreeNode<T>> childList) {
		this.childList = childList;
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

	public TreeNode<T> getParentNode() {
		return parentNode;
	}

	public void setParentNode(TreeNode<T> parentNode) {
		this.parentNode = parentNode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public <C> List<TreeNode<C>> getChildListByType(Class<C> clazz) {
		List<TreeNode<C>> returnValue = new ArrayList<TreeNode<C>>();
		List<TreeNode<T>> list = getChildList();
		for (TreeNode<T> treeNode : list) {
			if (treeNode.getData().getClass().equals(clazz)) {
				returnValue.add((TreeNode<C>) treeNode);
			}
		}
		return returnValue;
	}

	public <C> C getParentNodeDataByType(Class<C> clazz) {
		TreeNode<T> parentTreeNode = getParentNode();
		T data = parentTreeNode.getData();
		return (C) data;
	}
}