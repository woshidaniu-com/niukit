package com.woshidaniu.orm.mybatis.mapper;

import java.util.List;

/**
 * 
 * @className	： AnnotationMapper
 * @description	： Mapper 继承该接口后，无需编写 mapper.xml 文件，即可获得CRUD功能
 * @author 		： kangzhidong
 * @date		： Jan 26, 2016 4:41:33 PM
 * @version 	V1.0 
 * @param <T>
 */
public interface AnnotationMapper<T> {

	
	public T getModel(Object id);
	
	/**
	 * 新增
	 */
	int insert(T entity);

	/**
	 * 根据主键删除，主键名默认为id
	 */
	int deleteById(Object id);

	/**
	 * 根据主键修改，主键名默认为id
	 */
	int updateById(T entity);

	/**
	 * 根据主键查找，主键名默认为id
	 */
	T selectById(Object id);

	/**
	 * 查询全部
	 */
	List<T> selectAll();

}
