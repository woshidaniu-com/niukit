package com.woshidaniu.ftpclient.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import com.woshidaniu.basicutils.BlankUtils;



public class FTPPathUtils {
	/**
	 * Windows 系统路径分割符号 \
	 */
	protected static char SLASHES = '\\';
	/**
	 * Linux，Unix 系统路径分割符号 /
	 */
	protected static char BACKSLASHES = '/';
	/**
	 * 
	 * @description	： 基本路径 /ftp/加密内容
	 * @author 		： kangzhidong
	 * @date 		：Jan 18, 2016 5:21:35 PM
	 * @param ftpPath : 基本路径 /ftp/加密内容
	 * @return		：路径去除 /ftp/后的内容
	 */
	public static String getEncryptPath(String ftpPath,String prefix){
		return ftpPath.substring(prefix.length());
	}
	
	public static File getLocalDir(ServletContext servletContext,String localDir){
		//文件本地存储目录
		File directory = null;
		//从配置文件找到指定的外部存储路径配置
		String directoryPath = FTPPathUtils.getResolvePath(localDir);
		//基于指定路径的file 对象
		directory = new File(directoryPath);
		if( !directory.exists() || !directory.isDirectory()){
			//未找到指定的外部目录，使用本地程序根目录
			directoryPath = servletContext.getRealPath(localDir);
			//得到存储目录
			directory = FTPPathUtils.getExistDir(directoryPath);
		}
		return directory;
	}
	
	public static String getRootDir(String rootDir) throws IOException{
		if(BlankUtils.isBlank(rootDir)){
			return "" + BACKSLASHES;
		}
		// 将路径中的斜杠统一
		char[] chars = rootDir.toCharArray();
		StringBuffer sbStr = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (SLASHES == chars[i]) {
				sbStr.append(BACKSLASHES);
			} else {
				sbStr.append(chars[i]);
			}
		}
		//返回根目录
		return sbStr.toString();
	}

	public static String getPath(String ftpPath) throws IOException{
		// 将路径中的斜杠统一
		char[] chars = ftpPath.toCharArray();
		StringBuffer sbStr = new StringBuffer();
		for (int i = 0; i < chars.length; i++) {
			if (SLASHES == chars[i]) {
				sbStr.append(BACKSLASHES);
			} else {
				sbStr.append(chars[i]);
			}
		}
		ftpPath = sbStr.toString();
		return ftpPath.startsWith("" + BACKSLASHES) ? ftpPath.substring(1) : ftpPath;
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
		path = path.replace("\\\\", File.separator).replace("\\",File.separator).replace("//", File.separator).replace("/",	File.separator);
		if (path.endsWith(File.separator)) {
			path = path.substring(0, (path.length() - File.separator.length()));
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
