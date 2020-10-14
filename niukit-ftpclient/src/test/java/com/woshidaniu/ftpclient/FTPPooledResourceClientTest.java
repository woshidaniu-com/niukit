package com.woshidaniu.ftpclient;

import org.junit.Before;

import com.woshidaniu.ftpclient.client.FTPPooledResourceClient;
import com.woshidaniu.ftpclient.pool.FTPClientManager;
import com.woshidaniu.ftpclient.pool.FTPClientPoolConfig;
import com.woshidaniu.ftpclient.pool.FTPPooledClientManager;

public class FTPPooledResourceClientTest extends FTPClientTest {
	
	@Before
	public void setUp() {
		builder = new FTPClientBuilder("ftpclient.properties");
		FTPClientManager clientManager = new FTPPooledClientManager(builder,new FTPClientPoolConfig("ftpclient.properties"));
 		ftpClient = new FTPPooledResourceClient(clientManager);
	}
	 
 }
 