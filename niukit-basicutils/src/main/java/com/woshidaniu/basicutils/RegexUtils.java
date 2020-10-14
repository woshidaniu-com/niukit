package com.woshidaniu.basicutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * <p>
 *   <h3>niutal框架<h3>
 *   说明： 正则匹配工具
 * <p>
 * @author <a href="mailto:337836629@qq.com">Penghui.Qu[445]<a>
 * @version 2016年6月15日下午5:01:30
 */
public class RegexUtils {
	
    
	private RegexUtils(){
		super();
	}
	
    /**
     * 
     * <p>方法说明：判断字符串是否匹配正则<p>
     * <p>作者：<a href="mailto:337836629@qq.com">Penghui.Qu[445]<a><p>
     * <p>时间：2016年6月15日下午5:06:57<p>
     * @param content 字符串
     * @param regex 正则表达式
     * @return boolean
     */
    public static boolean isContentMatche(String content,String regex) {
    	Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        return matcher.matches();
        
        /*
         * Pattern.matches(regex, content) 等效于上面的三个语句
         * Pattern的实例是不可变的，可供多个并发线程安全使用。
         * Matcher 类的实例用于此目的则不安全。
         * 
         * 摘自JDK API
         */
    }
}
