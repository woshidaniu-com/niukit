 package com.woshidaniu.httpclient.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * 
 *@类名称	: BrowseAssertUtils.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 11:21:53 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class BrowseAssertUtils {
	
	public boolean isWechatBrowse(HttpServletRequest request){
		String user_agent = request.getHeader("user-agent");
		if( user_agent == null ){
			return false;
		}else{
			String ua =  user_agent.toLowerCase();
			if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
				return  true;
		    }
		}
		return false;
	}
	
	public static String getBrowser(String userAgent) {
        if(userAgent==null||userAgent.trim().length()<1){
            return "unknow ";
        }
        String[]brorserEN=new String[]{
                "MyIE2",
                "Firefox",
                "KuGooSoft",
                "LBBROWSER",
                "TheWord",
                "QQ",
                "Maxthon",
                "BIDUPlayerBrowser",
                "Opera",
                "Chrome",
                "Safari",
                "9A334",
                "UCWEB",
                "googlebot",
                "rv 11.0"};
        String[]brorserCN=new String[]{
                "MyIE2",
                "Firefox",
                "酷狗",
                "猎豹",
                "世界之窗",
                "QQ",
                "Maxthon",
                "百度影音",
                "Opera",
                "Chrome",
                "Safari",
                "360",
                "UCWEB",
                "googlebot",
                "IE 11.0"};
        for(int i=0;i<brorserEN.length;i++){
            if (userAgent.indexOf(brorserEN[i]) > -1) {
                return brorserCN[i];
            }
        }
        if (userAgent.indexOf("MSIE") > -1) {
            if(userAgent.indexOf("MSIE 9.0")>-1){
                return "IE 9.0";
            }else if(userAgent.indexOf("MSIE 10.0")>-1){
                return "IE 10.0";
            }else if(userAgent.indexOf("MSIE 8.0")>-1){
                return "IE 8.0";
            }else if(userAgent.indexOf("MSIE 7.0")>-1){
                return "IE 7.0";
            }else if(userAgent.indexOf("MSIE 6.0")>-1){
                return "IE 6.0";
            }
            return "IE";
        }
        return "unknow Browser";
    }
}

 
