package com.woshidaniu.ftpclient;

import org.apache.commons.net.ftp.FTPFile;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.ftpclient.utils.FTPStringUtils;
@FixMethodOrder(MethodSorters.JVM) 
public class FTPResourceClientListTest extends FTPResourceClientTest {
	
	/**
 	 * ftp列举文件
 	 */
 	@Test
 	public void listFile() {
 		try {
 			
 			for(FTPFile ftpFile : ftpClient.listFiles("20063586")){
 				System.err.println("File:" + ftpFile.getName() + " >> " +  FTPStringUtils.getLocalName(ftpClient.getFTPClient(), ftpFile.getName()));
 			}
 			
 			/*for(FTPFile ftpFile : ftpClient.listFiles("20063586/20160118")){
 				System.err.println("File:" + ftpFile.getName() + " >> " +  FTPStringUtils.getLocalName(ftpClient.getFTPClient(), ftpFile.getName()));
 			}*/
 			
 			System.err.println("Files:" +StringUtils.join(ftpClient.listNames("20063586"),","));
 			/*
 			FTPFile ftpFile1 = ftpClient.getFile("20063586", "tst1.txt");
 			System.err.println("File1:" + ftpFile1.getName());
 			FTPFile ftpFile2 = ftpClient.getFile("20063586/tst1.txt");
 			System.err.println("File2:" + ftpFile2.getName());*/
 			
 		} catch (Exception e) {
 			e.printStackTrace();
 		} 
 	}
 	
 }
 