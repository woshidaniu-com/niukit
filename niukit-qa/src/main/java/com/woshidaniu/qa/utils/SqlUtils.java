package com.woshidaniu.qa.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.woshidaniu.qa.entity.SqlSection;
import com.woshidaniu.qa.exception.ZTesterException;

/**
 * @author shouquan:
 * @version 创建时间：2017年5月12日 上午9:04:24 类说明
 */
public class SqlUtils {
	public static final String SQL_END_CHAR = ";";

	/**
	 * 解析sql文件
	 * 
	 * @param sqlFile
	 * @return
	 * @throws IOException
	 */
	public static List<SqlSection> parse(File sqlFile) {
		List<SqlSection> resultMaps = new ArrayList<SqlSection>();
		String currentDbEnv = null;
		boolean prevLineEndFlag = false;
		List<String> lines = new ArrayList<String>();
		List<String> tmpList = new ArrayList<String>();

		try {
			lines = FileUtils.readLines(sqlFile, "UTF-8");
			for (String line : lines) {
				line = StringUtils.trim(line);
				if (StringUtils.isEmpty(line)) {
					continue;
				}

				if (line.startsWith("[") && line.endsWith("]")) {
					if (!tmpList.isEmpty()) {
						resultMaps = mergeMap(currentDbEnv, tmpList, resultMaps);
						tmpList.clear();
					}
					prevLineEndFlag = false;
					currentDbEnv = StringUtils.substring(line, 1, line.length() - 1);
				} else {
					if (isStartWithKeyWord(line) && prevLineEndFlag) {
						if (!tmpList.isEmpty()) {
							resultMaps = mergeMap(currentDbEnv, tmpList, resultMaps);
							tmpList.clear();
						}
						prevLineEndFlag = false;
					}
					tmpList.add(line);
					if (StringUtils.endsWith(line, SQL_END_CHAR)) {
						prevLineEndFlag = true;
					}
				}
			}
			if (!tmpList.isEmpty()) {
				resultMaps = mergeMap(currentDbEnv, tmpList, resultMaps);
				tmpList.clear();
			}
		} catch (IOException e) {
			new ZTesterException("the sqlFile " + sqlFile + " can not be read");
		}
		return resultMaps;
	}

	/**
	 * 合并数据库结果
	 * 
	 * @param dbEnv
	 * @param tmpList
	 * @param resultMaps
	 * @return
	 */
	private static List<SqlSection> mergeMap(String currentDbEnv, List<String> tmpList, List<SqlSection> resultMaps) {
		SqlSection lastSection = null;

		if (resultMaps.size() > 0) {
			lastSection = resultMaps.get(resultMaps.size() - 1);
		}

		if (null != lastSection && lastSection.getDbEnv().equals(currentDbEnv)) {
			lastSection.getSqlList().add(StringUtils.join(tmpList, "\n"));
		} else {
			ArrayList<String> sqlList = new ArrayList<String>();
			sqlList.add(StringUtils.join(tmpList, "\n"));
			SqlSection sqlSection = new SqlSection();
			sqlSection.setDbEnv(currentDbEnv);
			sqlSection.setSqlList(sqlList);
			resultMaps.add(sqlSection);
		}
		return resultMaps;

	}

	/**
	 * 判断sql语句是否以特殊关键字开头
	 * 
	 * @param line
	 * @return
	 */
	private static boolean isStartWithKeyWord(String line) {
		line = StringUtils.trim(StringUtils.lowerCase(line));
		if (line.startsWith("select") || line.startsWith("delete") || line.startsWith("update")
				|| line.startsWith("insert") || line.startsWith("commit")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断sql是否select语句
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isSelectStatement(String line) {
		line = StringUtils.trim(StringUtils.lowerCase(line));
		if (line.startsWith("select")) {
			return true;
		}
		return false;
	}

	/**
	 * 判断sql语句是否commit语句
	 * 
	 * @param line
	 * @return
	 */
	public static boolean isCommitStatement(String line) {
		line = StringUtils.trim(StringUtils.lowerCase(line));
		if (line.startsWith("commit")) {
			return true;
		}
		return false;
	}
}
