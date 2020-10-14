package com.woshidaniu.ftpclient.client;


import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.FTPClientConfig;
import com.woshidaniu.ftpclient.pool.FTPClientManager;
 
/**
 * 
 * @className	： FTPPooledResourceClient
 * @description	： 基于 Apache Pool2的FTPClient资源服务客户端实现
 * @author 		：kangzhidong
 * @date		： Jan 12, 2016 9:07:53 AM
 */
public class FTPPooledResourceClient extends FTPResourceClient{
	
	private FTPClientManager clientManager = null;
	
	public FTPPooledResourceClient(){
		 
	}
	
	public FTPPooledResourceClient(FTPClientManager clientManager){
		 this.clientManager = clientManager;
	} 

	@Override
	public FTPClient getFTPClient() throws Exception {
		//从对象池获取FTPClient对象
		return getClientManager().getClient();
	}
 
	@Override
	public void releaseClient(FTPClient ftpClient) throws Exception{
		//释放FTPClient到对象池
    	getClientManager().releaseClient(ftpClient);
	}
	
	public FTPClientConfig getClientConfig() {
		return getClientManager().getClientConfig();
	}
	
	public FTPClientManager getClientManager() {
		return clientManager;
	}

	public void setClientManager(FTPClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
}
