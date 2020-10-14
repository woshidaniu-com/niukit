package com.woshidaniu.orm.mybatis;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.woshidaniu.orm.mybatis.entity.User;
import com.woshidaniu.orm.mybatis.interceptor.PaginationBounds;
import com.woshidaniu.orm.mybatis.mapper.UserMapper;
import com.woshidaniu.orm.mybatis.utils.IDWorker;

/**
 * <p>
 * MybatisPlus 测试类
 * </p>
 * 
 * @author hubin
 * @Date 2016-01-23
 */
public class UserMapperTest {
	
	private static final String RESOURCE = "mybatis-config.xml";

	/**
	 * RUN 测试
	 */
	public static void main(String[] args) {
		InputStream in = UserMapperTest.class.getClassLoader().getResourceAsStream(RESOURCE);

		/*
		 * 此处采用 MybatisSessionFactoryBuilder 构建
		 * SqlSessionFactory，目的是引入AutoMapper功能
		 */
		SqlSessionFactory sessionFactory = new MybatisSessionFactoryBuilder().build(in);
		SqlSession session = sessionFactory.openSession();
		UserMapper userMapper = session.getMapper(UserMapper.class);
		
		
		/*
		 * getModel 是从 AutoMapper 中继承而来的，UserMapper.xml中并没有申明该sql
		 */
		System.err.println("\n------------------getModel----------------------");
		User user = userMapper.getModel(1);
		print(user);
		sleep();
		
		int result = userMapper.deleteByName("test");
		System.out.println("\n------------------deleteByName----------------------\n result=" + result);
		sleep();
		
		Long id = IDWorker.getID();
		userMapper.insert(new User(id, "test", 18));
		System.out.println("\n------------------insert----------------------\n name=test, age=18");
		sleep();

		/*
		 * 此处的 selectById 被UserMapper.xml中的 selectById 覆盖了
		 */
		System.err.println("\n------------------selectById----------------------");
		User user2 = userMapper.selectById(2L);
		print(user2);

		/*
		 * updateById 是从 AutoMapper 中继承而来的，UserMapper.xml中并没有申明改sql
		 */
		System.err.println("\n------------------updateById----------------------");
		user.setName("MybatisPlus_" + System.currentTimeMillis());
		userMapper.updateById(user);
		sleep();
		
		/*
		 * 此处的 selectById 被UserMapper.xml中的 selectById 覆盖了
		 */
		user = userMapper.selectById(user.getId());
		print(user);

		System.err.println("\n------------------selectAll----------------------");
		List<User> userList = userMapper.selectAll();
		for (int i = 0; i < userList.size(); i++) {
			print(userList.get(i));
		}
		
		System.err.println("\n------------------list 分页查询，不查询总数（此时可自定义 count 查询）----------------------");
		List<User> rowList = userMapper.list(new RowBounds(0, 2));
		for (int i = 0; i < rowList.size(); i++) {
			print(rowList.get(i));
		}
		
		System.err.println("\n------------------list 分页查询，查询总数----------------------");
		PaginationBounds pagination = new PaginationBounds(0, 2);
		List<User> paginList = userMapper.list(pagination);
		for (int i = 0; i < paginList.size(); i++) {
			print(paginList.get(i));
		}
		System.err.println(pagination.toString());
		
		System.out.println("\n\n------------------deleteById----------------------");
		sleep();
		int del = userMapper.deleteById(id);
		System.err.println(" delete id=" + id + " ,result=" + del);
		
		// 提交
		session.commit();
	}

	/*
	 * 打印测试信息
	 */
	private static void print(User user) {
		sleep();
		if (user != null) {
			System.out.println("\n user: id=" + user.getId() + ", name=" + user.getName() + ", age=" + user.getAge());
		} else {
			System.out.println("\n user is null.");
		}
	}
	
	/*
	 * 慢点打印 
	 */
	private static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
