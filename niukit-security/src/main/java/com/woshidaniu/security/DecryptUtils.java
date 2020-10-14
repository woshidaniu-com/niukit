package com.woshidaniu.security;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.PrivateKey;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;

import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import com.woshidaniu.security.algorithm.Algorithm;

/**
 * 
 *@类名称:DecryptUtils.java
 *@类描述：解密工具类
 *@创建人：kangzhidong
 *@创建时间：2014-9-29 下午02:40:18
 *@版本号:v1.0
 */
public class DecryptUtils {
	
	private static final int CACHE_SIZE = 1024;


	public static byte[] decrypt(Cipher cipher,byte[] cipherText, PrivateKey privateKey) throws GeneralSecurityException  {
		// 用密钥初始化此 cipher ，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		return cipher.doFinal(cipherText);
	}

	public static String decryptHex(Cipher cipher,String cipherTextHex, PrivateKey privateKey) throws Exception {
		return new String(Hex.encode(decrypt(cipher,Hex.decode(URLEncoder.encode(cipherTextHex, "UTF-8").getBytes()), privateKey)),"UTF-8");
	}

	public static String decryptBase64(Cipher cipher,String cipherTextBase64,PrivateKey privateKey) throws Exception {
		return new String(Base64.encode(decrypt(cipher,Base64.decode(cipherTextBase64.getBytes()), privateKey)),"UTF-8");
	}
	
	public static byte[] decrypt(String algorithm,SecretKey secretKey,String text) throws Exception{
		return DecryptUtils.decrypt(algorithm, secretKey, URLEncoder.encode(text, "UTF-8").getBytes());
	}
	/**
	 * 
	 * @param data 待解密数据
	 * @param key 密钥
	 * @return byte[] 解密后的数据
	 * */
	public static byte[] decrypt(String algorithm,SecretKey secretKey,byte[] data) throws GeneralSecurityException{
		//实例化
		Cipher cipher = CipherUtils.getCipher(algorithm);
		return DecryptUtils.decrypt(cipher, secretKey, data);
	}
	
	/**
	 * 
	 * @description: 解密数据
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:10:36 
	 * @param cipher	加密组件
	 * @param key  		密钥
	 * @param data		待解密数据
	 * @return byte[] 	解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(Cipher cipher ,Key secretKey,byte[] data) throws GeneralSecurityException{
		// 用密钥初始化此 cipher ，设置为解密模式
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		//执行操作
		return cipher.doFinal(data);
	}
	
	/**
	 * 
	 * @description: 解密数据
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:10:26 
	 * @param cipher	加密组件
	 * @param key  		密钥
	 * @param data		待解密数据
	 * @return byte[] 	解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static byte[] decrypt(Cipher cipher ,Key secretKey,String data) throws GeneralSecurityException{
		return DecryptUtils.decrypt(cipher, secretKey, data.getBytes());
	}
	
	/**
	 * 
	 * @description: 解密数据
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:10:14 
	 * @param cipher	加密组件
	 * @param key  		密钥
	 * @param data		待解密数据
	 * @return string 	解密后的数据
	 * @throws Exception 
	 */
	public static String decryptString(Cipher cipher ,Key secretKey,byte[] data) throws Exception{
		return new String(DecryptUtils.decrypt(cipher, secretKey, data), "utf-8");
	}
	
	/**
	 * 
	 * @description: 解密数据
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:10:14 
	 * @param cipher	加密组件
	 * @param key  		密钥
	 * @param data		待解密数据
	 * @return string 	解密后的数据
	 * @throws GeneralSecurityException
	 */
	public static String decryptString(Cipher cipher,Key secretKey,String data) throws Exception{
		return DecryptUtils.decryptString(cipher, secretKey, data.getBytes());
	}
	
	/**
	 * 
	 * @description: 解密文件
	 * @author : kangzhidong
	 * @date : 2014-9-28
	 * @time : 下午8:08:25 
	 * @param algorithm  算法名称
	 * @param encryptedFile 加密过的文件
	 * @param decryptedFile 解密后的文件
	 * @param keyStream		证书
	 * @throws GeneralSecurityException
	 */
	public static void decrypt(String algorithm,InputStream encryptedFile,OutputStream decryptedFile,InputStream keyStream) throws GeneralSecurityException {
		try {
			//还原密钥
			Key key = SecretKeyUtils.readKey(keyStream);
			//实例化
			Cipher cipher = CipherUtils.getCipher(algorithm);
			//初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, key);
			//执行操作
			decrypt(cipher, encryptedFile, decryptedFile);
			//关闭输入输出流
			encryptedFile.close();
			decryptedFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void decrypt(Cipher cipher,String encryptedFile,String edcryptedFile) throws GeneralSecurityException, IOException {
		DecryptUtils.decrypt(cipher,new FileInputStream(encryptedFile),new FileOutputStream(edcryptedFile));
	}
	
	public static void decrypt(Cipher cipher,InputStream in, OutputStream out) throws GeneralSecurityException, IOException {
		CipherOutputStream cout = new CipherOutputStream(out, cipher);
        byte[] cache = new byte[CACHE_SIZE];
        int nRead = 0;
        while ((nRead = in.read(cache)) != -1) {
            cout.write(cache, 0, nRead);
            cout.flush();
        }
        cout.close();
        out.close();
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
			
			InputStream encryptedFileIn = new FileInputStream("D:/java环境变量设置说明-encrypt.txt");
			OutputStream decryptedFile = new FileOutputStream("D:/java环境变量设置说明-decrypt.txt");
			
			//对文件进行解密
			DecryptUtils.decrypt(Algorithm.KEY_AES, encryptedFileIn, decryptedFile, new FileInputStream("D:/secret.key"));
			
		}catch (GeneralSecurityException e){
			e.printStackTrace();
		}

	}
	
	
}
