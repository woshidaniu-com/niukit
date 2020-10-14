/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weiguangyue
 * 高级查询版本信息
 */
public class Version {
	
	private final static int MajorVersion    = 2020;//执行maven deploy 的年份
	private final static int MinorVersion    = 8;//执行maven deploy 的月份
	private final static int RevisionVersion = 12;//执行maven deploy 的日子
	
	public static final String VERSION_1 = "1.0";
	public static final String VERSION_2 = "2.0";
	
	public static final Map<String,String> versionToDescriptionMapping = new HashMap<String,String>(2);
	
	private static volatile String version = "1.0";

	static {
		versionToDescriptionMapping.put("1.0", "基础版本");
		versionToDescriptionMapping.put("2.0", "文本输入可回车输入多文本输入条件块(1.条件块内用or 2.多个条件块用and)");
	}
	
	/**
	 * 获得发布标签
	 * @return
	 */
    public static String getReleaseDate() {
        return MajorVersion + "." + MinorVersion + "." + RevisionVersion;
    }
	
	/**
	 * 设置版本号
	 * @param v
	 * @return
	 */
	public static boolean setVersion(String v) {
		if(versionToDescriptionMapping.containsKey(v)) {
			version = v;
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * 获得当前系统采用的版本号
	 * @return
	 */
	public static String getVersion() {
		return version;
	}
	
	/**
	 * 获得基础平台部最新版本号
	 * @return
	 */
	public static String lastVersion() {
		return "2.0";
	}
}
