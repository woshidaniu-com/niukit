package com.woshidaniu.basemodel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import com.woshidaniu.basemodel.datarange.DataRangeModel;

/**
 * 
 *@类名称	: BaseMap.java
 *@类描述	：自定义HashMap的子类；如Mybatis直接返回Map，其结果集字段名称为大写字符，返回BaseMap则结果集字段名称小写且分页时可包含分页信息
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:30:34 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings({"serial"})
public class BaseMap extends HashMap<String, Object> implements Serializable {

	/**
	 * 请求类型 
	 */
	protected String doType;
	/**
	 * 是否分页默认不分页
	 */
	protected boolean pagination = false;
	/**
	 * 总记录数
	 */
	protected String totalResult = "0";
	/**
	 * 分页查询模型
	 */
	protected QueryModel queryModel = new QueryModel();
	/**
	 * 用户模型
	 */
	protected UserModel userModel = new UserModel();
	/**
	 * 查询数据对象集合
	 */
	protected List queryList;
	/**
	 * 删除数据对象集合
	 */
	protected List deleteList;
	/**
	 * @description: 数据范围集合
	 * 				1.每个Map以键值对形式存在：如:
	 * @author : kangzhidong
	 */
	protected List<DataRangeModel> ranges = null;
	
	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

	@Override
	public Object put(String key, Object value) {
		if (!(key == null || key.trim().length() == 0)) {
			//解决查询结果列大写问题
			return super.put(key.toLowerCase(), value);
		}
		return null;
	}

	public String getDoType() {
	
		return doType;
	}

	public void setDoType(String doType) {
	
		this.doType = doType;
	}

	public boolean isPagination() {
	
		return pagination;
	}

	public void setPagination(boolean pagination) {
	
		this.pagination = pagination;
	}

	public List getQueryList() {
		
		return queryList;
	}

	public void setQueryList(List queryList) {
	
		this.queryList = queryList;
	}

	public List getDeleteList() {
	
		return deleteList;
	}

	public void setDeleteList(List deleteList) {
	
		this.deleteList = deleteList;
	}
	
	public List<DataRangeModel> getRanges() {
	
		return ranges;
	}

	public void setRanges(List<DataRangeModel> ranges) {
	
		this.ranges = ranges;
	}

	public String getTotalResult() {
	
		return totalResult;
	}

	public void setTotalResult(String totalResult) {
	
		this.totalResult = totalResult;
	}

	
}
