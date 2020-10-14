/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.util.Comparator;
import java.util.Map;

/**
 * 高级查询SQL解析器
 * @author Penghui.Qu
 * 	v1版本基础代码
 * @author zhangxiaobin
 *  v2版本基础代码
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 */
public class PinYinComparator implements Comparator<Map<String,Object>> {

	private static final String PINYIN_NAME = "pinyin";
	
	@Override
	public int compare(Map<String, Object> o1, Map<String, Object> o2) {
    	return ((String)o1.get(PINYIN_NAME)).compareTo((String)o2.get(PINYIN_NAME));
	}
}
