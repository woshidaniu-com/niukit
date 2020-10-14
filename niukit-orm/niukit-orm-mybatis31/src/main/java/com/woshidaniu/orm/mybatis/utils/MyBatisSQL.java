package com.woshidaniu.orm.mybatis.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @className	： MyBatisSql
 * @description	： TODO(描述这个类的作用)
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 4:46:53 PM
 * @version 	V1.0
 */
public class MyBatisSQL {
	
	/** * 预编译后的 sql 有 ? 号 */
	private String preSQL;
	/** * 运行期 sql */
	private String runSQL;
	/** * 参数 数组 */
	private Object[] parameters;
	
	public String getPreSQL() {
		return preSQL;
	}

	public void setPreSQL(String preSQL) {
		this.preSQL = preSQL;
	}

	public void setRunSQL(String runSQL) {
		this.runSQL = runSQL;
	}
	
	public String getRunSQL() {
		return runSQL;
		//return null!= sql? sql.replaceAll("\r|\n", "").replaceAll("\\s*","#").replaceAll("##"," ").replaceAll("#","") :"";
	}

	public void setParameters(Object[] parameters) {
		this.parameters = parameters;
	}

	public Object[] getParameters() {
		return parameters;
	}

	@Override
	public String toString() {
		if (parameters == null || runSQL == null) {
			return "";
		}
		List<Object> parametersArray = Arrays.asList(parameters);
		List<Object> list = new ArrayList<Object>(parametersArray);
		while (runSQL.indexOf(" ") != -1 && list.size() > 0 && parameters.length > 0) {
			runSQL = runSQL.replaceFirst("\\ ", list.get(0).toString());
			list.remove(0);
		}
		return runSQL.replaceAll("(\r \n(\\s*\r \n)+)", "\r\n");
	}

	public static void main(String[] args) {
		System.out.println("select t.xh,t.xsjbxxb_id,t.xqdmb_id,t.ssxy_id,t.zyfxdmb_id,".replaceAll("(\r \n(\\s*\r \n)+)", "\r\n").replaceAll(" +",""));
	}
}
