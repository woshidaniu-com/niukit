package com.woshidaniu.license.io.coder;

import java.lang.reflect.Method;

import com.woshidaniu.license.io.cl._CUSTOM_CL;

public class _CIPHER {
	Object _key;

	public _CIPHER() {
		try {
			_CUSTOM_CL _cl = new _CUSTOM_CL();

			// Class.forName("com.woshidaniu.license.io.coder._CipherKey");

			Class<?> loadClass = _cl.loadClass("_CipherKeyImpl");

			this._key = loadClass.newInstance();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void _encrypt(byte[] p) {
		try {
			for (int i = 0; i < p.length; ++i) {

				Method method = _key.getClass().getMethod("getKey");

				byte[] key = (byte[]) method.invoke(_key, new Object[] {});

				p[i] ^= key[(i % 32)];
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void _decrypt(byte[] p) {
		_encrypt(p);
	}

	public byte[] _encode(byte[] data) {
		return org.apache.commons.codec.binary.Base64.encodeBase64(data);
	}

	public byte[] _decode(byte[] data) {
		return org.apache.commons.codec.binary.Base64.decodeBase64(data);
	}

}
