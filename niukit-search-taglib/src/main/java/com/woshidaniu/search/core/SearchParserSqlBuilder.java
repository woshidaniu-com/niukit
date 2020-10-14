/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.util.Iterator;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.search.condition.DateColumnCondition;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * 高级查询SQL解析器
 * @author Penghui.Qu
 * 	v1版本基础代码
 * @author zhangxiaobin
 *  v2版本基础代码
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 */
class SearchParserSqlBuilder {
	
	private static final Logger log = LoggerFactory.getLogger(SearchParserSqlBuilder.class);
	
	enum SQLType{
		MYBATIS,
		JDBC
	};
	
	protected static String unescapeHtml(String input) {
		try {
			return StringEscapeUtils.unescapeHtml4(input);
		} catch (Exception e) {
			log.error("",e);
			return input;
		}
	}

	protected static String getNumberSQL(JSONObject numberJson, Map<String, String> params, SQLType sqlType) {
		if (numberJson.isEmpty()){
			return "";
		}
		
		Iterator<?> keys = numberJson.keys();
		StringBuilder sql = new StringBuilder();
		if (keys.hasNext()){
			sql.append("(");
			while(keys.hasNext()) {
				if (sql.length() > 1){
					sql.append(" or ");
				}
			    String key = (String)keys.next();
			    
			    JSONArray arr = (JSONArray) numberJson.get(key);
			    sql.append(getNumberSQL(key,arr,params));
			}
			sql.append(")");
		}
		
		if (sql.length() == 2){
			return "";
		}
		return sql.toString();
	}

	protected static String getDateSQL(JSONObject dateJson,final Map<String, String> params, SQLType sqlType) {
		if (dateJson.isEmpty()) {
			return "";
		}
		if(sqlType.equals(SQLType.JDBC)) {
			return "";
		}
		DateColumnCondition condition = new DateColumnCondition("searchModel.params");
		
		Iterator<?> keys = dateJson.keys();
		while (keys.hasNext()) {

			String column = (String) keys.next();

			JSONArray jsonArr = (JSONArray) dateJson.get(column);
			for (int i = 0 ; i < jsonArr.size() ; i++){
				
				JSONArray arr = jsonArr.getJSONArray(i);
				String format = arr.getString(0).replace("hh:mm:ss", "hh24:mi:ss").replace("HH:mm:ss", "hh24:mi:ss");
				String begin = arr.getString(1);
				String end = arr.getString(2);
				condition.addPair(column, begin, end, format);
			}
		}
		return condition.getSql(params);
	}
	
	//数字区间
	protected static Object getNumberSQL(String key, JSONArray jsonArr, Map<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0 , j = jsonArr.size() ; i < j ; i++){
			
			if (sb.length() > 1){
				sb.append(" or ");
			}
			
			JSONArray arr = jsonArr.getJSONArray(i);
			//String format = arr.getString(0);
			String begin = arr.getString(0);
			String end = arr.getString(1);
			
			String beginKey = String.format("%s_begin_%s", key, i);
			String endKey = String.format("%s_end_%s", key, i);
			
			if (!StringUtils.isNull(begin) && !StringUtils.isNull(end)){
				sb.append("(").append(key)
				   .append(" between #{searchModel.params.").append(beginKey).append("}")
				   .append(" and #{searchModel.params.").append(endKey).append("})");
				params.put(beginKey, begin);
				params.put(endKey, end);
			} else if (!StringUtils.isNull(begin)){
				sb.append(key).append(" >= #{searchModel.params.").append(beginKey).append("}");
				params.put(beginKey, begin);
			} else if (!StringUtils.isNull(end)){
				sb.append(key).append(" <= #{searchModel.params.").append(endKey).append("}");
				params.put(endKey, end);
			} 
		}
		
		return sb.toString();
	}

	//equal sql
	protected static String getEqualSQL(JSONObject selectJson, Map<String, String> params, SQLType sqlType) {
		if (selectJson.isEmpty()){
			return "";
		}
		Iterator<?> keys = selectJson.keys();
		StringBuilder sql = new StringBuilder();
		if (keys.hasNext()){
			sql.append("(");
			while (keys.hasNext()) {
				if (sql.length() > 1) {
					sql.append(" and ");
				}
				String key = (String) keys.next();
				JSONArray arr = (JSONArray) selectJson.get(key);
	
				switch (sqlType) {
				case MYBATIS:
					sql.append(getMybatisEqualSQL(key, arr, params));
					break;
				case JDBC:
	
					break;
				}
			}
			sql.append(")");
		}
		return sql.toString();
	}
	
	//mybatis equal sql
	protected static Object getMybatisEqualSQL(String key, JSONArray arr, Map<String, String> params) {
		StringBuilder sb = new StringBuilder("(");
		for (int i = 0 ; i < arr.size() ; i++){
			
			if (sb.length() > 1){
				sb.append(" or ");
			}
			
			if (StringUtils.isNull(arr.getString(i).trim())) {
				sb.append(key).append(" is null ");
			} else {
				String paramKey = String.format("%s_%s", key,i);
				sb.append(key).append("=#{searchModel.params.").append(paramKey).append("}");
				params.put(paramKey, arr.getString(i));
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
