/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.searchTaglibTest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.woshidaniu.search.condition.DateColumnCondition;

public class DateColumnConditionTest {

	@Test
	public void test() {
		
		Map<String, String> params = new HashMap<String, String>();
		
		DateColumnCondition dateColumnCondition = new DateColumnCondition("searchModel.params");
		dateColumnCondition.addPair("birthDay", "2018-1-1", "2018-2-2", "YYYY-MM-DD");
		dateColumnCondition.addPair("deadDay", "2017-1-1", "2017-2-2", "YYYY-MM-DD");
		dateColumnCondition.addPair("deadDay", "2018-1-1", "2018-2-2", "YYYY-MM-DD");
		String sql = dateColumnCondition.getSql(params);
		System.out.println(sql);
		System.out.println("...");
		System.out.println(params);
	}
}
