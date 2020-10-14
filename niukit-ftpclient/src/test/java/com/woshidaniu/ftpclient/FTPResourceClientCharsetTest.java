package com.woshidaniu.ftpclient;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.woshidaniu.ftpclient.utils.FTPStringUtils;
@FixMethodOrder(MethodSorters.JVM) 
public class FTPResourceClientCharsetTest extends FTPResourceClientTest {
	
	/**
 	 * ftp上传时候的字符集测试
 	 */
 	@Test
 	public void charset() {
 		try {
 			String  fileName = "第九套广播体操.mp4";
 			
 			System.out.println("RemoteName:" + FTPStringUtils.getRemoteName(ftpClient.getFTPClient(), fileName));
 			
 			String  ftpFileName = "ç¬¬ä¹å¥å¹¿æ­ä½æ.mp4";
 			
 			System.out.println("LocalName:" + FTPStringUtils.getLocalName(ftpClient.getFTPClient(), ftpFileName)); 
 				
 			
			
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 	 
 	
 }
 