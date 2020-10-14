package com.woshidaniu.component.bpm.common;

import java.util.List;
import java.util.Map;

/**
 * 类名称：通用Service接口
 * 创建时间:2012-5-3
 * 修改人：ZhenFei.Cao
 * 修改时间：2012-8-2
 */
public interface BaseService<T> {
	
	
	/**
	 * 增加记录
	 * @param t
	 * @return
	 */
	public boolean insert(T t);
	
	/**
	 * 修改记录
	 * @param t
	 * @return
	 */
	public boolean update(T t);
	
	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
	public T getModel(String id);
	
	/**
	 * 查询单条数据
	 * @param t
	 * @return
	 */
	public T getModel(T t);
	
	/**
	 * 批量删除
	 * @param map
	 * @return
	 */
	public boolean batchDelete(Map<String,Object> map);
	
	
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	public boolean batchDelete(List<?> list);
	
	/**
	 * 批量修改
	 * @param map
	 * @return
	 */
	public boolean batchUpdate(Map<String,Object> map);
	
	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
	public List<T> getPagedList(T t);
	
	
	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
	public List<T> getModelList(T t);
	
	
	/**
	 * 无分页查询<br>
	 * <p>
	 * MyBatis中对重载方法支持缺陷：XML中使用该方法映射无<br>
	 * 参和一个string参数会无法映射，建议XML中仅映射此方法一次，<br>
	 * 若有其它类似业务在自己接口中定义其它方法。<br>
	 * </p>
	 * @param str
	 * @return
	 */
	public List<T> getModelList(String... str);
	
	/**
	 * 统计记录数
	 * @param t
	 * @return
	 */
	public int getCount(T t);
	
	/**
	 * 按数据范围分页查询
	 * @param t
	 * @return
	 */
	public List<T> getPagedByScope(T t);
	
	/**
	 * 按数据范围无分页查询
	 * @param t
	 * @return
	 */
	public List<T> getModelListByScope(T t);
	
	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
	public List<T> getList(T t);
}
