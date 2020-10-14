package com.woshidaniu.license.test;

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import com.woshidaniu.license.LicenseOps;
import com.woshidaniu.license.LicenseOpsException;

public class LicenseOpsDataReadTester {

	/**
	 * 
	 * �������ݣ�
	 * 
	 * {
	 * 	"_AUTH_ID":	"188c2d74578046e783e9ee3d15cebe74",
	 * 	"AUTH_USER":	"5q2j5pa55aSn5a2m",
	 * 	"AUTH_USER_CODE":	"100001",
	 * 	"_AUTH_DATE":	"2016-06-20",
	 * 	"START_DATE":	"2016-06-21",
	 * 	"EXPIRE_DATE":	"2016-06-25",
	 * 	"USAGE":	5,
	 * 	"USAGE_COUNT":	0,
	 * 	"_ALERT":	1,
	 * 	"_ENCRYPT_SHA":	"MDo7ZzdiMDw8JykiJyJwITkxZj1gYzRsOCVyd3FxIiI0czFuMHZmPTxxQnwmdSd7MDIzNDU3Mjg4JCckJyUnJTYw",
	 * 	"_LAST_ACCESS":	"1466412372"
	 * 	}
	 * 
	 * 
	 * 
	 */
	private LicenseOps ops = LicenseOps.getInstance();
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩ��Ȩ�û�(����)��Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getAuthUser() {
		try {
			String authUser = ops.getAuthUser();
			System.out.println(authUser);
			Assert.assertEquals("5q2j5pa55aSn5a2m", authUser);
			Assert.assertEquals("���Ǵ�ţ��ѧ", new String(new Base64().decode(authUser),"utf-8"));
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩ��Ȩ�û�����(����)��Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getAuthUserCode() {
		try {
			String authUserCode = ops.getAuthUserCode();
			System.out.println(authUserCode);
			Assert.assertEquals("100001", authUserCode);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩ��ʼ���ڣ�Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getStartDate() {
		try {
			String getStartDate = ops.getStartDate();
			System.out.println(getStartDate);
			Assert.assertEquals("2016-06-21", getStartDate);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩ�������ڣ�Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getExpireDate() {
		try {
			String getExpireDate = ops.getExpireDate();
			System.out.println(getExpireDate);
			Assert.assertEquals("2016-06-25", getExpireDate);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩ��ʹ��ʱ�䣬Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getUsageCount() {
		try {
			int getUsageCount = ops.getUsageCount();
			System.out.println(getUsageCount);
			Assert.assertEquals(0, getUsageCount);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵������ȡ��Ȩʹ��ʱ�䣬Ӧ����ȷ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:06:31<p>
	 */
	@Test
	public void testLicenseOps_getUsage() {
		try {
			int getUsage = ops.getUsage();
			System.out.println(getUsage);
			Assert.assertEquals(5, getUsage);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}

}
