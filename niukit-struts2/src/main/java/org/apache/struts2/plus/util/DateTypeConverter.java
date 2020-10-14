/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package org.apache.struts2.plus.util;

/**
 *@类名称	: DateTypeConverter.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Jul 7, 2016 3:33:26 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

@SuppressWarnings("rawtypes") 
public class DateTypeConverter extends StrutsTypeConverter {
	
	private static final String FORMATDATE = "yyyy-MM-dd";
	private static final String FORMATTIME = "yyyy-MM-dd HH:mm:ss";

	private static final SimpleDateFormat FORMATDATE_SDF = new SimpleDateFormat(FORMATDATE);
	private static final SimpleDateFormat FORMATTIME_SDF = new SimpleDateFormat(FORMATTIME);
	
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (values == null || values.length == 0) {
			return null;
		}
		// 有时分秒的要先转换
		Date date = null;
		String dateString = values[0];
		if (dateString != null) {
			try {
				date = FORMATTIME_SDF.parse(dateString);
			} catch (ParseException e) {
				date = null;
			}
			if (date == null) {
				try {
					date = FORMATDATE_SDF.parse(dateString);
				} catch (ParseException e) {
					date = null;
				}
			}
		}
		return date;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if (o instanceof Date) {
			return FORMATTIME_SDF.format((Date) o);
		}
		return "";
	}

}
