/**
 * 
 */
package com.woshidaniu.license.io.p;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.woshidaniu.license.io.coder._CIPHER;
import com.woshidaniu.license.io.ex.LicenseFileNotFoundException;
import com.woshidaniu.license.io.ex.LicenseIOException;

public class LicenseIO {

	static final String LICENSE_FILE_NAME = "license.auth";

	static final String P_DEV_MODE = "_DEV_MODE";
	static final String P_AUTH_ID = "_AUTH_ID";
	static final String P_AUTH_USER = "AUTH_USER";
	static final String P_AUTH_USER_CODE = "AUTH_USER_CODE";
	static final String P_START_DATE = "START_DATE";
	static final String P_EXPIRE_DATE = "EXPIRE_DATE";
	static final String P_AUTH_DATE = "_AUTH_DATE";
	static final String P_USAGE = "USAGE";
	static final String P_USAGE_COUNT = "USAGE_COUNT";
	static final String P_ENCRYPT_SHA = "_ENCRYPT_SHA";
	static final String P_LAST_ACCESS = "_LAST_ACCESS";
	static final String P_PRODUCT_NAME = "_PRODUCT_NAME";
	static final String P_ALERT = "_ALERT";
	static final String P_INIT = "INIT";

	static final _CIPHER _CIPHER = new _CIPHER();
	
	public static _LICENSE getLICENSE() throws LicenseIOException {
		return new _LICENSE(read(_getLicenceFile()));
	}

	private static File _getLicenceFile() throws LicenseFileNotFoundException {
		File file = FileUtils.toFile(_getLicenceURL());
		if (file == null || (!file.exists()))
			new LicenseFileNotFoundException("license file not found!");
		return file;
	}

	private static URL _getLicenceURL() {
		return LicenseIO.class.getClassLoader().getResource(LICENSE_FILE_NAME);
	}

	public static byte[] read(File license) throws LicenseIOException {
		try {
			byte[] data = null;
			data = FileUtils.readFileToByteArray(license);
			data = _CIPHER._decode(data);
			_CIPHER._decrypt(data);
			return _CIPHER._decode(data);
		} catch (IOException e) {
			throw new LicenseIOException(e);
		}
	}

	public static byte[] read(URL license) throws LicenseIOException {
		try {
			byte[] data = null;
			data = IOUtils.toByteArray(license);
			data = _CIPHER._decode(data);
			_CIPHER._decrypt(data);
			return _CIPHER._decode(data);
		} catch (IOException e) {
			throw new LicenseIOException(e);
		}
	}

	public static void write(byte[] data, File license)
			throws LicenseIOException {
		try {
			data = _CIPHER._encode(data);
			_CIPHER._encrypt(data);
			data = _CIPHER._encode(data);
			FileUtils.writeByteArrayToFile(license, data);
		} catch (IOException e) {
			throw new LicenseIOException(e);
		}
	}

	public static class _LICENSE {
		private JSONObject j_node = null;

		public _LICENSE(byte[] j_data) throws LicenseIOException {
			try {
				this.j_node = JSONObject.fromObject(new String(j_data));
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		private String _getText(String _k) throws LicenseIOException {
			try {
				return j_node.getString(_k);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		private int _getInt(String _k) throws LicenseIOException {
			try {
				return j_node.getInt(_k);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		// /////////////////////////////////////////////////////
		public String getDevMode() throws LicenseIOException {
			try {
				return _getText(P_DEV_MODE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getAuthId() throws LicenseIOException {
			try {
				return _getText(P_AUTH_ID);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getAuthUser() throws LicenseIOException {
			try {
				return _getText(P_AUTH_USER);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getAuthUserCode() throws LicenseIOException {
			try {
				return _getText(P_AUTH_USER_CODE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getStartDate() throws LicenseIOException {
			try {
				return _getText(P_START_DATE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getExpireDate() throws LicenseIOException {
			try {
				return _getText(P_EXPIRE_DATE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getAuthDate() throws LicenseIOException {
			try {
				return _getText(P_AUTH_DATE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public Integer getUsage() throws LicenseIOException {
			try {
				return _getInt(P_USAGE);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public Integer getUsageCount() throws LicenseIOException {
			try {
				return _getInt(P_USAGE_COUNT);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public Integer getAlert() throws LicenseIOException {
			try {
				return _getInt(P_ALERT);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public Integer getInit() throws LicenseIOException {
			try {
				return _getInt(P_INIT);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getEncryptSHA() throws LicenseIOException {
			try {
				return _getText(P_ENCRYPT_SHA);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String getProductName() throws LicenseIOException {
			try {
				return _getText(P_PRODUCT_NAME);
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public String increseUsageCount() throws LicenseIOException {
			if (j_node == null) {
				throw new LicenseIOException();
			}

			String _AUTH_ID = getAuthId();
			String _AUTH_USER = getAuthUser();
			String _AUTH_USER_CODE = getAuthUserCode();
			int _USAGE = getUsage();
			int _USAGE_COUNT = getUsageCount();
			int _INIT = getInit();
			if (_INIT == 1) {
				j_node.put(P_INIT, 0);
			}
			if (_USAGE > _USAGE_COUNT) {
				_USAGE_COUNT++;
				j_node.put(P_USAGE_COUNT, _USAGE_COUNT);
			}
			Long currentTimeMillis = System.currentTimeMillis();
			StringBuilder sb = new StringBuilder();
			sb.append(_AUTH_ID).append(_AUTH_USER).append(_AUTH_USER_CODE)
					.append(_USAGE).append(_USAGE_COUNT)
					.append(currentTimeMillis);

			byte[] _encode = _CIPHER._encode(sb.toString().getBytes());
			_CIPHER._encrypt(_encode);
			_encode = _CIPHER._encode(_encode);

			j_node.put(P_ENCRYPT_SHA, new String(_encode));
			j_node.put(P_LAST_ACCESS, currentTimeMillis.toString());

			try {
				String writeValueAsString = j_node.toString();
				write(writeValueAsString.getBytes(), _getLicenceFile());
				return writeValueAsString;
			} catch (Exception e) {
				e.printStackTrace();
				throw new LicenseIOException(e);
			}

		}

		public Integer checkStatus(String SHA, String CURRENTDATE)
				throws LicenseIOException {
			try {
				int code = -1;
				String _ENCRYPT_SHA = getEncryptSHA();
				String _START_DATE = getStartDate();
				String _EXPIRE_DATE = getExpireDate();
				int USAGE = getUsage();
				int USAGE_COUNT = getUsageCount();
				int _ALERT = getAlert();
				int _INIT = getInit();
				if (_INIT == 1) {
					code = 0;
				} else if (!StringUtils.equals(SHA, _ENCRYPT_SHA)) {
					code = 4;
				} else if ((CURRENTDATE.compareTo(_START_DATE) < 0)
						|| (CURRENTDATE.compareTo(_EXPIRE_DATE) > 0)) {
					code = 3;
				} else if (USAGE <= USAGE_COUNT) {
					code = 3;
				} else if ((USAGE - USAGE_COUNT) <= _ALERT) {
					code = 2;
				} else if ((USAGE - USAGE_COUNT) > _ALERT) {
					code = 1;
				}
				return code;
			} catch (Exception e) {
				throw new LicenseIOException(e);
			}
		}

		public Integer getAvilableTime() throws LicenseIOException {
			return getUsage() - getUsageCount();
		}

	}

}
