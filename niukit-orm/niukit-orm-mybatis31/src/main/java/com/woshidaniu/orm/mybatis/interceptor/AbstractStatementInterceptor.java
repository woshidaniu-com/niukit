package com.woshidaniu.orm.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.MetaObject;

/**
 * 
 * @className	： AbstractStatementInterceptor
 * @description	： 通过拦截<code>StatementHandler</code>的<code>prepare</code>方法，
 *                重写mybatis的SQL语句，实现 物理分页,数据范围,动态SQL,日志记录等处理逻辑
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 4:01:23 PM
 * @version 	V1.0
 */
public abstract class AbstractStatementInterceptor extends AbstractInterceptor {

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public Object doPrepareIntercept(Invocation invocation,
			StatementHandler statementHandler, MetaObject metaStatementHandler)
			throws Throwable {
		return null;
	}

	@Override
	public Object doExecutorIntercept(Invocation invocation,
			Executor executorProxy, MetaObject metaExecutor) throws Throwable {

		return null;
	}

	@Override
	public Object doResultSetIntercept(Invocation invocation,
			ResultSetHandler resultSetHandler) throws Throwable {
		return null;
	}


}
