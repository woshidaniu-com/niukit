package com.woshidaniu.compress;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import com.woshidaniu.io.utils.FileUtils;


/**
 * 
 * @className	： TarArchiveUtils
 * @description	： Apache commons compress TAR打包
 * @author 		： kangzhidong
 * @date		： Aug 10, 2015 4:47:28 PM
 */
public abstract class TarUtils extends CompressUtils{
	
    protected static final String EXT = ".tar";  
    /**
     * 
     * @description	：归档 
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:31:47 AM
     * @param srcFile:要归档的文件或者目录
     * @throws IOException
     */
    public static void compress(File srcFile) throws IOException {  
    	File destFile = new File(srcFile.getParentFile(), FilenameUtils.getBaseName(srcFile.getName()) + EXT );
        TarUtils.compress(srcFile, destFile);
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
		TarUtils.compress(srcFile,destFile );
		if (delete) {
			srcFile.delete();
		}
	}
	
    /**
     * 
     * @description	： 归档 
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:32:21 AM
     * @param srcFile  :要归档的文件或者目录 
     * @param destPath :目标路径 
     * @throws IOException
     */
    public static void compress(File srcFile, File destFile) throws IOException {  
    	TarUtils.compress(srcFile, destFile, BASE_DIR); 
    }  
    
    /**
     * 
     * @description	： 归档 
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:32:21 AM
     * @param srcFile  :要归档的文件或者目录 
     * @param destPath :目标路径 
     * @throws IOException
     */
    public static void compress(File srcFile, File destFile,String basePath) throws IOException {  
    	TarArchiveOutputStream output =  null;
        try {
        	output = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));  
        	TarUtils.compress(srcFile, output, basePath); 
        	output.flush();  
		} finally {
			IOUtils.closeQuietly(output);
		}  
    } 
  
    public static void compress(File[] srcFiles, File destFile) throws IOException {
		//压缩
		ArUtils.compress(srcFiles, destFile ,BASE_DIR);
	}
	
	public static void compress(File[] srcFiles, File destFile,String basePath) throws IOException {
		TarArchiveOutputStream output = null;
		try {
			output = new TarArchiveOutputStream(new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE));  
        	for(File srcFile:srcFiles){
				//压缩
        		TarUtils.compress(srcFile, output , basePath);
			}
		} finally {
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
		TarUtils.compress(filePath, true);
	}

	/**
	 * 
	 * @description ： 文件压缩
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:18:34 AM
	 * @param filePath
	 * @param delete ：是否删除原始文件
	 * @throws Exception
	 */
	public static void compress(String filePath, boolean delete) throws IOException {
		TarUtils.compress(new File(filePath), delete);
	}
	
    /** 
     * 
     * @description	：文件压缩
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 10:57:03 AM
     * @param srcFile  源路径 
     * @param taos TarArchiveOutputStream 
     * @param basePath  归档包内相对路径 
     * @throws Exception
     */
    private static void compress(File srcFile,TarArchiveOutputStream taos,String basePath) throws IOException {  
        if (srcFile.isDirectory()) {  
        	compressDir(srcFile, taos, basePath);  
        } else {  
        	compressFile(srcFile, taos, basePath);  
        }  
    }  
  
    /** 
     * 
     * @description	：目录 归档
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 10:57:51 AM
     * @param dir
     * @param output
     * @param basePath
     * @throws Exception
     */
    private static void compressDir(File dir, TarArchiveOutputStream output,String basePath) throws IOException {  
        File[] files = dir.listFiles();  
        if (files.length < 1) {  
            TarArchiveEntry entry = new TarArchiveEntry(basePath + dir.getName() + File.separator);  
            output.putArchiveEntry(entry);  
            output.closeArchiveEntry();  
        }  
        for (File file : files) {  
            // 递归归档  
            compress(file, output, basePath + dir.getName() + File.separator);  
        }  
    }  
  
    /**
     * 
     * @description	：文件归档
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 10:58:55 AM
     * @param file：待归档文件
     * @param taos
     * @param dir
     * @throws Exception
     */
    private static void compressFile(File file, TarArchiveOutputStream taos,String basePath) throws IOException {  
        /** 
         * 归档内文件名定义 
         * <pre> 
         * 如果有多级目录，那么这里就需要给出包含目录的文件名 
         * 如果用WinRAR打开归档包，中文名将显示为乱码 
         * </pre> 
         */  
    	BufferedInputStream input = null;
        try {
			TarArchiveEntry entry = new TarArchiveEntry(basePath + file.getName());  
			entry.setSize(file.length());  
			taos.putArchiveEntry(entry);  
			input = new BufferedInputStream(new FileInputStream(  file), DEFAULT_BUFFER_SIZE);  
			IOUtils.copy(input, taos);
			taos.closeArchiveEntry();
		} finally {
			IOUtils.closeQuietly(input);
		}  
    }  
  
    /**
     * 
     * @description	： 解归档
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:42:20 AM
     * @param srcFile
     * @throws IOException
     */
    public static void decompress(File srcFile) throws IOException {  
        TarUtils.decompress(srcFile, true);  
    }  
  
    /**
	 * 
	 * @description ：解归档
	 * @author ： kangzhidong
	 * @date ：Aug 11, 2015 9:06:15 AM
	 * @param srcFile
	 * @param delete ：是否删除原始文件
	 * @throws Exception
	 */
	public static void decompress(File srcFile, boolean delete) throws IOException {
		// 解压缩
		TarUtils.decompress(srcFile, srcFile.getParentFile());
		if (delete) {
			srcFile.delete();
		}
	}
	
    /**
     * 
     * @description	：解归档 
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:41:09 AM
     * @param srcFile
     * @param destFile
     * @throws IOException
     */
    public static void decompress(File srcFile, File destFile) throws IOException {  
    	TarArchiveInputStream input = null;
    	try {
    		input = new TarArchiveInputStream(new FileInputStream(srcFile));  
    		TarUtils.decompress(destFile, input);  
		} finally {
			IOUtils.closeQuietly(input);
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
		TarUtils.decompress(filePath, true);
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
		TarUtils.decompress(new File(filePath), delete);
	}  
  
    /** 
     * 
     * @description	：文件 解归档
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:38:33 AM
     * @param destFile
     * @param tais
     * @throws IOException
     */
    private static void decompress(File destFile, TarArchiveInputStream tais)  throws IOException {  
        TarArchiveEntry entry = null;  
        while ((entry = tais.getNextTarEntry()) != null) {  
            // 文件  
            String dir = destFile.getPath() + File.separator + entry.getName();  
            File dirFile = new File(dir);  
            // 文件检查  
            FileUtils.forceProber(dirFile);  
            if (entry.isDirectory()) {  
                dirFile.mkdirs();  
            } else {  
            	decompressFile(dirFile, tais);  
            } 
        }  
    }
    
    /** 
     * 
     * @description	： 文件解归档
     * @author 		： kangzhidong
     * @date 		：Aug 11, 2015 11:35:24 AM
     * @param destFile ： 目标文件 
     * @param tais  ：TarArchiveInputStream
     * @throws IOException
     */
    private static void decompressFile(File destFile, TarArchiveInputStream tais) throws IOException {  
        BufferedOutputStream output = null;  
        try {
        	output = new BufferedOutputStream(new FileOutputStream(destFile), DEFAULT_BUFFER_SIZE);  
			IOUtils.copy(tais, output);
		} finally {
			IOUtils.closeQuietly(output);
		}
    }  
  
}
