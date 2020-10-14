package com.woshidaniu.qa.db;

import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.tools.ConfigHelper;
import com.woshidaniu.qa.tools.StringHelper;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:04:25 类说明:数据库类型枚举
 */
public enum DataSourceType {
	MYSQL(),

	ORACLE(),

	UNSUPPORT();

	private DataSourceType() {

	}

	public static DataSourceType databaseType(final String type) {
		String _type = type;
		if (StringHelper.isBlankOrNull(type)) {
			_type = ConfigHelper.databaseType();
		}
		if (StringHelper.isBlankOrNull(_type)) {
			throw new ZTesterException("please config property database.type");
		}
		try {
			DataSourceType dbType = DataSourceType.valueOf(_type.toUpperCase());

			return dbType;
		} catch (Throwable e) {
			throw new RuntimeException("unknown database type", e);
		}
	}
}
