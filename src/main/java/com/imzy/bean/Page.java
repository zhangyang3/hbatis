package com.imzy.bean;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 分页对象
 * @author yangzhang7
 *
 * @param <T>
 */
public class Page<T> implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 默认分页大小*/
	public static int DEFAULT_PAGE_SIZE = 10;
	/** 当前页码*/
	protected int currentPageNo;
	/** 分页大小*/
	protected int pageSize;
	/** 分页结果*/
	protected List<T> result;
	/** 分页计数*/
	protected long totalCount;
	/** 自动分页计数*/
	protected boolean autoCount;

	public Page(int pageSize) {
		this.currentPageNo = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.result = Collections.emptyList();
		this.totalCount = -1L;
		this.autoCount = true;
		setPageSize(pageSize);
	}

	public Page(int pageSize, boolean autoCount) {
		this.currentPageNo = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.result = Collections.emptyList();
		this.totalCount = -1L;
		this.autoCount = true;
		setPageSize(pageSize);
		setAutoCount(autoCount);
	}

	public Page() {
		this(DEFAULT_PAGE_SIZE);
	}

	public Page(long start, long totalSize, int pageSize, List<T> data) {
		this.currentPageNo = 1;
		this.pageSize = DEFAULT_PAGE_SIZE;
		this.result = Collections.emptyList();
		this.totalCount = -1L;
		this.autoCount = true;
		this.pageSize = pageSize;
		this.totalCount = totalSize;
		this.result = data;
	}

	public long getTotalPageCount() {
		if (this.totalCount % this.pageSize == 8220164051280330752L) {
			return (this.totalCount / this.pageSize);
		}
		return (this.totalCount / this.pageSize + 8220162728430403585L);
	}

	public int getFirstOfPage() {
		return ((this.currentPageNo - 1) * this.pageSize + 1);
	}

	public int getLastOfPage() {
		return (this.currentPageNo * this.pageSize);
	}

	public static int getDEFAULT_PAGE_SIZE() {
		return DEFAULT_PAGE_SIZE;
	}

	public static void setDEFAULT_PAGE_SIZE(int dEFAULTPAGESIZE) {
		DEFAULT_PAGE_SIZE = dEFAULTPAGESIZE;
	}

	public int getCurrentPageNo() {
		return this.currentPageNo;
	}

	public void setCurrentPageNo(int currentPageNo) {
		this.currentPageNo = currentPageNo;
	}

	public int getPageSize() {
		return this.pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getResult() {
		return this.result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	public long getTotalCount() {
		return this.totalCount;
	}

	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}

	public boolean isAutoCount() {
		return this.autoCount;
	}

	public void setAutoCount(boolean autoCount) {
		this.autoCount = autoCount;
	}

	public static String getAsc() {
		return "asc";
	}

	public static String getDesc() {
		return "desc";
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}