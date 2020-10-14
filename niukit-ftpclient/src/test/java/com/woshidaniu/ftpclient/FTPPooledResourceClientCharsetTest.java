package com.woshidaniu.ftpclient;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.woshidaniu.basicutils.StringUtils;

@FixMethodOrder(MethodSorters.JVM) 
public class FTPPooledResourceClientCharsetTest extends FTPPooledResourceClientTest {
	 
 	/**
 	 * ftp列举文件
 	 */
 	@Test
 	public void listFile() {
 		try {
 			
 			for(FTPFile ftpFile : ftpClient.listFiles("20063586")){
 				LOG.info("File:" + ftpFile.getName());
 			}
 			
 			LOG.info("Files:" +StringUtils.join(ftpClient.listNames("20063586"),","));
 			
 			FTPFile ftpFile1 = ftpClient.getFile("/u01/wwwroot/niutal-kod/data/Group/public/home/share/20063586", "34df8494-52af-4e89-9424-b1e395233186.mp4");
 			LOG.info("File1:" + ftpFile1.getName());
 			FTPFile ftpFile2 = ftpClient.getFile("20063586/34df8494-52af-4e89-9424-b1e395233186.mp4");
 			LOG.info("File2:" + ftpFile2.getName());
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 	 
 }
 