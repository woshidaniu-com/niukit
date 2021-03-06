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
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.CharsetUtils;
import com.woshidaniu.io.utils.FileUtils;

public abstract class ZipUtils extends CompressUtils{

	protected static final CharSequence EXT = ".zip";
	protected static Logger LOG = LoggerFactory.getLogger(ZipUtils.class);
	

	public static byte[] toCompressedBytes(Collection<File>  srcFiles, File destFile) throws IOException {
		byte[] outBytes = null;
		if(srcFiles != null){
			ByteArrayOutputStream outzip = null;
			try {	
				outzip = toCompressedOutputStream(srcFiles,destFile);
				//获取压缩后的结果
				outBytes = outzip.toByteArray();
			} finally {
				IOUtils.closeQuietly(outzip);
			}
			return outBytes;
		}
		return null;
	}
	
	public static byte[] toCompressedBytes(File srcFiles, File destFile) throws IOException {
		byte[] outBytes = null;
		if(srcFiles != null){
			ByteArrayOutputStream outzip = null;
			try {	
				outzip = toCompressedOutputStream(srcFiles,destFile);
				//获取压缩后的结果
				outBytes = outzip.toByteArray();
			} finally {
				IOUtils.closeQuietly(outzip);
			}
			return outBytes;
		}
		return null;
	}
	
	public static ByteArrayInputStream toCompressedInputStream(Collection<File> srcFile, File destFile) throws IOException {
		return new ByteArrayInputStream(toCompressedBytes(srcFile,destFile));
	}
	
	public static ByteArrayInputStream toCompressedInputStream(File srcFile, File destFile) throws IOException {
		return new ByteArrayInputStream(toCompressedBytes(srcFile,destFile));
	}
	
	public static ByteArrayOutputStream toCompressedOutputStream(Collection<File>  srcFiles, File destFile) throws IOException {
		if(srcFiles != null){
			ByteArrayOutputStream outzip = new ByteArrayOutputStream();
			ArchiveOutputStream archOuts = null;
			try {	
				archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);
				ZipUtils.compressFiles( srcFiles, archOuts, File.separator );
				//获取压缩后的结果
				return outzip;
			} catch (ArchiveException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(archOuts);
			}
		}
		return null;
	}
	
	public static ByteArrayOutputStream toCompressedOutputStream(File srcFile, File destFile) throws IOException {
		if(srcFile != null){
			ByteArrayOutputStream outzip = new ByteArrayOutputStream();
			ArchiveOutputStream archOuts = null;
			try {	
				archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);
				ZipUtils.compressFile( srcFile, archOuts, File.separator );
				//获取压缩后的结果
				return outzip;
			} catch (ArchiveException e) {
				e.printStackTrace();
			} finally {
				IOUtils.closeQuietly(archOuts);
			}
		}
		return null;
	}
	
	
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
		return CharsetUtils.newStringUtf8(ZipUtils.compress(text.getBytes()));
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
		ZipOutputStream zipOutput = null;
		ByteArrayOutputStream output = null;
		byte[] outBytes = null;
		try {
			input = new ByteArrayInputStream(databytes);
			output = new ByteArrayOutputStream();
			zipOutput = new ZipOutputStream(output);
			zipOutput.putNextEntry(new ZipEntry("0"));
			IOUtils.copy(input, zipOutput);
			zipOutput.closeEntry();
			//获取压缩后的结果
			outBytes = output.toByteArray();
		} catch (IOException e) {
			outBytes = null;
		}  finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
			IOUtils.closeQuietly(zipOutput);
		}
		return outBytes;
	}

	/** 用于单文件压缩 */
	public static void compress(File srcFile, File destFile) throws IOException {
		if(srcFile != null && srcFile.isFile()){
			ZipArchiveOutputStream out = null;
			InputStream is = null;
			try {
				is = new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE);
				out = new ZipArchiveOutputStream(new BufferedOutputStream( new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));
				out.putArchiveEntry(new ZipArchiveEntry(srcFile,srcFile.getName()));
				IOUtils.copy(is, out);
				out.closeArchiveEntry();
			} finally {
				IOUtils.closeQuietly(is);
				IOUtils.closeQuietly(out);
			}
		}
	}
	

	/**
	 * 
	 * @description	：压缩指定文件夹根目录下指定后缀类型的文件
	 * @author 		： kangzhidong
	 * @date 		：Aug 17, 2015 11:53:51 AM
	 * @param directory
	 * @param extensions
	 * @param outzip
	 * @throws IOException
	 */
	public static void compressDir(File directory, String[] extensions,OutputStream outzip) throws IOException {
		ZipUtils.compressDir(directory, extensions, true, outzip);
	}
	 
	/**
	 * 
	 * @description	： 压缩目录下指定文件后缀的文件
	 * @author 		： kangzhidong
	 * @date 		：Aug 17, 2015 11:52:54 AM
	 * @param directory：目录
	 * @param extensions：后缀
	 * @param recursive：是否递归
	 * @param outzip：压缩流的输出流
	 * @throws IOException
	 */
	public static void compressDir(File directory, String[] extensions, boolean recursive, OutputStream outzip) throws IOException {
		if(directory != null && directory.isDirectory()){
			ArchiveOutputStream ouput = null;
			try {
				/**
				 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
				 */
				ouput = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);
				Collection<File> inputFiles = FileUtils.listFiles(directory,extensions, recursive);
				ZipUtils.compressFiles(inputFiles, ouput, directory.getAbsolutePath());
			
			} catch (ArchiveException e) {
				LOG.error(e.getLocalizedMessage());
			} finally {
				IOUtils.closeQuietly(ouput);
			}
		}
	}


	/**
	 * 
	 * @description: 处理多文档模式的xls文件打包
	 * @author : kangzhidong
	 * @date : 2014-4-17
	 * @time : 上午09:20:52
	 * @param tmpDir
	 * @param sheet_name
	 * @param tempFiles
	 * @param out
	 * @throws IOException
	 * @throws ArchiveException
	 */
	public static void compressFiles(Collection<File> tempFiles, OutputStream outzip)throws IOException {
		ZipUtils.compressFiles(tempFiles, outzip, "");
	}

	/**
	 * 
	 * @description: TODO(描述这个方法的作用)
	 * @author : kangzhidong
	 * @date 下午07:44:49 2015-3-17
	 * @param tempFiles
	 * @param outzip
	 * @param basePath
	 * @throws IOException
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void compressFiles(Collection<File> tempFiles,OutputStream outzip,String basePath) throws IOException {
		LOG.info("开始执行zip压缩...");
		try {
			/**
			 * 打包的方法我们会用到ZipOutputStream这样一个输出流, 所以这里我们把输出流转换一下
			 */
			ArchiveOutputStream archOuts = FACTORY.createArchiveOutputStream(ArchiveStreamFactory.ZIP, outzip);
			ZipUtils.compressFiles(tempFiles, archOuts, basePath);
			
			archOuts.close();
			LOG.info("zip压缩完成！");
			LOG.info("开始删除临时文件...");
			for (File file : tempFiles) {
				file.deleteOnExit();
			}
			LOG.info("临时文件删除完成！");
		} catch (ArchiveException e) {
			LOG.error(e.getLocalizedMessage());
		}
	}

	/**
	 * 这个方法接受的就是一个所要打包文件的集合， 还有一个ArchiveOutputStream
	 */
	private static void compressFiles(Collection<File>  inputFiles,ArchiveOutputStream ouputStream, String basePath) throws IOException {
		for (File file : inputFiles) {
			compressFile(file, ouputStream, basePath);
		}
		ouputStream.flush();
		ouputStream.finish();
	}

	/**
	 * 
	 * @description	：根据输入的文件与输出流对文件进行ZIP打包
	 * @author 		： kangzhidong
	 * @date 		：Aug 11, 2015 4:01:29 PM
	 * @param inputFile
	 * @param ouputStream
	 * @param basePath
	 * @throws IOException
	 */
	private static void compressFile(File inputFile, ArchiveOutputStream ouputStream, String basePath) throws IOException {
		try {
			if (inputFile.exists()) {
				/**
				 * 如果是目录的话这里是不采取操作的， 至于目录的打包正在研究中
				 */
				if (inputFile.isFile()) {
					if (ouputStream instanceof ZipArchiveOutputStream) {

						ZipArchiveOutputStream zipOut = (ZipArchiveOutputStream) ouputStream;
						ZipArchiveEntry zipEntry = new ZipArchiveEntry(inputFile, inputFile.getPath().replace(basePath,""));
						zipOut.putArchiveEntry(zipEntry);
						zipOut.setUseZip64(Zip64Mode.AsNeeded);

						// 向压缩文件中输出数据
						FileInputStream input = null;
						BufferedInputStream bufferInput = null;
						try {
							int nNumber;
							byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
							input = new FileInputStream(inputFile);
							bufferInput = new BufferedInputStream(input,DEFAULT_BUFFER_SIZE);
							while ((nNumber = bufferInput.read(buffer)) != -1) {
								zipOut.write(buffer, 0, nNumber);
							}
							zipOut.closeArchiveEntry();
						} finally {
							// 关闭创建的流对象
							IOUtils.closeQuietly(bufferInput);
							IOUtils.closeQuietly(input);
						}
					}
				} else {
					try {
						Collection<File> inputFiles = FileUtils.listFiles(inputFile,null, true);
						for (File file : inputFiles) {
							compressFile(file, ouputStream, basePath);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (ZipException zipe) {
			LOG.error(inputFile.getName() + "不是一个ZIP文件！文件格式错误");
		}
	}

	

	public void compress(Collection<File>  files, File destFile) throws IOException {
		compress(files, destFile, "UTF-8");
	}

	public void compress(Collection<File>  files, File destFile, String encoding)
			throws IOException {
		if (files != null) {
			Map<String, File> map = new HashMap<String, File>();
			for (File f : files) {
				FileUtils.getFiles(f, null, map);
			}
			if (!map.isEmpty()) {
				ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(destFile);
				zaos.setEncoding(encoding);
				// 执行压缩
				compress(zaos, map);
			}
		}
	}

	public void compress(Collection<File>  files, OutputStream out) throws IOException {
		compress(files, out, "UTF-8");
	}

	public void compress(Collection<File>  files, OutputStream out, String encoding)
			throws IOException {
		if (files != null) {
			Map<String, File> map = new HashMap<String, File>();
			for (File f : files) {
				FileUtils.getFiles(f, null, map);
			}
			if (!map.isEmpty()) {
				try {
					ZipArchiveOutputStream zaos = new ZipArchiveOutputStream(
							out);
					// 设置编码，支持中文
					zaos.setEncoding(encoding);
					// 执行压缩
					compress(zaos, map);
				} catch (IOException ex) {
					LOG.error(ex.getMessage(), ex);
				}
			}
		}
	}

	private void compress(ArchiveOutputStream zaos, Map<String, File> map) throws IOException {
		for (Map.Entry<String, File> entry : map.entrySet()) {
			File file = entry.getValue();
			if (!file.exists()) {
				continue;
			}
			ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, entry.getKey());
			zaos.putArchiveEntry(zipArchiveEntry);
			// folder
			if (zipArchiveEntry.isDirectory()) {
				zaos.closeArchiveEntry();
				continue;
			}
			// file
			InputStream fis = new FileInputStream(file);
			IOUtils.copy(fis, zaos);
			fis.close();
			zaos.closeArchiveEntry();
		}
		zaos.finish();
		zaos.close();
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
		return CharsetUtils.newStringUtf8(ZipUtils.decompress(text.getBytes()));
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
	
	public void decompress(File zipFile) throws IOException {
		decompress(zipFile, zipFile);
	}
	
	public static void decompress(File srcFile, File destDir) throws IOException {
		// 预处理存储目标目录
		destDir = FileUtils.getDestDir(destDir);
		ZipArchiveInputStream input = null;
		try {
			input = new ZipArchiveInputStream(new BufferedInputStream(new FileInputStream(srcFile), DEFAULT_BUFFER_SIZE));
			ZipArchiveEntry entry = null;
			while ((entry = input.getNextZipEntry()) != null) {
				if (entry.isDirectory()) {
					File directory = new File(destDir, entry.getName());
					directory.mkdirs();
				} else {
					OutputStream output = null;
					try {
						output = new BufferedOutputStream(new FileOutputStream(new File(destDir, entry.getName())),DEFAULT_BUFFER_SIZE);
						IOUtils.copy(input, output);
					} finally {
						IOUtils.closeQuietly(output);
					}
				}
			}
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public void decompress(File srcFile, File destDir, String encoding) throws IOException {
		// 预处理存储目标目录
		destDir = FileUtils.getDestDir(destDir);
		// 加载zip文件对象
		ZipFile file = null;
		try {
			file = new ZipFile(srcFile.getAbsolutePath(), encoding);
			// 迭代zip文件明细
			Enumeration<ZipArchiveEntry> en = file.getEntries();
			ZipArchiveEntry ze;
			while (en.hasMoreElements()) {
				ze = en.nextElement();
				// 当前明细名称的文件名
				String zipname = ze.getName();
				// 后缀
				String extension = FilenameUtils.getExtension(zipname);
				if ("zip".equalsIgnoreCase(extension)) {
					decompressInnerZip(file, destDir, ze, encoding);
				} else if ("gz".equalsIgnoreCase(extension)) {
					decompressInnerZip(file, destDir, ze, encoding);
				} else {
					// 获得目标目录
					File folder = new File(destDir.getAbsolutePath(), zipname);
					// 创建完整路径
					if (ze.isDirectory()) {
						folder.setWritable(true);
						folder.setReadable(true);
						folder.mkdirs();
						continue;
					} else {
						File parent = folder.getParentFile();
						if (!parent.exists()) {
							parent.setWritable(true);
							parent.setReadable(true);
							parent.mkdirs();
						}
					}
					InputStream in = null;
					OutputStream os = null;
					try {
						in = new ZipArchiveInputStream(file.getInputStream(ze), encoding, true);
						os = new FileOutputStream(folder);
						IOUtils.copy(in, os);
					} finally {
						IOUtils.closeQuietly(in);
						IOUtils.closeQuietly(os);
					}
				}
			}
		}finally {
			IOUtils.closeQuietly(file);
		}
	}

	private void decompressInnerZip(ZipFile file, File destDir,ZipArchiveEntry ze, String encoding) throws IOException {
		// 当前明细名称的文件名
		String zipname = ze.getName();
		// 获得目标目录
		File folder = new File(destDir.getAbsolutePath());
		String innerzip = StringUtils.removeEnd(zipname, ".zip");
		File innerfolder = new File(folder + File.separator + innerzip);
		if (!innerfolder.exists()) {
			innerfolder.setWritable(true);
			innerfolder.setReadable(true);
			innerfolder.mkdirs();
		}
		ZipArchiveInputStream in = null;
		try {
			in = new ZipArchiveInputStream(file.getInputStream(ze), encoding, true);
			ZipArchiveEntry innerzae = null;
			while ((innerzae = in.getNextZipEntry()) != null) {
				OutputStream fos = null;
				try {
					fos = new FileOutputStream(folder + File.separator + innerzip + File.separator + innerzae.getName());
					IOUtils.copy(in, fos);
					fos.flush();
				} finally {
					IOUtils.closeQuietly(in);
					IOUtils.closeQuietly(fos);
				}
			}
		} finally {
			IOUtils.closeQuietly(in);
		}
	}
	
	public void decompress(String zipPath) throws IOException {
		decompress(zipPath, zipPath);
	}

	public void decompress(String zipPath, String destDirPath)throws IOException {
		decompress(zipPath, destDirPath, "Utf-8");
	}

	public void decompress(String zipPath, String destDirPath, String encoding) throws IOException {
		decompress(new File(zipPath), new File(destDirPath), encoding);
	}
}