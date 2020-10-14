package com.woshidaniu.orm.mybatis.spring.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 
 *@类名称:MybatisSqlSessionDaoSupport.java
 *@类描述：数据访问对象(Data Access Object) 接口实现
 *<pre>
 *  SqlSessionTemplate
 *	SqlSessionTemplate是MyBatis-Spring的核心。这个类负责管理MyBatis的SqlSession,调用MyBatis的SQL方法。
 *	SqlSessionTemplate是线程安全的，可以被多个DAO所共享使用。
 *	当调用SQL方法时，包含从映射器getMapper()方法返回的方法，SqlSessionTemplate将会保证使用的SqlSession是和当前Spring的事务相关的。此外，它管理session的生命周期，包含必要的关闭，提交或回滚操作。
 *	SqlSessionTemplate实现了SqlSession，这就是说要对MyBatis的SqlSession进行简易替换。
 *	SqlSessionTemplate通常是被用来替代默认的MyBatis实现的DefaultSqlSession，因为它不能参与到Spring的事务中也不能被注入，因为它是线程不安全的。相同应用程序中两个类之间的转换可能会引起数据一致性的问题。
 *	SqlSessionTemplate对象可以使用SqlSessionFactory作为构造方法的参数来创建。
 *</pre>
 *@创建人：kangzhidong
 *@创建时间：2015-1-13 上午11:57:51
 *@版本号:v1.0
 */
public class MybatisSqlSessionDaoSupport extends SqlSessionDaoSupport implements MybatisDataSourceDao{
	
	public SqlSessionDaoSupport getDaoSupport(){
		return this;
	}
	
	public <T> int insert(String statementID) {
		return this.insert(statementID, new Object());
	}
	
	public <T> int insert(String statementID, T entity) {
		return getSqlSession().insert(statementID,entity);
	}
	
	public int insert(String statementID,Map<String, Object> params) {
		return getSqlSession().insert(statementID,params);
	}
	
	public <T> void insert(String statementID, Collection<T> entities) {
		//执行更新
		for (T entity : entities) {
			getSqlSession().insert(statementID,entity);
		}
	}
	
	public int delete(String statementID) {
		return this.delete(statementID, new Object());
	}
	
	public  <T> int delete(String statementID, T entity) {
		return getSqlSession().delete(statementID,entity);
	}
	
	public int delete(String statementID,Map<String, Object> params) {
		return getSqlSession().delete(statementID,params);
	}
	
	public  <T> void delete(String statementID, Collection<T> entities) {
		//执行更新
		for (T entity : entities) {
			getSqlSession().delete(statementID,entity);
		}
	}
	
	public int update(String statementID) {
		return this.update(statementID, new Object());
	}
	
	public <T> int update(String statementID, T entity) {
		return getSqlSession().delete(statementID,entity);
	}
	
	public int update(String statementID,Map<String, Object> params) {
		return getSqlSession().delete(statementID,params);
	}
	
	public <T>  void update(String statementID, Collection<T> entities) {
		//执行更新
		for (T entity : entities) {
			getSqlSession().update(statementID,entity);
		}
	}

	public <T> void select(String statement, Object parameter, ResultHandler<T> handler) {
		getSqlSession().select(statement, parameter , handler);
	}
	
	public <T> void select(String statement, ResultHandler<T> handler) {
		getSqlSession().select(statement, handler);
	}
	
	public <T> void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler<T> handler) {
		getSqlSession().select(statement, parameter , rowBounds , handler);
	}
	
	public <E> List<E> selectList(String statement) {
		return getSqlSession().selectList(statement);
	}
	
	public <E> List<E> selectList(String statement, Object parameter) {
		return getSqlSession().selectList(statement,parameter);
	}
	
	public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds) {
		return getSqlSession().selectList(statement,parameter,rowBounds);
	}

	public <K, V> Map<K, V> selectMap(String statement, String mapKey) {
		return getSqlSession().selectMap(statement, mapKey);
	}
	
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey) {
		return getSqlSession().selectMap(statement, parameter, mapKey);
	}
	
	public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return getSqlSession().selectMap(statement, parameter, mapKey , rowBounds);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T selectOne(String statement) {
		return (T)getSqlSession().selectOne(statement);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T selectOne(String statement, Object parameter) {
		return (T)getSqlSession().selectOne(statement, parameter);
	}
	
}