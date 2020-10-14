package com.woshidaniu.orm.mybatis.entity;

import java.io.Serializable;
import java.lang.reflect.Field;

import com.woshidaniu.db.core.annotation.TableField;
import com.woshidaniu.db.core.annotation.TableId;
import com.woshidaniu.db.core.annotation.TableName;

/**
 * <p>
 * 测试用户类
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-23
 */
/* 表名 注解 */
@TableName(value = "USER_QW")
public class User implements Serializable {

	/* 表字段 主键，false 表中不存在的字段，可无该注解 默认 true */
	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/* 主键ID 注解，auto 属性 true 自增（默认 true），false 非自增（调用插入 insert 需要用户传入ID值）*/
	@TableId(auto = false)
	private long id;

	private String name;

	private int age;
	
	public User() {
		
	}

	public User(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public User(long id, String name, int age) {
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * 测试类型
	 */
	public static void main(String args[]) throws IllegalAccessException {
		User user = new User();
		user.setName("12306");
		user.setAge(3);
		System.err.println(User.class.getName());
		Field[] fields = user.getClass().getDeclaredFields();
		for (Field field : fields) {
			System.out.println("===================================");
			System.out.println(field.getName());
			System.out.println(field.getType().toString());
			field.setAccessible(true);
			System.out.println(field.get(user));
		}
	}
}
