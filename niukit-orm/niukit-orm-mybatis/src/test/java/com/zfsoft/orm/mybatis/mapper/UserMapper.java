package com.woshidaniu.orm.mybatis.mapper;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.woshidaniu.orm.mybatis.entity.User;

/**
 * <p>
 * 继承 AnnotationMapper，就自动拥有CRUD方法
 * </p>
 */
public interface UserMapper extends AnnotationMapper<User> {

	/**
	 * 根据用户名删除用户
	 * 
	 * @param name
	 *            用户名称
	 * @return
	 */
	int deleteByName(String name);

	/**
	 * 用户列表，分页显示
	 * 
	 * @param pagination
	 *            传递参数包含该属性，即自动分页
	 * @return
	 */
	List<User> list(RowBounds pagination);
}
