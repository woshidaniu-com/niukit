package com.woshidaniu.qa.tools;

import static com.woshidaniu.qa.utils.IOUtils.closeQuietly;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.cglib.core.MethodWrapper;

/**
 * @author shouquan
 * @version 创建时间：2017年5月12日 上午11:38:36 类说明 :配置文件读取类
 */
public class ResourceHelper {

	/**
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getResourceAsStream(final String file) throws FileNotFoundException {
		String _file = file;
		if (null == file) {
			throw new RuntimeException("execute file name can't be null!");
		}
		InputStream inputStream = null;
		if (file.startsWith("file:")) {
			_file = file.replaceFirst("file:", "");
			inputStream = new FileInputStream(new File(_file));
			return inputStream;
		} else {
			if (file.startsWith("classpath:")) {
				_file = file.replaceFirst("classpath:", "");
			}
			inputStream = ResourceHelper.class.getClassLoader().getResourceAsStream(_file);
			if (inputStream == null) {
				throw new FileNotFoundException(String.format("can't find classpath resource[%s]!", _file));
			} else {
				return inputStream;
			}
		}
	}

	/**
	 * 从文件中价值配置项<br>
	 * <br>
	 * o file:开头读取文件系统 <br>
	 * o classpath:开头读取classpath下的文件<br>
	 * o 否则读取 classpath下文件
	 * 
	 * @param file
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Properties loadPropertiesFrom(final String file) {
		InputStream in = null;
		try {
			in = ResourceHelper.getResourceAsStream(file);
			Properties props = new Properties();
			props.load(in);
			return props;
		} catch (IOException e) {
			throw new RuntimeException(String.format("load properties from file[%s] error.", file), e);
		} finally {
			closeQuietly(in);
		}
	}

}
