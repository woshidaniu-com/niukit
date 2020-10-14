package com.woshidaniu.basicutils;

import org.apache.commons.collections.Predicate;

import com.woshidaniu.basicutils.functors.TreeMapPredicate;

/**
 * 
 *@类名称	: PredicateUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Aug 8, 2016 12:01:09 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class PredicateUtils extends org.apache.commons.collections.PredicateUtils {
	
	public static Predicate treeMapPredicate(String key,String parent) {
		return new TreeMapPredicate(key,parent);
	} 
	
}
