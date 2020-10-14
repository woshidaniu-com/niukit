/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.orm.mybatis.spring.support;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * 
 * @类名称:MybatisDataSourceDao.java
 * @类描述：数据访问对象(Data Access Object) 接口
 * @创建人：kangzhidong
 * @创建时间：2015-1-13 上午11:54:56
 * @版本号:v1.0
 */
public interface MybatisDataSourceDao {

	public abstract SqlSessionDaoSupport getDaoSupport();

	/**
	 * Execute an insert statement.
	 * 
	 * @param statement Unique identifier matching the statement to execute.
	 * @return int The number of rows affected by the insert.
	 */
	public <T> int insert(String statementID);

	/**
	 * 
	 * @描述： 增加一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:37:25
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entity
	 * @return
	 */
	public abstract <T> int insert(String statementID, T entity);

	/**
	 * 
	 * @描述：增加一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:37:46
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param statementID
	 * @param params
	 * @return
	 */
	public abstract int insert(String statementID, Map<String, Object> params);

	/**
	 * 
	 * @描述：批量增加多条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:37:53
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entities
	 */
	public abstract <T> void insert(String statementID, Collection<T> entities);

	/**
	 * 
	 * @描述：删除一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:40:23
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param statementID
	 * @return
	 */
	public abstract int delete(String statementID);

	/**
	 * @描述：删除一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:40:33
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entity
	 * @return
	 */
	public abstract <T> int delete(String statementID, T entity);

	/**
	 * 
	 * @描述：删除一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:40:46
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param statementID
	 * @param params
	 * @return
	 */
	public abstract int delete(String statementID, Map<String, Object> params);

	/**
	 * 
	 * @描述：批量删除多条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:38:16
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entities
	 */
	public abstract <T> void delete(String statementID, Collection<T> entities);

	/**
	 * 
	 * @描述：更新一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:38:48
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param statementID
	 * @return
	 */
	public abstract int update(String statementID);

	/**
	 * 
	 * @描述：更新一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:39:00
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entity
	 * @return
	 */
	public abstract <T> int update(String statementID, T entity);

	/**
	 * 
	 * @描述：更新一条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:39:05
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param statementID
	 * @param params
	 * @return
	 */
	public abstract int update(String statementID, Map<String, Object> params);

	/**
	 * 
	 * @描述：批量修改多条记录
	 * @创建人:kangzhidong
	 * @创建时间:2014-10-8上午10:38:23
	 * @修改人:
	 * @修改时间:
	 * @修改描述:
	 * @param <T>
	 * @param statementID
	 * @param entities
	 */
	public abstract <T> void update(String statementID, Collection<T> entities);

	/**
	 * Retrieve a single row mapped from the statement key
	 * 
	 * @param <T>
	 *            the returned object type
	 * @param statement
	 * @return Mapped object
	 */
	<T> T selectOne(String statement);

	/**
	 * Retrieve a single row mapped from the statement key and parameter.
	 * 
	 * @param <T>
	 *            the returned object type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return Mapped object
	 */
	<T> T selectOne(String statement, Object parameter);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement, Object parameter);

	/**
	 * Retrieve a list of mapped objects from the statement key and parameter,
	 * within the specified row bounds.
	 * 
	 * @param <E>
	 *            the returned list element type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param rowBounds
	 *            Bounds to limit object retrieval
	 * @return List of mapped object
	 */
	<E> List<E> selectList(String statement, Object parameter,
			RowBounds rowBounds);

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects. Eg. Return a of Map[Integer,Author] for
	 * selectMap("selectAuthors","id")
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @return Map containing key pair data.
	 */
	<K, V> Map<K, V> selectMap(String statement, String mapKey);

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects.
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @return Map containing key pair data.
	 */
	<K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey);

	/**
	 * The selectMap is a special case in that it is designed to convert a list
	 * of results into a Map based on one of the properties in the resulting
	 * objects.
	 * 
	 * @param <K>
	 *            the returned Map keys type
	 * @param <V>
	 *            the returned Map values type
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param mapKey
	 *            The property to use as key for each value in the list.
	 * @param rowBounds
	 *            Bounds to limit object retrieval
	 * @return Map containing key pair data.
	 */
	<K, V> Map<K, V> selectMap(String statement, Object parameter,
			String mapKey, RowBounds rowBounds);

	/**
	 * Retrieve a single row mapped from the statement key and parameter using a
	 * {@code ResultHandler}.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param parameter
	 *            A parameter object to pass to the statement.
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	<T> void select(String statement, Object parameter, ResultHandler<T> handler);

	/**
	 * Retrieve a single row mapped from the statement using a
	 * {@code ResultHandler}.
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	<T> void select(String statement, ResultHandler<T> handler);

	/**
	 * Retrieve a single row mapped from the statement key and parameter using a
	 * {@code ResultHandler} and {@code RowBounds}
	 * 
	 * @param statement
	 *            Unique identifier matching the statement to use.
	 * @param rowBounds
	 *            RowBound instance to limit the query results
	 * @param handler
	 *            ResultHandler that will handle each retrieved row
	 * @return Mapped object
	 */
	<T> void select(String statement, Object parameter, RowBounds rowBounds,
			ResultHandler<T> handler);

}
