/**
 * 
 */
package com.woshidaniu.license.test;

import junit.framework.Assert;

import org.junit.Test;

import com.woshidaniu.license.LicenseOps;
import com.woshidaniu.license.LicenseOpsException;

/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵����������Ȩ�ļ�����
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��6��20������5:28:04
 */
public class LicenseOpsModifyTester {

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
	 * <p>����˵����ʹ��ʱ������<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:29:18<p>
	 */
	@Test
	public void testLicenseOps_increseUsageCount() {
		try {
			ops.increseUsageCount();
			int usageCount = ops.getUsageCount();
			Assert.assertEquals(1, usageCount);
		} catch (LicenseOpsException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * <p>����˵����ʹ��ʱ������<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��20������5:29:18<p>
	 */
	@Test
	public void testLicenseOps_runCheckThread() {
//		try {
//			DataSync dataSyncCallback = new DataSync(){
//
//				@Override
//				public boolean sync(String data) {
//					System.out.println(data);
//					return true;
//				}
//
//				@Override
//				public String getSHA() {
//					return null;
//				}
//
//				@Override
//				public boolean initLicenseData(String data) {
//					// TODO Auto-generated method stub
//					return false;
//				}
//
//				@Override
//				public String getDate() {
//					// TODO Auto-generated method stub
//					return null;
//				}};
//				
//			///ops.createAndRunLicenseThread(dataSyncCallback);
//		} catch (LicenseOpsException e) {
//			e.printStackTrace();
//		}
	}
	

}
