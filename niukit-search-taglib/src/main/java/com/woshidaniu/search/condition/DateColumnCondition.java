/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.woshidaniu.basicutils.StringUtils;

/**
 * @description	： 日期列条件
 * @author 		：康康（1571）
 */
public class DateColumnCondition {
	
	private Map<String/*columnName*/,List<Pair>/*pairs*/> map = new HashMap<String,List<Pair>>();
	
	private String mybatisParamName;
	
	public DateColumnCondition(String mybatisParamName) {
		super();
		this.mybatisParamName = mybatisParamName;
	}

	/**
	 * @description	： pair
	 * @author 		：康康（1571）
	 */
	private static class Pair{
		
		String begin;
		String end;
		String format;
		
		public Pair(String begin, String end,String format) {
			super();
			this.begin = begin;
			this.end = end;
			this.format = format;
		}
	}
	
	public void addPair(String columnName,String begin,String end,String format) {
		List<Pair> list = map.get(columnName);
		if(list == null) {
			list = new ArrayList<Pair>();
			map.put(columnName, list);
		}
		list.add(new Pair(begin, end,format));
	}

	public String getSql(final Map<String, String> params) {

		if(map.isEmpty()) {
			throw new IllegalStateException("没有添加任何键值对");
		}
		StringBuilder sqlBuilder = new StringBuilder();
		Iterator<Entry<String, List<Pair>>> it = map.entrySet().iterator();
		while(it.hasNext()) {
			//多个列的日期，使用and
			if (sqlBuilder.length() > 0){
				sqlBuilder.append(" and ");
			}
			
			Entry<String, List<Pair>> e = it.next();
			String column = e.getKey();
			List<Pair> paris = e.getValue();
			String oneColumnDateSql = this.getOneColumnDateSQL(column, paris, params);
			boolean needWrapped = paris.size() > 1;
			if(needWrapped) {
				sqlBuilder.append(" ( ").append(oneColumnDateSql).append(" ) ");				
			}else {
				sqlBuilder.append(oneColumnDateSql);
			}
		}
		String sql = sqlBuilder.toString(); 
		return sql;
	}
	
	private String getOneColumnDateSQL(String column, List<Pair> pairs, Map<String, String> params) {
		
		StringBuilder sb = new StringBuilder();
		
		for (int i = 0;i<pairs.size();i++){
			
			Pair pair = pairs.get(i);
			
			if (sb.length() > 0){
				sb.append(" or ");
			}
			
			String format = pair.format.replace("hh:mm:ss", "hh24:mi:ss").replace("HH:mm:ss", "hh24:mi:ss");
			
			//对数据库字段key进行字符串截取并设置对应的格式
			StringBuilder applyKeyFormatSql = new StringBuilder();
			if(format.equals("YYYY")) {
				applyKeyFormatSql.append("to_date(substr(").append(column).append(",1,4),'YYYY')");
			}else if(format.equals("YYYY-MM")) {
				applyKeyFormatSql.append("to_date(substr(").append(column).append(",1,7),'YYYY-MM')");
			}else if(format.equals("YYYY-MM-DD")) {
				applyKeyFormatSql.append("to_date(substr(").append(column).append(",1,10),'YYYY-MM-DD')");
			}else {
				applyKeyFormatSql.append("to_date(").append(column).append(",'").append(format).append("')");
			}
			
			String beginKey = String.format("%s_begin_%s", column, i);
			String endKey = String.format("%s_end_%s", column, i);
			
			if (!StringUtils.isNull(pair.begin) && !StringUtils.isNull(pair.end)){
				sb.append("(").append(applyKeyFormatSql.toString())
				   .append(" between to_date(#{").append(this.mybatisParamName).append(".").append(beginKey).append("},'").append(format).append("')")
				   .append(" and to_date(#{").append(this.mybatisParamName).append(".").append(endKey).append("},'").append(format).append("'))");
				params.put(beginKey, pair.begin);
				params.put(endKey, pair.end);
			} else if (!StringUtils.isNull(pair.begin)){
				sb.append("(").append(applyKeyFormatSql.toString())
				   .append(" >= to_date(#{").append(this.mybatisParamName).append(".").append(beginKey).append("},'").append(format).append("'))");
				params.put(beginKey, pair.begin);
			} else if (!StringUtils.isNull(pair.end)){
				sb.append("(").append(applyKeyFormatSql.toString())
				   .append(" <= to_date(#{").append(this.mybatisParamName).append(".").append(endKey).append("},'").append(format).append("'))");
				params.put(endKey, pair.end);
			} 
		}
		
		return sb.toString();
	}
}
