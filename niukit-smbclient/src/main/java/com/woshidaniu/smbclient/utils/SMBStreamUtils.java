package com.woshidaniu.smbclient.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import jcifs.smb.SmbFile;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.smbclient.SMBClient;
import com.woshidaniu.smbclient.io.CopyStreamProcessListener;

public class SMBStreamUtils {
	
	/**
     * Constant used to indicate the stream size is unknown.
     */
    public static final long UNKNOWN_STREAM_SIZE = -1;
    
    protected static Logger LOG = LoggerFactory.getLogger(SMBStreamUtils.class);
	
    public static long copyLarge(SMBClient sharedFile,OutputStream output) throws IOException {
    	return SMBStreamUtils.copyLarge(sharedFile, output, 0);
    }
    
    /**
     * 
     * @description	： 拷贝共享文件服务器的文件到本地
     * @author 		：kangzhidong
     * @date 		：Jan 20, 2016 4:08:28 PM
     * @param sharedFile
     * @param output
     * @param skipOffset
     * @return
     * @throws IOException
     */
    public static long copyLarge(SMBClient sharedFile,OutputStream output,long skipOffset) throws IOException {
    	InputStream input = null;
		try {
			LOG.info("开始连接...url："+ sharedFile.getURL().getPath());
			//尝试连接 ;SmbFile的connect()方法可以尝试连接远程文件夹，如果账号或密码错误，将抛出连接异常
			sharedFile.connect(); 
			LOG.info("连接成功."); 
			//获得共享文件的输入流，以便进行读取
			input = SMBStreamUtils.toBufferedInputStream(sharedFile.getInputStream(), sharedFile.getBufferSize());
			try {
				// 跳过已经存在的长度,实现断点续传  
				SMBStreamUtils.skip(input, skipOffset);
			} catch (Exception e) {
				LOG.error(ExceptionUtils.getFullStackTrace(e));
				return -1;
			}
			//进度监听
			CopyStreamProcessListener listener = sharedFile.getCopyStreamProcessListener();
			//分配缓存区;可能8M
			byte[] bytes = new byte[sharedFile.getBufferSize()];
			long totalRead = 0;
			int bytesRead = 0;
			long bytesBlock = 0;
		    /* 
		     * 注意FileChannel.write()是在while循环中调用的。因为无法保证write()方法一次能向FileChannel写入多少字节，
		     * 因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
		     */
			LOG.info("开始拷贝...");
		    //读取数据到byte[]
	        while (( bytesRead = input.read(bytes)) != -1) {
	        	output.write(bytes, 0, bytesRead);
	    		//计算距上次刷新已写出量
	    		bytesBlock = bytesBlock + bytesRead;
	    		//自动刷新缓存
				if(sharedFile.isAutoFlush() && bytesBlock >= sharedFile.getAutoFlushBlockSize()){
					//刷新缓冲的输出流
					output.flush();
	        		bytesBlock = 0;
				}
				totalRead = totalRead + bytesRead;
				if(listener != null){
					listener.bytesTransferred(totalRead, bytesRead, sharedFile.getContentLength());
				}
	        }
	        LOG.info("拷贝完成：" + sharedFile.getURL().getPath());
	        return totalRead;
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
    }
    
    public static long copyLarge(InputStream input,SMBClient sharedFile) throws IOException {
    	return SMBStreamUtils.copyLarge(input, sharedFile, 0);
    }
    
    
    public static void makeSharedDir(SMBClient sharedDir) throws IOException{
    	//目录不存在则继续
    	if(!sharedDir.exists()){
    		//共享的根目录
    		String rootPath = sharedDir.getParent();
    		//当前访问路径
    		String filePath = sharedDir.getPath();
    		String tmpPath =  filePath.substring(rootPath.length());
    		tmpPath = SMBPathUtils.getPath(tmpPath);
    		if (tmpPath.endsWith(SMBPathUtils.BACKSLASHES)) {
    			tmpPath = tmpPath.substring(0, (tmpPath.length() - SMBPathUtils.BACKSLASHES.length()));
    		}
    		//解析目录
    		List<String> dirList = new ArrayList<String>();
    		String[] dirArr = tmpPath.split("\\"+SMBPathUtils.BACKSLASHES);
    		StringBuilder builder = new StringBuilder();
    		for (int i = 0; i < dirArr.length; i++) {
    			builder.delete(0, builder.length());
    			for (int j = 0; j <= i; j++) {
    				builder.append(dirArr[j]);
    				if(j < i){
    					builder.append(SMBPathUtils.BACKSLASHES);
    				}
				}
    			dirList.add(new String(builder.toString()));
			}
    		
    		for (String tmpDir : dirList) {
    			//父级目录
        		SMBClient parentDir = sharedDir.wrap(new SmbFile(rootPath + tmpDir));
        		if(!parentDir.exists()){
        			parentDir.mkdir();
        		}else{
        			continue;
        		}
			}
		}
    }
    
    /**
     * 
     * @description	： 拷贝本地文件到共享文件服务器
     * @author 		：kangzhidong
     * @date 		：Jan 20, 2016 4:08:05 PM
     * @param input
     * @param sharedFile
     * @param skipOffset
     * @return
     * @throws IOException
     */
	public static long copyLarge(InputStream input,SMBClient sharedFile,long skipOffset) throws IOException {
		OutputStream output = null;
		try {
			LOG.info("开始连接...url："+ sharedFile.getURL().getPath());
			//尝试连接 ;SmbFile的connect()方法可以尝试连接远程文件夹，如果账号或密码错误，将抛出连接异常
			sharedFile.connect(); 
			LOG.info("连接成功.");
			try {
				// 跳过已经存在的长度,实现断点续传  
				SMBStreamUtils.skip(input, skipOffset);
			} catch (Exception e) {
				LOG.error(ExceptionUtils.getFullStackTrace(e));
				return -1;
			}
			
			if(!sharedFile.exists()){
				SMBClient sharedDir = new SMBClient(SMBPathUtils.getResolvePath(sharedFile.getParent()));
				makeSharedDir(sharedDir);
				sharedFile.createNewFile();
			}
			//获得共享文件的输出流，以便进行写入
			output = SMBStreamUtils.toBufferedOutputStream(sharedFile.getOutputStream(), sharedFile.getBufferSize());
			//进度监听
			CopyStreamProcessListener listener = sharedFile.getCopyStreamProcessListener();
			//分配缓存区;可能8M
			byte[] bytes = new byte[sharedFile.getBufferSize()];
			long totalRead = 0;
			int bytesRead = 0;
			long bytesBlock = 0;
		    /* 
		     * 注意FileChannel.write()是在while循环中调用的。因为无法保证write()方法一次能向FileChannel写入多少字节，
		     * 因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
		     */
			LOG.info("开始拷贝...");
		    //读取数据到byte[]
	        while (( bytesRead = input.read(bytes)) != -1) {
	        	 output.write(bytes, 0, bytesRead);
	    		//计算距上次刷新已写出量
	    		bytesBlock = bytesBlock + bytesRead;
	    		//自动刷新缓存
				if(sharedFile.isAutoFlush() && bytesBlock >= sharedFile.getAutoFlushBlockSize()){
					//刷新缓冲的输出流
					output.flush();
	        		bytesBlock = 0;
				}
				totalRead = totalRead + bytesRead;
				if(listener != null){
					listener.bytesTransferred(totalRead, bytesRead, UNKNOWN_STREAM_SIZE);
				}
	        }
	        LOG.info("拷贝完成：" + sharedFile.getURL().getPath());
	        return totalRead;
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(input);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        }
	}
	
	
	/**
	 * 
	 * @description	： 跳过指定的长度,实现断点续传  
	 * @author 		：kangzhidong
	 * @date 		：Jan 15, 2016 3:02:34 PM
	 * @param input
	 * @param offset
	 * @throws IOException
	 */
	public static long skip(InputStream input,long offset) throws IOException{
		long at = offset;
		while (at > 0) {
			long amt = input.skip(at);
			if (amt == -1) {
				throw new EOFException("offset [" + offset + "] larger than the length of input stream : unexpected EOF");  
			}
			at -= amt;
		}
		return at;
	}
	
	/**
	 * 
	 * @description	：跳过指定的长度,实现断点续传  
	 * @author 		：kangzhidong
	 * @date 		：Jan 15, 2016 3:02:50 PM
	 * @param channel
	 * @param offset
	 * @throws IOException
	 */
	public static void skip(FileChannel channel,long offset) throws IOException{
		if (offset > channel.size()) {  
           throw new EOFException("offset [" + offset + "] larger than the length of file : unexpected EOF");  
		}
		//通过调用position()方法跳过已经存在的长度
		channel.position(Math.max(0, offset)); 
	}
	
	public static InputStream toBufferedInputStream(InputStream input) throws IOException {
    	if(isBuffered(input)){
    		 return (BufferedInputStream) input ;
    	}else{
            return new BufferedInputStream(input);
    	}
    }
	
	public static OutputStream toBufferedOutputStream(OutputStream output) throws IOException {
		if(isBuffered(output)){
	   		 return (BufferedOutputStream) output ;
	   	}else{
	        return new BufferedOutputStream(output);
	   	}
    }
	
	public static InputStream toBufferedInputStream(File localFile,int bufferSize) throws IOException {
		// 包装文件输入流  
		return toBufferedInputStream(new FileInputStream(localFile),bufferSize);
    }
	
	public static InputStream toBufferedInputStream(InputStream input,int bufferSize) throws IOException {
    	if(isBuffered(input)){
    		return (BufferedInputStream)input ;
    	}else{
    		if (bufferSize > 0) {
    			return new BufferedInputStream(input, bufferSize);
    		}
    		return new BufferedInputStream(input);
    	}
    }
	
	public static OutputStream toBufferedOutputStream(OutputStream output,int bufferSize) throws IOException {
		if(isBuffered(output)){
			return (BufferedOutputStream)output ;
	   	}else{
	   		if (bufferSize > 0) {
	   			return new BufferedOutputStream(output, bufferSize);
	   		}
	   		return new BufferedOutputStream(output);
	   	}
    }

    private static boolean isBuffered(InputStream in) {
        return in instanceof BufferedInputStream;
    }
    
    private static boolean isBuffered(OutputStream out) {
        return out instanceof BufferedOutputStream;
    }
    
}
