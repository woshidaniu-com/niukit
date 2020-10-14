package com.woshidaniu.ftpclient.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletResponse;

import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ExceptionUtils;
import com.woshidaniu.ftpclient.FTPClient;
/**
 * 
 * @className	： FTPClientUtils
 * @description	： FTPClient常用方法：
 * <pre>
 * 常用方法：
 *   void setControlEncoding(String encoding)：设置FTP控制连接的编码方式(默认读取中文文件名时为乱码)
 *   boolean changeWorkingDirectory(String pathname)：设置当前的工作目录
 *   boolean changeToParentDirectory()：返回上级目录
 *   void setRestartOffset(long offset)：设置重新启动的偏移量(用于断点续传)
 * 下载文件：
 *   boolean retrieveFile(String,remote,OutputStream local)：从服务器返回指定名称的文件并且写入到OuputStream，以便写入到文件或其它地方。
 *   InputStream retrieveFileStream(String remote)：从服务器返回指定名称的文件的InputStream以便读取。
 * 上传文件：
 *   boolean storeFile(String remote,InputStream local)：利用给定的名字(remote)和输入流(InputStream)向服务器上传一个文件。
 *   OutputStream storeFileStream(String remote)：根据给定的名字返回一个能够向服务器上传文件的OutputStream。
 *   boolean storeUniqueFile(InputStream local)：根据服务器自己指定的唯一的名字和输入流InputStream向服务器上传一个文件。
 *   boolean storeUniqueFile(String remote,InputStream local)：根据指定的名字和输入流InputStream向服务器上传一个文件。
 *   OuputStream storeUniqueFileStream()：返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由服务器自己命名。
 *   OutputStream storeUniqueFileStream(String remote)：返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由用户自己指定。
 * </pre>
 * @author 		： kangzhidong
 * @date		： Jan 13, 2016 2:31:28 PM
 */
@SuppressWarnings("resource")
public class FTPClientUtils {

	protected static Logger LOG = LoggerFactory.getLogger(FTPClientUtils.class);
	
	/**
	 * 
	 * @description	：<pre> 追加文件至FTP服务器：
	 * ftpClient.appendFile(remote, local)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(FTPClient ftpClient,String ftpFileName, File localFile) throws IOException{
		InputStream input = null;
		try {
			FTPFile ftpFile = FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
			//异常检查
			FTPExceptionUtils.assertPut( localFile, ftpFile, ftpFileName);
			// 包装文件输入流  
			input = IOUtils.toBufferedInputStream(localFile, ftpClient.getBufferSize());
			//断点上传输入流
			return FTPClientUtils.appendStream(ftpClient, ftpFileName, input, localFile.length() - ftpFile.getSize());
		} finally {
        	//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 追加文件至FTP服务器：
	 * ftpClient.appendFile(remote, local)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(FTPClient ftpClient,String ftpFileName, File localFile,long skipOffset) throws IOException{
		InputStream input = null;
		try {
			FTPFile ftpFile = FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
			//异常检查
			FTPExceptionUtils.assertPut(localFile, ftpFile, ftpFileName ,skipOffset);
			// 包装文件输入流  
			input = IOUtils.toBufferedInputStream(localFile, ftpClient.getBufferSize());
			//断点上传输入流
			return FTPClientUtils.appendStream(ftpClient, ftpFileName, input, skipOffset);
		} finally {
        	//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 追加输入流至FTP服务器：
	 * ftpClient.appendFile(remote, local)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param input			：输入流
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendStream(FTPClient ftpClient,String ftpFileName,InputStream input,long skipOffset) throws IOException{
		try {
			boolean hasFile = FTPClientUtils.hasFile(ftpClient,ftpFileName);
			//异常检查
			FTPExceptionUtils.assertFile(hasFile, ftpFileName);
			try {
				// 跳过已经存在的长度,实现断点续传  
				IOUtils.skip(input, skipOffset);
			} catch (Exception e) {
				LOG.error(ExceptionUtils.getFullStackTrace(e));
				return false;
			}
			// 追加文件内容
			boolean hasAppend = ftpClient.appendFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName), input);
			//异常检查
			FTPExceptionUtils.assertAppend(hasAppend, ftpFileName);
			return true;
		} finally {
        	//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	： 切换目录至指定目录，如果指定目录不存在则切换回原目录，并返回结果标志
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 8:26:14 PM
	 * @param ftpClient
	 * @param ftpDir
	 * @return
	 * @throws IOException
	 */
	public static boolean changeDirectory(FTPClient ftpClient,String ftpDir) throws IOException{
		if (!BlankUtils.isBlank(ftpDir)) {
			//路径分割符
			String separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());
			//FTP服务根文件目录
			String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
			//转换目录为目标服务器目录格式
			String tempDir = FTPPathUtils.getPath(ftpDir);
			//转码后的目录名称
			String remoteDir = FTPStringUtils.getRemoteName(ftpClient,tempDir);
			//验证是否有该文件夹，有就转到，没有创建后转到该目录下
			boolean hasChanged = ftpClient.changeWorkingDirectory(remoteDir);
			ftpClient.printWorkingDirectory();
			//没有找到指定的目录，则逐级的创建目录（考虑兼容问题，逐级创建目录）
			if (!hasChanged && tempDir.indexOf(separator) != -1) {
				// 多层目录循环切换
				boolean hasNext = true;
				int index = 0;
				String dir = null;
				while (hasNext) {
					//是否包含/
					index = tempDir.indexOf(separator);
					//判断是否多级目录
					if(index != -1){
						dir = FTPStringUtils.getRemoteName(ftpClient,tempDir.substring(0, index));
						//子目录切换失败，表示不存在
						hasChanged = ftpClient.changeWorkingDirectory(dir);
						ftpClient.printWorkingDirectory();
						tempDir = tempDir.substring(index + 1, tempDir.length());
						hasNext = hasChanged;
					}else{
						//只有一层目录
						dir = FTPStringUtils.getRemoteName(ftpClient,tempDir);
						hasChanged = ftpClient.changeWorkingDirectory(dir);
						ftpClient.printWorkingDirectory();
						hasNext = false;
					}
				}
			}
			return hasChanged;
		}
		return false;
	}
	
	/**
	 * 
	 * @description	：切换目录至指定目录，如果指定目录不存在创建目录，并返回结果标志
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 8:27:34 PM
	 * @param ftpClient
	 * @param ftpDir
	 * @return
	 * @throws IOException
	 */
	public static boolean changeExistsDirectory(FTPClient ftpClient,String ftpDir) throws IOException{
		if(ftpDir == null){
			return false;
		}
		//路径分割符
		String separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//转换目录为目标服务器目录格式
		ftpDir = FTPPathUtils.getPath(ftpDir);
		//转码后的目录名称
		String remoteDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir);
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//验证是否有该文件夹，有就转到，没有创建后转到该目录下
		boolean hasChanged = ftpClient.changeWorkingDirectory(remoteDir);
		ftpClient.printWorkingDirectory();
		//没有找到指定的目录，则逐级的创建目录（考虑兼容问题，逐级创建目录）
		if (!hasChanged) {
			// 跳转到指定的文件目录
			String tempDir = FTPPathUtils.getPath(ftpDir);
			// 多层目录循环切换
			boolean hasNext = true;
			int index = 0;
			String dir = null;
			while (hasNext) {
				//是否包含/
				index = tempDir.indexOf(separator);
				//判断是否多级目录
				if(index != -1){
					dir = FTPStringUtils.getRemoteName(ftpClient,tempDir.substring(0, index));
					//子目录切换失败，表示不存在
					hasChanged = ftpClient.changeWorkingDirectory(dir);
					ftpClient.printWorkingDirectory();
					if(!hasChanged){
						ftpClient.makeDirectory(dir);
						hasChanged = ftpClient.changeWorkingDirectory(dir);
						ftpClient.printWorkingDirectory();
					}
					tempDir = tempDir.substring(index + 1, tempDir.length());
					hasNext = hasChanged;
				}else{
					//只有一层目录
					dir = FTPStringUtils.getRemoteName(ftpClient,tempDir);
					hasChanged = ftpClient.changeWorkingDirectory(dir);
					ftpClient.printWorkingDirectory();
					if(!hasChanged){
						ftpClient.makeDirectory(dir);
						hasChanged = ftpClient.changeWorkingDirectory(dir);
						ftpClient.printWorkingDirectory();
					}
					hasNext = false;
				}
			}
		}
		return hasChanged;
	}
	
	public static FTPFile getFTPFile(FTPClient ftpClient,String ftpFileName) throws IOException{
		//发送 LIST 命令至 FTP 站点，使用系统默认的机制列出当前工作目录的文件信息
		FTPFile[] files = ftpClient.listFiles(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
		//异常检查
		FTPExceptionUtils.assertFile(files, ftpFileName);
		FTPFile fileTmp = files[0];
		//异常检查
		FTPExceptionUtils.assertFile(fileTmp, ftpFileName);
		return fileTmp;
	}
	
	public static FTPFile getFTPFile(FTPClient ftpClient,String ftpDir,String ftpFileName) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			return FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static String[] listNames(FTPClient ftpClient,String ftpDir) throws IOException{
		//转码后的目录名称
		String remoteDir = FTPStringUtils.getRemoteName(ftpClient, ftpDir);
		//发送 LIST 命令至 FTP 站点，使用系统默认的机制列出当前工作目录的文件信息
		String[] fileNames = ftpClient.listNames(remoteDir);
		if (fileNames != null && fileNames.length > 0) {
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i] != null) {
					fileNames[i] = FTPStringUtils.getLocalName(ftpClient,fileNames[i]);
				}
			}
		}
        return fileNames;
	}
	
	public static List<FTPFile> listFiles(FTPClient ftpClient,String ftpDir) throws IOException{
		return listFiles(ftpClient, ftpDir, false);
	}
	
	public static List<FTPFile> listFiles(FTPClient ftpClient,String ftpDir,boolean recursion) throws IOException{
		//路径分割符
		String separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//创建文件类型的文件集合
		List<FTPFile> fileList = new ArrayList<FTPFile>();
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//发送 LIST 命令至 FTP 站点，使用系统默认的机制列出当前工作目录的文件信息
			FTPFile[] files = ftpClient.listFiles();
			//异常检查
			FTPExceptionUtils.assertDir(files, ftpDir);
			for(FTPFile ftpFile :files){
				String fileName = FTPStringUtils.getLocalName(ftpClient,ftpFile);
				if (ftpFile.isDirectory()) {
					if(recursion){
						fileList.addAll(FTPClientUtils.listFiles(ftpClient, ftpDir + separator + fileName));
					}else{
						fileList.add(ftpFile);
					}
				} else{
					fileList.add(ftpFile);
				}
			}
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
		return fileList;
	}
	
	public static List<FTPFile> listFiles(FTPClient ftpClient,String ftpDir, String[] extensions,boolean recursion) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//创建文件类型的文件集合
		List<FTPFile> fileList = new ArrayList<FTPFile>();
		try {
			//切换目录
			boolean hasChanged = changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//发送 LIST 命令至 FTP 站点，使用系统默认的机制列出当前工作目录的文件信息
			Collection<FTPFile> files = FTPFileUtils.listFiles(ftpClient.listFiles(), extensions);
			if(files != null && files.size() > 0){
				//循环文件
				for(FTPFile ftpFile :files){
					if (ftpFile.isDirectory()) {
						fileList.addAll( FTPFileUtils.listFiles(ftpClient, ftpFile, extensions, recursion ));
					} else{
						fileList.add(ftpFile);
					}
				}
			}
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
		return fileList;
	}
	
	public static List<FTPFile> listFiles(FTPClient ftpClient,String ftpDir, FTPFileFilter filter,boolean recursion) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//创建文件类型的文件集合
		List<FTPFile> fileList = new ArrayList<FTPFile>();
		try {
			//切换目录
			boolean hasChanged = changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//发送 LIST 命令至 FTP 站点，使用系统默认的机制列出当前工作目录的文件信息
			Collection<FTPFile> files = FTPFileUtils.listFiles(ftpClient.listFiles(), filter );
			if(files != null && files.size() > 0){
				//循环文件
				for(FTPFile ftpFile :files){
					if (ftpFile.isDirectory()) {
						fileList.addAll( FTPFileUtils.listFiles(ftpClient, ftpFile, filter, recursion ));
					} else{
						fileList.add(ftpFile);
					}
				}
			}
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
		return fileList;
	}
	
	public static boolean makeRootDir(FTPClient ftpClient,String ftpDir) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//转换目录为目标服务器目录格式
		ftpDir = FTPPathUtils.getPath(ftpDir);
		//转码后的目录名称
		String remoteDir = FTPStringUtils.getRemoteName(ftpClient, ftpDir);
		//验证是否有该文件夹，有就转到，没有创建后转到该目录下
		if (ftpClient.changeWorkingDirectory(remoteDir)) {
			ftpClient.printWorkingDirectory();
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
			return true;
		}
		//在当前工作目录下新建子目录
		return ftpClient.makeDirectory(remoteDir);
	}
	
	/**
	 * 
	 * @description	： 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 10:06:25 AM
	 * @param ftpClient
	 * @param ftpDir
	 * @return
	 * @throws IOException
	 */
	public static boolean makeDirectory(FTPClient ftpClient,String ftpDir) throws IOException{
		//路径分割符
		String separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//转换目录为目标服务器目录格式
		ftpDir = FTPPathUtils.getPath(ftpDir);
		//验证是否有该文件夹，有就转到，没有创建后转到该目录下
		String remoteDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir);
		if (ftpClient.changeWorkingDirectory(remoteDir) ) {
			ftpClient.printWorkingDirectory();
			return true;
		} 
		try {
			// 跳转到指定的文件目录
			boolean hasMaked = false;
			if (ftpDir != null && !ftpDir.equals("")) {
				String parentDir = null;
				// 多层目录循环切换
				if (ftpDir.indexOf(separator) != -1) {
					int index = 0;
					boolean hasNext = true;
					while ((index = ftpDir.indexOf(separator)) != -1 && hasNext) {
						//转码后的目录名称
						parentDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir.substring(0, index));
						//创建目录
						hasMaked = ftpClient.makeDirectory(parentDir);
						//切换至新建目录
						ftpClient.changeWorkingDirectory(parentDir);
						ftpClient.printWorkingDirectory();
						//下一级目录
						ftpDir = ftpDir.substring(index + 1, ftpDir.length());
						hasNext = hasMaked;
					}
					if (!ftpDir.equals("")) {
						//转码后的目录名称
						parentDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir);
						//创建目录
						hasMaked = ftpClient.makeDirectory(parentDir);
						//切换至新建目录
						ftpClient.changeWorkingDirectory(parentDir);
						ftpClient.printWorkingDirectory();
					}
				} else {// 只有一层目录
					
					//转码后的目录名称
					parentDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir);
					//创建目录
					hasMaked = ftpClient.makeDirectory(parentDir);
					//切换至新建目录
					ftpClient.changeWorkingDirectory(parentDir);
					ftpClient.printWorkingDirectory();
				}
			}
			return hasMaked;
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	/**
	 * 
	 * @description	：  在指定目录创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 10:07:13 AM
	 * @param ftpClient
	 * @param parentDir
	 * @param ftpDir
	 * @return
	 * @throws IOException
	 */
	public static boolean makeDirectory(FTPClient ftpClient,String parentDir,String ftpDir) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//转换目录为目标服务器目录格式
			parentDir = FTPPathUtils.getPath(parentDir);
			//转码后的目录名称
			String remoteParentDir = FTPStringUtils.getRemoteName(ftpClient,parentDir);
			//验证是否有该文件夹，有就转到，没有创建后转到该目录下
			if (!ftpClient.changeWorkingDirectory(remoteParentDir)) {
				ftpClient.makeDirectory(remoteParentDir);
				ftpClient.changeWorkingDirectory(remoteParentDir);
				ftpClient.printWorkingDirectory();
			}
			//转换目录为目标服务器目录格式
			String tempDir = FTPPathUtils.getPath(ftpDir);
			//转码后的目录名称
			return ftpClient.makeDirectory(FTPStringUtils.getRemoteName(ftpClient,tempDir));
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	
	
	public static InputStream getInputStream(FTPClient ftpClient,FTPFile ftpFile,long skipOffset) throws IOException{
		//设置接收数据流的起始位置
		ftpClient.setRestartOffset(Math.max(0, skipOffset));
		/**
		 * 从服务器返回指定名称的文件的InputStream以便读取;可能Socket每次接收8KB 
		 * 如果当前文件类型是ASCII，返回的InputStream将转换文件中的行分隔符到本地的代表性。 
		 * 您必须关闭InputStream的当你完成从它读。 本身的InputStream将被关闭，关闭后父数据连接插座的照顾。
		 * 为了完成文件传输你必须调用completePendingCommand并检查它的返回值来验证成功。 
		 */
		InputStream buffInput = IOUtils.toBufferedInputStream(ftpClient.retrieveFileStream(ftpFile.getName()), ftpClient.getBufferSize());
		//获得InputStream
		return FTPStreamUtils.toWrapedInputStream(buffInput, ftpClient);
	}
	
	public static InputStream getInputStream(FTPClient ftpClient,String ftpDir,String ftpFileName,long skipOffset) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			InputStream buffInput = null;
			if(FTPClientUtils.hasFile(ftpClient, ftpDir, ftpFileName)){
				//设置接收数据流的起始位置
				ftpClient.setRestartOffset(Math.max(0, skipOffset));
				/**
				 * 从服务器返回指定名称的文件的InputStream以便读取。 
				 * 如果当前文件类型是ASCII，返回的InputStream将转换文件中的行分隔符到本地的代表性。 
				 * 您必须关闭InputStream的当你完成从它读。 本身的InputStream将被关闭，关闭后父数据连接插座的照顾。
				 * 为了完成文件传输你必须调用completePendingCommand并检查它的返回值来验证成功。 
				 */
				buffInput = IOUtils.toBufferedInputStream(ftpClient.retrieveFileStream(FTPStringUtils.getRemoteName(ftpClient,ftpFileName)), ftpClient.getBufferSize());
			}
			//获得InputStream
			return FTPStreamUtils.toWrapedInputStream(buffInput, ftpClient);
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static OutputStream getOutputStream(FTPClient ftpClient,String ftpFileName) throws IOException {
		//获得OutputStream
		return FTPClientUtils.getOutputStream(ftpClient, ftpFileName, false);
	}
	
	public static OutputStream getOutputStream(FTPClient ftpClient,String ftpFileName,boolean unique) throws IOException {
		//转码后的文件名
		String remoteName = FTPStringUtils.getRemoteName(ftpClient,ftpFileName);
		//带缓冲的输出流
		OutputStream buffOutput = null;
		//文件存在
		if(FTPClientUtils.hasFile(ftpClient,ftpFileName)){
			//返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由用户自己指定
			buffOutput = IOUtils.toBufferedOutputStream(ftpClient.appendFileStream(remoteName), ftpClient.getBufferSize());
		}else{
			//返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由用户自己指定
			if(unique){
				buffOutput = IOUtils.toBufferedOutputStream(ftpClient.storeUniqueFileStream(ftpFileName), ftpClient.getBufferSize()); 
			}else{
				buffOutput = IOUtils.toBufferedOutputStream(ftpClient.storeFileStream(ftpFileName), ftpClient.getBufferSize());
			}
		}
		//获得OutputStream
		return FTPStreamUtils.toWrapedOutputStream(buffOutput, ftpClient);
	}
	
	public static OutputStream getOutputStream(FTPClient ftpClient,String ftpDir,String ftpFileName) throws IOException {
		//获得OutputStream
		return FTPClientUtils.getOutputStream(ftpClient, ftpDir, ftpFileName, false);
	}
	
	public static OutputStream getOutputStream(FTPClient ftpClient,String ftpDir,String ftpFileName,boolean unique) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//转码后的文件名
			String remoteName = FTPStringUtils.getRemoteName(ftpClient,ftpFileName);
			//带缓冲的输出流
			OutputStream buffOutput = null;
			//文件存在
			if(FTPClientUtils.hasFile(ftpClient, ftpDir, ftpFileName)){
				//返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由用户自己指定
				buffOutput = IOUtils.toBufferedOutputStream(ftpClient.appendFileStream(remoteName), ftpClient.getBufferSize());
			}else{
				//返回一个输出流OutputStream,以便向服务器写入一个文件，该文件由用户自己指定
				if(unique){
					buffOutput = IOUtils.toBufferedOutputStream(ftpClient.storeUniqueFileStream(ftpFileName), ftpClient.getBufferSize()); 
				}else{
					buffOutput = IOUtils.toBufferedOutputStream(ftpClient.storeFileStream(ftpFileName), ftpClient.getBufferSize());
				}
			}
			//获得OutputStream
			return FTPStreamUtils.toWrapedOutputStream(buffOutput, ftpClient);
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static String getParentDirectory(FTPClient ftpClient) throws IOException{
		//当前目录
		String currentDir = ftpClient.printWorkingDirectory();
		ftpClient.changeToParentDirectory();
		currentDir = ftpClient.printWorkingDirectory();
        return currentDir;
	}
	
	/**
	 * 
	 * @description	：检查指定目录是否存在
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 8:30:03 PM
	 * @param ftpClient
	 * @param ftpDir
	 * @return
	 * @throws IOException
	 */
	public static boolean hasDirectory(FTPClient ftpClient,String ftpDir) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			return FTPClientUtils.changeDirectory(ftpClient, ftpDir);
		} finally {
			//切换目录至根目录
        	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	/**
	 * 
	 * @description	： 检查指定文件是否存在
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 8:30:31 PM
	 * @param ftpClient
	 * @param ftpDir
	 * @param ftpFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean hasFile(FTPClient ftpClient,String ftpDir, String ftpFileName) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			boolean hasFile = false;
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//目录未切换成功;表示指定的目录不存在
			if(hasChanged){
				hasFile = FTPClientUtils.hasFile(ftpClient, ftpFileName);
			}
			return hasFile;
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static boolean hasFile(FTPClient ftpClient,String ftpFileName) throws IOException{
		boolean hasFile = false;
		String[] fileNames = ftpClient.listNames();
		if (fileNames != null && fileNames.length > 0) {
			for (int i = 0; i < fileNames.length; i++) {
				if (fileNames[i] != null && FTPStringUtils.getLocalName(ftpClient,fileNames[i]).equals(ftpFileName)) {
					hasFile = true;
					break;
				}
			}
		}
		return hasFile;
	}
	
	public static boolean remove(FTPClient ftpClient,String ftpFileName) throws IOException {
		//删除 FTP 站点上的一个指定文件
		boolean hasDel = ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
		//异常检查
		FTPExceptionUtils.assertDel(hasDel, ftpFileName);
		return hasDel;
	}
	
	public static boolean remove(FTPClient ftpClient,String[] ftpFiles) throws IOException{
		for(String ftpFileName : ftpFiles){
			//删除 FTP 站点上的一个指定文件
			boolean hasDel = ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
			if (!hasDel) {
				LOG.error("Can't remove file [" + ftpFileName + "] from FTP server.");
	        }
		}
        return true;
	}
	
	public static boolean remove(FTPClient ftpClient,String ftpDir, String ftpFileName) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		//删除 FTP 站点上的一个指定文件
		boolean hasDel = false;
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//删除文件
			hasDel = ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
			//异常检查
			FTPExceptionUtils.assertDel(hasDel, ftpFileName);
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
        return hasDel;
	}
	
	public static boolean remove(FTPClient ftpClient,String ftpDir, String[] ftpFiles) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			for(String ftpFileName:ftpFiles){
				//删除 FTP 站点上的一个指定文件
				boolean hasDel = ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
				if (!hasDel) {
					LOG.error("Can't remove file '" + ftpFileName + "' from FTP server.");
			    }
			}
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
        return true;
	}
	
	public static boolean removeDirectory(FTPClient ftpClient,String ftpDir) throws IOException {
		//切换目录
		boolean hasDir = FTPClientUtils.hasDirectory(ftpClient, ftpDir);
		//异常检查
		FTPExceptionUtils.assertDir(hasDir, ftpDir);
		//切换到父级目录
		ftpClient.changeToParentDirectory();
		return ftpClient.removeDirectory(FTPStringUtils.getRemoteName(ftpClient,ftpDir));
	}
	
	// delete all subDirectory and files.
	public static boolean removeDirectory(FTPClient ftpClient,String ftpDir, boolean isAll) throws IOException {
		
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasDir = FTPClientUtils.hasDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasDir, ftpDir);
			//转换目录为目标服务器目录格式
			ftpDir = FTPPathUtils.getPath(ftpDir);
			//转码后的目录名称
			String remoteDir = FTPStringUtils.getRemoteName(ftpClient,ftpDir);
			if (!isAll) {
				ftpClient.changeToParentDirectory();
				//异常检查
				FTPExceptionUtils.assertRomve(ftpClient.removeDirectory(remoteDir), ftpDir);
			}
			FTPFile[] ftpFileArr = ftpClient.listFiles(remoteDir);
			if (ftpFileArr == null || ftpFileArr.length == 0) {
				ftpClient.changeToParentDirectory();
				return ftpClient.removeDirectory(remoteDir);
			}
			//路径分割符
			String separator = FTPConfigurationUtils.getFileSeparator(ftpClient.getClientConfig());
			//切换到指定目录
			ftpClient.changeWorkingDirectory(remoteDir);
			ftpClient.printWorkingDirectory();
			for (FTPFile ftpFile : ftpFileArr) {
				String fileName = FTPStringUtils.getLocalName(ftpClient,ftpFile);
				if (ftpFile.isDirectory()) {
					LOG.info("Delete subDir ["+ftpDir + separator + fileName+"]");				
					FTPClientUtils.removeDirectory(ftpClient, ftpDir + separator + fileName, true);
				} else if (ftpFile.isFile()) {
					if(ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient, fileName))){
						LOG.info("Delete file [" + ftpDir + separator + fileName + "]");
					}
				} else if (ftpFile.isSymbolicLink()) {

				} else if (ftpFile.isUnknown()) {
					
				}
			}
			return ftpClient.removeDirectory(remoteDir);
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static void retrieveToDir(FTPClient ftpClient,String ftpDir,File localDir) throws Exception{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//遍历当前目录下的文件
			List<FTPFile> fileList = FTPClientUtils.listFiles(ftpClient,ftpDir);
			//切换到指定目录
			FTPClientUtils.changeDirectory(ftpClient, ftpDir);
			//循环下载文件
			for(FTPFile ftpFile :fileList){
				//解码后的文件名
				String fileName = FTPStringUtils.getLocalName(ftpClient,ftpFile);
				if(ftpFile.isFile()){
					//写FTPFile到指定文件路径
					FTPClientUtils.retrieveToFile(ftpClient, ftpFile, new File(localDir,fileName));
				}else if(ftpFile.isDirectory()){
					File newDir = new File(localDir ,fileName);
					if (!newDir.exists()) {
						newDir.mkdirs();
					}
					FTPClientUtils.retrieveToDir(ftpClient, ftpDir, localDir);
				}
			}
		} finally {
			//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static void retrieveToFile(FTPClient ftpClient,FTPFile ftpFile,File localFile) throws IOException{
		//解码后的文件名
		String fileName = FTPStringUtils.getLocalName(ftpClient,ftpFile);
		//异常检查
		FTPExceptionUtils.assertFile(ftpFile, fileName);
		InputStream input = null;
		FileChannel outChannel = null;
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			if(!localFile.exists()){
				File dir = localFile.getParentFile();
				if(!dir.exists()){
					dir.mkdirs();
				}
				localFile.setReadable(true);
				localFile.setWritable(true);
				localFile.createNewFile();
			}
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			outChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//初始化进度监听
			FTPCopyListenerUtils.initCopyListener(ftpClient, fileName);
			//获得InputStream
			input = FTPClientUtils.getInputStream(ftpClient, ftpFile, outChannel.size());
			//将InputStream写到FileChannel
			FTPChannelUtils.copyLarge(input, outChannel,ftpClient);
		} finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        	//关闭输出通道
        	IOUtils.closeQuietly(outChannel);
        	//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
        }
	}

	public static void retrieveToFile(FTPClient ftpClient,String ftpFileName,File localFile) throws IOException {
		//检测文件是否存在
		FTPFile ftpFile = FTPClientUtils.getFTPFile( ftpClient, ftpFileName);
		if(ftpFile != null){
			//下载FTPFile到指定路径
			FTPClientUtils.retrieveToFile(ftpClient, ftpFile, localFile);
		}
	}
	
	public static void retrieveToFile(FTPClient ftpClient,String ftpDir, String ftpFileName,File localFile) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//转换目录为目标服务器目录格式
			ftpDir = FTPPathUtils.getPath(ftpDir);
			//检测文件是否存在
			FTPFile ftpFile = FTPClientUtils.getFTPFile( ftpClient, ftpDir ,ftpFileName);
			if(ftpFile != null){
				//切换目录
				ftpClient.changeWorkingDirectory(FTPStringUtils.getRemoteName(ftpClient,ftpDir));
				ftpClient.printWorkingDirectory();
				//下载FTPFile到指定路径
				FTPClientUtils.retrieveToFile(ftpClient, ftpFile, localFile);
			}
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static void retrieveToStream(FTPClient ftpClient,String ftpDir, String ftpFileName,OutputStream output) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//检测文件是否存在
			if(FTPClientUtils.hasFile(ftpClient, ftpDir, ftpFileName)){
				//切换目录
				FTPClientUtils.changeDirectory(ftpClient, ftpDir);
				//从服务器返回指定名称的文件并且写入到OuputStream，以便写入到文件或其它地方（基于buffer）
	            boolean hasGet = ftpClient.retrieveFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName), output);
	            //异常检查
	    		FTPExceptionUtils.assertGet(hasGet, ftpFileName);
				//刷新输出
	            output.flush();
			}
        } finally {
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        	//切换目录至根目录
	    	ftpClient.changeWorkingDirectory(rootDir);
        }
	}

	public static void retrieveToStream(FTPClient ftpClient,String ftpFileName,OutputStream output) throws IOException {
		try {
			//检测文件是否存在
			if(FTPClientUtils.hasFile(ftpClient,ftpFileName)){
				//从服务器返回指定名称的文件并且写入到OuputStream，以便写入到文件或其它地方（基于buffer）
				boolean hasGet = ftpClient.retrieveFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName), output);
				//异常检查
	    		FTPExceptionUtils.assertGet(hasGet, ftpFileName);
				//刷新输出
	            output.flush();
			}
        } finally {
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	public static void retrieveToResponse(FTPClient ftpClient,String ftpDir, String ftpFileName,ServletResponse response) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//转换目录为目标服务器目录格式
			ftpDir = FTPPathUtils.getPath(ftpDir);
			//检测文件是否存在
			if(FTPClientUtils.hasFile(ftpClient, ftpDir, ftpFileName)){
				// 转换路径
				String targetDir = FTPStringUtils.getRemoteName(ftpClient, ftpDir );
				//切换目录
				ftpClient.changeWorkingDirectory(targetDir);
				ftpClient.printWorkingDirectory();
				//从服务器返回指定名称的文件并且写入到OuputStream，以便写入到文件或其它地方（基于buffer）
	            boolean hasGet = ftpClient.retrieveFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName), response.getOutputStream());
	            //异常检查
	    		FTPExceptionUtils.assertGet(hasGet, ftpFileName);
			}
		} finally {
			//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
		}
	}
	
	public static void retrieveToResponse(FTPClient ftpClient,String ftpFileName,ServletResponse response) throws IOException {
		try {
			//检测文件是否存在
			if(FTPClientUtils.hasFile(ftpClient,ftpFileName)){
				//从服务器返回指定名称的文件并且写入到OuputStream，以便写入到文件或其它地方（基于buffer）
				boolean hasGet = ftpClient.retrieveFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName), response.getOutputStream());
				//异常检查
	    		FTPExceptionUtils.assertGet(hasGet, ftpFileName);
				//刷新输出
	    		response.flushBuffer();;
			}
		} finally {
		}
	}
	
	/**
	 * 
	 * @description	：  <pre> 上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeUniqueFile(remote, local)
	 * ftpClient.storeFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 15, 2016 4:14:54 PM
	 * @param ftpClient
	 * @param ftpFileName
	 * @param localFile
	 * @param unique
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(FTPClient ftpClient,String ftpFileName,File localFile,boolean unique) throws IOException{
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		InputStream input = null;
		try {
			// 包装文件输入流  
			input = IOUtils.toBufferedInputStream(localFile, ftpClient.getBufferSize());
			// 追加文件内容
			return FTPClientUtils.storeStream(ftpClient, ftpFileName, input, true, unique);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.deleteFile(remote)
	 * ftpClient.storeUniqueFile(remote, local)
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(FTPClient ftpClient,String ftpDir,String ftpFileName, File localFile,boolean delIfExists) throws IOException {
		return FTPClientUtils.storeFile(ftpClient, ftpDir, ftpFileName, localFile, delIfExists, false);
	}
	
	/**
	 * 
	 * @description	：<pre> 上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.deleteFile(remote)
	 * ftpClient.storeUniqueFile(remote, local)
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(FTPClient ftpClient,String ftpDir,String ftpFileName, File localFile,boolean delIfExists,boolean unique) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//检查文件是否存在
			if(FTPClientUtils.hasFile(ftpClient, ftpDir, ftpFileName)){
				//切换目录
				FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
				//同名文件存在，要求删除
				if(delIfExists){
					//删除 FTP 站点上的一个指定文件
					boolean hasDel = ftpClient.deleteFile(FTPStringUtils.getRemoteName(ftpClient,ftpFileName));
					//异常检查
					FTPExceptionUtils.assertDel(hasDel, ftpFileName);
					//上传完整文件
					return FTPClientUtils.storeFile(ftpClient, ftpFileName, localFile, unique);
				}else{
					//获取文件信息
					FTPFile ftpFile = FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
					//异常检查
					FTPExceptionUtils.assertPut(localFile, ftpFile ,ftpFileName);
					//断点上传文件
					return FTPClientUtils.appendFile(ftpClient, ftpFileName, localFile, ftpFile.getSize());
				}
			}
			//切换目录
			FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//上传完整文件
			return FTPClientUtils.storeFile(ftpClient, ftpFileName, localFile, unique);
        } finally {
        	//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，采用方法：
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:22:43 AM
	 * @param localFile		：本地文件
	 * @param ftpClient		： FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpFileName) throws IOException {
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpFileName );
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpFileName,boolean delIfExists) throws IOException {
		//从File中读取数据写出到OutputStream
		return FTPClientUtils.storeFileChannel(ftpClient,localFile, ftpFileName, delIfExists , false);
	}
	
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpFileName,boolean delIfExists,boolean unique) throws IOException {
		FileChannel inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpFileName ,delIfExists , unique);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，采用方法：
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:22:43 AM
	 * @param localFile		：本地文件
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpDir,String ftpFileName) throws IOException {
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpDir, ftpFileName );
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	

	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeFileStream(remote)
	 * ftpClient.appendFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:22:43 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpDir,String ftpFileName, boolean delIfExists) throws IOException {
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpDir, ftpFileName, delIfExists);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeFileStream(remote)
	 * ftpClient.appendFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:22:43 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param localFile		：文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,File localFile,String ftpDir,String ftpFileName, boolean delIfExists,boolean unique) throws IOException {
		//异常检查
		FTPExceptionUtils.assertPut(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpDir, ftpFileName, delIfExists , unique);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，采用方法：
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:11:13 PM
	 * @param inChannel		：文件通道
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpFileName) throws IOException {
		OutputStream output = null;
		try {
			//如果文件存在，则先删除
			if(FTPClientUtils.hasFile(ftpClient, ftpFileName)){
				FTPClientUtils.remove(ftpClient, ftpFileName);
			}
			//初始化进度监听
			FTPCopyListenerUtils.initCopyListener(ftpClient, ftpFileName);
			//获得OutputStream
            output = FTPClientUtils.getOutputStream(ftpClient, ftpFileName);
			//从FileChannel中读取数据写出到OutputStream
			return FTPChannelUtils.copyLarge(inChannel, output, ftpClient);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.appendFileStream(remote)
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 3:03:07 PM
	 * @param inChannel		：文件通道
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpFileName,boolean delIfExists) throws IOException {
		//从FileChannel中读取数据写出到OutputStream
		return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpFileName, delIfExists , false);
	}
	
	/**
	 * 
	 * @description	： <pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.appendFileStream(remote)
	 * ftpClient.storeUniqueFileStream(remote)
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:20:57 PM
	 * @param inChannel		：文件通道
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpFileName,boolean delIfExists,boolean unique) throws IOException {
		OutputStream output = null;
		try {
			if(delIfExists){
				return FTPClientUtils.storeFileChannel(ftpClient , inChannel, ftpFileName);
			}
			//如果文件存在
			if(FTPClientUtils.hasFile(ftpClient, ftpFileName)){
				//获取文件信息
				FTPFile ftpFile = FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
				//异常检查
				FTPExceptionUtils.assertPut(inChannel, ftpFile, ftpFileName);
				//跳过指定的长度,实现断点续传
				IOUtils.skip(inChannel, ftpFile.getSize());
			}
			//初始化进度监听
			FTPCopyListenerUtils.initCopyListener(ftpClient, ftpFileName);
			//获得OutputStream
			output = FTPClientUtils.getOutputStream(ftpClient, ftpFileName , unique);
			//从FileChannel中读取数据写出到OutputStream
			return FTPChannelUtils.copyLarge(inChannel, output, ftpClient);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeFileStream(remote)
	 * ftpClient.appendFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:22:43 AM
	 * @param inChannel		：文件通道
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpDir,String ftpFileName) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//从FileChannel中读取数据写出到OutputStream
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpFileName);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//切回原目录
        	ftpClient.changeWorkingDirectory(rootDir);
        }
	}
	
	/**
	 * 
	 * @description	：<pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.appendFileStream(remote)
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 3:03:07 PM
	 * @param inChannel		：文件通道
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpDir,String ftpFileName,boolean delIfExists) throws IOException {
		//从FileChannel中读取数据写出到OutputStream
		return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpDir, ftpFileName, delIfExists , false);
	}
	
	/**
	 * 
	 * @description	： <pre> 采用NOI上传文件至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.appendFileStream(remote)
	 * ftpClient.storeUniqueFileStream(remote)
	 * ftpClient.storeFileStream(remote)
	 *</pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:20:57 PM
	 * @param inChannel		：文件通道
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FTPClient ftpClient,FileChannel inChannel,String ftpDir,String ftpFileName,boolean delIfExists,boolean unique) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//将指定的输入流写入 FTP 站点上的一个指定文件
			return FTPClientUtils.storeFileChannel(ftpClient, inChannel, ftpFileName, delIfExists, unique);
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
        }
	}
	
	/**
	 * 
	 * @description	：  <pre> 上传输入流至FTP服务器,默认先删除存在同名文件;采用方法：
	 * ftpClient.storeFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 3:51:01 PM
	 * @param ftpClient		：FTPClient对象
	 * @param ftpFileName	：文件名称 
	 * @param input			： 输入流
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(FTPClient ftpClient,String ftpFileName,InputStream input) throws IOException{
        try {
        	//如果文件存在，则先删除
    		if(FTPClientUtils.hasFile(ftpClient, ftpFileName)){
    			FTPClientUtils.remove(ftpClient, ftpFileName);
    		}
			//转码后的文件名
			String remoteName = FTPStringUtils.getRemoteName(ftpClient,ftpFileName);
			// 包装文件输入流  
			input = IOUtils.toBufferedInputStream(input,ftpClient.getBufferSize());
			//根据指定的名字和输入流InputStream向服务器上传一个文件
			boolean hasPut = ftpClient.storeUniqueFile(remoteName, input);
			//异常检查
			FTPExceptionUtils.assertPut(hasPut, ftpFileName);
            return true;
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	： <pre> 上传输入流至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:26:28 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param input			：输入流
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(FTPClient ftpClient,String ftpFileName,InputStream input,boolean delIfExists) throws IOException{
		//从InputStream中读取数据写出FTP服务器
		return FTPClientUtils.storeStream(ftpClient, ftpFileName, input, delIfExists , false);
	}
	
	/**
	 * 
	 * @description	： <pre> 上传输入流至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeUniqueFile(remote, local)
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:26:28 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param input			：输入流
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(FTPClient ftpClient,String ftpFileName,InputStream input,boolean delIfExists,boolean unique) throws IOException {
		try {
			//检查是否有同名文件
			if(FTPClientUtils.hasFile(ftpClient, ftpFileName)){
				//同名文件存在，要求删除
				if(delIfExists){
					//将指定的输入流写入 FTP 站点上的一个指定文件
					return FTPClientUtils.storeStream(ftpClient, ftpFileName, input);
				}else{
					//获取文件信息
					FTPFile ftpFile = FTPClientUtils.getFTPFile(ftpClient, ftpFileName);
					//追加文件内容
					return FTPClientUtils.appendStream(ftpClient, ftpFileName, input, ftpFile.getSize());
				}
			}
			//转码后的文件名
			String remoteName = FTPStringUtils.getRemoteName(ftpClient,ftpFileName);
			//包装文件输入流  
			input = IOUtils.toBufferedInputStream(input,ftpClient.getBufferSize());
			//上传成功的标志
			boolean hasPut = false;
			if(unique){
				//根据指定的名字和输入流InputStream向服务器上传一个文件
				hasPut = ftpClient.storeUniqueFile(remoteName, input);
			}else{
				hasPut = ftpClient.storeFile(remoteName, input);
			}
			//异常检查
			FTPExceptionUtils.assertPut(hasPut, ftpFileName);
			return true;
        } finally {
        	//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        }
	}
	
	public static boolean storeStream(FTPClient ftpClient,String ftpDir,String ftpFileName,InputStream input) throws IOException{
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//将指定的输入流写入 FTP 站点上的一个指定文件
			return FTPClientUtils.storeStream(ftpClient, ftpFileName, input);
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        	//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
        }
	}
	
	/**
	 * 
	 * @description	： <pre> 上传输入流至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:26:28 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param input			：输入流
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(FTPClient ftpClient,String ftpDir,String ftpFileName, InputStream input,boolean delIfExists) throws IOException {
		return FTPClientUtils.storeStream(ftpClient, ftpDir, ftpFileName, input, delIfExists, false);
	}
	
	/**
	 * 
	 * @description	： <pre> 上传输入流至FTP服务器，根据服务端是否已有文件采用不同的方法：
	 * ftpClient.storeUniqueFile(remote, local)
	 * ftpClient.storeFile(remote, local)
	 * ftpClient.appendFile(remote, local)
	 * </pre>
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:26:28 AM
	 * @param ftpClient		： FTPClient对象
	 * @param ftpDir		：上传目录
	 * @param ftpFileName	：文件名称
	 * @param input			：输入流
	 * @param delIfExists	：如果文件存在是否删除
	 * @param unique		：是否文件唯一，该参数决定使用那种方法进行上传操作
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(FTPClient ftpClient,String ftpDir,String ftpFileName, InputStream input,boolean delIfExists,boolean unique) throws IOException {
		//FTP服务根文件目录
		String rootDir = FTPPathUtils.getRootDir(ftpClient.getClientConfig().getRootdir());
		//切换目录至根目录
		ftpClient.changeWorkingDirectory(rootDir);
		try {
			//切换目录
			boolean hasChanged = FTPClientUtils.changeExistsDirectory(ftpClient, ftpDir);
			//异常检查
			FTPExceptionUtils.assertDir(hasChanged, ftpDir);
			//将指定的输入流写入 FTP 站点上的一个指定文件
			return FTPClientUtils.storeStream(ftpClient, ftpFileName, input , delIfExists, unique);
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        	//切换目录至根目录
			ftpClient.changeWorkingDirectory(rootDir);
        }
	}
	
}
