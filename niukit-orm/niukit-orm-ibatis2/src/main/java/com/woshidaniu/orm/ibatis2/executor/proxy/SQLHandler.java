package com.woshidaniu.orm.ibatis2.executor.proxy;
public interface SQLHandler {

	/**
	 * 处理sql语句
	 *
	 * @param sql ibatis生成的sql语句，其中参数用?号占位
	 * @param params sql对应的参数
	 * @return
	 * @throws Throwable
	 */
	String handle(String sql, Object[] params) throws Throwable;
	
}