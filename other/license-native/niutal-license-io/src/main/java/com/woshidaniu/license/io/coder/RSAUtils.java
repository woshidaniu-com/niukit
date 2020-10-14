package com.woshidaniu.license.io.coder;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public abstract class RSAUtils {

	/**
	 * 加密算法RSA
	 */
	protected final String KEY_ALGORITHM = "RSA";

	/**
	 * RSA最大加密明文大小
	 */
	protected final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA最大解密密文大小
	 */
	protected final int MAX_DECRYPT_BLOCK = 128;

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
		throw new NotImplementedException("not implements");
	}

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
		throw new NotImplementedException("not implements");
	}

}