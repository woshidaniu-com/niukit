package com.woshidaniu.io.utils;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.woshidaniu.basicutils.Assert;

/**
 * 
 *@类名称	: IOUtils.java
 *@类描述	：扩展org.apache.commons.io.IOUtils工具对象
 *@创建人	：kangzhidong
 *@创建时间	：Mar 5, 2016 9:02:58 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class IOUtils extends org.apache.commons.io.IOUtils{
	

	public static final int BUFFER_SIZE = 1024 * 4;
	
	//---------------------------------------------------------------------
	// Copy methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------
	
	/**
	 * Copy the contents of the given InputStream to the given OutputStream.
	 * Leaves both streams open when done.
	 * @param input the InputStream to copy from
	 * @param output the OutputStream to copy to
	 * @return the number of bytes copied
	 * @throws IOException in case of I/O errors
	 */
	public static long copy(InputStream input, OutputStream output,int bufferSize) throws IOException {
		Assert.notNull(input, "No InputStream specified");
		Assert.notNull(output, "No OutputStream specified");
		long count = 0;
		byte[] buffer = new byte[bufferSize];
		int n = -1;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
			count += n;
		}
		// Flush
		output.flush();
		return count;
	}
	
	/**
	 * Copy the contents of the given byte array to the given OutputStream.
	 * Closes the stream when done.
	 * @param in the byte array to copy from
	 * @param out the OutputStream to copy to
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static long copy(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		ByteArrayInputStream byteIn = new ByteArrayInputStream(in);
		long count = copy(byteIn, out);
		byteIn.close();
		IOUtils.closeQuietly(byteIn);
		return count;
	}
	
	/**
	 * Copy the contents of the given InputStream into a String.
	 * Leaves the stream open when done.
	 * @param in the InputStream to copy from
	 * @return the String that has been copied to
	 * @throws IOException in case of I/O errors
	 */
	public static String copyToString(InputStream in, Charset charset) throws IOException {
		Assert.notNull(in, "No InputStream specified");
		StringBuilder out = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in, charset);
		char[] buffer = new char[BUFFER_SIZE];
		int bytesRead = -1;
		while ((bytesRead = reader.read(buffer)) != -1) {
			out.append(buffer, 0, bytesRead);
		}
		IOUtils.closeQuietly(reader);
		return out.toString();
	}

	/**
	 * Copy the contents of the given String to the given output OutputStream.
	 * Leaves the stream open when done.
	 * @param in the String to copy from
	 * @param charset the Charset
	 * @param out the OutputStream to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void copy(String in, Charset charset, OutputStream out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(charset, "No charset specified");
		Assert.notNull(out, "No OutputStream specified");
		Writer writer = new OutputStreamWriter(out, charset);
		writer.write(in);
		writer.flush();
	}
	
	//---------------------------------------------------------------------
	// Write methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------

	/**
	 * Write the contents of the given byte array to the given OutputStream.
	 * Leaves the stream open when done.
	 * @param in the byte array to copy from
	 * @param out the OutputStream to copy to
	 * @throws IOException in case of I/O errors
	 */
	public static void write(byte[] in, OutputStream out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No OutputStream specified");
		out.write(in);
	}

	 /**
	* 方法用途和描述: 输出流
	* @param bytes
	* @param response
	* @throws IOException
	 */
    public static void write(byte bytes[], HttpServletResponse response) throws IOException{
		ServletOutputStream ouputStream = response.getOutputStream();
		ouputStream.write(bytes, 0, bytes.length);
		ouputStream.flush();
		ouputStream.close();
	}
    
    /**
	* 方法用途和描述: 向浏览器输出一个对象
	* @param response
	* @param obj
	* @throws IOException
	 */
	public static void write(HttpServletResponse response, Object obj)throws IOException{
		response.setContentType("application/octet-stream");
		ObjectOutputStream out = new ObjectOutputStream(response.getOutputStream());
		out.writeObject(obj);
		out.flush();
		out.close();
	}

	//---------------------------------------------------------------------
	// Reader methods for System.in
	//---------------------------------------------------------------------
	
	/**
	 * 接收键盘的输入
	 */
	public static String systemIn() throws IOException {
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter a line:");
		return stdin.readLine();
	}
	
	
	//---------------------------------------------------------------------
	// Copy methods for java.io.Reader / java.io.Writer
	//---------------------------------------------------------------------

	/**
	 * Copy the contents of the given Reader to the given Writer.
	 * Closes both when done.
	 * @param in the Reader to copy from
	 * @param out the Writer to copy to
	 * @return the number of characters copied
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static int copy(Reader in, Writer out,int bufferSize) throws IOException {
		Assert.notNull(in, "No Reader specified");
		Assert.notNull(out, "No Writer specified");
		try {
			int count = 0;
			char[] buffer = new char[bufferSize];
			int n = -1;
			while ((n = in.read(buffer)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
			return count;
		}
		finally {
			try {
				in.close();
				out.flush();
				out.close();
			}
			catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given String to the given output Writer.
	 * Closes the write when done.
	 * @param in the String to copy from
	 * @param out the Writer to copy to
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static void copy(String in, Writer out) throws IOException {
		Assert.notNull(in, "No input String specified");
		Assert.notNull(out, "No Writer specified");
		try {
			out.write(in);
		}
		finally {
			try {
				out.close();
			}
			catch (IOException ex) {
			}
		}
	}

	/**
	 * Copy the contents of the given Reader into a String.
	 * Closes the reader when done.
	 * @param in the reader to copy from
	 * @return the String that has been copied to
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static String copyToString(Reader in) throws IOException {
		StringWriter out = new StringWriter();
		copy(in, out);
		return out.toString();
	}
	
	//---------------------------------------------------------------------
	// Stream Warp methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------

    public static InputStream toBufferedInputStream(InputStream input) throws IOException {
    	if(isBuffered(input)){
    		 return (BufferedInputStream) input ;
    	}else{
            return ByteArrayOutputStream.toBufferedInputStream(input);
    	}
    }
	
    public static InputStream toBufferedInputStream(File localFile,int bufferSize) throws IOException {
		// 包装文件输入流  
		return toBufferedInputStream(new FileInputStream(localFile),bufferSize);
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
	        return new BufferedOutputStream(output);
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
	
	public static boolean isBuffered(InputStream in) {
        return in instanceof BufferedInputStream;
    }

	public static boolean isBuffered(OutputStream out) {
        return out instanceof BufferedOutputStream;
    }
    
    public static DataInputStream toDataInputStream(InputStream in) {
        return isData(in) ? (DataInputStream) in : new DataInputStream(in);
    }

    public static boolean isPrint(OutputStream out) {
        return out instanceof PrintStream;
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
    
    public static PrintStream toPrintStream(OutputStream out) {
        return isPrint(out) ? (PrintStream) out : new PrintStream(out);
    }

    private static boolean isData(InputStream in) {
        return in instanceof DataInputStream;
    }
    
    public static InputStream toByteArrayInputStream(String text) {
        return toByteArrayInputStream(text.getBytes());
    }
    
    public static InputStream toByteArrayInputStream(byte[] inputBytes){
        return new ByteArrayInputStream(inputBytes);
    }
    
    public static FileInputStream toFileInputStream(File file) throws IOException {
        return new FileInputStream(file);
    }
    
    /**
	 * Returns a variant of the given {@link InputStream} where calling
	 * {@link InputStream#close() close()} has no effect.
	 * @param in the InputStream to decorate
	 * @return a version of the InputStream that ignores calls to close
	 */
	public static InputStream toNonClosingInputStream(InputStream in) {
		Assert.notNull(in, "No InputStream specified");
		return new NonClosingInputStream(in);
	}

	/**
	 * Returns a variant of the given {@link OutputStream} where calling
	 * {@link OutputStream#close() close()} has no effect.
	 * @param out the OutputStream to decorate
	 * @return a version of the OutputStream that ignores calls to close
	 */
	public static OutputStream toNonClosingOutputStream(OutputStream out) {
		Assert.notNull(out, "No OutputStream specified");
		return new NonClosingOutputStream(out);
	}

	public static class NonClosingInputStream extends FilterInputStream {

		public NonClosingInputStream(InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
		}
	}

	public static class NonClosingOutputStream extends FilterOutputStream {

		public NonClosingOutputStream(OutputStream out) {
			super(out);
		}

		@Override
		public void write(byte[] b, int off, int let) throws IOException {
			// It is critical that we override this method for performance
			out.write(b, off, let);
		}

		@Override
		public void close() throws IOException {
		}
	}
	
	//---------------------------------------------------------------------
	// Close methods for java.io.InputStream / java.io.OutputStream
	//---------------------------------------------------------------------

	public static void closeInput(InputStream stream) {
        try {
            if (stream != null){
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeOutput(OutputStream stream) {
        try {
            if (stream != null){
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
	 * 
	 * @description	： 跳过指定的长度,实现断点续传  
	 * @author 		： kangzhidong 
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
	 * @author 		： kangzhidong
	 * @date 		：Jan 15, 2016 3:02:5kangzhidongrows IOException
	 */
	public static void skip(FileChannel channel,long offset) throws IOException{
		if (offset > channel.size()) {  
           throw new EOFException("offset [" + offset + "] larger than the length of file : unexpected EOF");  
		}
		//通过调用position()方法跳过已经存在的长度
		channel.position(Math.max(0, offset)); 
	}
}
