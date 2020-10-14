package com.woshidaniu.qa.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;


/**
 * @author
 * @version 创建时间：2017年5月12日 上午11:24:16 类说明:message工具类
 * 
 */
public class MessageHelper {
	
	/**
	 * 常量
	 */
	public static final int DEBUG = 0;
	public static final int INFO = 1;
	public static final int WARNING = 2;
	public static final int ERROR = 3;

	public static int level = DEBUG;

	/**
	 * 
	 * @param level
	 * @param maker
	 */
	private static void mark(int level, String marker) {
		switch (level) {
		case ERROR:
			System.err.println(marker);
			break;
		case WARNING:
		default:
			System.out.println(marker);
		}
	}

	/**
	 * 
	 * @param info
	 */
	public static void debug(String info) {
		if (level <= DEBUG) {
			mark(level, "DEBUG:" + info);
		}
	}

	/**
	 * 
	 * @param warn
	 */
	public static void warn(String warn) {
		if (level <= WARNING) {
			mark(WARNING, "WARNING: " + warn);
		}
	}

	/**
	 * 
	 * @param warn
	 * @param e
	 */
	public static void warn(String warn, Throwable e) {
		if (level <= WARNING) {
			mark(WARNING, "WARNING: " + warn);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param info
	 */
	public static void info(String info) {
		if (level <= INFO) {
			mark(INFO, "INFO: " + info);
		}
	}

	/**
	 * 
	 * @param err
	 */
	public static void error(String err) {
		mark(ERROR, "ERROR: " + err);
	}

	/**
	 * 
	 * @param err
	 * @param e
	 */
	public static void error(String err, Throwable e) {
		mark(ERROR, "ERROR: " + err);
		e.printStackTrace();
	}

	/**
	 * 
	 * @param marker
	 * @param e
	 */
	public static void mark(String marker, Throwable e) {
		System.out.println(marker);
		e.printStackTrace();
	}

	private static File debugFile = new File(System.getProperty("user.dir") + "/target/ztester.log");

	/**
	 * 用于记录运行的信息
	 * 
	 * @param info
	 */
	protected static void writerDebugInfo(String info) {
		Writer writer = null;
		try {
			writer = new FileWriter(debugFile, true);
			writer.write(info);
			writer.write("\n");
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
