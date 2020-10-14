package com.woshidaniu.smbclient.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.StringUtils;


/**
 * 
 * @className	： SMBPathUtils
 * @description	： 共享文件路径工具
 * @author 		：kangzhidong
 * @date		： Jan 22, 2016 3:16:57 PM
 */
public class SMBPathUtils {
	
	/**
	 * Windows 系统路径分割符号 \
	 */
	public static String SLASHES = "\\";
	/**
	 * Linux，Unix 系统路径分割符号 /
	 */
	public static String BACKSLASHES = "/";

	/**
	 * 
	 * @description	： 读取的共享文件的目录结构及IP地址，如果需要用户权限的话，那么你就要知道用户名和密码是多少。
	 *<pre>
	 *	例1：smb://userName:password@ip/filePath（这种情况是需要用户名密码的情况下输入的条件）
	 *	例2：smb://ip/filePath（这种情况是不需要用户名和密码的）
	 *</pre>
	 * @author 		：kangzhidong
	 * @date 		：Jan 20, 2016 11:20:19 AM
	 * @param username	: 访问共享目录的用户
	 * @param password	: 访问共享目录的用户密码
	 * @param host		: 共享服务地址
	 * @param filepath	 ： 访问共享目录或文件
	 * @return
	 */
	public static String getSharedURL(String username,String password,String host,String filepath){
		StringBuilder builder = new StringBuilder("smb://");
		//访问共享目录的用户和密码
		if(!BlankUtils.isBlank(username) && !BlankUtils.isBlank(password)){
			builder.append(username).append(":").append(password).append("@");
		}
		//共享服务地址
		builder.append(host).append(BACKSLASHES);
		//访问共享目录或文件
		if(!BlankUtils.isBlank(filepath)){
			builder.append(StringUtils.getSafeStr(filepath, "")).append(BACKSLASHES);
		}
		return builder.toString();
	}
	
	/**
	 * 
	 * @description	： 读取的共享文件的目录结构及IP地址
	 *<pre>
	 *	例2：smb://ip/filePath（这种情况是不需要用户名和密码的）
	 *</pre>
	 * @author 		：kangzhidong
	 * @date 		：Jan 20, 2016 11:20:19 AM
	 * @param host		: 共享服务地址
	 * @param filepath	 ： 访问共享目录或文件
	 * @return
	 */
	public static String getSharedURL(String host,String filepath){
		StringBuilder builder = new StringBuilder("smb://");
		//共享服务地址
		builder.append(host).append(BACKSLASHES);
		//访问共享目录或文件
		if(!BlankUtils.isBlank(filepath)){
			builder.append(StringUtils.getSafeStr(filepath, "")).append(BACKSLASHES);
		}
		return builder.toString();
	}
	
	public static String getAnonymousSharedURL(String host,String filepath){
		return SMBPathUtils.getSharedURL(null, null, host, filepath);
	}
	
	public static String getSharedDir(String sharedDir){
		return sharedDir.endsWith(BACKSLASHES) ? sharedDir : sharedDir + BACKSLASHES;
	}
	
	
	/**
	 * 
	 * @description	： 基本路径 /smb/加密内容
	 * @author 		：kangzhidong
	 * @date 		：Jan 18, 2016 5:21:35 PM
	 * @param ftpPath : 基本路径 /smb/加密内容
	 * @return		：路径去除 /smb/后的内容
	 */
	public static String getEncryptPath(String ftpPath,String prefix){
		return ftpPath.substring(prefix.length());
	}
	
	public static File getLocalDir(ServletContext servletContext,String localDir){
		//文件本地存储目录
		File directory = null;
		//从配置文件找到指定的外部存储路径配置
		String directoryPath = SMBPathUtils.getResolvePath(localDir);
		//基于指定路径的file 对象
		directory = new File(directoryPath);
		if( !directory.exists() || !directory.isDirectory()){
			//未找到指定的外部目录，使用本地程序根目录
			directoryPath = servletContext.getRealPath(localDir);
			//得到存储目录
			directory = SMBPathUtils.getExistDir(directoryPath);
		}
		return directory;
	}

	public static String getPath(String ftpPath) throws IOException{
		// 将路径中的斜杠统一
		char[] chars = ftpPath.toCharArray();
		StringBuffer sbStr = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if ('\\' == chars[i]) {
				sbStr.append(BACKSLASHES);
			} else {
				sbStr.append(chars[i]);
			}
		}
		ftpPath = sbStr.toString();
		return ftpPath;
	}
	
	/**
	 * 
	 * @description: 得到处理后的路径
	 * @author : kangzhidong
	 * @date : 2014-3-25
	 * @time : 上午10:23:43
	 * @param path
	 * @return
	 */
	public static String getResolvePath(String path) {
		path = path.replace("\\\\", BACKSLASHES).replace("\\", BACKSLASHES).replace("//", BACKSLASHES).replace("/",	BACKSLASHES);
		if (path.endsWith(BACKSLASHES)) {
			path = path.substring(0, (path.length() - BACKSLASHES.length()));
		}
		return path;
	}
	
	public static File getExistDir(String dirPath ) {
		File directory = new File(getResolvePath(dirPath));
		// 如果目录不存在，则创建根目录
		if (!directory.exists()) {
			directory.setReadable(true);
			directory.setWritable(true);
			directory.mkdirs();
		}
		return directory;
	}
	
}
