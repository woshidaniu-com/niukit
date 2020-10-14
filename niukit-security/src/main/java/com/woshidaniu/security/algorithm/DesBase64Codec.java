package com.woshidaniu.security.algorithm;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import com.woshidaniu.security.CipherUtils;
import com.woshidaniu.security.SecretKeyUtils;
/**
 * 
 *@类名称: DesBase64Codec.java
 *@类描述：新教务数据库地址等加密工具
 *@创建人：kangzhidong
 *@创建时间：Jul 2, 2015 11:42:42 AM
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 *@see DBEncrypt#eCode
 */
public class DesBase64Codec {

	private String encryptKeyText = "7EV/Zzutjzg=";

	@SuppressWarnings("unused")
	private String decryptJdbcUrl(String originalJdbcUrl) {
		return encrypt(originalJdbcUrl.getBytes());
	}

	public String encrypt(String plainText){
		return encrypt(plainText,encryptKeyText);
	}
	
	public String encrypt(String plainText,String encryptKey){
		return encrypt(plainText.getBytes(),encryptKey);
	}
	
	public String encrypt(byte[] plainBytes){
		return encrypt(plainBytes,encryptKeyText);
	}
	
	public String encrypt(byte[] plainBytes,String encryptKey){
		byte result[] = null;
		try {
			//获得加密密钥
			SecretKey secretKey = SecretKeyUtils.getSecretKey(encryptKey);
			//根据秘钥和算法获取加密执行对象
			Cipher enCipher = CipherUtils.getEncryptCipher(Algorithm.KEY_DES, secretKey);
			//执行加密操作
			result = enCipher.doFinal(plainBytes);
			//实例化一个ByteArrayOutputStream
			//ByteArrayOutputStream bos = new ByteArrayOutputStream();
			//使用base64加密算法对DES摘要算法结果进行加密
			result = Base64.encodeBase64(result);
			//b.encode(result, bos);
			//得到加密后的字节数组
			//result = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			throw new IllegalStateException(e.getCause());
		}
		return new String(result);
	}

	public String decrypt(String encryptedText){
		return decrypt(encryptedText.getBytes(),encryptKeyText);
	}
	
	public String decrypt(byte[] encryptedBytes){
		return decrypt(encryptedBytes,encryptKeyText);
	}
	
	public String decrypt(byte[] encryptedBytes,String decryptKey){
		String s = null;
		try {
			//获得加密密钥
			SecretKey secretKey = SecretKeyUtils.getSecretKey(decryptKey);
			// 根据秘钥和算法获取解密执行对象；用密钥初始化此 cipher ，设置为解密模式  ;
			Cipher deCipher = CipherUtils.getDecryptCipher(Algorithm.KEY_DES, secretKey);
			//使用base64加密算法对DES摘要算法结果进行解密
			encryptedBytes = Base64.decodeBase64(new String(encryptedBytes));//new BASE64Decoder().decodeBuffer(new String(encryptedBytes));
			//执行解密操作;得到解密后的字节数组
			byte strByte[] = deCipher.doFinal(encryptedBytes);
			//转换解密后的数组为字符
			s = new String(strByte);
		} catch (Exception e) {
			throw new IllegalStateException(e.getCause());
		}
		return s;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args){
		
		/*<property name="jdbcUrl" value="Kbs2u6NELkMD+i6RnR+aSRYguMAm9SijEILz1FLGIT4shFz/smsRY5+Y60A4RnMl"></property> 
        <property name="user" value="QeYUs2i9xuDoHF23D7sxnA=="></property>
        <property name="password" value="QeYUs2i9xuDoHF23D7sxnA=="></property>*/
		
		System.out.println(Base64.encodeBase64String("10353".getBytes()));
		System.out.println(new String(Base64.decodeBase64("7EV/Zzutjzg=".getBytes())));
		
		
		
    	String s = "jdbc:oracle:thin:@10.71.16.196:1521:orcl";
		//String s = "jdbc:oracle:thin:@10.71.32.62:1521:orcl";
    	DesBase64Codec codec = new DesBase64Codec();
    	String afterE = codec.encrypt("niutal_javajw");
    	System.out.println("加密后：" + afterE);
    	System.out.println("解密后：" +codec.decrypt("zB3S5sNyx9Z5jDXaiSNcNQ=="));
    	
    	
    	//"jdbc:oracle:thin:@10.71.32.37:1521:DevDB";  / szzyjwa
		String jdbcUrlText = "jdbc:oracle:thin:@10.71.32.37:1521:test";
		String userText = "niutal_jwcj";
		String passwordText = "niutal_jwcj";
		
		String jdbcUrl = codec.encrypt(jdbcUrlText);
		String user = codec.encrypt(userText);
		String password = codec.encrypt(passwordText);
		
		System.out.println("jdbcUrlText加密后：" + jdbcUrl);
		System.out.println("userText加密后：" + user);
		System.out.println("passwordText加密后：" + password);
		
    	System.out.println("jdbcUrl解密后：" + codec.decrypt(jdbcUrl));
    	System.out.println("user解密后：" + codec.decrypt(user));
    	System.out.println("password解密后：" + codec.decrypt(password));
    	
    	/*
    	
    	服务器：10.71.32.37  SID:test    用户名：niutal_jwcj   密码：niutal_jwcj


    	原方法： 
    	
    	 加密后：QeYUs2i9xuDoHF23D7sxnA==
		解密后：niutal_javajw
		
		现方法：
		
		加密后：QeYUs2i9xuDoHF23D7sxnA==
		解密后：niutal_javajw

		*/

    		
    	//System.out.println(p.dCode("Kbs2u6NELkMD+i6RnR+aSRYguMAm9SijEILz1FLGIT4shFz/smsRY5+Y60A4RnMl".getBytes()));
    }
}