package com.woshidaniu.orm.ibatis2.executor;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.statement.MappedStatement;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.mapping.statement.StatementType;
import com.ibatis.sqlmap.engine.scope.RequestScope;
import com.woshidaniu.db.core.builder.AnnotationSQLBuilder;
import com.woshidaniu.db.core.builder.impl.MybatisDynamicSQLBuilder;

/**
 * 
 * @className: AccessablePaginationExecutor
 * @description: 通过重写SqlExecutor,实现数据范围过滤，物理分页
 * @author : kangzhidong
 * @date : 下午2:11:39 2013-10-14
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class DynamicSQLDataRangeExecutor extends PaginationExecutor {

	protected static Logger LOG = LoggerFactory.getLogger(DynamicSQLDataRangeExecutor.class);
	private AnnotationSQLBuilder builder = new MybatisDynamicSQLBuilder();
	protected String dynamicID = ".*Dynamic*"; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	
	/**
	 * 
	 * @description: 重写SqlExecutor.executeQuery方法
	 * @author : kangzhidong
	 * @date : 2014-5-14
	 * @time : 下午02:59:32 
	 * @param request
	 * @param connection
	 * @param sql
	 * @param parameters
	 * @param skipResults
	 * @param maxResults
	 * @param callback
	 * @throws SQLException
	 */
	@Override
	public void executeQuery(RequestScope request, Connection connection, String sql,Object[] parameters, int skipResults, int maxResults,
			RowHandlerCallback callback) throws SQLException {
		String statementID = request.getStatement().getId();
		//拦截需要限制数据范围的SQL语句
        if(statementID.matches(this.getDynamicID())){ 
        	SqlMapClient sqlMapClient = request.getSession().getSqlMapClient();
        	if (sqlMapClient instanceof ExtendedSqlMapClient) {
        		SqlMapExecutorDelegate delegate = ((ExtendedSqlMapClient) sqlMapClient).getDelegate();
        		MappedStatement mappedStatement = delegate.getMappedStatement(statementID);
        		 // 根据xml的配置类型生成相应类型的sql语句
                if(StatementType.INSERT == mappedStatement.getStatementType()){
                	sql = builder.buildInsertSQL(parameters[0]);
                }else if(StatementType.DELETE == mappedStatement.getStatementType()){
                	sql = builder.buildUpdateSQL(parameters[0]);
                }else if(StatementType.UPDATE == mappedStatement.getStatementType()){
                	sql = builder.buildDeleteSQL(parameters[0]);
                }else if(StatementType.SELECT == mappedStatement.getStatementType()){
                	sql = builder.buildQuerySQL(parameters[0]);
                }
				if (LOG.isDebugEnabled()) {
					LOG.debug("Auto Generated SQL:" + sql);
				}
        	}
		}
		// 使用不分页机制调用SqlExecutor查询方法
		super.executeQuery(request, connection, sql ,parameters, skipResults,maxResults, callback);
	}

	public String getDynamicID() {
		return dynamicID;
	}

	public void setDynamicID(String dynamicID) {
		this.dynamicID = dynamicID;
	}
	
}
