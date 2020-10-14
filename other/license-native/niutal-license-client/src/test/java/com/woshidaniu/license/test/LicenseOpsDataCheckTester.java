package com.woshidaniu.license.test;

import junit.framework.Assert;

import org.junit.Test;

import com.woshidaniu.license.LicenseOps;
import com.woshidaniu.license.LicenseOpsException;
import com.woshidaniu.license.Status;

public class LicenseOpsDataCheckTester {

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
	 * <p>����˵���������Ȩ�ļ�״̬<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:20:40<p>
	 */
	@Test
	public void testLicenseOps_checkStatus_valid_sha() {
		String sha = "MDo7ZzdiMDw8JykiJyJwITkxZj1gYzRsOCVyd3FxIiI0czFuMHZmPTxxQnwmdSd7MDIzNDU3Mj04JCckJyUgJzE2";
		try {
			int checkStatus = ops.checkStatus(sha,"");
			Assert.assertEquals(Status.EXPIRED, checkStatus);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 
	 * <p>����˵���������Ȩ�ļ�״̬<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:20:40<p>
	 */
	@Test
	public void testLicenseOps_checkStatus_invalid_sha() {
		String sha = "FDo7ZzdiMDw8JykiJyJwITkxZj1gYzRsOCVyd3FxIiI0czFuMHZmPTxxQnwmdSd7MDIzNDU3Mjg4JCckJyUnJTYw";
		try {
			int checkStatus = ops.checkStatus(sha,"");
			Assert.assertEquals(Status.INVALID, checkStatus);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 
	 * <p>����˵���������Ȩʣ�����ʱ��<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:20:40<p>
	 */
	@Test
	public void testLicenseOps_getAavilableTime() {
		try {
			int avilableTime = ops.getAvilableTime();
			Assert.assertEquals(0, avilableTime);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
		
	}
	
	

}
