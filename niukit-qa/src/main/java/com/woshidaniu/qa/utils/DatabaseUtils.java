package com.woshidaniu.qa.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import com.woshidaniu.qa.db.DBEnvironmentFactory;
import com.woshidaniu.qa.entity.SqlSection;
import com.woshidaniu.qa.exception.ZTesterException;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2017年5月12日 下午2:59:06 类说明
 */
public class DatabaseUtils {
	public static Map<String, Object> executeFile(String sqlFilePath, Map<String, Object> context) {
		Map<String, Connection> connectionMap = new HashMap<String, Connection>(); // 数据库连接串的Map
		try {
			File sqlFile = null;
			try {
				sqlFile = ResourceUtils.getFile(sqlFilePath);
			} catch (FileNotFoundException e) {
				sqlFile = ResourceUtils.getFile(ResourceUtils.FILE_URL_PREFIX + sqlFilePath);
			}
			List<SqlSection> sqlSectionList = SqlUtils.parse(sqlFile);
			try {
				for (SqlSection sqlSection : sqlSectionList) {
					String dbEnv = sqlSection.getDbEnv();

					Connection connection = connectionMap.get(dbEnv);
					QueryRunner runner = new QueryRunner();
					// DatabaseCharset databaseCharset = charsetMap.get(dbEnv);
					if (null == connection) {
						connection = DBEnvironmentFactory.getDBEnvironment(dbEnv).getDataSource().getConnection();
						connection.setAutoCommit(false);
						connectionMap.put(dbEnv, connection);
					}

					// 依次执行sql语句
					for (String sql : sqlSection.getSqlList()) {
						// 先对sql进行解析,变量替换
						sql = StringUtils.trim(sql);// 参数替换
						// sql = StringUtils.trim(sql);
						// FIXBUG 对于执行语句，最后不能有;
						if (StringUtils.endsWith(sql, ";"))
							sql = StringUtils.substring(sql, 0, sql.length() - 1);
						if (SqlUtils.isCommitStatement(sql)) { // 如果是commit语句，暂时不提交
							;
						} else if (SqlUtils.isSelectStatement(sql)) {
							runner.query(connection, sql, new MapHandler());
						} else {
							runner.update(connection, sql);
						}
					}
				}
				// 最后进行提交操作
				for (Connection con : connectionMap.values())
					org.apache.commons.dbutils.DbUtils.commitAndCloseQuietly(con);
			} catch (SQLException e) {
				for (Connection con : connectionMap.values()) {
					org.apache.commons.dbutils.DbUtils.rollbackAndCloseQuietly(con);// 回滚
				}
				throw new ZTesterException("The sql file is executed error!", e);
			} catch (Exception e) {
				for (Connection con : connectionMap.values())
					org.apache.commons.dbutils.DbUtils.rollbackAndCloseQuietly(con);
				throw new ZTesterException("Sql file Parse error!"+e.getMessage(), e);
			}
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}
		return context;
	}
}
