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

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.woshidaniu.basicutils.CharsetUtils;
/**
 * 
 * @className	： GZipUtils
 * @description	： .gz 文件压缩解压工具
 * @author 		： kangzhidong
 * @date		： Aug 11, 2015 10:21:05 AM
 */
public abstract class GZipUtils extends CompressUtils{

	protected static final CharSequence EXT = ".gz";

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
		return CharsetUtils.newStringUtf8(GZipUtils.compress(text.getBytes()));
	}
	
	/**
	 * 
	 * @description	：字节压缩
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:24:27 AM
	 * @param databytes
	 * @return
	 * @throws Exception
	 */
	public static byte[] compress(byte[] databytes) throws IOException {
		InputStream input = null;
		ByteArrayOutputStream output = null;
		byte[] outBytes = null;
		try {
			input = new ByteArrayInputStream(databytes);
			output = new ByteArrayOutputStream();
			//压缩
			GZipUtils.compress(input,output );
			//获取压缩后的结果
			outBytes = output.toByteArray();
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		return outBytes;
	}

	/**
	 * 
	 * @description	： 文件压缩
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:24:17 AM
	 * @param file
	 * @throws Exception
	 */
	public static void compress(File file) throws IOException {
		GZipUtils.compress(file, true);
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
		GZipUtils.compress(srcFile,destFile );
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
			GZipUtils.compress(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}


	/**
	 * 
	 * @description	：数据流压缩
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:22:20 AM
	 * @param input
	 * @param os
	 * @throws IOException
	 */
	public static void compress(InputStream is, OutputStream os) throws IOException {
		InputStream input = null;
		GzipCompressorOutputStream output = null;
		try {
			input = new BufferedInputStream(is, DEFAULT_BUFFER_SIZE);
			output = new GzipCompressorOutputStream(new BufferedOutputStream(os, DEFAULT_BUFFER_SIZE));
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
		GZipUtils.compress(filePath, true);
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
		GZipUtils.compress(new File(filePath), delete);
	}


	/**
	 * 
	 * @description	： 解压缩字符串
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 9:39:04 AM
	 * @param text
	 * @return
	 * @throws IOException 
	 */
	public static String decompressString(String text) throws IOException {
		if (text == null || text.length() == 0) {
			return text;
		}
		return CharsetUtils.newStringUtf8(GZipUtils.decompress(text.getBytes()));
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
			GZipUtils.decompress(input, output);
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
		GZipUtils.decompress(file, true);
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
		GZipUtils.decompress(srcFile, srcFile.getParentFile());
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
			GZipUtils.decompress(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
	}


	/**
	 * 
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
			input = new GzipCompressorInputStream(new BufferedInputStream(in, DEFAULT_BUFFER_SIZE));
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
		GZipUtils.decompress(filePath, true);
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
		GZipUtils.decompress(new File(filePath), delete);
	}
	
}
