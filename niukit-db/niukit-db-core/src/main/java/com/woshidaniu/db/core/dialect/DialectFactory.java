package com.woshidaniu.db.core.dialect;

import com.woshidaniu.db.core.dialect.impl.DB2Dialect;
import com.woshidaniu.db.core.dialect.impl.DerbyDialect;
import com.woshidaniu.db.core.dialect.impl.H2Dialect;
import com.woshidaniu.db.core.dialect.impl.HSQLDialect;
import com.woshidaniu.db.core.dialect.impl.MySql5Dialect;
import com.woshidaniu.db.core.dialect.impl.OracleDialect;
import com.woshidaniu.db.core.dialect.impl.PostgreDialect;
import com.woshidaniu.db.core.dialect.impl.SQLServer2005Dialect;
import com.woshidaniu.db.core.dialect.impl.SQLServerDialect;
import com.woshidaniu.db.core.dialect.impl.SQLiteDialect;
import com.woshidaniu.db.core.dialect.impl.SybaseDialect;


/**
 * 
 *@类名称	: DialectFactory.java
 *@类描述	：SQL方言工厂类
 *@创建人	：kangzhidong
 *@创建时间	：Mar 29, 2016 5:08:46 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class DialectFactory {

	private static final Dialect DB2_DIALECT = new DB2Dialect();
	private static final Dialect DERBY_DIALECT = new DerbyDialect();
	private static final Dialect H2_DIALECT = new H2Dialect();
	private static final Dialect HSQL_DIALECT = new HSQLDialect();
	private static final Dialect MYSQL5_DIALECT = new MySql5Dialect();
	private static final Dialect ORACLE_DIALECT = new OracleDialect();
	private static final Dialect POSTGRE_DIALECT = new PostgreDialect();
	private static final Dialect SQLITE_DIALECT = new SQLiteDialect();
	private static final Dialect SQLSERVER2005_DIALECT = new SQLServer2005Dialect();
	private static final Dialect SQLSERVER_DIALECT = new SQLServerDialect();
	private static final Dialect SYBASE_DIALECT = new SybaseDialect();
	
	/**
	 * 
	 * @description	： 根据数据库类型选择不同分页方言
	 * @author 		： kangzhidong
	 * @date 		：Jan 26, 2016 12:20:45 PM
	 * @param dbtype	:数据库类型
	 * @return
	 * @throws Exception
	 */
	public static Dialect getDialectByType(String dbtype){
		if ("db2".equalsIgnoreCase(dbtype)) {
			return DB2_DIALECT;
		} else if ("derby".equalsIgnoreCase(dbtype)) {
			return DERBY_DIALECT;
		} else if ("h2".equalsIgnoreCase(dbtype)) {
			return H2_DIALECT;
		} else if ("hsql".equalsIgnoreCase(dbtype)) {
			return HSQL_DIALECT;
		} else if ("mysql5".equalsIgnoreCase(dbtype)) {
			return MYSQL5_DIALECT;
		} else if ("oracle".equalsIgnoreCase(dbtype)) {
			return ORACLE_DIALECT;
		} else if ("postgre".equalsIgnoreCase(dbtype)) {
			return POSTGRE_DIALECT;
		} else if ("sqlite".equalsIgnoreCase(dbtype)) {
			return SQLITE_DIALECT;
		} else if ("sqlserver2005".equalsIgnoreCase(dbtype)) {
			return SQLSERVER2005_DIALECT;
		} else if ("sqlserver".equalsIgnoreCase(dbtype)) {
			return SQLSERVER_DIALECT;
		} else if ("sybase".equalsIgnoreCase(dbtype)) {
			return SYBASE_DIALECT;
		} else {
			return null;
		}
	}

}
