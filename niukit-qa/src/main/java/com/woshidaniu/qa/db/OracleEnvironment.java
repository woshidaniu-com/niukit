package com.woshidaniu.qa.db;

import com.woshidaniu.qa.types.OracleTypeMap;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:36:10 类说明
 */
public class OracleEnvironment extends BaseEnvironment {
	
	public OracleEnvironment(String dataSourceName, String dataSourceFrom) {
		super(DataSourceType.ORACLE, dataSourceName, dataSourceFrom);
		typeMap = new OracleTypeMap();
	}

	public String getFieldQuato() {
		return "\"";
	}

	/**
	 * {@inheritDoc} <br>
	 * <br>
	 * 在oracle中将java.util.Date转为java.sql.Date对象
	 */
	@Override
	public Object converToSqlValue(Object value) {
		if (value != null && "java.util.Date".equals(value.getClass().getName())) {
			return new java.sql.Date(((java.util.Date) value).getTime());
		} else {
			return super.converToSqlValue(value);
		}
	}
}
