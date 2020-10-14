package com.woshidaniu.rjpj.api.codec;

import java.security.MessageDigest;

public class MD5Codec {
	
	/**
	 * 加密
	 * @param salt 盐
	 * @param text 要加密的文本
	 * @return
	 */
	public static String encrypt(String salt,String text) {
		byte[] result = null;
		try {
			String saltAndText = salt + "_woshidaniu_" + text;
			byte[] arr = saltAndText.getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(arr);
			byte[] resultArray = md.digest();
			String resultStr = Hex.encodeHexString(resultArray);
			md.reset();
			return resultStr;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(result);
	}
}