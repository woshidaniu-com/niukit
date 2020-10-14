/**
 * 
 */
package com.woshidaniu.license.dataSync;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;

import com.woshidaniu.license.integration.ServletContextAware;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.license.WhatOS;
import com.woshidaniu.license.WhoAmI;


/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵���������ļ���ʽ������ͬ��
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��6��23������2:04:01
 */
public class FileDataSync implements DataSync , ServletContextAware {
	
	private static final Logger log = LoggerFactory.getLogger(FileDataSync.class);
	
	private static final String DEFAULE_FILE_NAME = "_license.dat";
	
	private static final String DEFAULT_HIS_FILE_NAME = "_license.his";
	
	private static String DEFAULE_FILE_DIR = FileUtils.getUserDirectoryPath();
	
	private String fileLocation = DEFAULE_FILE_DIR;
	
	private String fileName = DEFAULE_FILE_NAME;
	
	private Object lockObject = new Object();

	private ServletContext sc;
	
	static{
		if(WhatOS.isLinuxPlatform){
			DEFAULE_FILE_DIR = "/var/zf_license";
		}
		if(WhatOS.isWinPlatform){
			DEFAULE_FILE_DIR = FileUtils.getUserDirectoryPath();
		}
	}
	
	@Override
	public  boolean sync(String data) {
		synchronized (lockObject) {
			Writer fileWriter = null;
			boolean ret = false;
			try {
				File file = FileUtils.getFile(DEFAULE_FILE_DIR + sc.getContextPath() + "/" + DEFAULE_FILE_NAME);
				if(file.exists()){
					fileWriter = new FileWriter(file);
					fileWriter.write(data, 0, data.length());
					fileWriter.flush();
					ret = true;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(fileWriter != null){
						fileWriter.close();
					}
				} catch (IOException e) {
					fileWriter = null;
					e.printStackTrace();
				}
			}
			return ret;
		}
	}

	@Override
	public String getSHA() {
		synchronized (lockObject) {
			Reader reader = null;
			StringBuffer ret = new StringBuffer();
			char[] buffer = new char[1024];
			try {
				File file = FileUtils.getFile(DEFAULE_FILE_DIR + sc.getContextPath() + "/" + DEFAULE_FILE_NAME);
				if(file.exists()){
					reader = new FileReader(file);
					while((reader.read(buffer)) != -1){
						ret.append(buffer);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(reader!=null){
						reader.close();
					}
				} catch (IOException e) {
					reader = null;
					e.printStackTrace();
				}
			}
			return ret.length() == 0 ? "-1" : ret.toString();
		}
		
	}
	

	@Override
	public String getHisData() {
		synchronized (lockObject) {
			Reader reader = null;
			StringBuffer ret = new StringBuffer();
			char[] buffer = new char[1024];
			try {
				File file = FileUtils.getFile(DEFAULE_FILE_DIR + sc.getContextPath() + "/" + DEFAULT_HIS_FILE_NAME);
				if(file.exists()){
					reader = new FileReader(file);
					while((reader.read(buffer)) != -1){
						ret.append(buffer);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(reader!=null){
						reader.close();
					}
				} catch (IOException e) {
					reader = null;
					e.printStackTrace();
				}
			}
			return ret.length() == 0 ? null : ret.toString();
		}
	}
	
	/**
	 * 
	 * <p>����˵�����״�ʹ��license�ļ�ʱ����ʼ������<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��21������4:34:54<p>
	 */
	@Override
	public boolean initLicenseData(String encryptSHA, String hisData){
		synchronized (encryptSHA) {
			OutputStream os = null;
			OutputStream hisos = null;
			boolean ret = false;
			try {
				String fileName = DEFAULE_FILE_DIR + sc.getContextPath() + "/" + DEFAULE_FILE_NAME;
				String hisFileName = DEFAULE_FILE_DIR + sc.getContextPath() + "/" + DEFAULT_HIS_FILE_NAME;
				File parentDir = new File(DEFAULE_FILE_DIR + sc.getContextPath());
				
				if(log.isDebugEnabled()){
					log.debug("Parent Dir Exist ? {},{}", new Object[]{parentDir.getAbsoluteFile(),parentDir.exists()});
				}
				
				if(!parentDir.exists()){
					boolean mkdirs = parentDir.mkdirs();
					
					if(log.isDebugEnabled()){
						log.debug("ParentDir make Sucecss? {}", mkdirs);
					}
					
				}
				
				File file = FileUtils.getFile(fileName);
				File hisFile = FileUtils.getFile(hisFileName);
				//�����ڣ������� /var/zf_license/niutal-core-web/_license.dat
				if(!file.exists()){
					if(log.isDebugEnabled()){
						log.debug("_license.dat file path : {}", file.getAbsolutePath());
					}
					file.createNewFile();
					file.setReadable(true);
					file.setWritable(true);
				}
				if(!hisFile.exists()){
					if(log.isDebugEnabled()){
						log.debug("_license.his file path : {}", hisFile.getAbsolutePath());
					}
					hisFile.createNewFile();
					hisFile.setReadable(true);
					hisFile.setWritable(true);
				}
				os = new FileOutputStream(file);
				hisos = new FileOutputStream(hisFileName);
				IOUtils.write(encryptSHA, os, Charsets.toCharset("UTF-8"));
				IOUtils.write(hisData, hisos, Charsets.toCharset("UTF-8"));
				ret = true;
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					os.close();
					hisos.close();
				} catch (IOException e) {
					os = null;
					hisos = null;
					e.printStackTrace();
				}
			}
			return ret;
		}
	}

	@Override
	public String getDate() {
		SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		String format = DATE_FORMAT.format(new Date());
		return format;
	}

	@Override
	public String getProductName(WhoAmI whoAmI) {
		if(whoAmI != null){
			return whoAmI.myNameIs();
		}
		return null;
	}
	
	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void setServletContext(ServletContext sc) {
		this.sc = sc;
		
	}
}
