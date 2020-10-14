package com.woshidaniu.ftpclient.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;

import com.woshidaniu.ftpclient.io.ReplyOnCloseInputStream;
import com.woshidaniu.ftpclient.io.ReplyOnCloseOutputStream;

public class FTPStreamUtils {
	
	public static void skip(FTPClient ftpClient,long offset) throws IOException{
		//设置接收数据流的起始位置
		ftpClient.setRestartOffset(Math.max(0, offset));
	}
	
	
	public static InputStream toWrapedInputStream(InputStream input,FTPClient ftpClient) throws IOException {
		if(isWraped(input)){
			 return (ReplyOnCloseInputStream) input ;
	   	}else{
	   		return new ReplyOnCloseInputStream(input,ftpClient);
	   	}
    }
	
	public static OutputStream toWrapedOutputStream(OutputStream output,FTPClient ftpClient) throws IOException {
		if(isWraped(output)){
			 return (ReplyOnCloseOutputStream) output ;
	   	}else{
	   		return new ReplyOnCloseOutputStream(output,ftpClient);
	   	}
    }
    
    private static boolean isWraped(InputStream in) {
        return in instanceof ReplyOnCloseInputStream;
    }
    
    private static boolean isWraped(OutputStream out) {
        return out instanceof ReplyOnCloseOutputStream;
    }
   
    
}
