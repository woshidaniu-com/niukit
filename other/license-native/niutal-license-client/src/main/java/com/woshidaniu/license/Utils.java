/**
 * ���Ǵ�ţ����ɷ����޹�˾
 */
package com.woshidaniu.license;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @���� DateUtils.java
 * @���� [1036]
 * @���� Kangzhidong 
 * @����ʱ�� 2016 2016��6��6�� ����6:05:33
 * @�������� license date utils
 * 
 */
public class Utils {
	
	static final String DATE_FORMAT_STR = "yyyy-MM-dd";
	
	static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_FORMAT_STR);
	
	
	/**
	 * 
	 * <p>����˵�����жϲ���ϵͳ���1��windows, 2:linux/unix<p>
	 * <p>���ߣ�a href="#">Kangzhidong [1036]<a><p>
	 * <p>ʱ�䣺2016��6��21������5:07:45<p>
	 */
	static int OS_ARCH(){
		int i = 0;
		String pathSep = System.getProperty("path.separator");
		if(";".equals(pathSep)){
			i = 1;
		}else if(":".equals(pathSep)){
			i = 2;
		}
		return i;
	}
	
	/**
	 * ��ȡ���ڶ���
	 * @param date
	 * @return
	 * @throws ParseException 
	 */
	static Date getDate(char[] date){
		Date parse = null;
		try {
			String dateStr = new String(date);
			parse = DATE_FORMAT.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}
	
	/**
	 * byte����ת����
	 * @param date
	 * @return
	 */
	static int byteArr2Int(byte[] date){
		Integer parse = null;
		try {
			String dateStr = new String(date);
			parse = Integer.parseInt(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}
	
	/**
	 * char����ת����
	 * @param date
	 * @return
	 */
	static int charArr2Int(char[] date){
		Integer parse = null;
		try {
			String dateStr = new String(date);
			parse = Integer.parseInt(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return parse;
	}
	
}
