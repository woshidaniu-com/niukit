package com.woshidaniu.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.woshidaniu.security.algorithm.Algorithm;

/**
 * 
 *@类名称:EncryptUtils.java
 *@类描述：加密工具类
 *@创建人：kangzhidong
 *@创建时间：2014-12-31 上午10:16:11
 *@版本号:v1.0
 */
public class EncryptUtils {
	
	private static final int CACHE_SIZE = 1024;

	public static byte[] encrypt(String algorithm,Key key,String text) throws Exception{
		return EncryptUtils.encrypt(algorithm, key, URLEncoder.encode(text, "UTF-8").getBytes());
	}
	
	/**
	 * 加密数据
	 * @param data 待加密数据
	 * @param key 密钥
	 * @return byte[] 加密后的数据
	 * */
	public static byte[] encrypt(String algorithm,Key key,byte[] data) throws GeneralSecurityException{
		//实例化
		Cipher cipher = CipherUtils.getCipher(algorithm);
		return EncryptUtils.encrypt(cipher, key, data);
	}
	
	public static byte[] encrypt(Cipher cipher,Key key,byte[] data) throws GeneralSecurityException{
		// 用密钥初始化此 cipher ，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, key);
		//执行操作
		return cipher.doFinal(data);
	}

	public static byte[] encrypt(Cipher cipher,byte[] plainText, PublicKey publicKey) throws GeneralSecurityException {
		// 用密钥初始化此 cipher ，设置为加密模式
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(plainText);
	}
	
	public static byte[] encrypt(Cipher cipher,String plainText, PublicKey publicKey) throws GeneralSecurityException, UnsupportedEncodingException {
		return encrypt(cipher,URLEncoder.encode(plainText, "UTF-8").getBytes(), publicKey);
	}

	public static String encryptHex(Cipher cipher,byte[] plainText, PublicKey publicKey) throws GeneralSecurityException {
		return new String(Hex.encode(encrypt(cipher,plainText, publicKey)));
	}

	public static String encryptHex(Cipher cipher,String plainText, PublicKey publicKey) throws GeneralSecurityException, UnsupportedEncodingException {
		return new String(Hex.encode(encrypt(cipher,plainText, publicKey)));
	}

	public static String encryptBase64(Cipher cipher,byte[] plainText, PublicKey publicKey) throws GeneralSecurityException {
		return new String(Base64.encode(encrypt(cipher,plainText, publicKey)));
	}

	public static String encryptBase64(Cipher cipher,String plainText, PublicKey publicKey) throws GeneralSecurityException, UnsupportedEncodingException {
		return new String(Base64.encode(encrypt(cipher,plainText, publicKey)));
	}
	
	/**
	 * 
	 * @description: 加密文件
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:08:03 
	 * @param algorithm		 算法名称
	 * @param plaintextFile	 未加密过的文件
	 * @param encryptedFile  加密过的文件
	 * @param keyStream      证书
	 * @throws GeneralSecurityException
	 */
	public static void encrypt(String algorithm,InputStream plaintextFile,OutputStream encryptedFile,InputStream keyStream) throws GeneralSecurityException {
		try {
			//还原密钥
			Key secretKey = SecretKeyUtils.readKey(keyStream);
			//实例化
			Cipher cipher = CipherUtils.getCipher(algorithm);
			//初始化，设置为加密模式
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//执行操作
			EncryptUtils.encrypt(cipher , plaintextFile, encryptedFile);
			//关闭输入输出流
			plaintextFile.close();
			encryptedFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void encrypt(Cipher cipher,String plaintextFile,String encryptedFile) throws GeneralSecurityException, IOException {
		EncryptUtils.encrypt(cipher,new FileInputStream(plaintextFile),new FileOutputStream(encryptedFile));
	}
	
	public static void encrypt(Cipher cipher,InputStream in, OutputStream out) throws GeneralSecurityException, IOException {
        CipherInputStream cin = new CipherInputStream(in, cipher);  
        byte[] cache = new byte[CACHE_SIZE];
        int i;  
        while ((i = cin.read(cache)) != -1) {  
            out.write(cache, 0, i);  
        }  
        out.close();  
        cin.close();  
        in.close();
		/*int blockSize = cipher.getBlockSize();
		int outputSize = cipher.getOutputSize(blockSize);
		byte[] inBytes = new byte[blockSize];
		byte[] outBytes = new byte[outputSize];
		int inLength = 0;
		boolean more = true;
		while (more) {
			inLength = in.read(inBytes);
			if (inLength == blockSize){
				int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
				out.write(outBytes, 0, outLength);
			}else{
				more = false;
			}
		}
		if (inLength > 0){
			outBytes = cipher.doFinal(inBytes, 0, inLength);
		}else{
			outBytes = cipher.doFinal();
		}
		out.write(outBytes);*/
	}

	public static void main(String[] args) throws Exception {
		
		/*java AESTest -genkey secret.key
		java AESTest -encrypt plaintextFile encryptedFile secret.key
		java AESTest -decrypt encryptedFile decryptedFile secret.key*/
		
		try {
			
			InputStream plaintextFile = new FileInputStream("D:/java环境变量设置说明.txt");
			OutputStream encryptedFileOut = new FileOutputStream("D:/java环境变量设置说明-encrypt.txt");
			
			//对文件进行加密
			EncryptUtils.encrypt(Algorithm.KEY_AES, plaintextFile, encryptedFileOut, new FileInputStream("D:/secret.key"));
			
		}catch (GeneralSecurityException e){
			e.printStackTrace();
		}

	}
	
}
