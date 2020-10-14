
package com.fastkit.xmlresolver.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

 /**
 * @package com.fastkit.imexport.utils
 * @className: TimeUtils
 * @description: TODO
 * @author : kangzhidong
 * @date : 2014-1-11
 * @time : 下午3:34:38 
 */

public class TimeUtils {

	private static Map<String,Integer> powers = new HashMap<String, Integer>();
	private static final Pattern regex = Pattern.compile("^([1-9][0-9]*\\.?[0-9]*)\\s*([smhd]{1})$");
	static{
		//(s:秒钟,m:分钟,h:小时,d:天)
		powers.put("s", 1000);//一秒
		powers.put("m", 60000);//一分钟
		powers.put("h", 360000);//一小时
		powers.put("d", 8640000);//一天
		
	}
	
	public static Integer timeParse(String value) {
		if (value == null){
			return -1;
		}
        Matcher matcher = regex.matcher(value.toLowerCase());
		if(matcher.matches()){
			if(matcher.group(1)!=null&&matcher.group(2)!=null){
				Integer num = Integer.valueOf(matcher.group(1));
				Integer mult = powers.get(matcher.group(2));
				return num * mult;
			}
		}
		return 0;
	};
	
	public static void main(String[] args) {
		System.out.println(TimeUtils.timeParse("5m"));;
	}
	
}



