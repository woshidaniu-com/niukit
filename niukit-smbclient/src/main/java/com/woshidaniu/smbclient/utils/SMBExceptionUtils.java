package com.woshidaniu.smbclient.utils;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import jcifs.smb.SmbFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.Assert;


public class SMBExceptionUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(SMBExceptionUtils.class);

	/**
	 * 追加内容的判断
	 */
	public static void assertAppend(File localFile,SmbFile sharedFile) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(!localFile.exists()){
			throw new FileNotFoundException("Local File [" + localFile.getAbsolutePath() + "] was not found .");
		}else if(!sharedFile.exists()){
			throw new FileNotFoundException("Shared File [" + sharedFile.getURL().getPath() + "] was not found .");
		}else if(sharedFile.isFile()){
			if(localFile.length() == sharedFile.getContentLength()){
				throw new IOException("The File [" + localFile.getName() + "] was exists on File sharing server.");
			}else if(localFile.length() < sharedFile.getContentLength()){
				throw new IOException("Remote File [" + sharedFile.getURL().getPath() + "] was bigger than local.");
			}
		}
	}
	
	/**
	 * 追加内容的判断
	 */
	public static void assertAppend(File localFile,SmbFile sharedFile,long skipOffset) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(!localFile.exists()){
			throw new FileNotFoundException("Local File [" + localFile.getAbsolutePath() + "] was not found .");
		}else if(!sharedFile.exists()){
			throw new FileNotFoundException("Shared File [" + sharedFile.getURL().getPath() + "] was not found .");
		}else if(sharedFile.isFile()){
			if(localFile.length() == sharedFile.getContentLength()){
				throw new IOException("The File [" + localFile.getName() + "] was exists on File sharing server.");
			}else if(localFile.length() < sharedFile.getContentLength()){
				throw new IOException("Remote File [" + sharedFile.getURL().getPath() + "] was bigger than local.");
			}
		}else if(localFile.length() < skipOffset){
			throw new EOFException("offset [" + skipOffset + "] larger than the length of local file : " + localFile.getAbsolutePath()); 
		}
	}
	
	public static void assertFile(SmbFile[] sharedFiles,String fileName) throws IOException {
		if (sharedFiles == null || sharedFiles.length == 0) {
			throw new FileNotFoundException("The File [" + fileName + "] was not found on File sharing server.");
        }
	}

	public static void assertFile(SmbFile sharedFile) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		if (!sharedFile.exists()) {
			throw new FileNotFoundException("The File [" + sharedFile.getURL().getPath() + "] was not found on File sharing server.");
        }else if(!sharedFile.isFile()){
			throw new IOException("The File [" + sharedFile.getURL().getPath() + "] was not a File.");
		}else if (sharedFile.getContentLength() > Integer.MAX_VALUE) {
            throw new IOException("The File [" + sharedFile.getURL().getPath() + "] is too large.");
        }
	}

	/**
	 * 下载的判断
	 */
	public static void assertGet(SmbFile sharedFile, long skipOffset) throws IOException{
		Assert.notNull(sharedFile, "sharedFile is null .");
		if(!sharedFile.exists()){
			throw new FileNotFoundException("The File [" + sharedFile.getURL().getPath() + "] was not found on File sharing server.");
		}else if(!sharedFile.isFile()){
			throw new IOException("The File [" + sharedFile.getURL().getPath() + "] was not a File.");
		}else if(sharedFile.getContentLength() < skipOffset){
			throw new EOFException("offset [" + skipOffset + "] larger than the length of localFile : unexpected EOF"); 
		}else if (sharedFile.getContentLength() > Integer.MAX_VALUE) {
            throw new IOException("The File [" + sharedFile.getURL().getPath() + "] is too large.");
        }
	}
	
	/**
	 * 下载的判断
	 */
	public static void assertGet(SmbFile sharedFile,File localFile) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(!sharedFile.exists()){
			throw new FileNotFoundException("The File [" + sharedFile.getURL().getPath() + "] was not found on File sharing server.");
		}else if(!sharedFile.isFile()){
			throw new IOException("The File [" + sharedFile.getURL().getPath() + "] was not a File.");
		}else if(localFile.exists() && localFile.length() > sharedFile.getContentLength()){
			throw new IOException("Local File [" + localFile.getAbsolutePath() + "] was bigger than remote.");
		}
	}
	
	/**
	 * 上传的判断
	 */
	public static void assertPut(SmbFile sharedFile) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		
	}
	
	/**
	 * 上传的判断
	 */
	public static void assertPut(File localFile,SmbFile sharedFile) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(!localFile.exists()){
			throw new FileNotFoundException("The Local File [" + localFile.getAbsolutePath() + "] was not found .");
		}else if(!localFile.isFile()){
			throw new IOException("Local File [" + localFile.getAbsolutePath() + "] was not a File.");
		}else if(sharedFile.exists() && (sharedFile.isFile() || sharedFile.isHidden())){
			if(localFile.length() == sharedFile.getContentLength()){
				throw new IOException("The File [" + localFile.getAbsolutePath() + "] was exists on File sharing server.");
			}else if(localFile.length() < sharedFile.getContentLength()){
				throw new IOException("Remote File [" + sharedFile.getURL().getPath() + "] was bigger than local.");
			}
		}
	}
	
	/**
	 * 上传的判断
	 */
	public static void assertPut(File localFile,SmbFile sharedFile,long skipOffset) throws IOException {
		Assert.notNull(sharedFile, "sharedFile is null .");
		Assert.notNull(localFile, "localFile is null .");
		if(!localFile.exists()){
			throw new FileNotFoundException("Local File [" + localFile.getAbsolutePath() + "] was not found .");
		}else if(sharedFile.exists() && (sharedFile.isFile() || sharedFile.isHidden())){
			if(localFile.length() == sharedFile.getContentLength()){
				throw new IOException("The File [" + localFile.getName() + "] was exists on File sharing server.");
			}else if(localFile.length() < sharedFile.getContentLength()){
				throw new IOException("Remote File [" + sharedFile.getURL().getPath() + "] was bigger than local.");
			}
		}else if(localFile.length() < skipOffset){
			throw new EOFException("offset [" + skipOffset + "] larger than the length of local file : " + localFile.getAbsolutePath()); 
		}
	}
	
	public static boolean assertRead(long totalRead,String fileName) throws IOException {
		if (totalRead == -1) {
			throw new IOException("Can't upload file [" + fileName + "]' to File sharing server. Check File sharing permissions and path.");
		}
		return true;
	}
	
	public static void assertDir(SmbFile smbDir) throws IOException {
		Assert.notNull(smbDir, "smbDir is null .");
		if(!smbDir.exists()){
			//指定的目录不存在
			throw new FileNotFoundException("The Directory [ " + smbDir.getUncPath() + " ] was not found on File sharing server.");
		}else if(!smbDir.isDirectory()){
			throw new IOException("The Directory [" + smbDir.getUncPath() + "] was not a directory.");
		}
	}
	
	public static void assertFile(File localFile) throws IOException {
		if(!localFile.exists()){
			throw new IOException("Local file [" + localFile.getPath() + "] was not found.");
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
