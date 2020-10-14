package com.woshidaniu.orm.mybatis.interceptor;

import org.apache.ibatis.session.RowBounds;

/**
 * 
 *@类名称	: PaginationBounds.java
 *@类描述	： <p> 简单分页模型 </p>
 * 用户可以通过继承 org.apache.ibatis.session.RowBounds实现自己的分页模型<br>
 * 注意：插件仅支持RowBounds及其子类作为分页参数
 *@创建人	：kangzhidong
 *@创建时间	：Aug 18, 2016 7:49:08 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class PaginationBounds extends RowBounds {
	
	/* 总记录条数 */
	private int total;

	/* 总记录条数 */
	private int size;

	/* 总记录条数 */
	private int pages;

	/* 当前页 */
	private int current;

	public PaginationBounds() {
		super();
	}

	/**
	 * 构造函数
	 * <p>
	 * 在不知道总记录条数的情况下使用该构造函数<br>
	 * 分页插件将会自动查询总记录条数并设置
	 * 
	 * @param offset
	 *            偏移量
	 * @param limit
	 *            界限
	 */
	public PaginationBounds(int offset, int limit) {
		super(offset, limit);
		this.size = limit;
	}

	/**
	 * 构造函数
	 * <p>
	 * 该构造函数会自动计算偏移量和界限值<br>
	 * 请注意：在知道总记录条数的情况下，插件是不会再查询总记录条数的
	 * 
	 * @param current
	 *            当前页
	 * @param size
	 *            每页显示条数
	 * @param total
	 *            总条数
	 */
	public PaginationBounds(int current, int size, int total) {
		super((current - 1) * size, size);
		this.total = total;
		this.size = size;
		this.current = current;
		this.pages = total / size;
		if (total % size != 0) {
			this.pages++;
		}
	}

	public boolean hasPrevious() {
		return this.current > 1;
	}

	public boolean hasNext() {
		return this.current < this.pages;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
		this.size = this.getLimit();
		this.pages = this.total / this.size;
		if (this.total % this.size != 0) {
			this.pages++;
		}
		this.current = this.getOffset() / this.size + 1;
	}

	public int getSize() {
		return size;
	}

	public int getPages() {
		return pages;
	}

	public int getCurrent() {
		return current;
	}

	@Override
	public String toString() {
		return "Pagination { total=" + total + " ,size=" + size + " ,pages=" + pages + " ,current=" + current + " }";
	}
}
