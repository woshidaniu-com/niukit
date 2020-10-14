package com.woshidaniu.ftpclient;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.JVM) 
public class FTPPooledResourceClientRemoveTest extends FTPPooledResourceClientTest {
	
	/**
 	 * ftp删除文件
 	 */
 	@Test
 	public void removeFile() {
 		try {
 			
 			ftpClient.remove("tst.mp4");
 			ftpClient.remove(new String[]{"tst2.mp4","tst1.mp4"});
 			ftpClient.remove("20063586", "tst.mp4");
 			ftpClient.remove("20063586",new String[]{"tst1.txt","tst2.mp4","tst1.mp4"});
 			ftpClient.removeDir("20063586/20160118");
 			ftpClient.removeDir("20063586/20160118", false);
 			ftpClient.removeDir("20063586/20160118", true);
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 }
 