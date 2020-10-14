package com.woshidaniu.io.utils;


import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import com.woshidaniu.basicutils.BlankUtils;
/**
 * 
 *@类名称	: DirectoryUtils.java
 *@类描述	：文件目录工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 5, 2016 8:59:16 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class DirectoryUtils {

	/**
	 * 
	 *@描述		：清空某个目录
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 5, 20168:59:24 PM
	 *@param file
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static void clearDir(File file) {
		if (file == null || !file.exists() || file.isFile()) {
			return;
		}
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				clearDir(f);
			}
			f.delete();
		}
	}

	/**
	 * 
	 *@描述		：得到处理后的路径
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 5, 20168:59:32 PM
	 *@param path
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static String getResolvePath(String path) {
		path = path.replace("\\\\", File.separator).replace("\\",File.separator).replace("//", File.separator).replace("/",	File.separator);
		if (path.endsWith(File.separator)) {
			path = path.substring(0, (path.length() - File.separator.length()));
		}
		return path;
	}


	/**
	 * 
	 *@描述		：获得用户目录
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 5, 20168:59:50 PM
	 *@param session 当前http会话
	 *@param userDir  用户目录名
	 *@param dir	分类目录
	 *@param fileType 文件类型
	 *@return
	 *@throws IOException
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static File getUserDir(HttpSession session,String userDir,String dir,String fileType) throws IOException {
		//获得根目录
		File directory = getRootDir(session);
		//得到用户目录下指定的目录
		if (!StringUtils.isBlank(dir)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + dir);
		}
		//得到用户目录下指定的目录下文件类型对应的目录
		if (!StringUtils.isBlank(fileType)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + fileType);
		}
		// 返回最终路径
		if (!directory.isDirectory()) {
			throw new IOException("Source '" + directory+ "' is not a directory");
		}
		return directory;
	}
	
	public static File getUserDir(HttpSession session,String userDir) throws IOException {
		//获得根目录
		File directory = getRootDir(session);
		//得到用户目录
		if (!StringUtils.isBlank(userDir)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + userDir);
		}
		// 返回最终路径
		if (!directory.isDirectory()) {
			throw new IOException("Source '" + directory+ "' is not a directory");
		}
		return directory;
	}
	
	
	public static File getTargetDir(HttpSession session,String targetDir) throws IOException {
		//获得根目录
		File directory = getRootDir(session);
		//添加模块路径
		if (!BlankUtils.isBlank(targetDir)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + targetDir);
		}
		return directory;
	}

	public static File getTargetDir(HttpSession session,String rootDir, String targetDir,String dir) throws IOException {
		//获得系统设置文件存储根目录
		File directory = getTargetDir(session,rootDir);
		//获取用户目录
		if (!BlankUtils.isBlank(targetDir)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + targetDir);
		}
		// 返回最终路径
		String targetPath = "";
		if(!BlankUtils.isBlank(dir)){
			// 处理目标路径
			targetPath = DirectoryUtils.getResolvePath(dir);
			if (targetPath.startsWith(File.separator)) {
				targetPath = targetPath.substring(File.separator.length());
			}
		}
		directory = getExistDir(directory.getAbsolutePath()+ File.separator + targetPath);
		if (!directory.isDirectory()) {
			throw new IOException("Source '" + directory+ "' is not a directory");
		}
		return directory;
	}
	
	
	/**
	 * 
	 *@描述		：获取根目录
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 5, 20169:00:53 PM
	 *@param session
	 *@return
	 *@throws IOException
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static File getRootDir(HttpSession session) throws IOException {
		File directory = null;
		// 从配置文件找到指定的外部存储路径配置
		String directoryPath = System.getProperty("system.root.dir");
		if (StringUtils.isBlank(directoryPath)) {
			// 未找到指定的外部目录，使用本地程序根目录
			directoryPath = session.getServletContext().getRealPath(File.separator);
			// .getContextPath()
			directory = getExistDir(directoryPath);
		} else {
			// 判断指定的路径是否存在
			directory = getExistDir(directoryPath);
		}
		return directory;
	}

	public static File getExistDir(HttpSession session,String dirPath ) throws IOException {
		//获得根目录
		return getExistDir(getRootDir(session).getAbsolutePath()+ File.separator + dirPath);
	}
	
	public static File getExistDir(String dirPath ) {
		File directory = new File(DirectoryUtils.getResolvePath(dirPath));
		// 如果目录不存在，则创建根目录
		if (!directory.exists()) {
			directory.setReadable(true);
			directory.setWritable(true);
			directory.mkdirs();
		}
		return directory;
	}
	
	public static File getTmpDir(HttpSession session) throws IOException {
		//获得tmpdir目录
		return getExistDir(getRootDir(session).getAbsolutePath()+ File.separator + System.getProperty("system.temp.dir"));
	}
	
	public static File getRealPath(HttpSession session,String dirPath ){
		//获得根目录
		return getExistDir(session.getServletContext().getRealPath(DirectoryUtils.getResolvePath(dirPath)));
	}

	/**
	 * 
	 *@描述		：根据业务模块得到附件目录
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 5, 20169:01:02 PM
	 *@param session
	 *@param module
	 *@return
	 *@throws IOException
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static File getAttachmentDir(HttpSession session,String module) throws IOException {
		//获得根目录
		File directory = getTargetDir(session,module);
		//添加文件目录路径
		String fileDir = File.separator + Calendar.getInstance().get(Calendar.YEAR) + File.separator + (Calendar.getInstance().get(Calendar.MONTH) + 1);
		if (!StringUtils.isBlank(fileDir)) {
			directory = getExistDir(directory.getAbsolutePath()+ File.separator + fileDir.toString());
		}
		if (!directory.isDirectory()) {
			throw new IOException("Source '" + directory+ "' is not a directory");
		}
		return directory;
	}
	
	
	public static File getAttachmentFile(HttpSession session,String module,String filePath) throws IOException{
		File directory = getAttachmentDir(session,module);
		//添加模块路径
		if (!StringUtils.isBlank(filePath)) {
			return new File(DirectoryUtils.getResolvePath(directory.getAbsolutePath()+ File.separator + filePath));
		}
		return null;
	}
	
	public static boolean isExist(HttpSession session,String path ) throws IOException {
		//获得根目录
		File directory = getRootDir(session);
		return isExist(directory.getAbsolutePath()+ File.separator + path);
	}
	
	public static boolean isExist(String path ) {
		File target = new File(DirectoryUtils.getResolvePath(path));
		// 如果目录不存在，则创建根目录
		if (!target.exists()) {
			return false;
		}
		return true;
	}
	
	
}
