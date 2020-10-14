/**
 * <p>Copyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.search.core;

/**
 * @author weiguangyue
 * 	v2版本整理，适配v1版本
 */
enum MultipleInputType {

	//*****************************************需要文本输入************************************************
	
	LIKE(0),			//包含
	NOT_LIKE(1),		//不包含
	EQUAL(2),			//等于
	NOT_EQUAL(3),		//不等于
	IN(4),				//属于
	NOT_IN(5),			//不属于
	GE(6),				//大于等于
	LE(7),				//小于等于
	
	//*****************************************不需要文本输入************************************************
	
	IS_NULL(8),		//为空
	IS_NOT_NULL(9);	//不为空
	
	private int key;
	
	MultipleInputType(int key){
		this.key = key;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}
	
	public static MultipleInputType getByKey(int key){
		
		for (MultipleInputType sqlType : MultipleInputType.values()){
			if (sqlType.getKey() ==  key) return sqlType;
		}
		return null;
	}
	
}
