package com.woshidaniu.ftpclient.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.woshidaniu.ftpclient.FTPClient;

/**
 * 
 * @className	： FTPClientPool
 * @description	： FTPClient连接池
 * @author 		： kangzhidong
 * @date		： Dec 24, 2015 9:13:39 AM
 */
public class FTPClientPool extends GenericObjectPool<FTPClient> {

	/**
	 * 
	 * @description	: 初始化连接池，需要注入一个工厂来提供FTPClient实例
	 * @author 		: kangzhidong
	 * @date 		: Dec 24, 2015 9:15:31 AM 
	 * @param factory
	 */
	public FTPClientPool(FTPPooledClientFactory factory) {
		super(factory,new FTPClientPoolConfig("ftpclient.properties"));
	}

	/**
	 * 
	 * @description	: 初始化连接池，需要注入一个工厂来提供FTPClient实例和连接池初始化对象
	 * @author 		: kangzhidong
	 * @date 		: Dec 24, 2015 9:15:37 AM 
	 * @param factory
	 * @param config
	 */
	public FTPClientPool(FTPPooledClientFactory factory,FTPClientPoolConfig config){
		super(factory,config);
	}

}