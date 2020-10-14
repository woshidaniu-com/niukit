package com.woshidaniu.smbclient.pool;

import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.SMBClientBuilder;
import com.woshidaniu.smbclient.SMBClientConfig;
/**
 * @className	： SMBPooledClientManager
 * @description	： 基于 Apache Pool2实现的 SMBClient对象连接池
 * @author 		： kangzhidong
 * @date		： Jan 8, 2016 2:50:08 PM
 */
public class SMBPooledClientManager implements SMBClientManager {

	private SMBClientPool clientPool = null;
	private SMBClientConfig configuration = null;
	
	public SMBPooledClientManager(SMBClientBuilder clientBuilder,SMBClientPoolConfig config) {
		this.clientPool = new SMBClientPool(new SMBPooledClientFactory(clientBuilder), config);
		this.configuration = clientBuilder.getConfiguration();
	}
	
	@Override
	public SMBClient getClient() throws Exception {
		return clientPool.borrowObject();
	}

	@Override
	public void releaseClient(SMBClient client) {
		try {
			if(client !=null){
				clientPool.returnObject(client);
			}
		} catch (Throwable e) {
			 
		}
	}

	@Override
	public SMBClientConfig getClientConfig() {
		return configuration;
	}

}
