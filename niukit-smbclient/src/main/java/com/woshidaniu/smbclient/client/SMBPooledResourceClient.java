package com.woshidaniu.smbclient.client;


import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.SMBClientConfig;
import com.woshidaniu.smbclient.pool.SMBClientManager;
 
/**
 * 
 *@类名称		: SMBPooledResourceClient.java
 *@类描述		: 基于 Apache Pool2的SMBClient共享文件资源服务客户端实现
 *@创建人	 	: kangzhidong
 *@创建时间	: Jan 22, 2016 3:07:09 PM
 *@修改人		: 
 *@修改时间	: 
 *@版本号		: v1.0
 */
public class SMBPooledResourceClient extends SMBResourceClient{
	
	private SMBClientManager clientManager = null;
	
	public SMBPooledResourceClient(){
		 
	}
	
	public SMBPooledResourceClient(SMBClientManager clientManager){
		 this.clientManager = clientManager;
	} 

	@Override
	public SMBClient getSMBClient() throws Exception {
		//从对象池获取FTPClient对象
		return getClientManager().getClient();
	}
 
	@Override
	public void releaseClient(SMBClient smbClient) throws Exception{
		//释放FTPClient到对象池
    	getClientManager().releaseClient(smbClient);
	}
	
	public SMBClientConfig getClientConfig() {
		return getClientManager().getClientConfig();
	}
	
	public SMBClientManager getClientManager() {
		return clientManager;
	}

	public void setClientManager(SMBClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
}
