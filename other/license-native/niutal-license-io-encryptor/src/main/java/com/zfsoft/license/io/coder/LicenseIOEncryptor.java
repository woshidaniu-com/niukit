package com.woshidaniu.license.io.coder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.Provider;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 
 */

/**
 * <p>
 * <h3>niutal框架
 * <h3>
 * 说明：TODO
 * <p>
 * 
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2017年7月3日下午12:17:44
 */
public final class LicenseIOEncryptor {

	/**
	 * <p>
	 * 方法说明：TODO
	 * <p>
	 * <p>
	 * 作者：a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * 时间：2017年7月3日下午12:17:44
	 * <p>
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws Exception {
		// InputStream resourceAsStream = LicenseIOEncryptor.class
		// .getResourceAsStream("_CIPHER.java");
		// System.out.println(resourceAsStream.available());
		//
		// byte[] buf = new byte[resourceAsStream.available()];
		//
		// IOUtils.read(resourceAsStream, buf);
		//
		// byte[] encrypt = new RSAUtils().encryptByPrivateKey(buf);
		//
		// File file = new File("_CIPHER.io");
		//
		// FileUtils.writeByteArrayToFile(file, encrypt);
		//
		// System.out.println(file.getAbsolutePath());

		// File file = new File("_CIPHER.io");
		//
		// byte[] readFileToByteArray = FileUtils.readFileToByteArray(file);
		//
		// byte[] decryptByPublicKey = new
		// RSAUtils().decryptByPublicKey(readFileToByteArray);
		//
		// System.out.println(new String(decryptByPublicKey));

		InputStream resourceAsStream = LicenseIOEncryptor.class
				.getResourceAsStream("/_CipherKeyImpl.class");
		System.out.println(resourceAsStream.available());

		byte[] buf = new byte[resourceAsStream.available()];

		IOUtils.read(resourceAsStream, buf);

		byte[] encrypt = new RSAEncryptionUtils().encryptByPrivateKey(buf);

		File file = new File("_CipherKeyImpl.io");

		FileUtils.writeByteArrayToFile(file, encrypt);

		System.out.println(file.getAbsolutePath());
	}

}
