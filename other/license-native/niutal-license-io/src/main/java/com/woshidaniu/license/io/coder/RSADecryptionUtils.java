package com.woshidaniu.license.io.coder;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class RSADecryptionUtils extends RSAUtils {

	private static final String PUBLIC_KEY_VALUE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCMm31rpxflq7DmZbRnB8ftD/x6uT58vP7iH8pE"
			+ "NMWlRc41QzhqK2r7el7/l6qUXL3bB0Gk5kzbV37vJoboxYgxqzWFpCQOX5VTcg2Uc6dmrmV5uYz5"
			+ "deVMFSRs7RRVdknSXgfmIX8suApYCI0iQY7hzwzP18LdLi7FIFbMM+uXCQIDAQAB";

	/**
	 * <p>
	 * 公钥解密
	 * </p>
	 * 
	 * @param encryptedData
	 *            已加密数据
	 * @param publicKey
	 *            公钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptByPublicKey(byte[] encryptedData) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(PUBLIC_KEY_VALUE);
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicK = keyFactory.generatePublic(x509KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.DECRYPT_MODE, publicK);
		int inputLen = encryptedData.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段解密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
				cache = cipher
						.doFinal(encryptedData, offSet, MAX_DECRYPT_BLOCK);
			} else {
				cache = cipher
						.doFinal(encryptedData, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_DECRYPT_BLOCK;
		}
		byte[] decryptedData = out.toByteArray();
		out.close();
		return decryptedData;
	}

}