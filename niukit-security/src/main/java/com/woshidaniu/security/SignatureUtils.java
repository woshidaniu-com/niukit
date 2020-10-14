package com.woshidaniu.security;

import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * 
 *@类名称:SignatureUtils.java
 *@类描述：签名工具类
 *@创建人：kangzhidong
 *@创建时间：2014-12-31 上午10:15:28
 *@版本号:v1.0
 */
public class SignatureUtils {
	
	public static Signature getSignature(String algorithm) throws GeneralSecurityException{
		return Signature.getInstance(algorithm);  
	}
	
	
	public static Signature getSignature(String algorithm, String provider) throws GeneralSecurityException{
		return Signature.getInstance(algorithm,provider);  
	}	
	
	public static byte[] sign(byte[] data,Signature signature,PrivateKey privateKey) throws GeneralSecurityException{
		signature.initSign(privateKey);
 		signature.update(data);
 		//用私钥对信息生成数字签名
 		return signature.sign();
	}
	
	public static boolean verify(byte[] data,byte[] sign,Signature signature,PublicKey publicKey) throws GeneralSecurityException{
		signature.initVerify(publicKey);
 		signature.update(data);
 		// 验证签名是否正常
 		return signature.verify(sign);
	}
	
}
