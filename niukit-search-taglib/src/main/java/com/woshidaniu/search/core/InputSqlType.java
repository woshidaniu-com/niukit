/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

/**
 * @author  Penghui.Qu
 * 高级查询-输入框搜索类型
 * @author weiguangyue
 * 未来准备移除此类
 */
@Deprecated
public enum InputSqlType {

	LIKE(0),
	NOT_LIKE(1),
	EQUAL(2),
	NOT_EQUAL(3);
	
	private int key;
	
	InputSqlType(int key){
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public static InputSqlType getByKey(int key){
		
		for (InputSqlType sqlType : InputSqlType.values()){
			if (sqlType.getKey() ==  key) return sqlType;
		}
		return null;
	}
	
}
