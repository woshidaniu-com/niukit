package com.woshidaniu.basemodel.datarange;

/**
 * 
 *@类名称	: DataRangeModel.java
 *@类描述	：数据范围对象实体：每个数据范围表的数据范围字段的详细数据范围
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 3:32:39 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DataRangeModel {
	
	private String field;//受数据范围限制的字段名称
	private DataRangeRelation relation;//受数据范围限制的字段与限制结果的条件关系
	private String range;//数据范围内容
	private String disc;//数据范围描述
	private String sql;//根据设置生成的数据范围sql
	
	public DataRangeModel(String field, DataRangeRelation relation, String range) {
		this.field = field;
		this.relation = relation;
		this.range = range;
		
	}

	public DataRangeModel(String field, DataRangeRelation relation, String range, String disc) {
		this.field = field;
		this.relation = relation;
		this.range = range;
		this.disc = disc;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public DataRangeRelation getRelation() {
		return relation;
	}
	
	public void setRelation(DataRangeRelation relation) {
		this.relation = relation;
	}
	
	public String getRange() {
		return range==null?"":range;
	}
	
	public void setRange(String range) {
		this.range = range;
	}

	public String getDisc() {
		return disc;
	}

	public void setDisc(String disc) {
		this.disc = disc;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
}



