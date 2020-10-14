package com.woshidaniu.basicutils;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ValueUtils {

    public static String defaultIfNullWithURLEncoder(String value) {
    	return defaultIfNullWithURLEncoder(value,"");
    }

	public static String defaultIfNullWithURLEncoder(String value, String defaultValue) {
    	String retValue = "";
    	try{
    		if(value != null){
    		   retValue = URLEncoder.encode(value,"GBK");	
    		}else{
    		   retValue = URLEncoder.encode(defaultValue,"GBK");	
    		}
    	}catch(Exception ex){}

    	return retValue;
    }

    public static String defaultIfNullWithURLEncoderEx(String value) {
    	return defaultIfNullWithURLEncoderEx(value,"");
    }

	public static String defaultIfNullWithURLEncoderEx(String value, String defaultValue) {
    	return defaultIfNullWithURLEncoder(value,defaultValue).replaceAll("\\+", "%20");
    }
	
    public static String defaultIfNull(String value) {
    	return defaultIfNull(value,"");
    }

	public static String defaultIfNull(String value, String defaultValue) {
        return (value == null) ? defaultValue : value;
    }

	public static Integer defaultIfNull(Integer value) {
        return defaultIfNull(value,new Integer(0));
    }

	public static Integer defaultIfNull(Integer value, Integer defaultValue) {
        return (value == null) ? defaultValue : value;
    }

	public static Long defaultIfNull(Long value) {
        return defaultIfNull(value,new Long(0));
    }

	public static Long defaultIfNull(Long value, Long defaultValue) {
        return (value == null) ? defaultValue : value;
    }

    public static Long yuanTofen(BigDecimal value) {
    	BigDecimal v = defaultIfNull(value);

    	return v.multiply(new BigDecimal(100)).longValue();
    }

    public static BigDecimal fenToyuan(long value) {
		BigDecimal val = new BigDecimal(value);
		
		return val.divide(new BigDecimal(100));    	
    }

    public static BigDecimal defaultIfNull(BigDecimal value) {
		return defaultIfNull(value,new BigDecimal(0));
    }

	public static BigDecimal defaultIfNull(BigDecimal value, BigDecimal defaultValue) {
        return (value == null) ? defaultValue : value;
    }
    
    public static Long milliSecondsToSeconds(Date value) {
    	Date date = defaultIfNull(value);
        return date.getTime() / 1000;
    }

	public static Date defaultIfNull(Date value) {
		return defaultIfNull(value,new Date());
    }

    public static Date defaultIfNull(Date value, Date defaultValue) {
        return (value == null) ? defaultValue : value;
    }

	public static long defaultIfNullWithMilliSeconds(Date value) {
		return defaultIfNull(value,new Date()).getTime();
    }
    

	public static Date convertStringToDateByDefaultPattern(String sDate)
			throws Exception {
		return convertStringToDate(sDate,"yyyy-MM-dd HH:mm:ss");
	}

    /*
	 * 将特定格式的字符串转化成日期 
	 * @param sDate 日期字符串 
	 * @param datePattern 字符串模式，如：yyyy年M月d日，yyyy-MM-dd等 
	 * @return 日期对象 
	 * @throws java.text.ParseException
	 */
    public static Date convertStringToDate(String sDate, String datePattern)
			throws Exception {
		Date date = null;

		/*
		if (sDate == null || sDate.equals(""))
			return date;
        */
		try {

			SimpleDateFormat formatter = new SimpleDateFormat(datePattern);
			date = formatter.parse(sDate);

			return date;
		} catch (Exception ex) {
			throw new Exception("convertStringToDate failed", ex);
		}
	}
}
