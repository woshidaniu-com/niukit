package com.woshidaniu.basemodel.pagination;

import com.woshidaniu.basemodel.BaseModel;
import com.woshidaniu.basemodel.QueryModel;

public class PageBounds {

	public final static long NO_ROW_OFFSET = 0;
	public final static long NO_ROW_LIMIT = Integer.MAX_VALUE;
	public final static PageBounds DEFAULT = new PageBounds();
	
	protected long offset;// 分页起始位置
	protected long limit;// 每页记录数
	protected String sortName;//排序字段名称
	protected String sortOrder;// 排序类型 asc \ desc
	
	public PageBounds() {
		this.offset = NO_ROW_OFFSET;
		this.limit = NO_ROW_LIMIT;
	}
	
	public <T extends BaseModel> PageBounds(QueryModel model) {
		this.offset = (model.getPageNo() - 1) * model.getPageSize();
		this.limit = model.getPageSize();
		this.sortName = model.getSortName();
		this.sortOrder = model.getSortOrder();
	}
	
	public PageBounds(Integer pageNo, Integer limit) {
		this.offset = (pageNo - 1) * limit;
		this.limit = limit;
	}

	public PageBounds(Integer pageNo, Integer limit,String sortName,String sortOrder) {
		this.offset = (pageNo - 1) * limit;
		this.limit = limit;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
	}

	public PageBounds(long offset, long limit) {
		this.offset = offset;
		this.limit = limit;
	}
	
	public PageBounds(long offset, long limit,String sortName,String sortOrder) {
		this.offset = offset;
		this.limit = limit;
		this.sortName = sortName;
		this.sortOrder = sortOrder;
	}

	public long getOffset() {
		return offset;
	}

	public long getLimit() {
		return limit;
	}

	public String getSortName() {
		return sortName;
	}

	public String getSortOrder() {
		return sortOrder;
	}

}
