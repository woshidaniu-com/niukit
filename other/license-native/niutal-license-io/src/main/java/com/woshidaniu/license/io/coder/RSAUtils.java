package com.woshidaniu.license.io.coder;

import org.apache.commons.lang.NotImplementedException;

/**
 * @author IceWee
 * @date 2012-4-26
 * @version 1.0
 */
public abstract class RSAUtils {

	/**
	 * �����㷨RSA
	 */
	protected final String KEY_ALGORITHM = "RSA";

	/**
	 * RSA���������Ĵ�С
	 */
	protected final int MAX_ENCRYPT_BLOCK = 117;

	/**
	 * RSA���������Ĵ�С
	 */
	protected final int MAX_DECRYPT_BLOCK = 128;

	/**
	 * <p>
	 * ��Կ����
	 * </p>
	 * 
	 * @param encryptedData
	 *            �Ѽ�������
	 * @param publicKey
	 *            ��Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public byte[] decryptByPublicKey(byte[] encryptedData) throws Exception {
		throw new NotImplementedException("not implements");
	}

	/**
	 * <p>
	 * ˽Կ����
	 * </p>
	 * 
	 * @param data
	 *            Դ����
	 * @param privateKey
	 *            ˽Կ(BASE64����)
	 * @return
	 * @throws Exception
	 */
	public byte[] encryptByPrivateKey(byte[] data) throws Exception {
		throw new NotImplementedException("not implements");
	}

}