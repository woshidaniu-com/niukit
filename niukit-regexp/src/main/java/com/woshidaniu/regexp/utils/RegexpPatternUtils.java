/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.regexp.utils;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ConfigUtils;

/**
 *@类名称	: PatternUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 23, 2016 4:08:38 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class RegexpPatternUtils {

	protected static Logger LOG = LoggerFactory.getLogger(RegexpPatternUtils.class);
	protected static ConcurrentMap<String, Pattern> COMPLIED_PATTERN = new ConcurrentHashMap<String, Pattern>();
	protected static String[] REGEXP_ARR = new String[]{"date","html","math","mobile","net","normal","special","sql"};
	
	/**
	 * 
	 *@描述		：
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 23, 20164:04:47 PM
	 *@param regexp
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public static Pattern getPattern(String regexp) {
		if (StringUtils.isNotEmpty(regexp)) {
			Pattern ret = COMPLIED_PATTERN.get(regexp);
			if (ret != null) {
				return ret;
			}
			ret = Pattern.compile(regexp);
			Pattern existing = COMPLIED_PATTERN.putIfAbsent(regexp, ret);
			if (existing != null) {
				ret = existing;
			}
			return ret;
		}
		return null;
	}
	
	public static Properties getRegexpProperties(String name) {
		Properties regexp = null;
		if(!BlankUtils.isBlank(name)){
			if(name.matches("([a-zA-Z0-9_$\u4E00-\u9FA5]+)")){
				String regexp_file_name = "regexp_"+name+".properties";
				regexp = ConfigUtils.getProperties(RegexpPatternUtils.class, regexp_file_name);
				if(BlankUtils.isBlank(regexp)){
					LOG.error("the Properties which named "+regexp_file_name+" is not find on root directory !");
				}
			}else{
				LOG.error("name not match [a-zA-Z0-9_$\u4E00-\u9FA5]");
			}
		}else{
			LOG.error(" name is null !");
		}
		return regexp;
	}

	public static String getRegexp(String fileName,String regexpName) {
		if (BlankUtils.isBlank(fileName) || BlankUtils.isBlank(regexpName)) {
			return null;
		}
		Properties regexpProperties = getRegexpProperties(fileName);
		String regexStr = regexpProperties.getProperty(regexpName);
		if(BlankUtils.isBlank(regexStr)){
			LOG.error("the regex which key named "+regexpName+" is not find in Properties !");
		}
		return regexStr;
	}
	
	public static String getRegexp(String regexpName) {
		if (BlankUtils.isBlank(regexpName)) {
			return null;
		}
		String regexStr = null;
		for (String name : REGEXP_ARR) {
			String regexp_file_name = "regexp_"+name+".properties";
			Properties regexpProperties = ConfigUtils.getProperties(RegexpPatternUtils.class, regexp_file_name);
			if(!BlankUtils.isBlank(regexpProperties)){
				regexStr = regexpProperties.getProperty(regexpName);
				if(!BlankUtils.isBlank(regexStr)){
					break;
				}
			}
		}
		if(BlankUtils.isBlank(regexStr)){
			LOG.error("the regex which key named "+regexpName+" is not find in Properties !");
		}
		return regexStr;
	}
	
}
