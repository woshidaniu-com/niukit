package com.woshidaniu.basicutils;

import java.sql.Timestamp;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 
 *@类名称	: TimeUtils.java
 *@类描述	：时间工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 10, 2016 2:31:29 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class TimeUtils {

	protected static ConcurrentMap<String, SimpleDateFormat> COMPLIED_FORMAT = new ConcurrentHashMap<String, SimpleDateFormat>();
	
    /**
     * 时间格式：HH:mm:ss.SSS 
     */
    public static final String TIME_FORMAT_ONE = "HH:mm:ss.SSS";
    public static final SimpleDateFormat FORMAT_ONE = new ThreadSafeSimpleDateFormat(TIME_FORMAT_ONE);
    
    /**
     * 时间格式：HH:mm:ss 
     */
    public static final String TIME_FORMAT_TWO = "HH:mm:ss";
    public static final SimpleDateFormat FORMAT_TWO = new ThreadSafeSimpleDateFormat(TIME_FORMAT_TWO);
    
    /**
     * 时间格式：HH:mm
     */
    public static final String TIME_FORMAT_THREE = "HH:mm";
    public static final SimpleDateFormat FORMAT_THREE = new ThreadSafeSimpleDateFormat(TIME_FORMAT_THREE);
    
    /**
     * 时间格式：h:m:s a
     */
    public static final String TIME_FORMAT_FOUR = "h:m:s a";
    public static final SimpleDateFormat FORMAT_FOUR = new ThreadSafeSimpleDateFormat(TIME_FORMAT_FOUR);
    
    /**
     * 时间格式：h:m a
     */
    public static final String TIME_FORMAT_FIVE = "h:m a";
    public static final SimpleDateFormat FORMAT_FIVE = new ThreadSafeSimpleDateFormat(TIME_FORMAT_FIVE);
    
    /**
     * 时间格式：HH时mm分ss秒SSS毫秒
     */
    public static final String TIME_FORMAT_SIX = "HH时mm分ss秒SSS毫秒";
    public static final SimpleDateFormat FORMAT_SIX = new ThreadSafeSimpleDateFormat(TIME_FORMAT_SIX);
    
    /**
     * 时间格式：HH时mm分ss秒
     */
    public static final String TIME_FORMAT_SEVEN = "HH时mm分ss秒";
    public static final SimpleDateFormat FORMAT_SEVEN = new ThreadSafeSimpleDateFormat(TIME_FORMAT_SEVEN);
    
    /**
     * 时间格式：HH时mm分
     */
    public static final String TIME_FORMAT_EIGHT = "HH时mm分";
    public static final SimpleDateFormat FORMAT_EIGHT = new ThreadSafeSimpleDateFormat(TIME_FORMAT_EIGHT);
    
    /**
     * 时间格式：a h时m分s秒
     */
    public static final String TIME_FORMAT_NINE = "a h时m分s秒";
    public static final SimpleDateFormat FORMAT_NINE = new ThreadSafeSimpleDateFormat(TIME_FORMAT_NINE);
    
    /**
     * 时间格式：a h时m分
     */
    public static final String TIME_FORMAT_TEN = "a h时m分";
    public static final SimpleDateFormat FORMAT_TEN = new ThreadSafeSimpleDateFormat(TIME_FORMAT_TEN);
    
    /**
     * 时间格式：hh12  
     */
    public static final String TIME_FORMAT_MONTH = "MMMM";
    public static final SimpleDateFormat FORMAT_MONTH = new ThreadSafeSimpleDateFormat(TIME_FORMAT_MONTH);
    
    /**
     * 时间格式：EEE  
     */
    public static final String TIME_FORMAT_WEEK = "EEE";
    public static final SimpleDateFormat FORMAT_WEEK = new ThreadSafeSimpleDateFormat(TIME_FORMAT_WEEK);
    
    /**
     * 时间格式：hh24  
     */
    public static final String TIME_FORMAT_24HOUR = "hh24";
    public static final SimpleDateFormat FORMAT_24HOUR = new ThreadSafeSimpleDateFormat(TIME_FORMAT_24HOUR);
    
    /**
     * 时间格式：hh12  
     */
    public static final String TIME_FORMAT_12HOUR = "hh12";
    public static final SimpleDateFormat FORMAT_12HOUR = new ThreadSafeSimpleDateFormat(TIME_FORMAT_12HOUR);
    
	protected static Map<String,Integer> TIME_POWERS = new HashMap<String, Integer>();
	private static final Pattern regex = Pattern.compile("^([1-9][0-9]*\\.?[0-9]*)\\s*([smhd]{1})$");
	
	static {
        
        //时间格式
        COMPLIED_FORMAT.put(TIME_FORMAT_ONE, FORMAT_ONE );
        COMPLIED_FORMAT.put(TIME_FORMAT_TWO, FORMAT_TWO );
        COMPLIED_FORMAT.put(TIME_FORMAT_THREE, FORMAT_THREE );
        COMPLIED_FORMAT.put(TIME_FORMAT_FOUR, FORMAT_FOUR );
        COMPLIED_FORMAT.put(TIME_FORMAT_FIVE, FORMAT_FIVE );
        COMPLIED_FORMAT.put(TIME_FORMAT_SIX, FORMAT_SIX );
        COMPLIED_FORMAT.put(TIME_FORMAT_SEVEN, FORMAT_SEVEN );
        COMPLIED_FORMAT.put(TIME_FORMAT_EIGHT, FORMAT_EIGHT );
        COMPLIED_FORMAT.put(TIME_FORMAT_NINE, FORMAT_NINE );
        COMPLIED_FORMAT.put(TIME_FORMAT_TEN, FORMAT_TEN );
        COMPLIED_FORMAT.put(TIME_FORMAT_MONTH, FORMAT_MONTH );
        COMPLIED_FORMAT.put(TIME_FORMAT_WEEK, FORMAT_WEEK );
        COMPLIED_FORMAT.put(TIME_FORMAT_24HOUR, FORMAT_24HOUR );
        COMPLIED_FORMAT.put(TIME_FORMAT_12HOUR, FORMAT_12HOUR );
        
		//(s:秒钟,m:分钟,h:小时,d:天)
		TIME_POWERS.put("s", 1000);//一秒
		TIME_POWERS.put("m", 60 * 1000);//一分钟
		TIME_POWERS.put("h", 60 * 60 * 1000);//一小时
		TIME_POWERS.put("d", 24 * 60 * 60 * 1000);//一天
	}
	
	/**
	 * 
	 *@描述 ：根据给出的字符串格式，获取相应的时间格式化对象
	 *@创建人 : kangzhidong
	 *@创建时间 : Mar 10, 20169:53:46 PM
	 *@param format
	 *@return
	 *@修改人 :
	 *@修改时间 :
	 *@修改描述 :
	 */
	public static SimpleDateFormat getTimeFormat(String format) {
		if (StringUtils.isNotEmpty(format)) {
			//检测是否已在日期格式缓存中存在
			if(DateUtils.hasDateFormat(format)){
				return DateUtils.getDateFormat(format);
			}
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
	
	/**
	 * 
	 *@描述		：得到字符时间对应的时间Ingeter值：如 5s 对应值为 50000
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 201611:55:04 AM
	 *@param value
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static Integer getTimeMillis(String value) {
		if (value == null){
			return -1;
		}
        Matcher matcher = regex.matcher(value.toLowerCase());
		if(matcher.matches()){
			if(matcher.group(1)!=null&&matcher.group(2)!=null){
				Integer num = Integer.valueOf(matcher.group(1));
				Integer mult = TIME_POWERS.get(matcher.group(2));
				return Integer.valueOf(num * mult);
			}
		}
		return 0;
	}
	
	private static Calendar getBeginCalendar() {
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c;
	}

	/**
	 * 
	 *@描述		：取得当前天零晨时间
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 20162:30:44 PM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static long getBeginTimeOfDay() {
		return getBeginCalendar().getTimeInMillis();
	}
	
	
	/**
	 * 
	 *@描述		：取得当前天偏移指定天数后的零晨时间 <br/>
	 *     			offset > 0 ,往后延迟offset天， <br/>
	 *     			offset < 0 向前推进 offset天 <br/>
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 20163:12:36 PM
	 *@param offset
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static long getBeginTimeOfDay(int offset) {
		Calendar c = getBeginCalendar();
		c.add(Calendar.DAY_OF_YEAR, offset);
		return c.getTimeInMillis();
	}
	
	
	public static String getTime(long time, String format) {
		return TimeUtils.getTimeFormat(format).format(new Timestamp(time));
	}

    /**
	 * 
	 *@描述		：取得当前时间
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 20162:35:00 PM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static long getNow() {
		return System.currentTimeMillis();
	}
    
    /**
     * 
     *@描述		：获取当前时间戳 ,等同于DateUtils.getFomratCurrentDate("yyyyMMddHHmmssSSS")
     *@创建人	: kangzhidong
     *@创建时间	: Mar 10, 201610:53:56 PM
     *@return	: 当前时间的时间戳
     *@修改人	: 
     *@修改时间	: 
     *@修改描述	:
     */
    public static String getTimestamp() {
        return getTimestamp(new Date());
    }
    
    /**
     * 
     *@描述		：获取指定时间的时间戳 等同于DateUtils.getFomratCurrentDate(date,"yyyyMMddHHmmssSSS")
     *@创建人	: kangzhidong
     *@创建时间	: Mar 10, 201610:53:29 PM
     *@param date : 指定将获取时间戳的时间对象
     *@return 	: 指定时间的时间戳
     *@修改人	: 
     *@修改时间	: 
     *@修改描述	:
     */
    public static String getTimestamp(Date date) {
    	StringBuilder buffer = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        buffer.append(calendar.get(Calendar.YEAR));
        if (calendar.get(Calendar.MONTH) < 9) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.MONTH) + 1);
        if (calendar.get(Calendar.DATE) < 10) {
            buffer.append(0);
        } 
        buffer.append(calendar.get(Calendar.DATE));
        if (calendar.get(Calendar.HOUR_OF_DAY) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.HOUR_OF_DAY));
        if (calendar.get(Calendar.MINUTE) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.MINUTE));
        if (calendar.get(Calendar.SECOND) < 10) {
            buffer.append(0);
        }
        buffer.append(calendar.get(Calendar.SECOND));
        buffer.append(calendar.get(Calendar.MILLISECOND));
        return buffer.toString();
    }
    
    /**
     * 
     *@描述		：取得当前时间（数据库时间搓）
     *@创建人	: kangzhidong
     *@创建时间	: Mar 11, 20162:33:21 PM
     *@param time
     *@return
     *@修改人	: 
     *@修改时间	: 
     *@修改描述	:
     */
	public static Timestamp getTimestamp(long time) {
		return new Timestamp(time);
	}
	
	/**
	 * 
	 *@描述		：取得指定时间的时间搓
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 20162:34:04 PM
	 *@param time
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static Timestamp getTimestamp(String time) {
		try {
			return getTimestamp(DateUtils.getDateFormat(DateUtils.DATE_FORMAT_ONE).parse(time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 *@描述		：取得当前时间（数据库时间搓）
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 11, 20163:18:21 PM
	 *@param time
	 *@param format
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static Timestamp getTimestamp(String time, String format) {
		try {
			return getTimestamp(TimeUtils.getTimeFormat(format).parse(time).getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 当前年份 */
	public static String getYear() {
		return getTimeFormat(DateUtils.DATE_FORMAT_YEAR).format(new java.util.Date());
	}
	
	/** 年月日 */
	public static String getDay() {
		return getTimeFormat(DateUtils.DATE_FORMAT_FOUR).format(new java.util.Date());
	}

	/** 小时 */
	public static String getHour() {
		return getTimeFormat(TIME_FORMAT_24HOUR).format(new java.util.Date());
	}
    
	public static void main(String[] args) {
		System.out.println(TimeUtils.getTimeMillis("2d"));
		System.out.println(TimeUtils.getBeginTimeOfDay(2) - TimeUtils.getBeginTimeOfDay());
	}
	
	//线程安全的DateFormat
	private static class ThreadSafeSimpleDateFormat extends SimpleDateFormat{

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
}