package com.woshidaniu.component.bpm.common;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;


/**
 * 通用Service实现<br>
 * 创建时间：2012-5-3<br>
 * 说明：daoBase注入方式
 * 修改人：ZhenFei.Cao
 * 修改时间：2012-08-02
 * @param <T>
 *            model
 * @param <E>
 *            dao
 * 
 */
public class BaseServiceImpl<T, E extends BaseDao<T>> implements BaseService<T>,InitializingBean {

	protected static Logger log = LoggerFactory.getLogger(BaseServiceImpl.class);
	
	protected E dao;

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}
	
	
	/**
	 * 用于spring注入
	 * 
	 * @param dao
	 */
	public void setDao(E dao) {
		this.dao = dao;
	}
	
	/**
	 * 增加记录
	 * @param t
	 * @return
	 */
	public boolean insert(T t){
		int result = dao.insert(t);
		return result > 0;
	}

	/**
	 * 修改记录
	 * @param t
	 * @return
	 */
	public boolean update(T t){
		int result = dao.update(t);
		return result > 0;
	}

	/**
	 * 查询单条数据
	 * @param id
	 * @return
	 */
	public T getModel(String id){
		return dao.getModel(id);
	}

	/**
	 * 查询单条数据
	 * @param t
	 * @return
	 */	
	public T getModel(T t){
		return dao.getModel(t);
	}

	/**
	 * 批量删除
	 * @param map
	 * @return
	 */	
	public boolean batchDelete(Map<String, Object> map){
		int result = dao.batchDelete(map);
		return result > 0;
	}
	
	/**
	 * 批量删除
	 * @param list
	 * @return
	 */
	public boolean batchDelete(List<?> list){
		int result = dao.batchDelete(list);
		return result > 0;
	}

	/**
	 * 批量删除
	 * @param map
	 * @return
	 */
	public boolean batchUpdate(Map<String, Object> map){
		int result = dao.batchUpdate(map);
		return result > 0;
	}
	
	/**
	 * 分页查询
	 * @param t
	 * @return
	 */
	public List<T> getPagedList(T t){
		return dao.getPagedList(t);
	}

	/**
	 * 无分页查询
	 * @param t
	 * @return
	 */
	public List<T> getModelList(T t){

		return dao.getModelList(t);
	}

	/**
	 * 无分页查询
	 * @param str
	 * @return
	 */
	public List<T> getModelList(String... str){

		return dao.getModelList(str);
	}

	/**
	 * 统计记录数
	 * @param t
	 * @return
	 */
	public int getCount(T t){
		return dao.getCount(t);
	}

	/**
	 * 按数据范围分页查询
	 * @param t
	 * @return
	 */
	public List<T> getModelListByScope(T t) {
		 
		return dao.getModelListByScope(t);
	}

	/**
	 * 按数据范围无分页查询
	 * @param t
	 * @return
	 * @throws Exception
	 */
	public List<T> getPagedByScope(T t) {
		
		return dao.getPagedByScope(t);
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.woshidaniu.common.service.BaseService#getList(java.lang.Object)
	 */
	public List<T> getList(T t) {
		if (t instanceof ModelBase){
			((ModelBase) t).getQueryModel().setShowCount(Integer.MAX_VALUE);
			return getPagedList(t);
		}
		return getModelList(t);
	}

	
}
