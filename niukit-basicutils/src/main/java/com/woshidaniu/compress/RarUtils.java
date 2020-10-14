package com.woshidaniu.compress;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class RarUtils extends CompressUtils{
	
	/**
	 * 根据原始rar路径，解压到指定文件夹下.
	 * 
	 * @param srcFile 原始rar路径
	 * @param destDir 解压到的文件夹
	 * @throws RarException
	 */
	public static void decompress(File srcFile, File destDir) throws IOException, RarException {
		Archive output = null;
		try {
			if (!destDir.exists()) {// 目标目录不存在时，创建该文件夹
				destDir.mkdirs();
			}
			output = new Archive(srcFile);
			output.getMainHeader().print(); // 打印文件信息.
			FileHeader fh = output.nextFileHeader();
			while (fh != null) {
				if (fh.isDirectory()) { // 文件夹
					File fol = new File(destDir,fh.getFileNameString());
					fol.mkdirs();
				} else { // 文件
					File out = new File(destDir,fh.getFileNameString().trim());
					// System.out.println(out.getAbsolutePath());
					FileOutputStream os = null;
					try {
						// 之所以这么写try，是因为万一这里面有了异常，不影响继续解压.
						if (!out.exists()) {
							if (!out.getParentFile().exists()) {// 相对路径可能多级，可能需要创建父目录.
								out.getParentFile().mkdirs();
							}
							out.createNewFile();
						}
						os = new FileOutputStream(out);
						output.extractFile(fh, os);
					} catch (Exception ex) {
						ex.printStackTrace();
					} finally {
						IOUtils.closeQuietly(os);
					}
				}
				fh = output.nextFileHeader();
			}
		} finally {
			IOUtils.closeQuietly(output);
		}
	}
	
}
