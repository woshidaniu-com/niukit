package com.woshidaniu.ftpclient.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.channels.FileChannel;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;


public class FTPExceptionUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FTPExceptionUtils.class);
	
	public static void assertFile(FTPFile[] ftpFiles,String ftpFileName) throws IOException {
		if (ftpFiles == null || ftpFiles.length == 0) {
			throw new FileNotFoundException("File " + ftpFileName + " was not found on File sharing server.");
        }
	}
	
	public static void assertFile(FTPFile ftpFile,String ftpFileName) throws IOException {
		if (ftpFile == null) {
			throw new FileNotFoundException("File " + ftpFileName + " was not found on FTP server.");
        }
		if(!ftpFile.isFile()){
			throw new IOException("File [ " + ftpFileName + " ] was not a file.");
		}
		// Check file size.
        long size = ftpFile.getSize();
        if (size > Integer.MAX_VALUE) {
            throw new IOException("File " + ftpFileName + " is too large.");
        }
	}
	
	public static void assertFile(boolean hasFile,String ftpFileName) throws IOException {
		if (!hasFile) {
			throw new FileNotFoundException("File " + ftpFileName + " was not found on FTP server.");
        }
	}
	
	public static void assertPut(File localFile,FTPFile ftpFile,String ftpFileName) throws IOException {
		Assert.notNull(ftpFile, "ftpFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(localFile.length() == ftpFile.getSize()){
			throw new IOException("The File [" + localFile.getName() + "] named of  [" + ftpFileName + "] was exists on FTP server.");
		}else if(localFile.length() < ftpFile.getSize()){
			throw new IOException("Remote File [" + ftpFileName + "] was bigger than local.");
		}
	}
	
	public static void assertPut(File localFile,FTPFile ftpFile,String ftpFileName,long skipOffset) throws IOException {
		Assert.notNull(ftpFile, "ftpFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(localFile.length() == ftpFile.getSize()){
			throw new IOException("The File [" + localFile.getName() + "] named of  [" + ftpFileName + "] was exists on FTP server.");
		}else if(localFile.length() < ftpFile.getSize()){
			throw new IOException("Remote File [" + ftpFileName + "] was bigger than local.");
		}else if(localFile.length() < skipOffset){
			throw new EOFException("offset [" + skipOffset + "] larger than the length of localFile : unexpected EOF"); 
		}
	}
	
	public static void assertPut(FileChannel inChannel,FTPFile ftpFile,String ftpFileName) throws IOException {
		Assert.notNull(ftpFile, "ftpFile is null .");
		Assert.notNull(inChannel, "inChannel is null .");
		long channelSize = inChannel.size();
		if( channelSize == ftpFile.getSize()){
			throw new IOException("The File [" + ftpFileName + "] was exists on FTP server.");
		}else if(channelSize < ftpFile.getSize()){
			throw new IOException("Remote File [" + ftpFileName + "] was bigger than local.");
		}
	}
	
	
	public static void assertGet(boolean hasGet,String ftpFileName) throws IOException {
		if (!hasGet) {
            throw new IOException("Error loading file [" + ftpFileName + "] from FTP server. Check FTP permissions and path.");
        }
	}

	public static void assertAppend(boolean hasAppend,String ftpFileName) throws IOException {
		if (!hasAppend) {
			throw new IOException("File [" + ftpFileName + "] append to FTP server fail.");
        }
	}
	
	public static void assertPut(boolean hasPut,String ftpFileName) throws IOException {
		if (!hasPut) {
			throw new IOException("File [" + ftpFileName + "] Upload to FTP server fail.");
        }
	}
	
	public static void assertDel(boolean hasDel,String ftpFileName) throws IOException {
		if (!hasDel) {
		    throw new IOException("Can't remove file [" + ftpFileName + "] from FTP server. Check FTP permissions and path.");
		}
	}
	
	public static void assertRomve(boolean hasRomve,String ftpDir) throws IOException {
		if(!hasRomve){
			 throw new IOException("Directory [ " + ftpDir + " ] was not empty on FTP server.");
		}
	}
	
	public static void assertDir(boolean hasChanged,String ftpDir) throws IOException {
		//目录未切换成功;表示指定的目录不存在
		if(!hasChanged){
			throw new FileNotFoundException("Directory [ " + ftpDir + " ] was not found on FTP server.");
		}
	}

	public static void assertDir(FTPFile[] files, String ftpDir)  throws IOException {
		if(files == null || files.length == 0){
			throw new FileNotFoundException("Directory [ " + ftpDir + " ] was empty .");
		}
	}
	
	public static void assertPut(File localFile) throws IOException {
		if(!localFile.exists()){
			throw new IOException("Local file [" + localFile.getPath() + "] not exist.");
        }
	}
	
	
	/**
	 * 
	 *@描述		：直接输出纯Text.
	 *@创建人	: kangzhidong
	 *@创建时间	: Jun 2, 20169:28:33 AM
	 *@param response
	 *@param text
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static void renderException(HttpServletResponse response, String text) {
		PrintWriter out = null;
		try {
			response.setContentType("text/plain;charset=UTF-8");
			out = response.getWriter();
			out.write(text);
			out.flush();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
}
