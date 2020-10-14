package com.woshidaniu.basemodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
/**
 * 
 *@类名称	: QueryModel.java
 *@类描述	：分页查询model
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:31:51 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings({"serial","unchecked"})
public class QueryModel implements Cloneable, Serializable{
	
	protected static final int DEFAULT_LIMIT = 15;
	
	/**
	 * 分页起始位置
	 */
	protected int offset = 0;
	/**
	 * 每页记录数
	 */
	protected int limit = 15;
	/**
	 * 当前页码
	 */
	protected int pageNo;
	/**
	 * 每页数据量
	 */
	protected int pageSize;
	/**
	 * 总页数
	 */
	protected int totalPage;
	/**
	 * 总记录数
	 */
	protected int totalCount;
	/**
	 * 排序字段名称
	 */
	protected String sortName;
	/**
	 * 排序类型 asc \ desc
	 */
	protected String sortOrder;
	
	/** ==============支持多列排序================*/
	protected List<Map<String,String>> multiSort;
	/** ==============支持多列排序================*/
	
	protected List items;
	
	public int getPageNo() {
		return pageNo < 0 ? (getOffset() / getPageSize() + 1 ) : pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize <= 0 ? getLimit() : pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getOffset() {
		// 计算第一条记录的位置，Oracle分页是通过rownum进行的，而rownum是从1开始的
		return offset < 0 ? ( pageNo < 0 ? 0 : ( (getPageNo() - 1 ) * getPageSize() + 1) ): offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public int getLimit() {
		return limit <= 0 ? DEFAULT_LIMIT : limit;
	}

	public void setLimit(int limit) {
	
		this.limit = limit;
	}

	public void prevPage(){
		setPageNo(( getPageNo()-1 ) > 0 ? getPageNo() - 1 : 0);
	}
	
	public void nextPage(){
		setPageNo(getPageNo()+1);
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getSortName() {
		return sortName;
	}

	public void setSortName(String sortName) {
		this.sortName = sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public List getItems() {
		return items;
	}

	public void setItems(List items) {
		this.items = items;
	}

	
	public List<Map<String, String>> getMultiSort() {
		return multiSort;
	}

	public List<Sort> getSorts() {
		List<Sort> sorts = new ArrayList<QueryModel.Sort>();
		if(this.multiSort != null && this.multiSort.size() > 0){
			Iterator<Map<String, String>> it = multiSort.iterator();
			while(it.hasNext()){
				Map<String, String> next = it.next();
				sorts.add(new Sort(StringUtils.trim(next.get("sortName")), StringUtils.trim(next.get("sortOrder"))));
			}
		}
		return sorts;
	}

	public void setMultiSort(List<Map<String, String>> multiSort) {
		this.multiSort = multiSort;
	}

	public static class Sort {
		protected String sortName;
		protected String sortOrder;
		
		public Sort() {
			super();
		}
		public Sort(String sortName, String sortOrder) {
			super();
			this.sortName = sortName;
			this.sortOrder = sortOrder;
		}
		public String getSortName() {
			return sortName;
		}
		public void setSortName(String sortName) {
			this.sortName = sortName;
		}
		public String getSortOrder() {
			return sortOrder;
		}
		public void setSortOrder(String sortOrder) {
			this.sortOrder = sortOrder;
		}
		
	}
}
