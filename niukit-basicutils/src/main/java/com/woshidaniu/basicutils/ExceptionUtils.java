/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.basicutils;

/**
 *@类名称:ExceptionUtils.java
 *@类描述：
 *@创建人：kangzhidong
 *@创建时间：Jan 28, 2016 10:58:48 AM
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class ExceptionUtils extends org.apache.commons.lang.exception.ExceptionUtils {
	
	// 从当前系统中获取换行符，默认是"\n"
	protected static String lineSeparator = System.getProperty("line.separator", "\n");

	public static String getFullHtmlStackTrace(Throwable throwable) {
		StringBuilder builder = new StringBuilder();
		builder.append(throwable.toString()).append(lineSeparator);
		StackTraceElement[] trace = throwable.getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			// System.out.println("\tat " + trace[i]);
			builder.append("\tat ").append(trace[i]).append(lineSeparator);
		}
		Throwable ourCause = throwable.getCause();
		if (ourCause != null) {
			trace = ourCause.getStackTrace();
			for (int i = 0; i < trace.length; i++) {
				// System.out.println("\tat " + trace[i]);
				builder.append("\tat ").append(trace[i]).append(lineSeparator);
			}
		}
		return builder.toString().replace(lineSeparator, "<br/>").replace("\tat", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
	}

}
