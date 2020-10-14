package com.woshidaniu.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.woshidaniu.basicutils.CharsetUtils;

/**
 * 
 * @className	： BZip2Utils
 * @description	：.bz2文件压缩解压工具
 * @author 		： kangzhidong
 * @date		： Aug 11, 2015 10:43:15 AM
 */
public abstract class BZip2Utils extends CompressUtils{
	
	protected static final CharSequence EXT = ".bz2";

	/**
	 * 
	 * @description	： 压缩字符串
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:39:04 AM
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public static String compressString(String text) throws IOException {
		if (text == null || text.length() == 0) {
			return text;
		}
		return CharsetUtils.newStringUtf8(BZip2Utils.compress(text.getBytes()));
	}

	
	/**
	 * 
	 * @description	：压缩字节码
	 * @author 		： kangzhidong
	 * @date 		：Aug 10, 2015 5:29:46 PM
	 * @param databytes
	 * @return
	 * @throws IOException
	 */
	public static byte[] compress(byte[] databytes) throws IOException {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] outBytes = null;
		try {
			input = new ByteArrayInputStream(databytes);
			output = new ByteArrayOutputStream();
			//压缩
			BZip2Utils.compress(input,output );
			//获取压缩后的结果
			outBytes = output.toByteArray();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return outBytes;
	}

	/**
	 * 文件压缩
	 * 
	 * @param file
	 * @param delete 是否删除原始文件
	 * @throws IOException
	 */
	public static void compress(File file) throws IOException {
		BZip2Utils.compress(file, true);
	}

	/**
	 * 
	 * @description	：文件压缩
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:23:58 AM
	 * @param srcFile
	 * @param delete：是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(File srcFile, boolean delete) throws IOException {
		File destFile = new File(srcFile.getParentFile(), FilenameUtils.getBaseName(srcFile.getName()) + EXT );
		//压缩
		BZip2Utils.compress(srcFile,destFile );
		if (delete) {
			srcFile.delete();
		}
	}

	public static void compress(File srcFile, File destFile) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new FileInputStream(srcFile);
			output = new FileOutputStream(destFile);
			//压缩
			BZip2Utils.compress(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
	
	public static void compress(InputStream in, OutputStream out) throws IOException {
		InputStream input = null;
		BZip2CompressorOutputStream output = null;
		try {
			input = new BufferedInputStream(in, DEFAULT_BUFFER_SIZE);
			output = new BZip2CompressorOutputStream(new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE));
			IOUtils.copy(input, output);
			output.finish();
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
 
	/**
	 * 
	 * @description ：文件压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:18:23 AM
	 * @param filePath
	 * @throws Exception
	 */
	public static void compress(String filePath) throws IOException {
		BZip2Utils.compress(filePath, true);
	}
	 
	/**
	 * 
	 * @description ： 文件压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:18:34 AM
	 * @param filePath
	 * @param delete
	 *            ：是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(String filePath, boolean delete) throws IOException {
		BZip2Utils.compress(new File(filePath), delete);
	}
	 


	/**
	 * 
	 * @description	： 字节解压缩
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:30:54 AM
	 * @param databytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] decompress(byte[] databytes) throws IOException {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] outBytes = null;
		try {
			input = new ByteArrayInputStream(databytes);
			output = new ByteArrayOutputStream();
			// 解压缩
			BZip2Utils.decompress(input, output);
			//获取解压后的数据
			outBytes = output.toByteArray();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return outBytes;
	}
	
	/**
	 * 
	 * @description ： 文件解压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:11:13 AM
	 * @param file
	 * @throws Exception
	 */
	public static void decompress(File file) throws IOException {
		BZip2Utils.decompress(file, true);
	}

	/**
	 * 
	 * @description ：文件解压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:06:15 AM
	 * @param srcFile
	 * @param delete ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File srcFile, boolean delete) throws IOException {
		// 解压缩
		BZip2Utils.decompress(srcFile, srcFile.getParentFile());
		if (delete) {
			srcFile.delete();
		}
	}
	
	public static void decompress(File srcFile, File destDir) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			File destFile = new File(destDir, FilenameUtils.getBaseName(srcFile.getName()));
			input = new FileInputStream(srcFile);
			output = new FileOutputStream(destFile);
			BZip2Utils.decompress(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}
    
	/**
	 * @description ： 数据流解压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:03:36 AM
	 * @param in
	 * @param out
	 * @throws Exception
	 */
	public static void decompress(InputStream in, OutputStream out) throws IOException {
		InputStream input = null;
		OutputStream output = null;
		try {
			input = new BZip2CompressorInputStream(new BufferedInputStream(in, DEFAULT_BUFFER_SIZE));
			output = new BufferedOutputStream(out, DEFAULT_BUFFER_SIZE);
			IOUtils.copy(input, output);
			output.flush();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}

	/**
	 * 
	 * @description ： 文件解压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:03:56 AM
	 * @param filePath
	 * @throws Exception
	 */
	public static void decompress(String filePath) throws Exception {
		BZip2Utils.decompress(filePath, true);
	}

	/**
	 * 
	 * @description ：文件解压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:04:12 AM
	 * @param filePath
	 * @param delete
	 *            ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(String filePath, boolean delete) throws IOException {
		BZip2Utils.decompress(new File(filePath), delete);
	}
 
}
