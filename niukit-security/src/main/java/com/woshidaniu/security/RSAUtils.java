package com.woshidaniu.security;



import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：Utils - RSA加密解密
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月17日上午10:09:49
 */
public final class RSAUtils {

	/** 安全服务提供者 */
	private static final Provider PROVIDER = new BouncyCastleProvider();

	/** 密钥大小 */
	private static final int KEY_SIZE = 1024;
	
	private static final String KEY_ALGORITHM = "RSA";
	private static final String SIGNATURE_ALGORITHM   = "SHA1withRSA";

	
	private RSAUtils() {
		super();
	}

	/**
	 * 生成密钥对
	 * @return 密钥对
	 */
	public static KeyPair generateKeyPair() {
		try {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", PROVIDER);
			keyPairGenerator.initialize(KEY_SIZE, new SecureRandom());
			return keyPairGenerator.generateKeyPair();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param data
	 *            数据
	 * @return 加密后的数据
	 */
	public static byte[] encrypt(PublicKey publicKey, byte[] data) {
		if(publicKey == null || data == null){
			throw new RuntimeException("参数错误");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 加密
	 * 
	 * @param publicKey
	 *            公钥
	 * @param text
	 *            字符串
	 * 
	 * @return Base64编码字符串
	 */
	public static String encrypt(PublicKey publicKey, String text) {
		if(publicKey == null || text == null){
			throw new RuntimeException("参数错误");
		}
		byte[] data = encrypt(publicKey, text.getBytes());
		return data != null ? new String(Base64.encodeBase64(data)) : null;
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param data
	 *            数据
	 * @return 解密后的数据
	 */
	public static byte[] decrypt(PrivateKey privateKey, byte[] data) {
		if(privateKey == null || data == null){
			throw new RuntimeException("参数错误");
		}
		try {
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", PROVIDER);
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 解密
	 * 
	 * @param privateKey
	 *            私钥
	 * @param text
	 *            Base64编码字符串
	 * @return 解密后的数据
	 */
	public static String decrypt(PrivateKey privateKey, String text) {
		if(privateKey == null || text == null){
			throw new RuntimeException("参数错误");
		}
		byte[] data = decrypt(privateKey, Base64.decodeBase64(text));
		return data != null ? new String(data) : null;
	}

	
	/**
	 * 
	 * <p>方法说明：生成公钥<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月17日上午10:11:58<p>
	 * @param key String
	 * @return PublicKey
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	public static PublicKey getPublicKey(String key) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException{  
          
		// 解密由base64编码的公钥     
        byte[] keyBytes = Base64.decodeBase64(key.getBytes());     
        // 构造X509EncodedKeySpec对象     
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);     
        // KEY_ALGORITHM 指定的加密算法     
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);     
        // 取公钥匙对象     
        PublicKey pubKey = keyFactory.generatePublic(keySpec);  
          
        return pubKey;  
    }
	
	
	/**
	 * 
	 * <p>方法说明：验证<p>
	 * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
	 * <p>时间：2016年6月17日上午10:12:43<p>
	 * @param data 数据
	 * @param publicKey 公钥
	 * @param sign 签名
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean verify(byte[] data, String publicKey, String sign)     
            throws Exception {     
        // 解密由base64编码的公钥     
        byte[] keyBytes = Base64.decodeBase64(publicKey.getBytes());     
        // 构造X509EncodedKeySpec对象     
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);     
        // KEY_ALGORITHM 指定的加密算法     
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);     
        // 取公钥匙对象     
        PublicKey pubKey = keyFactory.generatePublic(keySpec);     
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);     
        signature.initVerify(pubKey);     
        signature.update(data);     
        // 验证签名是否正常     
        return signature.verify(Base64.decodeBase64(sign.getBytes()));     
    }  
	
	
}