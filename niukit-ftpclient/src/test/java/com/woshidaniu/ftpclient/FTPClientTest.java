package com.woshidaniu.ftpclient;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.ftpclient.client.IFTPClient;

public abstract class FTPClientTest {

	Logger LOG = LoggerFactory.getLogger(FTPResourceClientTest.class);
	// 要写入的文件内容
	String fileContent = "hello world，你好世界";
	IFTPClient ftpClient = null;
	FTPClientBuilder builder = null;
}
