package com.woshidaniu.security.algorithm;


import sun.misc.BASE64Encoder;

import com.woshidaniu.basicutils.CharsetUtils;
import com.woshidaniu.security.digest.DigestUtils;

/**
 * 
 *@类名称:	MD5Encrypt.java
 *@类描述：	新教务密码加密方法，标准MD5使用java类库的security包的MessageDigest类处理
 *@创建人：	kangzhidong
 *@创建时间：2014-6-24 下午04:29:07
 *@版本号:v1.0
 *@see 原方法 Encrypt#encrypt(String)
 */
@SuppressWarnings({ "restriction" })
public class MD5Codec {
	
	/**
	 * JDK 自带的 Base64 加密解码算法对象
	 */
	private static BASE64Encoder b = new BASE64Encoder();
	
	public static String encrypt(String plainText) {
		byte[] buffer = null;
		try {
			buffer = plainText.getBytes("UTF-8");
		} catch (Exception e) {
			buffer = plainText.getBytes();
		}
		return "{MD5}" + encrypt(buffer);
	}
	
	public static String encryptHex(String plainText) {
		byte[] result = null;
		try {
			result = DigestUtils.getDigest(Algorithm.KEY_MD5).digest(plainText.getBytes("UTF-8"));
			return CharsetUtils.getHexString(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(result);
	}
	
	public static String encrypt(byte[] plainBytes) {
		byte[] result = null;
		try {
			result = DigestUtils.getDigest(Algorithm.KEY_MD5).digest(plainBytes);
			return b.encode(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(result);
	}
	
	/**
	 * 密码验证方法
	 */
	public static boolean verify(String plantText, String encryptedText) {
		return encrypt(plantText).equals(encryptedText);
	}

	/**
	 * 提供一个测试的主函数
	 */
	public static void main(String[] args) {
		System.out.println(MD5Codec.encrypt("123456"));
		System.out.println(MD5Codec.encryptHex("123456"));
		/*select getmd5('123456') from dual

		{MD5}4QrcOUm6Wau+VuBX8g+IPg==
*/
		System.out.println("test:" + encrypt("ddddddddddddddddddddddddddddddddddddddddd").length());
		System.out.println("123:" + encrypt("123").length());
		System.out.println("123456789:" + encrypt("123456789").length());
		System.out.println("sarin:" + encrypt("sarin").length());
	}
}