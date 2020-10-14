package com.woshidaniu.basicutils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 
 *@类名称	: URLUtils.java
 *@类描述	： URL转码和解码工具
 *@创建人	：kangzhidong
 *@创建时间	：Sep 8, 2016 8:38:04 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class URLUtils {

	//验证规则
    protected static String regEx = ".*%[0-9A-F]{2}.*";
    //编译正则表达式
    protected static Pattern pattern = Pattern.compile(regEx);
    
	/**
	 * @描述:判断字串是否经过URLEncoder编码(正则判断字串是否匹配"%xy"，其中xy是两位16进制的数值)
	 * @param String
	 * @return
	 */
	public static boolean isURLEncoder(String str){
		if(str == null || str.length() == 0){
			return false;
		}else{
		    Matcher matcher = pattern.matcher(str);
		    // 字符串是否与正则表达式相匹配
		    return matcher.matches();		    
		}
	}
	
    public static String escape(String name) {
        String ret = "";

        try {
            ret = URLEncoder.encode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public static String unescape(String name) {
        String ret = "";

        try {
            ret = URLDecoder.decode(name, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return ret;
    }

}
