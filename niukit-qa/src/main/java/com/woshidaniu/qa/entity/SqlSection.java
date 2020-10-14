package com.woshidaniu.qa.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shouquan
 * @version 
 * 创建时间：2017年5月12日 上午9:12:29 
 * 类说明
 */
public class SqlSection {
	private String dbEnv;
	private List<String> sqlList = new ArrayList<String>();

	public String getDbEnv() {
		return dbEnv;
	}

	public void setDbEnv(String dbEnv) {
		this.dbEnv = dbEnv;
	}

	public List<String> getSqlList() {
		return sqlList;
	}

	public void setSqlList(List<String> sqlList) {
		this.sqlList = sqlList;
	}

	/**
	 * 增加SQL语句
	 * @param sql
	 */
	public void addSql(String sql) {
		sqlList.add(sql);
	}
}
