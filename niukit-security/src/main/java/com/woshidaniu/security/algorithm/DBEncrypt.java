/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.security.algorithm;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.woshidaniu.security.SecretKeyUtils;
/**
 * 
 *@类名称:DBEncrypt.java
 *@类描述：此类已作废，仅作修改后的对比用
 *@创建人：kangzhidong
 *@创建时间：2014-9-29 下午05:06:00
 *@版本号:v1.0
 *@see DesBase64Codec#encrypt(String)
 */
@SuppressWarnings("restriction")
@Deprecated
public class DBEncrypt{
	public String encryptKeyText = "7EV/Zzutjzg=";
	public Properties properties;

	public Object getObject() throws Exception {
		return getProperties();
	}

	public Class getObjectType() {
		return java.util.Properties.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties inProperties) {
		this.properties = inProperties;
		String originalUsername = properties.getProperty("user");
		String originalPassword = properties.getProperty("password");
		String originalJdbcUrl = properties.getProperty("jdbcUrl");
		if (originalUsername != null) {
			String newUsername = deEncryptUsername(originalUsername);
			properties.put("user", newUsername);
		}
		if (originalPassword != null) {
			String newPassword = deEncryptPassword(originalPassword);
			properties.put("password", newPassword);
		}
//		if (originalJdbcUrl != null) {
//			String newJdbcUrl = deEncryptJdbcUrl(originalJdbcUrl);
//			properties.put("jdbcUrl", newJdbcUrl);
//		}
	}

	public String deEncryptUsername(String originalUsername) {
		return dCode(originalUsername.getBytes());
	}
	
	public String deEncryptJdbcUrl(String originalJdbcUrl) {
		return dCode(originalJdbcUrl.getBytes());
	}

	public String deEncryptPassword(String originalPassword) {
		return dCode(originalPassword.getBytes());
	}

	public String eCode(String needEncrypt){
		return eCode(needEncrypt,encryptKeyText);
	}
	
	public String eCode(String needEncrypt,String encryptKey){
		byte result[] = null;
		try {
			Cipher enCipher = Cipher.getInstance("DES");
			SecretKey key = SecretKeyUtils.getSecretKey(encryptKey);
			enCipher.init(1, key);
			result = enCipher.doFinal(needEncrypt.getBytes());
			BASE64Encoder b = new BASE64Encoder();
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			b.encode(result, bos);
			result = bos.toByteArray();
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
		return new String(result);
	}

	public String dCode(byte[] result){
		return dCode(result,encryptKeyText);
	}
	
	public String dCode(byte[] result,String encryptKey){
		String s = null;
		try {
			Cipher deCipher = Cipher.getInstance("DES");
			SecretKey key = SecretKeyUtils.getSecretKey(encryptKey) ;
			deCipher.init(2, key);
			BASE64Decoder d = new BASE64Decoder();
			result = d.decodeBuffer(new String(result));
			byte strByte[] = deCipher.doFinal(result);
			s = new String(strByte);
		} catch (Exception e) {
			throw new IllegalStateException("System doesn't support DES algorithm.");
		}
		return s;
	}

	/**
	 * 
	 *@描述：此类已作废，仅作修改后的对比用
	 *@创建人:kangzhidong
	 *@创建时间:2014-9-29下午05:05:23
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@param args
	 */
	public  static void main(String[] args){
    	String s = "jdbc:oracle:thin:@10.71.16.196:1521:orcl";
		//String s = "jdbc:oracle:thin:@10.71.32.62:1521:orcl";
    	DBEncrypt p = new DBEncrypt();
    	String afterE = p.eCode("niutal_javajw");
    	System.out.println("加密后：" + afterE);
    	System.out.println("解密后：" +p.dCode(afterE.getBytes()));
    	//System.out.println(p.dCode("Kbs2u6NELkMD+i6RnR+aSRYguMAm9SijEILz1FLGIT4shFz/smsRY5+Y60A4RnMl".getBytes()));
    }
}