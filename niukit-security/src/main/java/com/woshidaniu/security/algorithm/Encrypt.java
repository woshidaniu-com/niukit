package com.woshidaniu.security.algorithm;

import java.security.MessageDigest;


/**
 * 加密用到的工具类,MD5加密,目前无法进行解密:新教务中不再使用该类，仅做对比使用
 * @author lt
 * @time 2011-3-14
 * @see MD5Codec#encrypt(String)
 */
@Deprecated
public class Encrypt {
	
	private String key;

	public Encrypt() {
		this.key = "Encrypt01";
	}
	
	@Deprecated
	public static String encrypt(String PlainStr) {
		return "{MD5}" + testHA2(PlainStr, "md5");
	}
	
	@Deprecated
	public static String testHA2(String data, String ha) {
		byte[] buffer = null;
		MessageDigest messageDigest = null;
		String s = "";
		try {
			buffer = data.getBytes("UTF-8");
			messageDigest = MessageDigest.getInstance(ha);
			messageDigest.update(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		buffer = messageDigest.digest();
		s = new sun.misc.BASE64Encoder().encode(buffer);
		return s;
	}

	public static void main(String[] args) {
		//System.out.println(e.encrypt("VZvHND"));
		System.out.println(Encrypt.encrypt("0"));
	}
	
}