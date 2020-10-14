package com.woshidaniu.security.algorithm;


import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.woshidaniu.basicutils.CharsetUtils;
import com.woshidaniu.security.HmacUtils;

/**
 * 
 *@类名称:HmacMD5Codec.java
 *@类描述：HmacMD5 单向加密
 *@创建人：kangzhidong
 *@创建时间：2014-10-8 下午07:05:07
 *@版本号:v1.0
 */
public class HmacMD5Codec {
	
	//key采用公司统一的加密解密算法，方便使用小工具生成加密字符串
	private static DesBase64Codec codec = new DesBase64Codec();
	
	public static String toString(String plainText,byte[] base64Key) {
		return "{HMACMD5}" + encrypt(plainText,base64Key);
	}
	
	public static String encrypt(String plainText,byte[] base64Key) {
		return encrypt(plainText.getBytes(),base64Key);
	}
	
	public static String encrypt(byte[] plainBytes,byte[] base64Key) {
		try {
			//执行消息摘要处理
			return CharsetUtils.getUTF8String(Hex.encode(HmacUtils.hmacMD5(plainBytes, base64Key))).toUpperCase();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 密码验证方法
	 */
	public static boolean verify(String plantText, String encryptedText,byte[] base64Key) {
		return encrypt(plantText,base64Key).equals(encryptedText);
	}
	
	public static String getEncryptKey(String plainText){
		return codec.encrypt(plainText);
	}

	public static String getDecryptKey(String encryptedText){
		return codec.decrypt(encryptedText);
	}
	
	/**
	 * 提供一个测试的主函数
	 */
	public static void main(String[] args) {
		
		System.out.println("学校随机秘钥："+getEncryptKey("201510511"));
		
		//成绩加密密钥：学号、课程号、成绩、学校随机密钥（存二维表实例下的表）
		String keyText = "3000611031-72120170-不及格-"+ getDecryptKey(getEncryptKey("201510511")) ;
		System.out.println("keyText:"+keyText);
		byte[] key = Base64.encode(keyText.getBytes());
		
		System.out.println("sssssssssssssssss:" );
		System.out.println("test:" + encrypt("不及格",key));
		System.out.println("123:" + encrypt("123",key));
		System.out.println("123456789:" + encrypt("123456789",key));
		System.out.println("sarin:" + encrypt("sarin",key).length());
	}
}