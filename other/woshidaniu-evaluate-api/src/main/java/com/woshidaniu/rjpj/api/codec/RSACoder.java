package com.woshidaniu.rjpj.api.codec;


import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA签名和验证
 * @author 1571
 */
public class RSACoder {

	private static final String KEY_ALGORITHM = "RSA";
	
	private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	
	public static final String PUBLIC_KEY = "RSAPublicKey";
	
	public static final String PRIVATE_KEY = "RSAPrivateKey";
	
	private static final int KEY_SIZE = 512;
	
	/**
	 * 得到签名
	 * @param data 被签名数据
	 * @param privateKey 私钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] sign(byte[] data,byte[] privateKey)throws Exception{
		
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
		
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		
		PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
		
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		
		signature.initSign(priKey);
		
		signature.update(data);
		
		return signature.sign();
	}
	
	/**
	 * 验证数据和签名是否有效 
	 * @param data 被签名数据
	 * @param publicKey 公钥
	 * @param sign 签名数据
	 * @return
	 * @throws Exception
	 */
	public static boolean verify(byte[] data,byte[] publicKey,byte[] sign) throws Exception{
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKey);
		
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		
		PublicKey pubKey = keyFactory.generatePublic(keySpec);
		
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(pubKey);
		signature.update(data);
		return signature.verify(sign);
	}
	
	public static byte[] getPrivateKey(Map<String,Object> map){
		Key key = (Key) map.get(RSACoder.PRIVATE_KEY);
		return key.getEncoded();
	}
	
	public static byte[] getPublicKey(Map<String,Object> map){
		Key key = (Key) map.get(RSACoder.PUBLIC_KEY);
		return key.getEncoded();
	}
	
	public static Map<String,Key> initKey(){
		KeyPairGenerator keyPairGenerator;
		try {
			keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage());
		}
		keyPairGenerator.initialize(KEY_SIZE);
		
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		
		Map<String,Key> keyMap = new HashMap<String,Key>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		
		return keyMap;
	}
}
