package com.woshidaniu.io.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.woshidaniu.basicutils.Assert;
/**
 * 
 * @className: FileCopyUtils
 * @description: 文件拷贝操作工具
 * @author : kangzhidong
 * @date : 上午11:14:22 2015-3-18
 * @modify by:
 * @modify date :
 * @modify description :
 */
public class FileCopyUtils extends FileUtils{
	
	//---------------------------------------------------------------------
	// Copy methods for java.io.File
	//---------------------------------------------------------------------

	public static long copy(String infile, String destFile) throws IOException {
		Assert.notNull(infile, "No input File Path specified");
		Assert.notNull(destFile, "No input File Path specified");
		File srcFile = new File(infile);
		File distFile = new File(destFile);
		if (distFile.exists()) {
			distFile.delete();
		}
		return copy(srcFile, distFile);
	}
	
	/**
	 * Copy the contents of the given input File to the given output File. <br/>
     * 将给定的文件内容拷贝到给定的输出文件里
	 * @param in the file to copy from <br/>
     * 被拷贝的文件
	 * @param out the file to copy to
     * 输出文件
	 * @return the number of bytes copied <br/>
     * 拷贝的总bytes
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static long copy(File in, File out) throws IOException {
		Assert.notNull(in, "No input File specified");
		Assert.notNull(out, "No output File specified");
		long length = 0;
		InputStream fileIn = null;
		OutputStream fileOut = null;
		try {
			fileIn = new FileInputStream(in);
			fileOut = new FileOutputStream(out);
			length = IOUtils.copy(fileIn,fileOut,BUFFER_SIZE);
		} finally  {
			IOUtils.closeQuietly(fileIn);
			IOUtils.closeQuietly(fileOut);
		}
		return length;
	}

	/**
	 * Copy the contents of the given byte array to the given output File. <br/>
     * 将给定的字节数据内容拷贝到给定的文件里
	 * @param in the byte array to copy from <br/>
     * 字节数组内容
	 * @param out the file to copy to  <br/>
     * 输出到的文件类
	 * @throws java.io.IOException in case of I/O errors
	 */
	public static void copy(byte[] in, File out) throws IOException {
		Assert.notNull(in, "No input byte array specified");
		Assert.notNull(out, "No output File specified");
		ByteArrayInputStream inStream = null;
		FileOutputStream fileOut = null;
		try {
			inStream = new ByteArrayInputStream(in);
			fileOut = new FileOutputStream(out);
			IOUtils.copy(inStream, fileOut,BUFFER_SIZE);
		} finally  {
			IOUtils.closeQuietly(inStream);
			IOUtils.closeQuietly(fileOut);
		}
	}
	
	/**
	 * Recursively copy the contents of the <code>src</code> file/directory
	 * to the <code>dest</code> file/directory. <br/>
     * 递归的拷贝给定的src文件或文件夹到目标文件夹或文件内
	 * @param src the source directory <br/>
     * 源文件夹或文件
	 * @param dest the destination directory <br/>
     * 目标 文件夹或文件
	 * @throws java.io.IOException in the case of I/O errors
	 */
	public static void copyDir(File src, File dest) throws IOException {
		Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
		Assert.notNull(dest, "Destination File must not be null");
		//递归拷贝
		doCopyRecursively(src, dest);
	}

	/**
	 * Actually copy the contents of the <code>src</code> file/directory
	 * to the <code>dest</code> file/directory. <br/>
     * 实际拷贝文件或文件夹的内容到目标文件或文件夹里
	 * @param src the source directory<br/>源目录
	 * @param dest the destination directory,<br/>目标目录
	 * @throws java.io.IOException in the case of I/O errors
	 */
	private static void doCopyRecursively(File src, File dest) throws IOException {
		if (src.isDirectory()) {
			dest.mkdir();
			File[] entries = src.listFiles();
			if (entries == null) {
				throw new IOException("Could not list files in directory: " + src);
			}
			for (File entry : entries) {
				doCopyRecursively(entry, new File(dest, entry.getName()));
			}
		}
		else if (src.isFile()) {
			try {
				dest.createNewFile();
			}
			catch (IOException ex) {
				IOException ioex = new IOException("Failed to create file: " + dest);
				ioex.initCause(ex);
				throw ioex;
			}
			copy(src, dest);
		}
		else {
			// Special File handle: neither a file not a directory.
			// Simply skip it when contained in nested directory...
		}
	}
	
}
