package com.woshidaniu.license.io.coder;

import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

/**
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public class RSAEncryptionUtils extends RSAUtils {

	private static final String PRIVATE_KEY_VALUE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIybfWunF+WrsOZltGcHx+0P/Hq5"
			+ "Pny8/uIfykQ0xaVFzjVDOGoravt6Xv+XqpRcvdsHQaTmTNtXfu8mhujFiDGrNYWkJA5flVNyDZRz"
			+ "p2auZXm5jPl15UwVJGztFFV2SdJeB+Yhfyy4ClgIjSJBjuHPDM/Xwt0uLsUgVswz65cJAgMBAAEC"
			+ "gYA3dUjFwjUZwFtYtHqwPJBtf2WtHg14c67wj2te9ZWTN3I67dV9gryCjeSUWUTNmbrEfhs1KCTe"
			+ "SqdlOkPx4Z9yqZDQiMQzhj8qqgsNFXrRm7frAg91ESfKu3ZGyfGkHMDN07Xgp3x2MmjMFVkO8yLi"
			+ "kPVPCVvRbi4x03pQT3YR9QJBAM4SPmQmMvA8AEIG5mk/rD3Ht7oTOWcHcJJZxfKde/CrlN4DOij3"
			+ "EOfNzLaadWneHmQU+J52WPNjlgFbwBOYPQcCQQCurMowyD7nKqZbRzsQTICRQ3BkVHi3QplFy3jG"
			+ "i4IZLHsdmTab1QiaAQm/uo7aMBaAVOLBRxnu/qS6+NS/RpdvAkBOvaXdesMoDKVElYJhYRUdnjzT"
			+ "+xiKB8u6AFek9wwu0EFX+/+zO4TYLkk01RxcUaVnNKQzWKFwjvL4nehtFFTtAkEAkMwNw8iLVGen"
			+ "ha0TihxQy5pC9fiMJSZhFlloYfXNv/5+hHe4rA851SgdI8GLn9UTIgi7/AprthGDDrcs7O6gowJB"
			+ "AJT+g3/IfXKg/Q9OqcuBk0cGFJXR2eHomX4FjejjUezSlzrc3aSJixKNIDHu0Er0qUA8OjEdXV3E"
			+ "rAcXJEO12dI=";

	/**
	 * 加密算法RSA
	 */
	public static final String KEY_ALGORITHM = "RSA";

	/**
	 * RSA最大加密明文大小
	 */
	private static final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * <p>
	 * 私钥加密
	 * </p>
	 * 
	 * @param data
	 *            源数据
	 * @param privateKey
	 *            私钥(BASE64编码)
	 * @return
	 * @throws Exception
	 */
	public byte[] encryptByPrivateKey(byte[] data) throws Exception {
		byte[] keyBytes = Base64.decodeBase64(PRIVATE_KEY_VALUE);
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateK = keyFactory.generatePrivate(pkcs8KeySpec);
		Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
		cipher.init(Cipher.ENCRYPT_MODE, privateK);
		int inputLen = data.length;
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int offSet = 0;
		byte[] cache;
		int i = 0;
		// 对数据分段加密
		while (inputLen - offSet > 0) {
			if (inputLen - offSet > MAX_ENCRYPT_BLOCK) {
				cache = cipher.doFinal(data, offSet, MAX_ENCRYPT_BLOCK);
			} else {
				cache = cipher.doFinal(data, offSet, inputLen - offSet);
			}
			out.write(cache, 0, cache.length);
			i++;
			offSet = i * MAX_ENCRYPT_BLOCK;
		}
		byte[] encryptedData = out.toByteArray();
		out.close();
		return encryptedData;
	}

}