/**
 * 
 */
package com.woshidaniu.license;


/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����ƽ̨����
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��7��21������9:31:39
 */
public abstract class WhatOS {
	
	public static final String osName = System.getProperty("os.name").toLowerCase();
	
	public static final boolean isWinPlatform = isWindows();
	
	public static final boolean isLinuxPlatform = isLinux();
	
	public static final String osbit = "";
	
	public static final String jrebit = System.getProperty("sun.arch.data.model");
	
	public static final boolean isWindows(){
		return osName.indexOf("windows") >= 0;
	}
	
	public static final boolean isLinux(){
		return osName.indexOf("linux") >= 0;
	}
}
