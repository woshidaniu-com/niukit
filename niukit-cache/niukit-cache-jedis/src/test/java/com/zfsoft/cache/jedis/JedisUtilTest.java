package com.woshidaniu.cache.jedis;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Test;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by lujianing on 14-2-28.
 */
public class JedisUtilTest {

	JedisPool pool;
	Jedis jedis;

	@org.junit.Before
	public void setUp() {
		
		try {
			 
		    
			JedisPoolConfig config = new JedisPoolConfig();
			//borrowObject返回对象时，是采用DEFAULT_LIFO（last in first out，即类似cache的最频繁使用队列），如果为False，则表示FIFO队列；是否启用后进先出, 默认true
			config.setLifo(true);
			//连接池中最少空闲的连接数,默认为0.
			config.setMinIdle(3);
			//最大能够保持idel状态的对象数
			//控制一个pool最多有多少个状态为idle的jedis实例；
			config.setMaxIdle(50);
			//链接池中最大空闲的连接数,默认为8.
			//控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；如果赋值为-1，则表示 不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态就成exhausted了，在JedisPoolConfig
			config.setMaxTotal(500);
			//当池内没有返回对象时，最大等待时间获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
			//表示当borrow一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
			config.setMaxWaitMillis(-1);
			//连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
			config.setBlockWhenExhausted(false);
			//逐出连接的最小空闲时间 默认1800000毫秒(30分钟)，达到此值后空闲连接将可能会被移除。负值(-1)表示不移除。
			//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setMinEvictableIdleTimeMillis(-1);
			//连接空闲的最小时间，达到此值后空闲链接将会被移除，且保留“minIdle”个空闲连接数。默认为-1.
			//对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数 时直接逐出,不再根据MinEvictableIdleTimeMillis判断  (默认逐出策略)
			//在minEvictableIdleTimeMillis基础上，加入了至少 minIdle个对象已经在pool里面了。
			//如果为-1，evicted不会根据idle time驱逐任何对象。
			//如果minEvictableIdleTimeMillis>0，则此项设置无意义，且只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setSoftMinEvictableIdleTimeMillis(-1);
			//每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3 .
			//表示idle object evitor每次扫描的最多的对象数；
			config.setNumTestsPerEvictionRun(3);
			//向调用者输出“链接”资源时，是否检测是有有效，如果无效则从连接池中移除，并尝试获取继续获取。默认为false。建议保持默认值.
			//在borrow一个jedis实例时，是否提前进行alidate操作；如果为true，则得到的jedis实例均是可用的；
			config.setTestOnBorrow(false);
			//向连接池“归还”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
			config.setTestOnReturn(false);
			//向连接池“获取”链接时，是否检测“链接”对象的有效性。默认为false。建议保持默认值.
			config.setTestOnCreate(false);
			//向调用者输出“链接”对象时，是否检测它的空闲超时；默认为false。如果“链接”空闲超时，将会被移除。建议保持默认值.
			//如果为true，表示有一个idle object evitor线程对idle object进行扫描，如果validate失败，此object会被从pool中drop掉；这一项只有在 timeBetweenEvictionRunsMillis大于0时才有意义；
			config.setTestWhileIdle(true);
			//“空闲链接”检测线程，检测的周期，毫秒数。如果为负值，表示不运行“检测线程”。默认为-1.
			//表示idle object evitor两次扫描之间要sleep的毫秒数；
			config.setTimeBetweenEvictionRunsMillis(-1);
			//是否启用pool的jmx管理功能, 默认true -->
			//config.setJmxEnabled(true);
			///MBean ObjectName = new ObjectName("org.apache.commons.pool2:type=GenericObjectPool,name=" + "pool" + i)
			// 默 认为"pool", JMX不熟,具体不知道是干啥的...默认就好. -->
			//config.setJmxNamePrefix("pool");
			//当“连接池”中active数量达到阀值时，即“链接”资源耗尽时，连接池需要采取的手段
			//表示当pool中的jedis实例都被allocated完时，pool要采取的操作；
			//默认有三种
			//WHEN_EXHAUSTED_FAIL（表示无jedis实例时，直接抛出NoSuchElementException）、
			//WHEN_EXHAUSTED_BLOCK（则表示阻塞住，或者达到maxWait时抛出JedisConnectionException）、 
			//WHEN_EXHAUSTED_GROW（则表示新建一个jedis实例，也就说设置的maxActive无用）；
			//jedis.pool.whenExhaustedAction=WHEN_EXHAUSTED_GROW
			//设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
			config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
			//默认是2000,改为100000为100s，可根据需求改
		     pool = new JedisPool(config,"121.43.110.87",6379, 5000);
		     
			 jedis = pool.getResource();
			 jedis.auth("redis");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Test
	public void testGet() {
		boolean flag = true;
		try{
			System.out.println(jedis.get("lu"));
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
	}

	/**
	 * Redis存储初级的字符串 CRUD
	 */
	@Test
	public void testBasicString() {
		boolean flag = true;
		try{
			// -----添加数据----------
			jedis.set("name", "minxr");// 向key-->name中放入了value-->minxr
			System.out.println(jedis.get("name"));// 执行结果：minxr

			// -----修改数据-----------
			// 1、在原来基础上修改
			jedis.append("name", "jarorwar"); // 很直观，类似map 将jarorwar
												// append到已经有的value之后
			System.out.println(jedis.get("name"));// 执行结果:minxrjarorwar

			// 2、直接覆盖原来的数据
			jedis.set("name", "闵晓荣");
			System.out.println(jedis.get("name"));// 执行结果：闵晓荣

			// 删除key对应的记录
			jedis.del("name");
			System.out.println(jedis.get("name"));// 执行结果：null

			/**
			 * mset相当于 jedis.set("name","minxr"); jedis.set("jarorwar","闵晓荣");
			 */
			jedis.mset("name", "minxr", "jarorwar", "闵晓荣");
			System.out.println(jedis.mget("name", "jarorwar"));
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
		

	}

	/**
	 * jedis操作Map
	 */
	//@Test
	public void testMap() {
		boolean flag = true;
		try{
			Map<String, String> user = new HashMap<String, String>();
			user.put("name", "minxr");
			user.put("pwd", "password");
			jedis.hmset("user", user);
			// 取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
			// 第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
			List<String> rsmap = jedis.hmget("user", "name");
			System.out.println(rsmap);

			// 删除map中的某个键值
			// jedis.hdel("user","pwd");
			System.out.println(jedis.hmget("user", "pwd")); // 因为删除了，所以返回的是null
			System.out.println(jedis.hlen("user")); // 返回key为user的键中存放的值的个数1
			System.out.println(jedis.exists("user"));// 是否存在key为user的记录 返回true
			System.out.println(jedis.hkeys("user"));// 返回map对象中的所有key [pwd, name]
			System.out.println(jedis.hvals("user"));// 返回map对象中的所有value [minxr,

			Iterator<String> iter = jedis.hkeys("user").iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				System.out.println(key + ":" + jedis.hmget("user", key));
			}
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
		

	}

	/**
	 * jedis操作List
	 */
	//@Test
	public void testList() {
		boolean flag = true;
		try{
			// 开始前，先移除所有的内容
			jedis.del("java framework");
			System.out.println(jedis.lrange("java framework", 0, -1));
			// 先向key java framework中存放三条数据
			jedis.lpush("java framework", "spring");
			jedis.lpush("java framework", "struts");
			jedis.lpush("java framework", "hibernate");
			// 再取出所有数据jedis.lrange是按范围取出，
			// 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
			System.out.println(jedis.lrange("java framework", 0, -1));
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
		
	}

	/**
	 * jedis操作Set
	 */
	//@Test
	public void testSet() {
		boolean flag = true;
		try{
			// 添加
			jedis.sadd("sname", "minxr");
			jedis.sadd("sname", "jarorwar");
			jedis.sadd("sname", "闵晓荣");
			jedis.sadd("sanme", "noname");
			// 移除noname
			jedis.srem("sname", "noname");
			System.out.println(jedis.smembers("sname"));// 获取所有加入的value
			System.out.println(jedis.sismember("sname", "minxr"));// 判断 minxr
																	// 是否是sname集合的元素
			System.out.println(jedis.srandmember("sname"));
			System.out.println(jedis.scard("sname"));// 返回集合的元素个数
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
		
	}

	//@Test
	public void test() throws InterruptedException {
		boolean flag = true;
		try{
			// keys中传入的可以用通配符
			System.out.println(jedis.keys("*")); // 返回当前库中所有的key [sose, sanme, name,
													// jarorwar, foo, sname, java
													// framework, user, braand]
			System.out.println(jedis.keys("*name"));// 返回的sname [sname, name]
			System.out.println(jedis.del("sanmdde"));// 删除key为sanmdde的对象 删除成功返回1
														// 删除失败（或者不存在）返回 0
			System.out.println(jedis.ttl("sname"));// 返回给定key的有效时间，如果是-1则表示永远有效
			jedis.setex("timekey", 10, "min");// 通过此方法，可以指定key的存活（有效时间） 时间为秒
			Thread.sleep(5000);// 睡眠5秒后，剩余时间将为<=5
			System.out.println(jedis.ttl("timekey")); // 输出结果为5
			jedis.setex("timekey", 1, "min"); // 设为1后，下面再看剩余时间就是1了
			System.out.println(jedis.ttl("timekey")); // 输出结果为1
			System.out.println(jedis.exists("key"));// 检查key是否存在
			System.out.println(jedis.rename("timekey", "time"));
			System.out.println(jedis.get("timekey"));// 因为移除，返回为null
			System.out.println(jedis.get("time")); // 因为将timekey 重命名为time 所以可以取得值
													// min

			// jedis 排序
			// 注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
			jedis.del("a");// 先清除数据，再加入数据进行测试
			jedis.rpush("a", "1");
			jedis.lpush("a", "6");
			jedis.lpush("a", "3");
			jedis.lpush("a", "9");
			System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
			System.out.println(jedis.sort("a")); // [1, 3, 6, 9] //输入排序后结果
			System.out.println(jedis.lrange("a", 0, -1));
		}catch(Exception e){
			flag = false;
			e.printStackTrace();
		}finally{
			if (flag){
               pool.returnResource(jedis);
            }else{
               pool.returnBrokenResource(jedis);
            }
		}
	}

}
