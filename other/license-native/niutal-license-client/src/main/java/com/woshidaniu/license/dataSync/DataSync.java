/**
 * 
 */
package com.woshidaniu.license.dataSync;

import com.woshidaniu.license.WhoAmI;


/**
 * <p>
 *   <h3>niutal���<h3>
 *   ˵��������ͬ���ӿ�,�ýӿ��û�ͬ����Ȩ�ļ���ENCRIPT_SHA �����ݿ� ��ENCIPT_SHA ֵ
 * <p>
 * @author <a href="#">Kangzhidong [1036]<a>
 * @version 2016��6��12������5:33:18
 */
public interface DataSync {
	
	/**
	 * 
	 * <p>����˵�����״�ʹ��license�ļ�ʱ����ʼ������<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��21������4:34:54<p>
	 */
	boolean initLicenseData(String encryptSHA, String hisData);
	
	/**
	 * 
	 * <p>����˵������ȡ��ʷ��¼����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��8��11������4:02:20<p>
	 */
	String getHisData();
	
	/**
	 * 
	 * <p>����˵��������ͬ��,����ͬ����Ȩ�ļ������ݿ�����<p>
	 */
	boolean sync(String data);
	
	/**
	 * 
	 * <p>����˵������ȡ�洢��SHAֵ<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��14������2:14:49<p>
	 */
	String getSHA();
	
	/**
	 * 
	 * <p>����˵������ȡ��ǰʱ��<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��22������10:50:28<p>
	 */
	String getDate();
	
	/**
	 * 
	 * <p>����˵������ȡҵ��ϵͳ����<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��8��10������6:17:47<p>
	 */
	String getProductName(WhoAmI whoAmI);
	
}
