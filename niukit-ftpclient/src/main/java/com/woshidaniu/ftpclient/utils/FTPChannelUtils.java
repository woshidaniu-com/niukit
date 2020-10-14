package com.woshidaniu.ftpclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;

import com.woshidaniu.ftpclient.FTPClient;

public class FTPChannelUtils extends FTPStreamUtils{

	public static boolean copyLarge(FileChannel inChannel,OutputStream output,FTPClient ftpClient) throws IOException{
		ByteBuffer byteBuffer = null;
		try {
			//进度监听
			CopyStreamListener listener = ftpClient.getMergedCopyListeners();
			//根据不同大小分配不同类型的缓存区;可能2M
			if(ftpClient.getChannelWriteBufferSize() > 1024 * 10000){
				byteBuffer = ByteBuffer.allocateDirect(ftpClient.getChannelReadBufferSize());
			}else{
				byteBuffer = ByteBuffer.allocate(ftpClient.getChannelReadBufferSize());
			}
			//Java.nio.charset.Charset处理了字符转换问题。它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
			//Charset charset = Charset.forName("GBK");  
			//CharsetDecoder decoder = charset.newDecoder();  
			//CharBuffer charBuffer = CharBuffer.allocate(1024); 
			long totalRead = inChannel.position();
			int bytesRead = 0;
			long bytesBlock = 0;
			//读取数据到ByteBuffer;该方法将数据从FileChannel读取到Buffer中。read()方法返回的int值表示了有多少字节被读到了Buffer中。如果返回-1，表示到了文件末尾。
			while ( (bytesRead = inChannel.read(byteBuffer)) != -1) {
				//反转缓冲区。首先将限制设置为当前位置，然后将位置设置为 0。如果已定义了标记，则丢弃该标记。 常与compact方法一起使用。通常情况下，在准备从缓冲区中读取数据时调用flip方法
				//翻转，也就是让flip之后的position到limit这块区域变成之前的0到position这块，翻转就是将一个处于存数据状态的缓冲区变为一个处于准备取数据的状态 
				byteBuffer.flip();
				//定义byte[]缓冲区
				byte[] bytes = new byte[bytesRead];
				//取ByteBuffer的缓冲到byte[]
				byteBuffer.get(bytes, 0, bytes.length);
				//写byte[]
				output.write(bytes);
				//clear方法将缓冲区清空，一般是在重新写缓冲区时调用
				byteBuffer.clear();
				//计算距上次刷新已读取量
				bytesBlock = bytesBlock + bytesRead;
				//自动刷新缓存
				if(ftpClient.isAutoFlush() && bytesBlock >= ftpClient.getAutoFlushBlockSize()){
					output.flush();
					bytesBlock = 0;
				}
				if(listener != null){
					totalRead = totalRead + bytesRead;
					listener.bytesTransferred(totalRead, bytesRead, CopyStreamEvent.UNKNOWN_STREAM_SIZE);
				}
				//发送一个NOOP命令到FTP服务器。 这是为防止服务器超时有用，与 noop() 类似。防止连接超时，也可以根据返回值检查连接的状态。
				ftpClient.sendNoOp();
			}
			return true;
		} finally {
        	//关闭输入通道
        	IOUtils.closeQuietly(inChannel);
        	//关闭输出流
        	IOUtils.closeQuietly(output);
        	if(byteBuffer != null){
        		//释放缓冲区
        		byteBuffer.clear();
        		byteBuffer = null;
        	}
        }
	}
	
	public static boolean copyLarge(InputStream input,FileChannel outChannel,FTPClient ftpClient) throws IOException{
		ByteBuffer 	 byteBuffer = null;
		try {
			//通过调用position()方法跳过已经存在的长度
			outChannel.position(outChannel.size()); 
			//进度监听
			CopyStreamListener listener = ftpClient.getMergedCopyListeners();
			//分配缓存区;可能8M
			byte[] bytes = new byte[ftpClient.getChannelWriteBufferSize()];
			//根据不同大小分配不同类型的缓存区;可能2M
	    	if(ftpClient.getChannelWriteBufferSize() > 1024 * 10000){
	    		byteBuffer = ByteBuffer.allocateDirect(ftpClient.getChannelWriteBufferSize());
	    	}else{
	    		byteBuffer = ByteBuffer.allocate(ftpClient.getChannelWriteBufferSize());
	    	}
	        long totalRead = outChannel.position();
		    int bytesRead = 0;
		    long bytesBlock = 0;
		    /* 
		     * 注意FileChannel.write()是在while循环中调用的。因为无法保证write()方法一次能向FileChannel写入多少字节，
		     * 因此需要重复调用write()方法，直到Buffer中已经没有尚未写入通道的字节。
		     */
		    //读取数据到byte[]
	        while (( bytesRead = input.read(bytes)) != -1) {
	        	//clear方法将缓冲区清空，一般是在重新写缓冲区时调用
	    		byteBuffer.clear();
	    		//拷贝byte[]到ByteBuffer,注意这里要使用bytesRead标记结束位置，可能最后一次读取不够一个缓存区
	    		byteBuffer.put(bytes,0,bytesRead);
	    		//反转缓冲区。首先将限制设置为当前位置，然后将位置设置为 0。如果已定义了标记，则丢弃该标记。 常与compact方法一起使用。通常情况下，在准备从缓冲区中读取数据时调用flip方法
	    		//也就是让flip之后的position到limit这块区域变成之前的0到position这块，翻转就是将一个处于存数据状态的缓冲区变为一个处于准备取数据的状态 
	    		byteBuffer.flip();
	    		while(byteBuffer.hasRemaining()) {
	    			//写ByteBuffer到文件
	    			outChannel.write(byteBuffer);
				}
	    		//计算距上次刷新已写出量
	    		bytesBlock = bytesBlock + bytesRead;
	    		//自动刷新缓存
				if(ftpClient.isAutoFlush() && bytesBlock >= ftpClient.getAutoFlushBlockSize()){
					/*
					 * FileChannel.force()方法将通道里尚未写入磁盘的数据强制写到磁盘上。出于性能方面的考虑，操作系统会将数据缓存在内存中，
					 * 所以无法保证写入到FileChannel里的数据一定会即时写到磁盘上。要保证这一点，需要调用force()方法。
					 * force()方法有一个boolean类型的参数，指明是否同时将文件元数据（权限信息等）写到磁盘上。
					 */
	        		outChannel.force(true);
	        		bytesBlock = 0;
				}
				if(listener != null){
					totalRead = totalRead + bytesRead;
					listener.bytesTransferred(totalRead, bytesRead, CopyStreamEvent.UNKNOWN_STREAM_SIZE);
				}
	    		//发送一个NOOP命令到FTP服务器。 这是为防止服务器超时有用，与 noop() 类似。防止连接超时，也可以根据返回值检查连接的状态。
				ftpClient.sendNoOp();
	        }
	        return true;
		} finally {
			//恢复起始位
        	ftpClient.setRestartOffset(0); 
        	//关闭输入流
        	IOUtils.closeQuietly(input);
        	//关闭输出通道
        	IOUtils.closeQuietly(outChannel);
        	//释放缓冲区
        	if(byteBuffer != null){
        		byteBuffer.clear();
        		byteBuffer = null;
        	}
        }
	}
}
