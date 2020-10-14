package com.woshidaniu.security.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.URLUtils;


/**
 * 
 *@类名称:DesBase64Utils.java
 *@类描述：基于DesBase64Codec对象的静态工具类
 *@创建人：kangzhidong
 *@创建时间：Oct 14, 2015 9:29:15 AM
 *@修改人：
 *@修改时间：
 *@版本号:v1.0
 */
public abstract class DesBase64Utils {
	
	protected static Logger LOG = LoggerFactory.getLogger(DesBase64Utils.class);
	protected static DesBase64Codec codec = new DesBase64Codec();
	
	public static String encrypt(String plainText){
		return URLUtils.escape(codec.encrypt(plainText));
	}
	
	public String encrypt(String plainText,String encryptKey){
		return URLUtils.escape(codec.encrypt(plainText.getBytes(),encryptKey));
	}
	
	public static String encrypt(byte[] plainBytes){
		return URLUtils.escape(codec.encrypt(plainBytes));
	}
	
	public static String encrypt(byte[] plainBytes,String encryptKey){
		return URLUtils.escape(codec.encrypt(plainBytes,encryptKey));
	}
	
	public static String decrypt(String encryptedText){
		return codec.decrypt(URLUtils.unescape(encryptedText).getBytes());
	}
	
	public static String decrypt(byte[] encryptedBytes){
		return codec.encrypt(URLUtils.unescape(new String(encryptedBytes)).getBytes());
	}
	
	public static String decrypt(byte[] encryptedBytes,String decryptKey){
		return codec.encrypt(URLUtils.unescape(new String(encryptedBytes)).getBytes(),decryptKey);
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args){
		
		/*<property name="jdbcUrl" value="Kbs2u6NELkMD+i6RnR+aSRYguMAm9SijEILz1FLGIT4shFz/smsRY5+Y60A4RnMl"></property> 
        <property name="user" value="QeYUs2i9xuDoHF23D7sxnA=="></property>
        <property name="password" value="QeYUs2i9xuDoHF23D7sxnA=="></property>*/
		
        
    	String s = "jdbc:oracle:thin:@10.71.16.196:1521:orcl";
		//String s = "jdbc:oracle:thin:@10.71.32.62:1521:orcl";
    	String afterE = DesBase64Utils.encrypt("niutal_javajw");
    	System.out.println("加密后：" + afterE);
    	System.out.println("解密后：" +DesBase64Utils.decrypt("zB3S5sNyx9Z5jDXaiSNcNQ=="));
    	
    	
    	//"jdbc:oracle:thin:@10.71.32.37:1521:DevDB";  / szzyjwa
		String jdbcUrlText = "jdbc:oracle:thin:@10.71.32.37:1521:test";
		String userText = "niutal_jwcj";
		String passwordText = "niutal_jwcj";
		
		String jdbcUrl = DesBase64Utils.encrypt(jdbcUrlText);
		String user = DesBase64Utils.encrypt(userText);
		String password = DesBase64Utils.encrypt(passwordText);
		
		System.out.println("jdbcUrlText加密后：" + jdbcUrl);
		System.out.println("userText加密后：" + user);
		System.out.println("passwordText加密后：" + password);
		
    	System.out.println("jdbcUrl解密后：" + DesBase64Utils.decrypt(jdbcUrl));
    	System.out.println("user解密后：" + DesBase64Utils.decrypt(user));
    	System.out.println("password解密后：" + DesBase64Utils.decrypt(password));
    	
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