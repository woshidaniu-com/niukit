/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;

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
class SearchParserSqlBuilderV1 extends SearchParserSqlBuilder{
	
	private static final Logger log = LoggerFactory.getLogger(SearchParserSqlBuilderV1.class);
	
	public static void build(SearchModel searchModel) {
		
		JSONObject inputJson = JSONObject.fromObject(unescapeHtml(searchModel.getInputType()));
		JSONObject selectJson = JSONObject.fromObject(unescapeHtml(searchModel.getSelectType()));
		JSONObject dateJson = JSONObject.fromObject(unescapeHtml(searchModel.getDateType()));
		JSONObject numberJson = JSONObject.fromObject(unescapeHtml(searchModel.getNumberType()));
		InputSqlType inputSqlType= InputSqlType.getByKey(Integer.valueOf(searchModel.getInputSqlType()));
		
		Map<String,String> params = new HashMap<String, String>();
		
		String eqSql = getEqualSQL(selectJson, params, SQLType.MYBATIS);
		String likeSql = getInputSQL(inputJson, params, inputSqlType, SQLType.MYBATIS);
		String dateSql = getDateSQL(dateJson, params, SQLType.MYBATIS);
		String numberSql = getNumberSQL(numberJson, params, SQLType.MYBATIS);
		
		StringBuilder sql = new StringBuilder();
		if (!StringUtils.isNull(eqSql)){
			sql.append(eqSql);
		}
		
		if (!StringUtils.isNull(likeSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(likeSql);
		
		if (!StringUtils.isNull(dateSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(dateSql);
		
		if (!StringUtils.isNull(numberSql) && sql.length() > 0){
			sql.append(" and ");
		}
		sql.append(numberSql);
		String querySql = sql.toString();
		if(log.isDebugEnabled()) {
			log.debug("高级查询版本:{},releaseDate:{},构建文本查询sql:{}",Version.getVersion(),Version.getReleaseDate(),querySql);			
		}
		searchModel.setQuerySQL(querySql);
		searchModel.setParams(params);
	}
	/**
	 * 
	 * <p>方法说明：获取预编译SQL<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2017年3月9日下午3:09:36<p>
	 * @param model
	 * @return
	 */
	public static String getJdbcSQL(SearchModel model){
		build(model);
		String sql = model.getQuerySQL();
		return sql.replaceAll("#\\{[^\\}]*\\}", "?");
	}
	
	//like 语句拼写
	private static String getInputSQL(JSONObject inputJson,Map<String,String> params,InputSqlType inputSqlType, SQLType sqlType){
		if (inputJson.isEmpty()){
			return "";
		}
		
		Iterator<?> keys = inputJson.keys();
		StringBuilder sql = new StringBuilder();
		if (keys.hasNext()){
			while(keys.hasNext()) {
				if (sql.length() > 1){
					//不包含、不等于用 and ，包含、等于用or 
					switch (inputSqlType) {
						case NOT_LIKE:
							sql.append(" and ");
							break;
						case EQUAL:
							sql.append(" or ");
							break;
						case NOT_EQUAL:
							sql.append(" and ");
							break;
						default:
							sql.append(" or ");
							break;
					}
				
				}
			    String key = (String)keys.next();
			    JSONArray arr = (JSONArray) inputJson.get(key);
			    
			    switch (sqlType) {
					case MYBATIS:
						 sql.append(getMybatisInputSQL(key,arr,params,inputSqlType));
						break;
					case JDBC:
						
						break;
				}
			}
		}
		String sqlString = sql.toString();
		if(StringUtils.isNotEmpty(sqlString)) {
			return "(" + sqlString +")" ;
		}else {
			return "";
		}
	}

	//mybatis like sql 拼接
	private static Object getMybatisInputSQL(String key, JSONArray arr, Map<String, String> params, InputSqlType inputSqlType) {
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0 ; i < arr.size() ; i++){
			
			if (StringUtils.isNull(arr.getString(i).trim())) continue;
			
			String paramKey = String.format("%s_%s", key,i);
			
			switch (inputSqlType) {
				case NOT_LIKE:
					if (sb.length() > 0){
						sb.append(" and ");
					}
					sb.append("(").append(key).append(" is null").append(" or ").append(key).append(" not like concat('%',concat(#{searchModel.params.").append(paramKey).append("},'%'))").append(")");
					break;
				case EQUAL:
					if (sb.length() > 0){
						sb.append(" or ");
					}
					sb.append("(").append(key).append("=#{searchModel.params.").append(paramKey).append("}").append(")");
					break;
				case NOT_EQUAL:
					if (sb.length() > 0){
						sb.append(" and ");
					}
					sb.append("(").append(key).append(" is null ").append(" or ").append(key).append(" <> #{searchModel.params.").append(paramKey).append("}").append(")");
					break;
				default:
					if (sb.length() > 0){
						sb.append(" or ");
					}
					sb.append("(").append(key).append(" like concat('%',concat(#{searchModel.params.").append(paramKey).append("},'%'))").append(")");
					break;
			}
			params.put(paramKey, arr.getString(i));
		}
		String sqlString = sb.toString();
		if(StringUtils.isNotEmpty(sqlString)) {
			return "(" + sqlString +")" ;
		}else {
			return "";
		}
	}
}

