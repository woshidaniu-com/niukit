package com.woshidaniu.qa.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import org.springframework.util.ResourceUtils;

import com.woshidaniu.qa.entity.SqlDefaultPath;
import com.woshidaniu.qa.exception.ZTesterException;
import com.woshidaniu.qa.utils.DatabaseUtils;

/**
 * @author shouquan
 * @version 创建时间：2017年5月11日 下午5:55:49 类说明
 */
public class DbPrepareRunner {

	/**
	 * 
	 * @param testClazz
	 * @param sqlFilePath
	 * @throws FileNotFoundException 
	 */
	public static void runDbPrepare(Class<?> testClazz, String sqlFilePath) {
		SqlDefaultPath sqlDefaultPath = null;
		sqlDefaultPath = getDefaultSqlFilePath(testClazz, sqlFilePath);// 获取默认的配置文件路径
		String defaultSqlFilePath = sqlDefaultPath.getPath();
		File sqlFile = null;
		// 文件路径
		try {
			sqlFile = ResourceUtils.getFile(defaultSqlFilePath);
		} catch (FileNotFoundException e) {

			try {
				sqlFile = ResourceUtils.getFile(sqlFilePath);
			} catch (FileNotFoundException e1) {
				 throw new ZTesterException("The sql file is not existed! The sqlFilePath is:" + defaultSqlFilePath, e);
				//throw new FileNotFoundException(String.format("can't find classpath resource[%s]!", sqlFilePath));
			}
		}
		HashMap<String, Object> params = new HashMap<String, Object>();
		params = (HashMap<String, Object>) DatabaseUtils
				.executeFile(ResourceUtils.FILE_URL_PREFIX + sqlFile.getAbsolutePath(), params);
	}

	/**
	 * 获取文件默认的路径
	 * 
	 * @param testClazz
	 * @param sqlFile
	 * @return
	 */
	private static SqlDefaultPath getDefaultSqlFilePath(Class<?> testClazz, String sqlFile) {
		SqlDefaultPath sqlDefaultPath = new SqlDefaultPath();
		String className = testClazz.getName();
		String packageName = className.substring(0, className.lastIndexOf("."));
		String path = null;
		StringBuffer sb = new StringBuffer();
		sb.append("classpath:").append(packageName.replaceAll("\\.", "/")).append("/").append(sqlFile);
		path = sb.toString();
		sqlDefaultPath.setPath(path);
		return sqlDefaultPath;
	}
}