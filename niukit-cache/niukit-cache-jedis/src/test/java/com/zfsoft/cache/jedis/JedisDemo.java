package com.woshidaniu.cache.jedis;

import java.util.List;

import redis.clients.jedis.Jedis;
                                                                 
public class JedisDemo {  
                                                                 
    public void test(){  
   Jedis  redis = new Jedis ("121.43.110.87",6379);//连接redis  
   redis.auth("redis");//验证密码,如果需要验证的话
   // STRING 操作
                                                                  
  //SET key value将字符串值value关联到key。
  redis.set("name", "wangjun1");
  redis.set("id", "123456");
  redis.set("address", "guangzhou");
                                                                  
  //SETEX key seconds value将值value关联到key，并将key的生存时间设为seconds(以秒为单位)。
  redis.setex("foo", 5, "haha");
                                                                  
  //MSET key value [key value ...]同时设置一个或多个key-value对。
  redis.mset("haha","111","xixi","222");
         
  redis.flushDB();
  //redis.flushAll();清空所有的key
  System.out.println(redis.dbSize());//dbSize是多少个key的个数
                                                                  
  //APPEND key value如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
  redis.append("foo", "00");//如果key已经存在并且是一个字符串，APPEND命令将value追加到key原来的值之后。
                                                                  
  //GET key 返回key所关联的字符串值
  redis.get("foo");
                                                                  
  //MGET key [key ...] 返回所有(一个或多个)给定key的值
  List list = redis.mget("haha","xixi");
  for(int i=0;i<list.size();i++){
      System.out.println(list.get(i));
  }
    }
    public static void main(String[] args) {     
   JedisDemo t1 = new JedisDemo();  
   t1.test();  
    }  
                                                                 
}
