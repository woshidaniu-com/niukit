package com.woshidaniu.smbclient.utils;

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

import jcifs.smb.SmbFile;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.filefilter.IOSmbFileFilter;

/**
 * 
 * @className	： SMBClientUtils
 * @description	： SMBClient常用方法：
 * @author 		： kangzhidong
 * @date		： Jan 13, 2016 2:31:28 PM
 */
public class SMBClientUtils {

	protected static Logger LOG = LoggerFactory.getLogger(SMBClientUtils.class);
	
	/**
	 * 
	 * @description	：追加文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：相对SMBClient路径的文件路径
	 * @param localFile		：文件
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(SMBClient sharedFile, File localFile) throws IOException{
		InputStream input = null;
		try {
			//异常检查
			SMBExceptionUtils.assertAppend(localFile,sharedFile);
			// 包装文件输入流  
			input = SMBStreamUtils.toBufferedInputStream(localFile, sharedFile.getBufferSize());
			//断点上传输入流
			return SMBClientUtils.appendStream(sharedFile, input, Math.max(0, localFile.length() - sharedFile.getContentLength()));
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：追加文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：相对SMBClient路径的文件路径
	 * @param localFile		：文件
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(SMBClient smbClient,String filepath, File localFile) throws IOException{
		InputStream input = null;
		try {
			//当前文件
			SMBClient sharedFile = smbClient.get(filepath);
			//异常检查
			SMBExceptionUtils.assertAppend(localFile, sharedFile);
			// 包装文件输入流  
			input = SMBStreamUtils.toBufferedInputStream(localFile, smbClient.getBufferSize());
			//断点上传输入流
			return SMBClientUtils.appendStream(sharedFile, input, Math.max(0, localFile.length() - sharedFile.getContentLength()));
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：追加文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：相对SMBClient路径的文件路径
	 * @param localFile		：文件
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendFile(SMBClient smbClient,String filepath, File localFile,long skipOffset) throws IOException{
		InputStream input = null;
		try {
			//当前文件
			SMBClient sharedFile = smbClient.get(filepath);
			//异常检查
			SMBExceptionUtils.assertAppend(localFile, sharedFile, skipOffset);
			// 包装文件输入流  
			input = SMBStreamUtils.toBufferedInputStream(localFile, smbClient.getBufferSize());
			//断点上传输入流
			return SMBClientUtils.appendStream(sharedFile, input, skipOffset);
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：追加输入流至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param sharedFile		： SMBClient对象
	 * @param input			：输入流
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendStream(SMBClient sharedFile,InputStream input) throws IOException{
		try {
			//断点上传输入流
			return SMBClientUtils.appendStream(sharedFile, input, sharedFile.getContentLength());
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：追加输入流至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param sharedFile		： SMBClient对象
	 * @param input			：输入流
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendStream(SMBClient sharedFile,InputStream input,long skipOffset) throws IOException{
		try {
			//异常检查
			SMBExceptionUtils.assertFile(sharedFile);
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			// 追加文件内容
			long totalRead = SMBStreamUtils.copyLarge(input, sharedFile, skipOffset);
			//异常检查
			return SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：追加输入流至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：相对SMBClient路径的文件路径
	 * @param input			：输入流
	 * @param skipOffset	：跳过已经存在的长度
	 * @return
	 * @throws IOException
	 */
	public static boolean appendStream(SMBClient smbClient,String filepath,InputStream input,long skipOffset) throws IOException{
		try {
			//当前文件
			SMBClient sharedFile = smbClient.get(filepath);
			//异常检查
			SMBExceptionUtils.assertFile(sharedFile);
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			// 追加文件内容
			long totalRead = SMBStreamUtils.copyLarge(input, sharedFile , skipOffset);
			//异常检查
			return SMBExceptionUtils.assertRead(totalRead,filepath);
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：切换目录至指定目录，如果指定目录不存在创建目录，并返回结果标志
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 8:27:34 PM
	 * @param smbClient
	 * @param targetDir
	 * @return
	 * @throws IOException
	 */
	public static SMBClient changeExistsDir(SMBClient smbClient,String targetDir) throws IOException{
		if(targetDir == null){
			return smbClient;
		}
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,targetDir);
		if(!currentDir.exists()){
			currentDir.mkdirs();
		}
		return currentDir;
	}

	public static String[] listNames(SMBClient smbClient,String sharedDir) throws IOException{
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,SMBPathUtils.getSharedDir(sharedDir));
		//异常检查
		SMBExceptionUtils.assertDir(currentDir);
		//返回文件名数组
		return currentDir.list();
	}
	
	public static List<SMBClient> listFiles(SMBClient sharedDir) throws IOException{
		return listFiles(sharedDir, false);
	}
	
	public static List<SMBClient> listFiles(SMBClient sharedDir,boolean recursion) throws IOException{
		//异常检查
		SMBExceptionUtils.assertDir(sharedDir);
		//创建文件类型的文件集合
		List<SMBClient> fileList = new ArrayList<SMBClient>();
		//列出当前工作目录的文件信息
		SmbFile[] files = sharedDir.listFiles();
		//循环共享文件
		for(SmbFile sharedFile : files){
			if (sharedFile.isDirectory() ) {
				if( recursion){
					fileList.addAll(SMBClientUtils.listFiles(sharedDir, sharedFile.getName()));
				}else {
					fileList.add(sharedDir.wrap(sharedFile));
				}
			} else{
				fileList.add(sharedDir.wrap(sharedFile));
			}
		}
		return fileList;
	}
	
	public static List<SMBClient> listFiles(SMBClient smbClient,String sharedDir) throws IOException{
		return listFiles(smbClient, sharedDir, false);
	}
	
	public static List<SMBClient> listFiles(SMBClient smbClient,String sharedDir,boolean recursion) throws IOException{
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,SMBPathUtils.getSharedDir(sharedDir));
		//异常检查
		SMBExceptionUtils.assertDir(currentDir);
		//创建文件类型的文件集合
		List<SMBClient> fileList = new ArrayList<SMBClient>();
		//列出当前工作目录的文件信息
		SmbFile[] files = currentDir.listFiles();
		//循环共享文件
		for(SmbFile sharedFile : files){
			if (sharedFile.isDirectory() ) {
				if(recursion){
					fileList.addAll(SMBClientUtils.listFiles(currentDir, sharedFile.getName()));
				}else{
					fileList.add(smbClient.wrap(sharedFile));
				}
			} else{
				fileList.add(smbClient.wrap(sharedFile));
			}
		}
		return fileList;
	}
	
	public static List<SMBClient> listFiles(SMBClient smbClient, String sharedDir, String[] extensions, boolean recursion) throws IOException {
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,SMBPathUtils.getSharedDir(sharedDir));
		//异常检查
		SMBExceptionUtils.assertDir(currentDir);
		//创建文件类型的文件集合
		List<SMBClient> fileList = new ArrayList<SMBClient>();
		//列出当前工作目录的文件信息
		Collection<SmbFile> files = SMBFileUtils.listFiles(currentDir.listFiles(), extensions);
		if(files != null && files.size() > 0){
			//循环共享文件
			for(SmbFile sharedFile : files){
				if (sharedFile.isDirectory() ) {
					if(recursion){
						fileList.addAll( smbClient.wrapAll(SMBFileUtils.listFiles( sharedFile, extensions, recursion)) );
					}else{
						fileList.add( smbClient.wrap(sharedFile));
					}
				} else{
					fileList.add(smbClient.wrap(sharedFile));
				}
			}
		}
		return fileList;
	}
	
	public static List<SMBClient> listFiles(SMBClient smbClient, String sharedDir, IOSmbFileFilter filter, boolean recursion) throws IOException {
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,SMBPathUtils.getSharedDir(sharedDir));
		//异常检查
		SMBExceptionUtils.assertDir(currentDir);
		//创建文件类型的文件集合
		List<SMBClient> fileList = new ArrayList<SMBClient>();
		//列出当前工作目录的文件信息
		Collection<SmbFile> files = SMBFileUtils.listFiles(currentDir.listFiles(), filter);
		if(files != null && files.size() > 0){
			//循环共享文件
			for(SmbFile sharedFile : files){
				if (sharedFile.isDirectory() ) {
					if(recursion){
						fileList.addAll( smbClient.wrapAll(SMBFileUtils.listFiles( sharedFile, filter, recursion)) );
					}else{
						fileList.add( smbClient.wrap(sharedFile));
					}
				} else{
					fileList.add(smbClient.wrap(sharedFile));
				}
			}
		}
		return fileList;
	}
	
	/**
	 * 
	 * @description	： 循环创建目录，并且创建完目录后，设置工作目录为当前创建的目录下
	 * @author 		： kangzhidong
	 * @date 		：Jan 12, 2016 10:06:25 AM
	 * @param smbClient
	 * @param targetDir
	 * @return
	 * @throws IOException
	 */
	public static boolean makeDir(SMBClient smbClient,String targetDir) throws IOException{
		SmbFile sharedDir = new SmbFile(smbClient,targetDir);
		//验证是否有该文件夹，有就转到，没有创建后转到该目录下
		if (sharedDir.exists()) {
			return true;
		} 
		sharedDir.mkdirs();
		return true;
	}
	
	public static InputStream getInputStream(SMBClient sharedFile,long skipOffset) throws IOException{
		//异常检查
		SMBExceptionUtils.assertGet(sharedFile,skipOffset);
		//设置接收数据流的起始位置
		sharedFile.setRestartOffset(skipOffset);
		//获得InputStream
		return SMBStreamUtils.toBufferedInputStream(sharedFile.getInputStream(), sharedFile.getBufferSize());
	}
	
	public static InputStream getInputStream(SMBClient smbClient,String filepath) throws IOException{
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//异常检查
		SMBExceptionUtils.assertFile(sharedFile);
		//获得InputStream
		return SMBStreamUtils.toBufferedInputStream(sharedFile.getInputStream(), smbClient.getBufferSize());
	}
	
	public static InputStream getInputStream(SMBClient smbClient,String filepath,long skipOffset) throws IOException{
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//异常检查
		SMBExceptionUtils.assertGet(sharedFile,skipOffset);
		//设置接收数据流的起始位置
		sharedFile.setRestartOffset(skipOffset);
		//获得InputStream
		return SMBStreamUtils.toBufferedInputStream(sharedFile.getInputStream(), smbClient.getBufferSize());
	}
	
	public static boolean remove(SMBClient sharedDir,String filepath) throws IOException {
		//当前文件
		SmbFile sharedFile = new SmbFile(sharedDir,filepath);
		//删除【共享文件】服务器上的一个指定文件
		if(sharedFile.exists()){
			sharedFile.delete();
		}
		return true;
	}
	
	public static boolean remove(SMBClient sharedDir, String[] fileNames) throws IOException {
		//异常检查
		SMBExceptionUtils.assertDir(sharedDir);
		//循环要删除的文件列表
		for(String fileName:fileNames){
			//删除【共享文件】服务器上的一个指定文件
			SMBClientUtils.remove(sharedDir, fileName);
		}
        return true;
	}
	
	public static boolean rename(SMBClient smbClient,String filepath,String filename) throws Exception{
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//异常检查
		SMBExceptionUtils.assertFile(sharedFile);
		//新的路径
		String destpath = FilenameUtils.getFullPath(sharedFile.getURL().getPath())  + FilenameUtils.getBaseName(filename) + "." + StringUtils.getSafeStr(FilenameUtils.getExtension(filename), FilenameUtils.getExtension(filepath));
		//移动或重命名
		sharedFile.renameTo(new SmbFile(smbClient,destpath.substring(smbClient.getURL().getPath().length())));
		return true;
	}
	
	public static boolean renameTo(SMBClient smbClient,String filepath,String destpath) throws Exception{
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//异常检查
		SMBExceptionUtils.assertFile(sharedFile);
		//移动或重命名
		sharedFile.renameTo(new SmbFile(smbClient,destpath));
		return true;
	}
	
	// delete all subDirectory and files.
	public static boolean removeDir(SMBClient smbClient,String sharedDir) throws IOException {
		//当前目录
		SMBClient currentDir = new SMBClient(smbClient,SMBPathUtils.getSharedDir(sharedDir));
		//异常检查
		SMBExceptionUtils.assertDir(currentDir);
		try {
			currentDir.delete();
		} catch (Exception e) {
			SmbFile[] sharedFileArr = currentDir.listFiles();
			for (SmbFile sharedFile : sharedFileArr) {
				if (sharedFile.isDirectory()) {
					LOG.info("Delete subDir ["+ sharedFile.getURL().getPath() +"]");				
					SMBClientUtils.removeDir(smbClient, sharedDir + "/" + sharedFile.getName());
				} else{
					sharedFile.delete();
				}
			}
		}
		return true;
	}
	
	public static void retrieveToDir(SMBClient sharedDir,File localDir) throws Exception{
		//异常检查
		SMBExceptionUtils.assertDir(sharedDir);
		//遍历当前目录下的文件
		List<SMBClient> fileList = SMBClientUtils.listFiles(sharedDir);
		//循环下载文件
		for(SmbFile sharedFile :fileList){
			if(sharedFile.isDirectory()){
				File newDir = new File(localDir ,sharedFile.getName());
				if (!newDir.exists()) {
					newDir.mkdirs();
				}
			}else{
				//写SmbFile到指定文件路径
				SMBClientUtils.retrieveToFile(sharedDir.wrap(sharedFile), new File(localDir, sharedFile.getName()));
			}
		}
	}
	
	public static void retrieveToFile(SMBClient sharedFile,File localFile) throws IOException{
		//异常检查
		SMBExceptionUtils.assertGet(sharedFile, localFile);
		InputStream input = null;
		FileChannel outChannel = null;
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
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, localFile.getName());
			//获得InputStream
			input = SMBClientUtils.getInputStream(sharedFile,outChannel.size());
			//将InputStream写到FileChannel
			SMBChannelUtils.copyLarge(input, outChannel ,sharedFile);
		} finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        	//关闭输出通道
        	IOUtils.closeQuietly(outChannel);
        	//恢复起始位
        	sharedFile.setRestartOffset(0); 
        }
	}
	
	public static void retrieveToStream(SMBClient sharedFile,OutputStream output) throws IOException {
		try {
			//异常检查
			SMBExceptionUtils.assertFile(sharedFile);
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			// 追加文件内容
			long totalRead = SMBStreamUtils.copyLarge(sharedFile, output);
			//异常检查
			SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
        } finally {
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        	//恢复起始位
        	sharedFile.setRestartOffset(0); 
        }
	}
	
	/**
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Jun 6, 20163:31:21 PM
	 *@param smbFile
	 *@param response
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	: 
	 */
	public static void retrieveToResponse(SMBClient sharedFile, ServletResponse response) throws IOException {
		try {
			//异常检查
			SMBExceptionUtils.assertFile(sharedFile);
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			// 追加文件内容
			long totalRead = SMBStreamUtils.copyLarge(sharedFile, response.getOutputStream());
			//异常检查
			SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
        } finally {
        	//恢复起始位
        	sharedFile.setRestartOffset(0); 
        }
	}
	
	/**
	 * 
	 * @description	： 上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:03:18 PM
	 * @param localFile		：本地文件
	 * @param sharedFile	：共享文件
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(File localFile,SMBClient sharedFile) throws IOException{
		//异常检查
		SMBExceptionUtils.assertFile(localFile);
		InputStream input = null;
		try {
			//如果共享文件存在，则先删除
			if(sharedFile.exists()){
				sharedFile.delete();
			}
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, localFile.getName());
			// 包装文件输入流  
			input = SMBStreamUtils.toBufferedInputStream(localFile, sharedFile.getBufferSize());
			// 拷贝文件内容
			long totalRead = SMBStreamUtils.copyLarge(input, sharedFile);
			//异常检查
			return SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	：上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:55:58 AM
	 * @param localFile		：本地文件
	 * @param sharedFile	：共享文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(File localFile,SMBClient sharedFile,boolean delIfExists) throws IOException {
		//异常检查
		SMBExceptionUtils.assertFile(localFile);
		//同名文件存在，要求删除
		if(delIfExists){
			//上传完整文件
			return SMBClientUtils.storeFile(localFile,sharedFile);
		}else{
			if(sharedFile.exists()){
				//断点上传文件
				return SMBClientUtils.appendFile(sharedFile,localFile);
			}
			//上传完整文件
			return SMBClientUtils.storeFile(localFile,sharedFile);
		}
	}
	
	
	/**
	 * 
	 * @description	： 上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 1:55:22 PM
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：共享文件路径
	 * @param localFile		：本地文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFile(File localFile,SMBClient smbClient,String filepath,boolean delIfExists) throws IOException {
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//上传文件
		return SMBClientUtils.storeFile(localFile, sharedFile, delIfExists);
	}
	
	/**
	 * 
	 * @description	： 采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:11:13 PM
	 * @param inChannel		：文件通道
	 * @param sharedFile	：共享文件
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FileChannel inChannel,SMBClient sharedFile) throws IOException {
		OutputStream output = null;
		try {
			//如果共享文件存在，则先删除
			if(sharedFile.exists()){
				sharedFile.delete();
			}
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			//获得OutputStream
			output = SMBStreamUtils.toBufferedOutputStream(sharedFile.getOutputStream(), sharedFile.getBufferSize());
			//从FileChannel中读取数据写出到OutputStream
			return SMBChannelUtils.copyLarge(inChannel, output, sharedFile);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 
	 * @description	： 采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:20:57 PM
	 * @param inChannel		：文件通道
	 * @param sharedFile	：共享文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(FileChannel inChannel,SMBClient sharedFile,boolean delIfExists) throws IOException {
		OutputStream output = null;
		try {
			if(delIfExists){
				return SMBClientUtils.storeFileChannel(inChannel, sharedFile);
			}
			if(sharedFile.exists()){
				//跳过指定的长度,实现断点续传
				SMBStreamUtils.skip(inChannel, sharedFile.getContentLength());
			}
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			//获得OutputStream
			output = SMBStreamUtils.toBufferedOutputStream(sharedFile.getOutputStream(), sharedFile.getBufferSize());
			//从FileChannel中读取数据写出到OutputStream
			return SMBChannelUtils.copyLarge(inChannel, output, sharedFile);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	/**
	 * 
	 * @description	：采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:26:57 PM
	 * @param localFile		：本地文件
	 * @param sharedFile	：共享文件
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(File localFile,SMBClient sharedFile) throws IOException {
		//异常检查
		SMBExceptionUtils.assertFile(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, localFile.getName());
			//从FileChannel中读取数据写出到OutputStream
			return SMBClientUtils.storeFileChannel(inChannel, sharedFile);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	/**
	 * 
	 * @description	： 采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:26:45 PM
	 * @param localFile		：本地文件
	 * @param sharedFile	：共享文件
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(File localFile,SMBClient sharedFile,boolean delIfExists) throws IOException {
		//异常检查
		SMBExceptionUtils.assertFile(localFile);
		FileChannel  inChannel = null;
		try {
			//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
			//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
			//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
			inChannel = new RandomAccessFile(localFile,"rws").getChannel();
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, localFile.getName());
			//从FileChannel中读取数据写出到OutputStream
			return SMBClientUtils.storeFileChannel(inChannel, sharedFile, delIfExists);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        }
	}
	
	/**
	 * 
	 * @description	：采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:26:50 PM
	 * @param localFile		：本地文件
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：共享文件路径
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(File localFile,SMBClient smbClient,String filepath) throws IOException {
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//从FileChannel中读取数据写出到OutputStream
		return SMBClientUtils.storeFileChannel(localFile, sharedFile);
	}
	
	/**
	 * 
	 * @description	： 采用NOI上传文件至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 21, 2016 2:26:36 PM
	 * @param localFile		：本地文件
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：共享文件路径
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeFileChannel(File localFile,SMBClient smbClient,String filepath,boolean delIfExists) throws IOException {
		//共享文件
		SMBClient sharedFile = smbClient.get(filepath);
		//从FileChannel中读取数据写出到OutputStream
		return SMBClientUtils.storeFileChannel(localFile, sharedFile, delIfExists);
	}
	
	public static boolean storeStream(InputStream input,SMBClient sharedFile) throws IOException{
		try {
			//清除原文件
			if(sharedFile.exists()){
				sharedFile.delete();
				sharedFile.createNewFile();
			}
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			//拷贝文件内容
			long totalRead = SMBStreamUtils.copyLarge(input, sharedFile);
			//异常检查
			return SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}
	
	/**
	 * 
	 * @description	： 上传输入流至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 15, 2016 3:29:38 PM
	 * @param smbClient
	 * @param filepath
	 * @param input
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(InputStream input,SMBClient smbClient,String filepath) throws IOException{
		try {
			//共享文件
			SMBClient sharedFile = smbClient.get(filepath);
			//从InputStream中读取数据写出共享服务器
			return SMBClientUtils.storeStream(input, sharedFile);
        } finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        }
	}

	/**
	 * 
	 * @description	： 上传输入流至【文件共享服务器】
	 * @author 		： kangzhidong
	 * @date 		：Jan 13, 2016 10:26:28 AM
	 * @param input			：输入流
	 * @param smbClient		： SMBClient对象
	 * @param filepath		：文件路径
	 * @param delIfExists	：如果文件存在是否删除
	 * @return
	 * @throws IOException
	 */
	public static boolean storeStream(InputStream input,SMBClient smbClient,String filepath, boolean delIfExists) throws IOException {
		try {
			if(delIfExists){
				return SMBClientUtils.storeStream(input, smbClient ,filepath);
			}
			//共享文件
			SMBClient sharedFile = smbClient.get(filepath);
			if(sharedFile.exists()){
				//追加文件内容
				return SMBClientUtils.appendStream(sharedFile, input);
			}
			//初始进度监听
			SMBCopyListenerUtils.initCopyListener(sharedFile, sharedFile.getName());
			//拷贝文件内容
			long totalRead = SMBStreamUtils.copyLarge(input, sharedFile);
			//异常检查
			return SMBExceptionUtils.assertRead(totalRead,sharedFile.getURL().getPath());
			
        } finally {
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        }
	}

	
	
}
