package com.woshidaniu.smbclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import jcifs.smb.SmbFile;

import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.smbclient.client.ISMBClient;
import com.woshidaniu.smbclient.client.SMBPooledResourceClient;
import com.woshidaniu.smbclient.pool.SMBClientManager;
import com.woshidaniu.smbclient.pool.SMBClientPoolConfig;
import com.woshidaniu.smbclient.pool.SMBPooledClientManager;

public class SMBPooledResourceClientTest {
	
	// 要写入的文件内容
	String fileContent = "hello world，你好世界";
	protected static Logger LOG = LoggerFactory.getLogger(SMBResourceClientTest.class);
	ISMBClient smbClient = null;
	SMBClientBuilder builder = null;
	SMBClientManager clientManager = null;
	
	@Before
	public void setUp() {
		builder = new SMBClientBuilder("smbclient.properties");
		clientManager = new SMBPooledClientManager(builder,new SMBClientPoolConfig("smbclient.properties"));
 		smbClient = new SMBPooledResourceClient(clientManager);
	}
	
	/**
 	 * 向smb写文件(数据)
 	 */
	//@Test
 	public void uploadFile() {
 		try {
 			File localFile = new File("E:\\第九套广播体操.mp4");
 			
 			smbClient.upload("E:\\第九套广播体操.mp4","tst1.mp4");
 			smbClient.upload(localFile, "tst.mp4");
 			smbClient.upload(localFile,"20063586/tst.mp4");
 			smbClient.upload(localFile ,"20063586/20160118","tst.mp4");
 			
 			smbClient.upload(new FileInputStream(localFile),"tst2.mp4");
 			smbClient.upload(new FileInputStream(localFile),"20063586","tst2.mp4" );
 			smbClient.upload(new FileInputStream(localFile),"20063586/20160118","tst2.mp4");
 			
 			smbClient.upload(new StringBuilder(fileContent),"tst1.txt");
 			smbClient.upload(new StringBuilder(fileContent),"20063586","tst2.txt");
 			smbClient.upload(new StringBuilder(fileContent),"20063586/20160118","tst3.txt");
 			
 			smbClient.upload("E:\\第九套广播体操.mp4","tst3.mp4");
 			smbClient.upload("E:\\第九套广播体操.mp4","20063586","tst4.mp4");
 			smbClient.upload("E:\\第九套广播体操.mp4","20063586/20160118","tst5.mp4");
 			smbClient.uploadByChannel(localFile, "tst-c.mp4");
 			smbClient.uploadByChannel(localFile,"20063586/20160118","tst-c.mp4");
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		}  
 	}
 	
 	/**
 	 * smb列举文件
 	 */
 	//@Test
 	public void listFile() {
 		try {
 			
 			for(SmbFile smbFile : smbClient.listFiles("20063586")){
 				LOG.info("File:" + smbFile.getName());
 			}
 			
 			LOG.info("Files:" +StringUtils.join(smbClient.listNames("20063586"),","));
 			
 			SmbFile smbFile1 = smbClient.getFile("20063586", "tst1.txt");
 			LOG.info("File1:" + smbFile1.getName());
 			SmbFile smbFile2 = smbClient.getFile("20063586/tst1.txt");
 			LOG.info("File2:" + smbFile2.getName());
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 	
 	/**
 	 * smb下载数据
 	 */
 	//@Test
 	public void downFile() {
 		try {
 			
 			smbClient.downloadToFile("tst1.mp4", "E:\\test\\第九套广播体操1.mp4");
 			smbClient.downloadToFile("tst.mp4", new File("E:\\test\\第九套广播体操.mp4"));
 			smbClient.downloadToFile("20063586/20160118","tst.mp4", "E:\\test\\20063586\\第九套广播体操1.mp4");
 			smbClient.downloadToFile("20063586/20160118","tst.mp4", new File("E:\\test\\20063586\\第九套广播体操2.mp4"));
 			smbClient.downloadToStream("tst2.mp4", new FileOutputStream(new File("E:\\test\\第九套广播体操2.mp4")));
 			smbClient.downloadToStream("20063586","tst2.mp4", new FileOutputStream(new File("E:\\test\\20063586\\第九套广播体操3.mp4")));
 			smbClient.downloadToDir("20063586/20160118", "E:\\test\\20063586\\dir01");
 			smbClient.downloadToDir("20063586/20160118", new File("E:\\test\\20063586\\dir02"));
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 	
 	/**
 	 * smb移动和创建目录
 	 */
 	//@Test
 	public void moveFile() {
 		try {
 			smbClient.makeDir("20063586");
 			smbClient.makeDir("20063586/dddd");
 			smbClient.makeDir("20063586/ss/sdsd");
 			
 			smbClient.rename("tst2-sss9.mp4","tst1.mp4");
 			smbClient.rename("tst1.mp4", "tst2-sss9.mp4");
 			smbClient.renameTo("tst2-sss9.mp4", "20063586//rename.mp4");
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
	
	/**
 	 * smb删除文件
 	 */
 	//@Test
 	public void removeFile() {
 		try {
 			smbClient.remove("tst.mp4");
 			smbClient.remove(new String[]{"tst2.mp4","tst1.mp4"});
 			smbClient.remove("20063586", "tst.mp4");
 			smbClient.remove("20063586",new String[]{"tst1.txt","tst2.mp4","tst1.mp4"});
 			smbClient.removeDir("20063586");
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 }
 