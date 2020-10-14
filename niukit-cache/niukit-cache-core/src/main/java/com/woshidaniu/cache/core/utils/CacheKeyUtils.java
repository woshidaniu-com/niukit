package com.woshidaniu.cache.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.cache.core.ICacheClient;

/**
 * 
 *@类名称	: CacheKeyUtils.java
 *@类描述	：缓存key生成工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 18, 2016 9:56:26 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class CacheKeyUtils {
	
	public static String genKey(Class clazz,String methodName,String...strings){
		return CacheKeyUtils.genKey(clazz.getName(), methodName, strings);
	}
	
	public static String genKey(String clazzName,String methodName,String...strings){
		StringBuilder builder = new StringBuilder();
		//类全名+调用方法
		builder.append(clazzName).append(".").append(methodName);
		if(!BlankUtils.isBlank(strings)){
			for (String string : strings) {
				builder.append(".").append(string);
			}
		}
		return Base64.encodeBase64String(DigestUtils.md5(builder.toString())) ;
	}
	 
	public static String genPrefix(String... prefixs){
		List<String> roleKeys = new ArrayList<String>();
		if(!BlankUtils.isBlank(prefixs)){
			for (String prefix : prefixs) {
				roleKeys.add(prefix);
			}
		}
		return StringUtils.join(roleKeys,",");
	}
	
	public static String genMutexKey(String autoKey){
		return ICacheClient.MUTEX_KEY_PREFIX + autoKey;
	}
	
	public static String genQueryKey(String autoKey){
		return autoKey+"_QueryModel";
	}
	 
	
}
