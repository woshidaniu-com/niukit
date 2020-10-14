package com.woshidaniu.license.io.cl;

import java.io.InputStream;

import com.woshidaniu.license.io.coder.RSADecryptionUtils;

/**
 * 这个类必须深度混淆
 * <p>
 * <h3>niutal框架
 * <h3>
 * 说明：TODO
 * <p>
 * 
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2017年6月30日下午1:59:57
 */
public class _CUSTOM_CL extends ClassLoader {

	private static _CUSTOM_CL __CUSTOM_CL = new _CUSTOM_CL();

	private static final String _CL_NAME = "_CipherKeyImpl";
	
	public static _CUSTOM_CL getInstance() {
		return __CUSTOM_CL;
	}

	public _CUSTOM_CL() {
		super();
	}

	@Override
	protected Class findClass(String name) throws ClassNotFoundException {
		Class clazz = null;
		try {
			//InputStream cipherKeyImplIO = new FileInputStream("_CipherKeyImpl");
			
			if(_CL_NAME.equals(name)){
				InputStream cipherKeyImplIO = this.getClass().getClassLoader().getResourceAsStream("_CipherKeyImpl");
				
				byte[] classBytes = new byte[cipherKeyImplIO.available()];

				org.apache.commons.io.IOUtils.read(cipherKeyImplIO, classBytes);

				byte[] decrypt = new RSADecryptionUtils().decryptByPublicKey(classBytes);

				clazz = defineClass(name, decrypt, 0, decrypt.length);
			}else{
				//super.loadClass(name);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (clazz == null) {
			throw new ClassNotFoundException(name);
		}

		return clazz;
	}

}
