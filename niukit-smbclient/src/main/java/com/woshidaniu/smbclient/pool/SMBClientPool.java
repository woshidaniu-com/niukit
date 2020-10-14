package com.woshidaniu.smbclient.pool;

import org.apache.commons.pool2.impl.GenericObjectPool;

import com.woshidaniu.smbclient.SMBClient;

/**
 * 
 * @className	： SMBClientPool
 * @description	： SMBClient连接池
 * @author 		： kangzhidong
 * @date		： Jan 20, 2016 12:29:33 PM
 */
public class SMBClientPool extends GenericObjectPool<SMBClient> {

	/**
	 * 
	 * @description	: 初始化连接池，需要注入一个工厂来提供SMBClient实例
	 * @author 		: kangzhidong
	 * @date 		: Dec 24, 2015 9:15:31 AM 
	 * @param factory
	 */
	public SMBClientPool(SMBPooledClientFactory factory) {
		super(factory,new SMBClientPoolConfig("smbclient.properties"));
	}

	/**
	 * 
	 * @description	: 初始化连接池，需要注入一个工厂来提供SMBClient实例和连接池初始化对象
	 * @author 		: kangzhidong
	 * @date 		: Dec 24, 2015 9:15:37 AM 
	 * @param factory
	 * @param config
	 */
	public SMBClientPool(SMBPooledClientFactory factory,SMBClientPoolConfig config){
		super(factory,config);
	}

}