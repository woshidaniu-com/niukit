package com.woshidaniu.basemodel;

import java.io.Serializable;
import java.util.List;

import com.woshidaniu.basemodel.datarange.DataRangeModel;
/**
 * 
 *@类名称	: BaseModel.java
 *@类描述	：基础域模型
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:30:43 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
@SuppressWarnings({"serial"})
public class BaseModel implements Serializable{
	
	/**
	 * 国际化Local值，默认zh_CN,其他值如en_US,zh_CN
	 */
	public String localeKey = "zh_CN";
	/**
	 * 对应Oracle数据的row_id
	 */
	protected String row_id;
	/**
	 * 请求类型 
	 */
	protected String doType;
	/**
	 * 是否启用分页的标记
	 */
	protected boolean pageable = true;
	/**
	 * 是否启用数据范围的标记
	 */
	protected boolean rangeable = true;
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
	protected List<?> queryList;
	/**
	 * 删除数据对象集合
	 */
	protected List<?> deleteList;
	/**
	 * 数据范围集合
	 */
	protected transient List<DataRangeModel> ranges = null;
	

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public List<?> getQueryList() {
		return queryList;
	}

	public void setQueryList(List<?> queryList) {
		this.queryList = queryList;
	}

	public List<?> getDeleteList() {
		return deleteList;
	}

	public void setDeleteList(List<?> deleteList) {
		this.deleteList = deleteList;
	}

	public boolean isPageable() {
		return pageable;
	}

	public void setPageable(boolean pageable) {
		this.pageable = pageable;
	}

	public String getDoType() {
		return doType;
	}

	public void setDoType(String doType) {
		this.doType = doType;
	}

	public List<DataRangeModel> getRanges() {
		return ranges;
	}

	public void setRange(List<DataRangeModel> ranges) {
		this.ranges = ranges;
	}

	public QueryModel getQueryModel() {
		return queryModel;
	}

	public void setQueryModel(QueryModel queryModel) {
		this.queryModel = queryModel;
	}

	public void setRanges(List<DataRangeModel> ranges) {
		this.ranges = ranges;
	}

	public UserModel getUserModel() {
		return userModel;
	}

	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}

	public String getTotalResult() {
		return totalResult;
	}

	public void setTotalResult(String totalResult) {
		this.totalResult = totalResult;
	}

	/**
	 * @return the localeKey
	 */
	public String getLocaleKey() {
		return localeKey;
	}

	/**
	 * @param localeKey the localeKey to set
	 */
	public void setLocaleKey(String localeKey) {
		this.localeKey = localeKey;
	}

	/**
	 * @return the rangeable
	 */
	public boolean isRangeable() {
		return rangeable;
	}

	/**
	 * @param rangeable the rangeable to set
	 */
	public void setRangeable(boolean rangeable) {
		this.rangeable = rangeable;
	}
	
}
