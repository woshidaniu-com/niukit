package com.woshidaniu.basicutils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：BlankUtils 单元测试
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月7日下午3:35:26
 */
public class BlankUtilsTest {

	
	@Test
	public void testIsBlank(){
		/*字符串**/
		String str1 = "" , str2 = null , str3 = " " ;
		boolean result = BlankUtils.isBlank(str1);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(str2);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(str3);
		Assert.assertTrue(result);
		
		/*数组**/
		Object[] objArr1 = null ,objArr2 = new String[]{};
		result = BlankUtils.isBlank(objArr1);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(objArr2);
		Assert.assertTrue(result);
		
		/*集合*/
		Collection<?> c1 = new ArrayList<String>(),c2 = new HashSet<String>(),c3 = null;
		result = BlankUtils.isBlank(c1);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(c2);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(c3);
		Assert.assertTrue(result);
		
		Map<?,?> m1 = new HashMap<String,Object>() , m2 = null;
		result = BlankUtils.isBlank(m1);
		Assert.assertTrue(result);
		
		result = BlankUtils.isBlank(m2);
		Assert.assertTrue(result);
		
	}
}
