package com.woshidaniu.orm.ibatis2.executor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ibatis.sqlmap.engine.impl.ExtendedSqlMapClient;
import com.ibatis.sqlmap.engine.impl.SqlMapExecutorDelegate;
import com.ibatis.sqlmap.engine.mapping.statement.RowHandlerCallback;
import com.ibatis.sqlmap.engine.scope.RequestScope;
import com.woshidaniu.basemodel.BaseModel;
import com.woshidaniu.db.core.dialect.Dialect;
import com.woshidaniu.db.core.dialect.impl.MySql5Dialect;

/**
 * 
 * @package com.woshidaniu.orm.ibatis2.executor
 * @className: PaginationExecutor
 * @description: 通过重写SqlExecutor,实现数据范围过滤，物理分页
 * @author : kangzhidong
 * @date : 2014-5-14
 * @time : 下午03:00:44
 */
@SuppressWarnings("unused")
public class PaginationExecutor extends SqlExecutor {

	protected static Logger LOG = LoggerFactory.getLogger(PaginationExecutor.class);
	public String dialect;
	protected Dialect dialectObject = new MySql5Dialect();
	protected boolean enableLimit = true;
	protected boolean pagedResult = false;//是否 查询的结果是Page对象
	protected String pagedID = ".*Paged*"; // 分页Id,mapper.xml中需要拦截的ID(正则匹配)
	
	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
		if (this.getDialect() == null||this.getDialect().length()==0) {
			throw new RuntimeException("the value of the dialect property in com.fastkit.orm.ibatis2.executor.PaginationExecutor is not defined : "+ this.getDialect());
		}
		try {
			dialectObject = (dialectObject==null)?(Dialect) Class.forName(dialect).newInstance():dialectObject;
		} catch (InstantiationException e) {
			throw new RuntimeException(this.getDialect() + " is not defined !",e.getCause());
		} catch (IllegalAccessException e) {
			throw new RuntimeException(this.getDialect() + " is IllegalAccess !",e.getCause());
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(this.getDialect() + " is not found !",e.getCause());
		}
		
	}

	public boolean isEnableLimit() {
		return enableLimit;
	}

	public void setEnableLimit(boolean enableLimit) {
		this.enableLimit = enableLimit;
	}
	
	/**
	 * 
	 * @description: 重写SqlExecutor.executeQuery方法
	 * @author : kangzhidong
	 * @date : 2014-5-14
	 * @time : 下午03:00:28 
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
		// 拦截需要分页的SQL
		if (statementID.matches(getPagedID())) {
			if (this.isLimit(sql, skipResults, maxResults)) {
				// 有分页信息、可物理分页SQL
				SqlMapClient sqlMapClient = request.getSession().getSqlMapClient();
	        	if (sqlMapClient instanceof ExtendedSqlMapClient) {
					SqlMapExecutorDelegate delegate = ((ExtendedSqlMapClient) sqlMapClient).getDelegate();
	        		String countSQL = dialectObject.getCountSQL(sql);// 获得总数查询SQL
					sql = dialectObject.getLimitSQL(sql,skipResults, maxResults);// 获得物理分页SQL
					skipResults = NO_SKIPPED_RESULTS;// 设置skipResults为SqlExecutor不分页
					maxResults = NO_MAXIMUM_RESULTS;// 设置maxResults为SqlExecutor不分页
					//如果返回结果是一个Page对象
					if(isPagedResult()){
						//记录统计
						PreparedStatement countStmt = connection.prepareStatement(countSQL,ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
						//设置查询记录数的预编译过程
						request.getStatement().getParameterMap().setParameters(request,countStmt, parameters);
						//设置参数
						//setParameters(countStmt,request,parameters);
						//执行查询
						ResultSet rs = countStmt.executeQuery();
						int totalCount = 0;
						if (rs.next()) {
							totalCount = rs.getInt(1);
							((BaseModel)parameters[0]).getQueryModel().setTotalCount(totalCount);
						}
					}
	        	}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(" Pagination SQL : " + sql);
			}
		}else if (LOG.isDebugEnabled()) {
			LOG.debug(" Final SQL : " + sql);
		}
		// 使用不分页机制调用SqlExecutor查询方法
		super.executeQuery(request, connection, sql ,parameters, skipResults,maxResults, callback);
	}
	
	/**
	 * 是否允许执行分页
	 * 
	 * @param sql
	 * @param skipResults
	 * @param maxResults
	 * @return
	 */
	private boolean isLimit(String sql, int skipResults, int maxResults) {
		return (skipResults != NO_SKIPPED_RESULTS || maxResults != NO_MAXIMUM_RESULTS) && enableLimit && isSelect(sql);
	}

	/**
	 * 是否可物理分页SQL
	 * 
	 * @param sql
	 * @return
	 */
	private boolean isSelect(String sql) {
		if (sql.toLowerCase().indexOf("select") >= 0) {
			if (sql.toLowerCase().indexOf("rownum") >= 0) {
				return false;
			}
			return true;
		}
		return false;
	}

	public String getPagedID() {
		return pagedID;
	}

	public void setPagedID(String pagedID) {
		this.pagedID = pagedID;
	}

	public Dialect getDialectObject() {
		return dialectObject;
	}

	public boolean isPagedResult() {
		return pagedResult;
	}

	public void setPagedResult(boolean pagedResult) {
		this.pagedResult = pagedResult;
	}

	
}
