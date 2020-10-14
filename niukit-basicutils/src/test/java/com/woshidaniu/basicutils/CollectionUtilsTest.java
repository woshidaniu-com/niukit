package com.woshidaniu.basicutils;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：CollectionUtil 单元测试
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月7日下午7:03:07
 */
public class CollectionUtilsTest {

	@Test
	public void testSplitList(){
		
		List<String> list = new ArrayList<String>();
		for (int i = 0 ; i < 9 ; i ++){
			list.add("str"+i);
		}
		
		List<List<?>> rList = CollectionUtils.splitList(list, 3);
		
		for (List<?> l : rList){
			Assert.assertTrue(l.size() == 3);
		}
	}
	
	@Test
	public void testRemoveDupValue(){
		/*基本数据类型*/
		List<Integer> list1 = new ArrayList<Integer>();
		list1.add(1);
		list1.add(1);
		list1.add(2);
		list1.add(2);
		list1.add(3);
		
		List<Integer> rList = CollectionUtils.removeDupValue(list1);
		Assert.assertTrue(rList.size() == 3);
		
		/*字符串*/
		List<String> list2 = new ArrayList<String>();
		list2.add("c");
		list2.add("c");
		list2.add("a");
		list2.add("a");
		list2.add("b");
		
		List<String> rList2 = CollectionUtils.removeDupValue(list2);
		Assert.assertTrue(rList2.size() == 3);
		
		/*对象*/
		List<Object> list3 = new ArrayList<Object>();
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		
		list3.add(o1);
		list3.add(o1);
		list3.add(o2);
		list3.add(o2);
		list3.add(o3);
		
		List<Object> rList3 = CollectionUtils.removeDupValue(list3);
		Assert.assertTrue(rList3.size() == 3);
	}
}
