package com.woshidaniu.basicutils;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

/**
 * 
 *@类名称	: BlankUtil.java
 *@类描述	：空对象、空字符串判断工具类
 *@创建人	：kangzhidong
 *@创建时间	：Dec 30, 2015 9:49:26 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class BlankUtils {

	/**
	 * 
	 * @param str
	 * @return
	 * @description：   判断字符串是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:16:35
	 */
	public static boolean isBlank(final String str) {
		return str == null || str.trim().isEmpty();
	}
	
	
	
	public static boolean isBlank(final BigDecimal str) {
		return (str == null);
	}
	
	
	
	/**
	 * 
	 * <p>方法说明：判断数组是否为空<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月7日下午2:35:00<p>
	 * @param arr Object[]
	 * @return boolean
	 */
	public static boolean isBlank(final Object... arr) {
		return arr == null || arr.length == 0;
	}
	

	/**
	 * 
	 * @param cha
	 * @return
	 * @description：   判断字符是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:16:42
	 */
	public static boolean isBlank(final Character cha) {
		return (cha == null) || cha.equals(' ');
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断对象是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:16:50
	 */
	public static boolean isBlank(final Object obj) {
		return !(obj != null && !"null".equalsIgnoreCase(String.valueOf(obj)));
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断对象是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:16:50
	 */
	public static boolean isBlank(final Properties properties) {
		return (properties == null||properties.isEmpty());
	}
	


	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断Collectionj是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:05
	 */
	public static <E> boolean isBlank(final Collection<E> obj) {
		return obj == null || obj.isEmpty();
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断AbstractList是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:05
	 */
	public static <E> boolean isBlank(final Vector<E> obj) {
		return (obj == null) || (obj.size() <= 0);
	}
	
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断Set是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:14
	 */
	public static <E> boolean isBlank(final Set<E> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断Serializable是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:21
	 */
	public static boolean isBlank(final Serializable obj) {
		return obj == null;
	}

	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断Map是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:29
	 */
	public static <K,V> boolean isBlank(final Hashtable<K,V> obj) {
		return (obj == null) || obj.isEmpty();
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @description：   判断Map是否为空
	 * @return: boolean
	 * @method: isBlank
	 * @author: kangzhidong
	 * @version: 2010-12-15 下午09:17:29
	 */
	public static <K,V> boolean isBlank(final Map<K,V> obj) {
		return obj == null || obj.isEmpty();
	}
	

}
