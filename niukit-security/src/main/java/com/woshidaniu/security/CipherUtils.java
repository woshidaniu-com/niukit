package com.woshidaniu.security;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.DESKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.woshidaniu.security.algorithm.Algorithm;


/**
 * 
 *@类名称:CipherUtils.java
 *@类描述：Cipher 工具类
 *@创建人：kangzhidong
 *@创建时间：2014-12-31 上午10:16:54
 *@版本号:v1.0
 */
public final class CipherUtils {
	
	static {
		// 加入bouncyCastle支持
		Security.addProvider(new BouncyCastleProvider());
	}
	
	/**
	 * 
	 * @description: 生成一个实现RSA转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * @author : kangzhidong
	 * @date : 2014-9-29
	 * @time : 上午9:08:24 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getRSACipher() throws GeneralSecurityException {
		return Cipher.getInstance(Algorithm.KEY_CIPHER_RSA);
	}
	
	/**
	 * 
	 * @description: 生成一个实现AES转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * @author : kangzhidong
	 * @date : 2014-9-29
	 * @time : 上午9:08:24 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getAESCipher() throws GeneralSecurityException {
		//生成一个实现AES转换的 Cipher 对象
		return Cipher.getInstance(Algorithm.KEY_CIPHER_AES);
	}
	
	/**
	 * 
	 * @description: 生成一个实现DES转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * @author : kangzhidong
	 * @date : 2014-9-29
	 * @time : 上午9:08:24 
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getDESCipher() throws GeneralSecurityException {
		//生成一个实现DES转换的 Cipher 对象
		return Cipher.getInstance(Algorithm.KEY_CIPHER_DES);
	}
	
	/**
	 * 
	 * @description:  加密解密第2步：生成一个实现指定转换的 Cipher 对象。Cipher对象实际完成加解密操作
	 * @author : kangzhidong
	 * @date : 2014-9-29
	 * @time : 上午9:08:14 
	 * @param algorithm
	 * @return
	 * @throws GeneralSecurityException
	 */
	public static Cipher getCipher(String algorithm) throws GeneralSecurityException {
		//生成一个实现转换的 Cipher 对象
		return Cipher.getInstance(algorithm);
	}
	
	public static Cipher getEncryptCipher(String algorithm,Key secretKey) throws GeneralSecurityException {
		// 返回 cipher
		return  CipherUtils.getEncryptCipher(algorithm,secretKey,null);
	}
	
	public static Cipher getEncryptCipher(String algorithm,Key secretKey,SecureRandom random) throws GeneralSecurityException {
		//生成一个实现转换的 Cipher 对象
		Cipher cipher = CipherUtils.getCipher(algorithm);
		// 用密钥初始化此 cipher ，设置为解密模式  
		if(null == random){
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);  
		}else{
			cipher.init(Cipher.ENCRYPT_MODE, secretKey , random);  
		}
		// 返回 cipher
		return cipher;
	}
	
	public static Cipher getDecryptCipher(String algorithm,Key secretKey) throws GeneralSecurityException {
		// 返回 cipher
		return  CipherUtils.getDecryptCipher(algorithm,secretKey,null);
	}
	
	public static Cipher getDecryptCipher(String algorithm,Key secretKey,SecureRandom random) throws GeneralSecurityException {
		//生成一个实现RSA转换的 Cipher 对象
		Cipher cipher = CipherUtils.getCipher(algorithm);
		// 用密钥初始化此 cipher ，设置为解密模式  
		if(null == random){
			cipher.init(Cipher.DECRYPT_MODE, secretKey);  
		}else{
			cipher.init(Cipher.DECRYPT_MODE, secretKey , random);  
		}
		// 返回 cipher
		return cipher;
	}
	
	/**
	 * 
	 * @description: 数据分段加密/解密
	 * @author : kangzhidong
	 * @date 下午5:07:53 2014-10-9 
	 * @param cipher
	 * @param bytes
	 * @param block
	 * @return
	 * @throws GeneralSecurityException
	 * @return  byte[] 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static byte[] segment(Cipher cipher,byte[] bytes,int block) throws GeneralSecurityException{
		//分段加密
 	 	int inputLen = bytes.length;  
        ByteArrayOutputStream out = new ByteArrayOutputStream();  
        int offSet = 0;  
        byte[] cache;  
        int i = 0;  
        // 对数据分段加密  
        while (inputLen - offSet > 0) {  
            if (inputLen - offSet > block) {  
                cache = cipher.doFinal(bytes, offSet, block);  
            } else {  
                cache = cipher.doFinal(bytes, offSet, inputLen - offSet);  
            }  
            out.write(cache, 0, cache.length);  
            i++;  
            offSet = i * block;  
        }  
        byte[] binaryData = out.toByteArray();  
        try {
			out.close();
		} catch (IOException e) {
			return null;
		} 
        return binaryData;
	}

	

	public static void main(String[] args) throws Exception {
		
		try {
			
			SecretKeyUtils.genSecretKey(new DESKeySpec("11111111".getBytes()),Algorithm.KEY_CIPHER_AES);
				
			 
			byte[] key = SecretKeyUtils.genBinarySecretKey(Algorithm.KEY_AES, 56);
			
			CipherUtils.getDecryptCipher(Algorithm.KEY_CIPHER_AES, SecretKeyUtils.genSecretKey(key,Algorithm.KEY_AES));
			
			
		}catch (GeneralSecurityException e){
			e.printStackTrace();
		}

	}

}
