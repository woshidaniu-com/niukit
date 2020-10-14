package com.woshidaniu.ftpclient.pool;

import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.FTPClientBuilder;
import com.woshidaniu.ftpclient.FTPClientConfig;
/**
 * @className	： FTPPooledClientManager
 * @description	： 基于 Apache Pool2实现的 FTPClient对象连接池
 * @author 		：kangzhidong
 * @date		： Jan 8, 2016 2:50:08 PM
 */
public class FTPPooledClientManager implements FTPClientManager {

	private FTPClientPool clientPool = null;
	private FTPClientConfig configuration = null;
	
	public FTPPooledClientManager(FTPClientBuilder clientBuilder,FTPClientPoolConfig config) {
		this.clientPool = new FTPClientPool(new FTPPooledClientFactory(clientBuilder), config);
		this.configuration = clientBuilder.getConfiguration();
	}
	
	@Override
	public FTPClient getClient() throws Exception {
		return clientPool.borrowObject();
	}

	@Override
	public void releaseClient(FTPClient client) {
		try {
			if(client !=null){
				clientPool.returnObject(client);
			}
		} catch (Throwable e) {
			 
		}
	}

	@Override
	public FTPClientConfig getClientConfig() {
		return configuration;
	}

}
