package com.woshidaniu.orm.ibatis2.executor.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.ibatis.sqlmap.engine.mapping.sql.Sql;
import com.ibatis.sqlmap.engine.scope.RequestScope;

public final class AccessablePaginationProxyHandler implements InvocationHandler {

	private static Log log = LogFactory.getLog(AccessablePaginationProxyHandler.class);
	private Sql sql;
	private ThreadLocal<SQLHandler> sqlHandler = new ThreadLocal<SQLHandler>();

	public AccessablePaginationProxyHandler(Sql sql, SQLHandler handler) {
		this.sql = sql;
		this.setSqlHandler(handler);
	}

	public Sql getSql() {
		return sql;
	}

	public void setSqlHandler(SQLHandler handler) {
		this.sqlHandler.set(handler);
	}

	public SQLHandler getSqlHandler() {
		return sqlHandler.get();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Object result = method.invoke(getSql(), args);
		if ("getSql".equals(method.getName()) && getSqlHandler() != null) {
			log.debug("原SQL： " + result);
			RequestScope statementScope = (RequestScope) args[0];
			Object[] params = statementScope.getParameterMap().getParameterObjectValues(statementScope, args[1]);
			result = getSqlHandler().handle((String) result, params);
			log.debug("处理后： " + result);
			//执行完成后清除线程局部变量，下次调用需要设置新值，否则不拦截getSql方法
			setSqlHandler(null);
		}
		return result;
	}
}
