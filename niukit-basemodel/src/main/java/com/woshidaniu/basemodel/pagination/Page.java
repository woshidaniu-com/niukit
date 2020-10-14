package com.woshidaniu.basemodel.pagination;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.woshidaniu.basemodel.QueryModel;
/**
 * 
 *@类名称	: Page.java
 *@类描述	：分页对象，包含一页的数据,页码,总页数,总记录数，每页大小
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:32:27 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
public class Page<T> implements Serializable{

	private static final long serialVersionUID = 1L;

	private List<T> result;
	private long totalCount;
	private Integer pageSize = PageConstant.DEFAULT_PAGE_SIZE;//每页记录数
	private Integer pageNo = 1;
	private PageBounds bounds = null;
	
	public Page() {
		this(1,PageConstant.DEFAULT_PAGE_SIZE,  0L, ((List<T>) (new ArrayList<T>())));
	}

	public Page(Integer pageSize) {
		this(1, pageSize, 0, ((List<T>) (new ArrayList<T>())));
	}
	
	public Page(QueryModel entity,long totalSize,List<T> result) {
		this(entity.getPageNo(), entity.getPageSize(),totalSize,result);
	}

	public Page(Integer pageNo,Integer pageSize, long totalSize,List<T> result) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.totalCount = totalSize;
		this.result = result;
		this.bounds = new PageBounds(pageNo,pageSize);
	}

	public long getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public long getPageSize() {
		return pageSize;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setResult(List<T> result) {
		this.result = result;
	}

	public List<T> getResult() {
		return result;
	}
	
	public long getCountPageNo() {
		return totalCount % pageSize != 0 ? totalCount/  pageSize + 1L: totalCount/ pageSize;
	}
	
	public long getPageStartIndex() {
		return getPageStartIndex(getPageNo());
	}

	public long getPageEndIndex() {
		return getPageEndIndex(getPageNo());
	}
	
	public long getPageStartIndex(long pageNo) {
		if(pageNo<1){
			throw new IndexOutOfBoundsException(" pageNo not allow one number less than 1!");
		}else if(pageNo>getTotalCount()){
			throw new IndexOutOfBoundsException(" pageNo not allow one number more than "+getTotalCount()+"!");
		}
		return (pageNo - 1) * pageSize;
	}

	public long getPageEndIndex(long pageNo) {
		if(pageNo<1){
			throw new IndexOutOfBoundsException(" pageNo not allow one number less than 1 !");
		}else if(pageNo>getTotalCount()){
			throw new IndexOutOfBoundsException(" pageNo not allow one number more than "+getTotalCount()+"!");
		}
		long endIndex = pageNo * pageSize - 1;
		return endIndex < totalCount ? endIndex : totalCount - 1;
	}
	
	public boolean hasNextPage() {
		return (long) getPageNo() < getCountPageNo();
	}

	public boolean hasPreviousPage() {
		return getPageNo() > 1;
	}

	public boolean isEmpty() {
		return result == null || result.isEmpty();
	}

	public long getOffset() {
		return getBounds().getOffset();
	}

	public PageBounds getBounds() {
		return bounds;
	}
}
