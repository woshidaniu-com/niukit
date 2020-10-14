package com.woshidaniu.basicutils;

import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *@类名称 : DateUtils.java
 *@类描述 ：
 *@创建人 ：kangzhidong
 *@创建时间 ：Mar 10, 2016 2:31:11 PM
 *@修改人 ：
 *@修改时间 ：
 *@版本号 :v1.0
 */
public abstract class DateUtils extends org.apache.commons.lang.time.DateUtils {

	protected static ConcurrentMap<String, SimpleDateFormat> COMPLIED_FORMAT = new ConcurrentHashMap<String, SimpleDateFormat>();

	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss.SSS
	 */
	public static final String DATE_FORMAT_ONE = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final SimpleDateFormat FORMAT_ONE = new ThreadSafeSimpleDateFormat(DATE_FORMAT_ONE);
	
	/**
	 * 日期格式：yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_TWO = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat FORMAT_TWO = new ThreadSafeSimpleDateFormat(DATE_FORMAT_TWO);
	
	/**
	 * 日期格式：yyyy-MM-dd HH:mm
	 */
	public static final String DATE_FORMAT_THREE = "yyyy-MM-dd HH:mm";
	public static final SimpleDateFormat FORMAT_THREE = new ThreadSafeSimpleDateFormat(DATE_FORMAT_THREE);
	
	/**
	 * 日期格式：yyyy-MM-dd
	 */
	public static final String DATE_FORMAT_FOUR = "yyyy-MM-dd";
	public static final SimpleDateFormat FORMAT_FOUR = new ThreadSafeSimpleDateFormat(DATE_FORMAT_FOUR);
	
	/**
	 * 日期格式：yyyy/MM/dd HH:mm:ss.SSS
	 */
	public static final String DATE_FORMAT_FIVE = "yyyy/MM/dd HH:mm:ss.SSS";
	public static final SimpleDateFormat FORMAT_FIVE = new ThreadSafeSimpleDateFormat(DATE_FORMAT_FIVE);
	
	/**
	 * 日期格式：yyyy/MM/dd HH:mm:ss
	 */
	public static final String DATE_FORMAT_SIX = "yyyy/MM/dd HH:mm:ss";
	public static final SimpleDateFormat FORMAT_SIX = new ThreadSafeSimpleDateFormat(DATE_FORMAT_SIX);
	
	/**
	 * 日期格式：yyyy/MM/dd HH:mm
	 */
	public static final String DATE_FORMAT_SEVEN = "yyyy/MM/dd HH:mm";
	public static final SimpleDateFormat FORMAT_SEVEN = new ThreadSafeSimpleDateFormat(DATE_FORMAT_SEVEN);
	
	/**
	 * 日期格式：yyyy/MM/dd
	 */
	public static final String DATE_FORMAT_EIGHT = "yyyy/MM/dd";
	public static final SimpleDateFormat FORMAT_EIGHT = new ThreadSafeSimpleDateFormat(DATE_FORMAT_EIGHT);
	
	/**
	 * 日期格式：yyyy年MM月dd日 HH:mm:ss.SSS
	 */
	public static final String DATE_FORMAT_NINE = "yyyy年MM月dd日 HH:mm:ss.SSS";
	public static final SimpleDateFormat FORMAT_NINE = new ThreadSafeSimpleDateFormat(DATE_FORMAT_NINE);
	
	/**
	 * 日期格式：yyyy年MM月dd日 HH:mm:ss
	 */
	public static final String DATE_FORMAT_TEN = "yyyy年MM月dd日 HH:mm:ss";
	public static final SimpleDateFormat FORMAT_TEN = new ThreadSafeSimpleDateFormat(DATE_FORMAT_FOUR);
	
	/**
	 * 日期格式：yyyy年MM月dd日 HH:mm
	 */
	public static final String DATE_FORMAT_ELEVEN = "yyyy年MM月dd日 HH:mm";
	public static final SimpleDateFormat FORMAT_ELEVEN = new ThreadSafeSimpleDateFormat(DATE_FORMAT_ELEVEN);
	
	/**
	 * 日期格式：yyyy年MM月dd日
	 */
	public static final String DATE_FORMAT_TWELVE = "yyyy年MM月dd日";
	public static final SimpleDateFormat FORMAT_TWELVE = new ThreadSafeSimpleDateFormat(DATE_FORMAT_TWELVE);
	
	/**
	 * 日期格式：yyyy年MM月
	 */
	public static final String DATE_FORMAT_THIRTEEN = "yyyy年MM月";
	public static final SimpleDateFormat FORMAT_THIRTEEN = new ThreadSafeSimpleDateFormat(DATE_FORMAT_THIRTEEN);
	
	/**
	 * 日期格式：MM月dd日
	 */
	public static final String DATE_FORMAT_FOURTEEN = "MM月dd日";
	public static final SimpleDateFormat FORMAT_FOURTEEN = new ThreadSafeSimpleDateFormat(DATE_FORMAT_FOURTEEN);

	/**
	 * 日期格式：yyyyMMddHHmmssSSS
	 */
	public static final String DATE_FORMAT_LONG = "yyyyMMddHHmmssSSS";
	public static final SimpleDateFormat FORMAT_LONG = new ThreadSafeSimpleDateFormat(DATE_FORMAT_LONG);
	
	/**
	 * 日期格式：yyyyMMdd
	 */
	public static final String DATE_FORMAT_SORT = "yyyyMMdd";
	public static final SimpleDateFormat FORMAT_SORT = new ThreadSafeSimpleDateFormat(DATE_FORMAT_SORT);
	
	/**
	 * 日期格式：yyyy
	 */
	public static final String DATE_FORMAT_YEAR = "yyyy";
	public static final SimpleDateFormat FORMAT_YEAR = new ThreadSafeSimpleDateFormat(DATE_FORMAT_YEAR);
	
	/**
	 * 默认验证日期正则表达式
	 */
	private static final String DATE_PATTERN_DEFAULT = "(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)";
	/**
	 * yyyy-MM-dd HH:mm:ss.m 格式正则
	 */
	private static final String DATE_PATTERN_ONE = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}\\.\\d{1,3}$";
	/**
	 * yyyy-MM-dd HH:mm:ss格式正则
	 */
	private static final String DATE_PATTERN_TWO = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy-MM-dd HH:mm格式正则
	 */
	private static final String DATE_PATTERN_THREE = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2} \\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy-MM-dd格式正则
	 */
	private static final String DATE_PATTERN_FOUR = "^\\d{2,4}\\-\\d{1,2}\\-\\d{1,2}$";
	/**
	 * yyyy/MM/dd HH:mm:ss格式正则
	 */
	private static final String DATE_PATTERN_FIVE = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$";
	/**
	 * yyyy/MM/dd格式正则
	 */
	private static final String DATE_PATTERN_SIX = "^\\d{2,4}\\/\\d{1,2}\\/\\d{1,2}$";

	static {

		// 日期格式
		COMPLIED_FORMAT.put(DATE_FORMAT_ONE, FORMAT_ONE);
		COMPLIED_FORMAT.put(DATE_FORMAT_TWO, FORMAT_TWO);
		COMPLIED_FORMAT.put(DATE_FORMAT_THREE, FORMAT_THREE);
		COMPLIED_FORMAT.put(DATE_FORMAT_FOUR, FORMAT_FOUR);
		COMPLIED_FORMAT.put(DATE_FORMAT_FIVE, FORMAT_FIVE);
		COMPLIED_FORMAT.put(DATE_FORMAT_SIX, FORMAT_SIX);
		COMPLIED_FORMAT.put(DATE_FORMAT_SEVEN, FORMAT_SEVEN);
		COMPLIED_FORMAT.put(DATE_FORMAT_EIGHT, FORMAT_EIGHT);
		COMPLIED_FORMAT.put(DATE_FORMAT_NINE, FORMAT_NINE);
		COMPLIED_FORMAT.put(DATE_FORMAT_TEN, FORMAT_TEN);
		COMPLIED_FORMAT.put(DATE_FORMAT_ELEVEN, FORMAT_ELEVEN);
		COMPLIED_FORMAT.put(DATE_FORMAT_TWELVE, FORMAT_TWELVE);
		COMPLIED_FORMAT.put(DATE_FORMAT_THIRTEEN, FORMAT_THIRTEEN);
		COMPLIED_FORMAT.put(DATE_FORMAT_FOURTEEN, FORMAT_FOURTEEN);
		COMPLIED_FORMAT.put(DATE_FORMAT_LONG, FORMAT_LONG);
		COMPLIED_FORMAT.put(DATE_FORMAT_SORT, FORMAT_SORT);
		COMPLIED_FORMAT.put(DATE_FORMAT_YEAR, FORMAT_YEAR);

	}

	/**
	 * 
	 *@描述 ：根据给出的字符串格式，获取相应的日期格式化对象
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 20169:53:46 PM
	 *@param format
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static SimpleDateFormat getDateFormat(String format) {
		if (StringUtils.isNotEmpty(format)) {
			SimpleDateFormat ret = COMPLIED_FORMAT.get(format);
			if (ret != null) {
				return ret;
			}
			ret = new SimpleDateFormat(format);
			SimpleDateFormat existing = COMPLIED_FORMAT.putIfAbsent(format, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	public static boolean hasDateFormat(String format) {
		return COMPLIED_FORMAT.containsKey(format);
	}

	/**
	 * 
	 *@描述 ：将日期格式化成字符串：yyyy-MM-dd HH:mm:ss
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 11, 20169:53:01 AM
	 *@param date 将被格式的Date对象
	 *@return 被格式化后的日期字符串形式
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static String format(Date date) {
		return DateUtils.getDateFormat(DATE_FORMAT_TWO).format(date);
	}

	/**
	 * 
	 *@描述 ：根据传入的日期格式将日期化成相应格式的字符串，如：
	 *    <p/>
	 *    <ul>
	 *    <li>yyyy-MM-dd HH:mm:ss
	 *    <li>yyyy-MM-dd
	 *    <li>yyyy/MM/dd HH:mm:ss
	 *    <li>yyyy/MM/dd
	 *    </ul>
	 *    </p>
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 20165:26:48 PM
	 *@param date : 待格式化的时间对象
	 *@param format ：时间格式
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static String format(Date date, String format) {
		Assert.notNull(date, " date must not be null ");
		Assert.hasText(format, " format must not be null ");
		return DateUtils.getDateFormat(format).format(date);
	}

	/**
	 * 
	 *@描述 ：将long类型的日期格式化成yyyy-MM-dd HH:mm:ss格式的日期字符串形式
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 11, 20169:55:03 AM
	 *@param date 将被格式化的long类型日期
	 *@return 格式化后的字符串形式日期
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static String format(long date) {
		return DateUtils.getDateFormat(DATE_FORMAT_TWO).format(date);
	}

	/**
	 * 
	 *@描述 ：根据传入的日期格式将系统当前日期对象进行格式后返回
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 20165:27:26 PM
	 *@param format ：时间格式
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static String format(String format) {
		Assert.hasText(format, " format must not be null ");
		return DateUtils.getDateFormat(format).format(new Date());
	}

	/**
	 * 
	 *@描述 ： 将日期格式化成字符串,如果时间是 00:00:00则去掉时间
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 11, 20169:52:37 AM
	 *@param date 将被格式的Date对象
	 *@return 格式化后的字符串
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static String formatBySituation(Date date) {
		String ds = DateUtils.getDateFormat(DATE_FORMAT_TWO).format(date);
		return ds.endsWith("00:00:00") ? ds.substring(0, 10) : ds;
	}


	/**
	 * 将日期字符串解析成日期对象，支持一下格式<br/>
	 * <p>
	 * <ul>
	 * <li>yyyy-MM-dd HH:mm:ss.m</li>
	 * <li>yyyy-MM-dd HH:mm:ss</li>
	 * <li>yyyy-MM-dd HH:mm</li>
	 * <li>yyyy-MM-dd</li>
	 * <li>yyyy/MM/dd HH:mm:ss</li>
	 * <li>yyyy/MM/dd</li>
	 * </ul>
	 * </p>
	 * @param dateStr 字符串形式的日期
	 * @return 字符串格式日期经相应的格式格式化后的Date形式<br/>
	 *         如果传入的日期跟内置日期格式不匹配,抛出异常
	 * @throws Exception
	 */
	public static Date parse(String dateStr) throws Exception {
		Date date = null;
		try {
			Pattern p1 = Pattern.compile(DATE_PATTERN_TWO);
			Matcher m1 = p1.matcher(dateStr);
			if (m1.matches()) {
				date = DateUtils.getDateFormat(DATE_FORMAT_TWO).parse(dateStr);
			}

			if (date == null) {
				Pattern p2 = Pattern.compile(DATE_PATTERN_FOUR);
				Matcher m2 = p2.matcher(dateStr);
				if (m2.matches()) {
					date = DateUtils.getDateFormat(DATE_FORMAT_THREE).parse(dateStr);
				}
			}

			if (date == null) {
				Pattern p3 = Pattern.compile(DATE_PATTERN_FIVE);
				Matcher m3 = p3.matcher(dateStr);
				if (m3.matches()) {
					date = DateUtils.getDateFormat(DATE_FORMAT_SIX).parse(dateStr);
				}
			}

			if (date == null) {
				Pattern p4 = Pattern.compile(DATE_PATTERN_SIX);
				Matcher m4 = p4.matcher(dateStr);
				if (m4.matches()) {
					date = DateUtils.getDateFormat(DATE_FORMAT_EIGHT).parse(dateStr);
				}
			}

			if (date == null) {
				Pattern p5 = Pattern.compile(DATE_PATTERN_THREE);
				Matcher m5 = p5.matcher(dateStr);
				if (m5.matches()) {
					date = DateUtils.getDateFormat(DATE_FORMAT_THREE).parse(dateStr);
				}
			}

			if (date == null) {
				Pattern p6 = Pattern.compile(DATE_PATTERN_ONE);
				Matcher m6 = p6.matcher(dateStr);
				if (m6.matches()) {
					date = DateUtils.getDateFormat(DATE_FORMAT_ONE).parse(dateStr);
				}
			}

		} catch (ParseException e) {
			throw new Exception("非法日期字符串，解析失败：" + dateStr, e);
		}
		return date;
	}

	/**
	 * 
	 *@描述 ：根据指定的格式strFormat,将给定的字符串形式的日期转换成date类型
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 201610:23:23 PM
	 *@param dateStr  	: 要转换的日期字符串
	 *@param format 	: 日期格式
	 *@return 转换以后的日期
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static Date parseDate(String dateStr, String format) {
		Assert.hasText(dateStr, " dateStr must not be null ");
		Assert.hasText(format, " format must not be null ");
		Date newDate = null;
		try {
			newDate = DateUtils.getDateFormat(format).parse(dateStr);
		} catch (ParseException pe) {
			newDate = null;
		}
		return newDate;
	}

	public static Date parseDate(String dateStr) {
		return parseDate(dateStr, DATE_FORMAT_TWO);
	}
	
	/**
	 * 
	 *@描述 ：使用正则表达式判定日期
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 201610:16:36 PM
	 *@param date
	 *@param expression
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static boolean isDate(String date, String expression) {
		// 使用正则表达式进行判定
		if (expression == null || expression.trim().length() == 0) {
			expression = DATE_PATTERN_DEFAULT;
		}
		Pattern format;
		// System.out.println(expression);
		format = Pattern.compile(expression, 2);
		Matcher matcher = format.matcher(date.trim());
		if (!matcher.matches()) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 *@描述 ：验证日期合法性(通过抛出错误方式来验证)
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 201610:15:40 PM
	 *@param date
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static boolean isDate(String dateStr) {
		return isFormat(dateStr,DATE_FORMAT_ONE);
	}
	
	public static boolean isFormat(String dateStr, String format) {
		Assert.hasText(dateStr, " dateStr must not be null ");
		Assert.hasText(format, " format must not be null ");
		SimpleDateFormat sdf = DateUtils.getDateFormat(format);
		// 这个的功能是不让系统自动转换 不把1996-13-3 转换为1997-3-1
		sdf.setLenient(false);
		try {
			sdf.parse(dateStr);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	//线程安全的DateFormat
	private static class ThreadSafeSimpleDateFormat extends SimpleDateFormat {

		private static final long serialVersionUID = -2242489411655155686L;

		public ThreadSafeSimpleDateFormat(String pattern) {
			super(pattern);
		}

		@Override
		public synchronized StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
			return super.format(date, toAppendTo, pos);
		}

		@Override
		public synchronized AttributedCharacterIterator formatToCharacterIterator(Object obj) {
			return super.formatToCharacterIterator(obj);
		}

		@Override
		public synchronized Date parse(String text, ParsePosition pos) {
			return super.parse(text, pos);
		}
	}

	public static void main(String[] args) {
		ThreadSafeSimpleDateFormat df = new ThreadSafeSimpleDateFormat("yyyy-MM-dd");
		try {
			Date date = df.parse("2018-1-1");
			System.out.println(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println(DateUtils.isDate("2016-05-5 56:44:11"));
	}
}
