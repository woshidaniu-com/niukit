package com.woshidaniu.ftpclient.pool;

import com.woshidaniu.ftpclient.FTPClient;
import com.woshidaniu.ftpclient.FTPClientConfig;



/**
 * 
 * @className	： FTPClientManager
 * @description	： FTP Client：管理接口
 * @author 		：kangzhidong
 * @date		： Dec 22, 2015 1:03:14 PM
 */
public interface FTPClientManager {

	/**
	 * 
	 * @description	： 得到FTPClient配置对象
	 * @author 		：kangzhidong
	 * @date 		：Jan 8, 2016 2:26:53 PM
	 * @return
	 */
	public FTPClientConfig getClientConfig();
	
	/**
	 * 
	 * @description	： 获取FTPClient对象
	 * @author 		：kangzhidong
	 * @date 		：Dec 22, 2015 1:02:54 PM
	 * @return
	 * @throws Exception
	 */
	public FTPClient getClient() throws Exception;

	/**
	 * 
	 * @description	： 释放获取FTPClient对象
	 * @author 		：kangzhidong
	 * @date 		：Dec 22, 2015 1:02:48 PM
	 * @param client
	 */
	public void releaseClient(FTPClient client);
	
	
	
}
