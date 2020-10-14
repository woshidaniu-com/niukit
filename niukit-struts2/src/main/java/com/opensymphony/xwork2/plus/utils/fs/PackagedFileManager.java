package com.opensymphony.xwork2.plus.utils.fs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.util.fs.DefaultFileManager;

/**
 * 
 *@类名称		： PackagedFileManager.java
 *@类描述		：
 *@创建人		：kangzhidong
 *@创建时间	：Feb 15, 2017 5:17:54 PM
 *@修改人		：
 *@修改时间	：
 *@版本号		: v1.0
 */
public class PackagedFileManager extends DefaultFileManager {

	private static final Logger LOG = LoggerFactory.getLogger(PackagedFileManager.class);
	
	public InputStream loadFile(URL fileUrl) {
        if (fileUrl == null) {
            return null;
        }
        InputStream is = null;
        if (isJarURL(fileUrl)) {
        	try {
				String fileName = fileUrl.getFile();
				String realFile = fileName.substring(0, fileName.indexOf("#$"));// 从文件名中得到jar的文件名
				String targetName = fileName.substring(fileName.indexOf("#$") + 2);// 从文件名中得到匹配到的配置文件名
				//修改源代码，如果fileName被“#$”标记，说明是jar包中的文件
				JarFile jar = new JarFile(new File(realFile));
				//Enumeration<JarEntry> jes = jar.entries();
				JarEntry je = jar.getJarEntry(targetName);
				if (je != null) {
					is = jar.getInputStream(je);
				}
        	} catch (IOException e) {
        		LOG.error("Unable to close input stream", e);
			}
        } else {
        	is = openFile(fileUrl);	
        }
        monitorFile(fileUrl);
        return is;
    }
	
	protected InputStream openFile(URL fileUrl) {
        try {
            InputStream is = fileUrl.openStream();
            if (is == null) {
                throw new IllegalArgumentException("No file '" + fileUrl + "' found as a resource");
            }
            return is;
        } catch (IOException e) {
            throw new IllegalArgumentException("No file '" + fileUrl + "' found as a resource");
        }
    }
	
	public boolean support() {
		return true;
	}
	
	public boolean internal() {
		return false;
	}
	
}
