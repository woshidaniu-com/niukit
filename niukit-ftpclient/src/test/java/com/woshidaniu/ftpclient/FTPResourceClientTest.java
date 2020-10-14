package com.woshidaniu.ftpclient;

import org.junit.Before;

import com.woshidaniu.ftpclient.client.FTPResourceClient;

public class FTPResourceClientTest extends FTPClientTest {
	
	@Before
	public void setUp() {
		builder = new FTPClientBuilder("ftpclient.properties");
 		ftpClient = new FTPResourceClient(builder);
	}
 	
 }
 