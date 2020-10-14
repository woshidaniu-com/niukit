package com.woshidaniu.orm.mybatis.interceptor;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.Invocation;

import com.woshidaniu.orm.mybatis.interceptor.meta.MetaExecutor;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaParameterHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaResultSetHandler;
import com.woshidaniu.orm.mybatis.interceptor.meta.MetaStatementHandler;

/**
 * 
 *@类名称	: AbstractInterceptorAdapter.java
 *@类描述	： Mybatis拦截器插件适配器: 执行顺序是: doExecutorIntercept，doParameterIntercept，doStatementIntercept，doResultSetIntercept
 *@创建人	：kangzhidong
 *@创建时间	：Nov 3, 2016 12:13:47 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class AbstractInterceptorAdapter extends AbstractInterceptor {

	protected boolean isRequireIntercept(Invocation invocation,Executor executorProxy, MetaExecutor metaExecutor) {
		return true;
	}
	
	protected boolean isRequireIntercept(Invocation invocation, ParameterHandler parameterHandler, MetaParameterHandler metaParameterHandler) {
		return true;
	}
	
	protected boolean isRequireIntercept(Invocation invocation,StatementHandler statementHandler, MetaStatementHandler metaStatementHandler) {
		return true;
	}
	
	protected boolean isRequireIntercept(Invocation invocation,ResultSetHandler resultSetHandler,MetaResultSetHandler metaResultSetHandler) {
		return true;
	}
	
	@Override
	public Object doExecutorIntercept(Invocation invocation,Executor executorProxy, MetaExecutor metaExecutor) throws Throwable {
		if (isRequireIntercept(invocation, executorProxy, metaExecutor)) {
			//do some things
		}
		return invocation.proceed();
	}
	
	@Override
	public Object doParameterIntercept(Invocation invocation, ParameterHandler parameterHandler, MetaParameterHandler metaParameterHandler) throws Throwable {
		if (isRequireIntercept(invocation, parameterHandler, metaParameterHandler)) {
			//do some things
		}
		return invocation.proceed();
	}
	
	@Override
	public Object doStatementIntercept(Invocation invocation,StatementHandler statementHandler, MetaStatementHandler metaStatementHandler) throws Throwable {
		if (isRequireIntercept(invocation, statementHandler, metaStatementHandler)) {
			//do some things
		}
		return invocation.proceed();
	}

	@Override
	public Object doResultSetIntercept(Invocation invocation,ResultSetHandler resultSetHandler,MetaResultSetHandler metaResultSetHandler) throws Throwable {
		if (isRequireIntercept(invocation, resultSetHandler, metaResultSetHandler)) {
			//do some things
		}
		return invocation.proceed();
	}
	
	@Override
	public void doDestroyIntercept(Invocation invocation) throws Throwable{
		
	}

}
