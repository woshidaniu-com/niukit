package com.woshidaniu.smbclient.pool;

import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.SMBClientConfig;


/**
 * 
 * @className	： SMBClientManager
 * @description	： SMBClient：管理接口
 * @author 		： kangzhidong
 * @date		： Jan 20, 2016 12:24:44 PM
 */
public interface SMBClientManager {

	/**
	 * 
	 * @description	： 得到FTPClient配置对象
	 * @author 		： kangzhidong
	 * @date 		：Jan 8, 2016 2:26:53 PM
	 * @return
	 */
	public SMBClientConfig getClientConfig();
	
	/**
	 * 
	 * @description	： 获取SMBClient对象
	 * @author 		： kangzhidong
	 * @date 		：Dec 22, 2015 1:02:54 PM
	 * @return
	 * @throws Exception
	 */
	public SMBClient getClient() throws Exception;

	/**
	 * 
	 * @description	： 释放获取SMBClient对象
	 * @author 		： kangzhidong
	 * @date 		：Dec 22, 2015 1:02:48 PM
	 * @param client
	 */
	public void releaseClient(SMBClient client);
	
	
	
}
