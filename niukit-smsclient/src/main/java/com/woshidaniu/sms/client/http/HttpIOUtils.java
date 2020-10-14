package com.woshidaniu.sms.client.http;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;

/**
 * 
 * @className	： HttpIOUtils
 * @description	：  扩展org.apache.commons.io.IOUtils工具对象
 * @date		： 2017年6月13日 下午9:20:40
 * @version 	V1.0
 */
public abstract class HttpIOUtils extends org.apache.commons.io.IOUtils{
	

	public static final int BUFFER_SIZE = 1024 * 4;
	
    public static void closeConnect(final URLConnection conn) {
        try {
            if (conn != null) {
            	if (conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            }
        } catch (final Exception ioe) {
            // ignore
        }
    }
    
	//---------------------------------------------------------------------
	// Stream Warp methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------

	public static InputStream toBufferedInputStream(File localFile,int bufferSize) throws IOException {
		// 包装文件输入流  
		return toBufferedInputStream(new FileInputStream(localFile),bufferSize);
    }
	
    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
    	if(isBuffered(input)){
    		 return (BufferedInputStream) input ;
    	}else{
            return org.apache.commons.io.output.ByteArrayOutputStream.toBufferedInputStream(input);
    	}
    }
	
	public static InputStream toBufferedInputStream(InputStream input,int bufferSize) throws IOException {
    	if(isBuffered(input)){
    		 return (BufferedInputStream) input ;
    	}else{
    		if (bufferSize > 0) {
	            return new BufferedInputStream(input, bufferSize);
	        }
	        return new BufferedInputStream(input);
    	}
    }
	
	public static OutputStream toBufferedOutputStream(OutputStream output) throws IOException {
		if(isBuffered(output)){
	   		return (BufferedOutputStream) output ;
	   	}else{
	        return toBufferedOutputStream(output , BUFFER_SIZE);
	   	}
    }
	
	public static OutputStream toBufferedOutputStream(OutputStream output,int bufferSize) throws IOException {
		if(isBuffered(output)){
	   		 return (BufferedOutputStream) output ;
	   	}else{
	   		if (bufferSize > 0) {
	            return new BufferedOutputStream(output, bufferSize);
	        }
	        return new BufferedOutputStream(output);
	   	}
    }
	
	public static boolean isBuffered(InputStream input) {
        return input instanceof BufferedInputStream;
    }

    public static boolean isBuffered(OutputStream output) {
        return output instanceof BufferedOutputStream;
    }
    
    public static BufferedReader toBufferedReader(InputStream input){
		return new BufferedReader(new InputStreamReader(input));
	}
    
    public static BufferedWriter toBufferedWriter(OutputStream output){
		return new BufferedWriter(new OutputStreamWriter(output));
	}
    
    public static boolean isPrint(OutputStream output) {
        return output instanceof PrintStream;
    }

    public static InputStream toByteArrayInputStream(byte[] inputBytes){
        return new ByteArrayInputStream(inputBytes);
    }
    
    public static InputStream toByteArrayInputStream(String text) {
        return toByteArrayInputStream(text.getBytes());
    }
    
    public static DataInputStream toDataInputStream(InputStream input) {
        return isDataInput(input) ? (DataInputStream) input : new DataInputStream(input);
    }
    
    private static boolean isDataInput(InputStream input) {
        return input instanceof DataInputStream;
    }
    
	public static DataOutputStream toDataOutputStream(OutputStream output){
		return isDataOutput(output) ? (DataOutputStream) output : new DataOutputStream(output);
	}
	
	private static boolean isDataOutput(OutputStream output) {
        return output instanceof DataOutputStream;
    }
	
    
    public static FileInputStream toFileInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }
    
    /**
     * 获得一个FileOutputStream对象
     * @param file
     * @param append ： true:向文件尾部追见数据; false:清楚旧数据
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream toFileOutputStream(File file,boolean append) throws FileNotFoundException {
		return new FileOutputStream(file, append);
	}
    
	public static PrintStream toPrintStream(File file) {
    	FileOutputStream stream = null;
        try {
        	stream = new FileOutputStream(file,true);
			return toPrintStream(stream);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
    }
    
    public static PrintStream toPrintStream(OutputStream output) {
        return isPrint(output) ? (PrintStream) output : new PrintStream(output);
    }
	
    public static String toInputText(InputStream input, String charset) throws IOException {
		BufferedReader reader = null;
		try {
			StringBuffer result = new StringBuffer();
			reader = new BufferedReader(new InputStreamReader(input, charset));
			String strRead = null;
			int row = 0;
			while ((strRead = reader.readLine()) != null) {
				// 多行数据，且当前行读取到数据，则在上一行之后添加换行符
				if (row > 0 && strRead != null && strRead.length() > 0) {
					result.append("\r\n");
				}
				result.append(strRead);
				row++;
			}
			return result.toString();
		} finally {
			// 释放资源
			HttpIOUtils.closeQuietly(input);
			HttpIOUtils.closeQuietly(reader);
		}
    }
    
}
