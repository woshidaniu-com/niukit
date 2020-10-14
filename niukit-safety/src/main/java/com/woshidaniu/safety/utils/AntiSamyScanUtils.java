package com.woshidaniu.safety.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringEscapeUtils;
import org.owasp.validator.html.AntiSamy;
import org.owasp.validator.html.CleanResults;
import org.owasp.validator.html.Policy;
import org.owasp.validator.html.PolicyException;
import org.owasp.validator.html.ScanException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.URLUtils;
import com.woshidaniu.safety.xss.factory.AntiSamyProxy;

/**
 * 
 *@类名称	: AntiSamyScanUtils.java
 *@类描述	： XSS扫描过滤工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 30, 2016 2:15:42 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class AntiSamyScanUtils {
	
	protected static Logger LOG = LoggerFactory.getLogger(AntiSamyScanUtils.class);
	protected static Pattern HTML_PATTERN = Pattern.compile("<[^>]+>");
	protected static ConcurrentMap<String, String> COMPLIED_FIXS = new ConcurrentHashMap<String, String>();
	
	protected static String fix(AntiSamyProxy proxy, String tag, String esc) throws ScanException, PolicyException {
		// AntiSamy对象
		AntiSamy antiSamy = proxy.getAntiSamy();
		//Policy策略对象
		Policy policy = proxy.getPolicy();
		// 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现
		int scanType = proxy.getScanType();
		String ret = COMPLIED_FIXS.get(tag);
		if( ret != null){
			return ret;
		}
		ret =  ( policy != null ? antiSamy.scan(esc, policy, scanType) : antiSamy.scan(esc, scanType)).getCleanHTML();
		String existing = COMPLIED_FIXS.putIfAbsent(tag, ret);
		if (existing != null) {
			ret = existing;
		}
		return ret;
	}
	
	public static String xssClean(AntiSamyProxy proxy,String taintedHTML,boolean cleanbad) {
		if (!BlankUtils.isBlank(proxy) && taintedHTML != null) {
			
			try {
				// 进行解码：防止攻击者，采用转码方式进行注入
				if(URLUtils.isURLEncoder(taintedHTML)){
					taintedHTML = URLUtils.unescape(taintedHTML);
				}
			} catch (Exception e1) {
				LOG.error(e1.getLocalizedMessage());
			}
			
			try {
				// AntiSamy对象
				AntiSamy antiSamy = proxy.getAntiSamy();
				//Policy策略对象
				Policy policy = proxy.getPolicy();
				// 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现
				int scanType = proxy.getScanType();
				LOG.trace("TaintedHTML :" + taintedHTML);
				//XSS扫描
				CleanResults cr = policy != null ?  antiSamy.scan(taintedHTML, policy, scanType) : antiSamy.scan(taintedHTML, scanType) ;
				String cleanHTML = cr.getCleanHTML();
				LOG.trace("XSS CleanHTML :" + cleanHTML);
				cleanHTML = StringEscapeUtils.unescapeHtml4(cleanHTML);
				if( HTML_PATTERN.matcher(taintedHTML).find() && cleanbad ){
					//安全的HTML输出
					LOG.debug("UNEscape Html4 :" + cleanHTML);
					//解决“&nbsp;”转换成乱码问题
					cleanHTML = cleanHTML.replace(fix(proxy, "nbsp", "&nbsp;"), "&nbsp;" );
					//解决“&ensp;”转换成乱码问题
					cleanHTML = cleanHTML.replace(fix(proxy, "ensp", "&ensp;"), "&ensp;" );
				    LOG.debug("Fixed CleanHTML :" + cleanHTML);
				}
			    return cleanHTML;
			} catch (ScanException e) {
				LOG.error("XSS Scan Exception:" + e.getLocalizedMessage());
			} catch (PolicyException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		return taintedHTML;
	}
	
	public static String xssClean(HttpServletRequest request, AntiSamyProxy proxy, String taintedHTML) {
		String cleanbadStr = request.getParameter("cleanbad");
		boolean cleanbad = Boolean.parseBoolean((cleanbadStr != null && cleanbadStr.trim().length() > 0) ? cleanbadStr.trim() : "true" ) ;
		return xssClean(proxy, taintedHTML, cleanbad);
	}
	
	public static String xssClean(AntiSamyProxy proxy,String taintedHTML) {
		if (!BlankUtils.isBlank(proxy) && taintedHTML != null) {
			try {
				// AntiSamy对象
				AntiSamy antiSamy = proxy.getAntiSamy();
				//Policy策略对象
				Policy policy = proxy.getPolicy();
				// 扫描器类型，0：DOM类型扫描器,1:SAX类型扫描器；两者的区别如同XML解析中DOM解析与Sax解析区别相同，实际上就是对两种解析方式的实现
				int scanType = proxy.getScanType();
				LOG.debug("TaintedHTML :" + taintedHTML);
				//XSS扫描
				CleanResults cr = policy != null ?  antiSamy.scan(taintedHTML, policy, scanType) : antiSamy.scan(taintedHTML, scanType) ;
				String cleanHTML = cr.getCleanHTML();
				LOG.debug("XSS CleanHTML :" + cleanHTML);
			    return cleanHTML;
			} catch (ScanException e) {
				LOG.error("XSS Scan Exception:" + e.getLocalizedMessage());
			} catch (PolicyException e) {
				LOG.error(e.getLocalizedMessage());
			}
		}
		return taintedHTML;
	}

}
