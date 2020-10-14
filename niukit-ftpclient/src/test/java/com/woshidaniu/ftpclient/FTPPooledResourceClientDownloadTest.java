package com.woshidaniu.ftpclient;

import java.io.File;
import java.io.FileOutputStream;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
@FixMethodOrder(MethodSorters.JVM) 
public class FTPPooledResourceClientDownloadTest extends FTPPooledResourceClientTest {
	
 	/**
 	 * ftp下载数据
 	 */
 	@Test
 	public void downFile() {
 		try {
 			
 			ftpClient.downloadToFile("tst1.mp4", "E:\\test\\第九套广播体操1.mp4");
 			ftpClient.downloadToFile("tst.mp4", new File("E:\\test\\第九套广播体操.mp4"));
 			ftpClient.downloadToFile("20063586/20160118","tst.mp4", "E:\\test\\20063586\\第九套广播体操1.mp4");
 			ftpClient.downloadToFile("20063586/20160118","tst.mp4", new File("E:\\test\\20063586\\第九套广播体操2.mp4"));
 			ftpClient.downloadToStream("tst2.mp4", new FileOutputStream(new File("E:\\test\\第九套广播体操2.mp4")));
 			ftpClient.downloadToStream("20063586","tst2.mp4", new FileOutputStream(new File("E:\\test\\20063586\\第九套广播体操3.mp4")));
 			ftpClient.downloadToFileByChannel("tst.mp4", "E:\\test\\第九套广播体操1.mp4");
 			ftpClient.downloadToFileByChannel("tst.mp4", new File("E:\\test\\第九套广播体操.mp4"));
 			ftpClient.downloadToFileByChannel("20063586/20160118","tst.mp4", "E:\\test\\20063586\\第九套广播体操1.mp4");
 			ftpClient.downloadToFileByChannel("20063586/20160118","tst1.txt", new File("E:\\test\\20063586\\tst1.txt"));
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
	
 
 }
 