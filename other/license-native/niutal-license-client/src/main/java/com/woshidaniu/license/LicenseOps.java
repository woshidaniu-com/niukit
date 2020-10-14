/**
 * ���Ǵ�ţ����ɷ����޹�˾
 */
package com.woshidaniu.license;

import com.woshidaniu.license.dataSync.DataSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.woshidaniu.license.io.ex.LicenseIOException;
import com.woshidaniu.license.io.p.LicenseIO;
import com.woshidaniu.license.io.p.LicenseIO._LICENSE;

/**
 * 
 * <p>
 * <h3>niutal���
 * <h3>
 * ˵��������license�ļ��ĺ����࣬��Ҫ�Ĺ����ǲ�ѯ���޸�license�ļ�
 * <p>
 * 
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 1.0
 * @since 2016��6��12������10:51:47
 */
@Service("licenseOps")
public final class LicenseOps extends WhatOS {

	private static final Logger log = LoggerFactory.getLogger(LicenseOps.class);

	private Object loakObject = new Object();

	private static LicenseOps _this = new LicenseOps();

	private _LICENSE _LICENSE;

	private LicenseOps() {
		try {
			this._LICENSE = LicenseIO.getLICENSE();
		} catch (LicenseIOException e) {
			log.error("license��ʼ��ʧ�ܣ�", e);
			throw new LicenseOpsException(e);
		}
	}

	// //���ؿ��Ƿ���سɹ�
	// private static boolean isNativeLibLoadSuccess = false;

	/**
	 * 
	 * <p>
	 * ����˵��������
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��25������12:16:28
	 * <p>
	 */
	public static LicenseOps getInstance() {
		return _this;
	}

//	/**
//	 * ��Ȩ�ļ�·��
//	 */
//	private String licenseFilePath;

	/**
	 * �Ƿ�����������������Ͳ�����licenseģ��
	 */
	private boolean isEnable = true;

	// /**
	// * license�ļ��Ƿ����
	// */
	// private boolean isLicenseFileExist = false;
	// /**
	// * ���ض�̬���ӿ�
	// */
	// static{
	// String classpath = null;
	// try {
	// classpath = LicenseOps.class.getResource("/").toURI().getPath();
	// } catch (URISyntaxException e) {
	// e.printStackTrace();
	// throw new RuntimeException(e);
	// }
	// StringBuilder path = new StringBuilder();
	// if (isLinuxPlatform) {
	// path.append(classpath).append("libniutal-license-linux-").append(jrebit).append(".so");
	// }
	//
	// if(isWinPlatform){
	// path.append(classpath.substring(1)).append("libniutal-license-win-").append(jrebit).append(".dll");
	// }
	//
	// if(path.length() == 0){
	// throw new LicenseLoadNativeLibException("�޷�ʶ�����ϵͳ����Ȩģ����ļ��޷���λ.");
	// }
	//
	// if(log.isDebugEnabled()){
	// log.debug("License lib path is {}", path);
	// }
	//
	// try {
	// System.load(path.toString());
	// isNativeLibLoadSuccess = true;
	// } catch (Exception e) {
	// throw new LicenseLoadNativeLibException("������Ȩģ����ļ�ʧ��.",e);
	// }
	// }

	/**
	 * 
	 * <p>
	 * ����˵������ȡ_DEV_MODE����
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getDevMode() throws LicenseOpsException {
		try {
			return _LICENSE.getDevMode();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ��Ȩ�ļ�ID
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��8��11������3:44:51
	 * <p>
	 */
	public String getAuthId() throws LicenseOpsException {
		try {
			return _LICENSE.getAuthId();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ����Ȩ�ˣ�ָѧУ���ƣ�
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getAuthUser() throws LicenseOpsException {
		try {
			return _LICENSE.getAuthUser();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ����Ȩ�˴��루ָѧУ���룩
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getAuthUserCode() throws LicenseOpsException {
		try {
			return _LICENSE.getAuthUserCode();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������Ȩ��ʼ����
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getStartDate() throws LicenseOpsException {
		try {
			return _LICENSE.getStartDate();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������Ȩ��������
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getExpireDate() throws LicenseOpsException {
		try {
			return _LICENSE.getExpireDate();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ�ļ�����
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�char[]
	 * </p>
	 * 
	 */
	public String getAuthDate() throws LicenseOpsException {
		try {
			return _LICENSE.getAuthDate();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ��Ȩʹ��ʱ�䣨��λΪ���졯��
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�int
	 * </p>
	 * 
	 */
	public int getUsage() throws LicenseOpsException {
		try {
			return _LICENSE.getUsage();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ��ʹ��ʱ�䣨��λ:���졯��
	 * <p>
	 * 
	 * <p>
	 * ����ֵ���ͣ�int
	 * </p>
	 * 
	 */
	public int getUsageCount() throws LicenseOpsException {
		try {
			return _LICENSE.getUsageCount();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡ�����ַ�
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��21������5:26:24
	 * <p>
	 */
	public String getEncryptSHA() throws LicenseOpsException {
		try {
			return _LICENSE.getEncryptSHA();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡҵ��ϵͳ����
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��8��10������6:48:14
	 * <p>
	 */
	public String getProductName() throws LicenseOpsException {
		try {
			return _LICENSE.getProductName();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵����</br> �÷�����Ҫ��������:</br> 1. �޸���Ȩʹ������(UsageCount)��ÿ���1��</br> 2.
	 * �޸ļ���ժҪ(EncriptSHA)</br>
	 * 
	 * </br>�÷�����֤ԭ���ԣ���UsageCount��EncriptSHAͬʱ�޸Ļ�ͬʱ���޸�
	 * </p>
	 * 
	 * <p>
	 * ����ֵ˵�����޸ĺ�Ļ���ժҪֵ(EncriptSHA)
	 * </p>
	 * 
	 */
	public String increseUsageCount() throws LicenseOpsException {
		try {
			return _LICENSE.increseUsageCount();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵�������auth״̬,�����ǰ��Ȩ�ļ��ļ�
	 * <p>
	 * <p>
	 * ����˵�����洢���ⲿ��encrypt_shaֵ,��ǰʱ���ַ���:yyyy-MM-dd
	 * </p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��17������10:11:35
	 * <p>
	 */
	public int checkStatus(String sha, String currentDate)
			throws LicenseOpsException {
		try {
			return _LICENSE.checkStatus(sha, currentDate);
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵������ȡʣ�����ʱ�䣨��λ:�죩
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��20������11:36:00
	 * <p>
	 */
	public int getAvilableTime() throws LicenseOpsException {
		try {
			return _LICENSE.getAvilableTime();
		} catch (LicenseIOException e) {
			throw new LicenseOpsException(e);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵����TODO
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��25������12:28:00
	 * <p>
	 */
	public void j_inceaseUsgaeCount(DataSync dataSync)
			throws LicenseOpsException {
		synchronized (loakObject) {
			String increseUsageCount = increseUsageCount();
			if (increseUsageCount == null) {
				throw new LicenseOpsException(
						"********** FILE %license.auth% DO NOT EXIST **********");
			}
			dataSync.sync(increseUsageCount);
		}
	}

	/**
	 * 
	 * <p>
	 * ����˵���������Ȩ�ļ��Ƿ��ǳ�ʼ���ļ�
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��6��25������12:37:44
	 * <p>
	 */
	public boolean j_checkStatusIntial() {
		synchronized (loakObject) {
			try {
				return checkStatus("0", "0") == 0;
			} catch (LicenseOpsException e) {
				e.printStackTrace();
			}
			return false;
		}

	}

	/**
	 * 
	 * <p>
	 * ����˵���������Ȩ״̬
	 * <p>
	 * <p>
	 * ��鵱ǰ�ļ��ĳ�ʼ��״̬��
	 * <p>
	 * ����ǳ�ʼ��״̬����Ҫͬ������
	 * <p>
	 * ������ǳ�ʼ��״̬���ȼ��hashֵ
	 * <p>
	 * ����˵��������ͬ���ص�����
	 * </p>
	 * <p>
	 * ����ֵ���ͣ�int
	 * </p>
	 * <p>
	 * ����ֵ˵����0��������1������
	 * </p>
	 */
	public int j_checkLicenseStatus(DataSync syncCallback) {
		synchronized (loakObject) {
			/**
			 * �����ȡ����shaֵ��Ϊ��Ȩ�ļ��Ƿ�
			 */
			String sha = syncCallback.getSHA();
			String date = syncCallback.getDate();
			if (sha == null || sha.trim().equals("-1")) {
				return Status.INVALID;
			}

			try {
				return checkStatus(sha, date);
			} catch (LicenseOpsException e) {
				e.printStackTrace();
			}
			return Status.UNKNOWN_ERROR;
		}

	}

	/**
	 * 
	 * <p>
	 * ����˵�������ļ��Ƿ���سɹ�
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��8��16������3:20:17
	 * <p>
	 */
	// public boolean isNativeLibLoadSuccess(){
	// return LicenseOps.isNativeLibLoadSuccess;
	// }

	/**
	 * 
	 * <p>
	 * ����˵�����Ƿ���������Ȩģ��
	 * <p>
	 * <p>
	 * ���ߣ�a href="#">Kangzhidong [1036]<a>
	 * <p>
	 * <p>
	 * ʱ�䣺2016��8��16������3:47:06
	 * <p>
	 */
	public boolean isLicenseOpsEnabled() {
		return this.isEnable;
	}

//	public String getLicenseFilePath() {
//		return licenseFilePath;
//	}
//
//	public void setLicenseFilePath(String licenseFilePath) {
//		this.licenseFilePath = licenseFilePath;
//	}

	public boolean isEnable() {
		return isEnable;
	}

	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}

	// public boolean isLicenseFileExist() {
	// return isLicenseFileExist;
	// }
	//
	// public void setLicenseFileExist(boolean isLicenseFileExist) {
	// this.isLicenseFileExist = isLicenseFileExist;
	// }

}
