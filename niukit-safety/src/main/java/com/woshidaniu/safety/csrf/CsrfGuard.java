/**
 * 
 */
package com.woshidaniu.safety.csrf;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.safety.csrf.strategy.CsrfVerifyStrategy;
import com.woshidaniu.web.utils.WebUtils;

/**
 * <p>
 *   <h3>niutal框架<h3>
 *   说明：class com.woshidaniu.web.filter.security.csrf.CsrfGuard
 * <p>
 * @author <a href="#">Zhangxiaobin[1036]<a>
 * @version 2016年8月5日下午7:44:55
 */
public final class CsrfGuard {

	private static final Logger log = LoggerFactory.getLogger(CsrfGuard.class);
	
	public static final String SESSION_CSRF_TOKEN_KEY = "CSRF_HTTP_SESSION_TOKEN";
	
	public static final String SESSION_CSRF_TOKEN_DEFAULT_VALUE = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
	
	public static final String CSRF_TOKEN_NAME = "niutal_CSRF_TOKEN";
	
	public static final String X_REQUEST_TOKEN_NAME = "niutal_CSRF_X_REQUEST_TOKEN";
	
	public static final int MODE_POST = 1;
	
	public static final int MODE_ALL = 0;
	
	private static final String TEMPLATE_NAME = "csrfguard.template";
	
	private CsrfGuard(){}
	
	private RandomTokenGenerator tokenGenerator = new RandomTokenGenerator();
	
	private String templateCodeCache = null;
	
	public static CsrfGuard getInstance(){
		return SingleInstanceHolder.instance;
	}
	
	/**
	 * 
	 * <p>方法说明：获取javascript模板<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月9日上午8:27:30<p>
	 */
	public synchronized String getJavascriptTemplateCode(){
		if(this.templateCodeCache != null){
			return templateCodeCache;
		}
		try{
			this.templateCodeCache = getJavascriptTemplateCodeOldStyle();
		}catch (Exception e) {
			log.error("load javascriptTemplateCode["+ TEMPLATE_NAME +"] error,cause:"+e.getMessage()+",try new style to load");
			this.templateCodeCache = getJavascriptTemplateCodeNewStyle();
			if(this.templateCodeCache != null){
				log.info("load javascriptTemplateCode["+ TEMPLATE_NAME +"] success!");
			}
		}
		return this.templateCodeCache;
	}
	
	public String getJavascriptTemplateCodeOldStyle() throws Exception{
		String file = CsrfGuard.class.getResource(TEMPLATE_NAME).getFile();
		FileReader fr = null;
		StringBuffer template = new StringBuffer();
		char[] buf = new char[1024];
		try {
			int ch = 0;
			fr = new FileReader(file);
			while((ch = fr.read(buf)) != -1){
				template.append(buf , 0 , ch);
			}
			return template.toString();
		} catch (Exception e) {
			throw e;
		}finally{
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e) {
					log.info("关闭文件{}流出现异常,cause:"+e.getMessage(),TEMPLATE_NAME, e);
				}
			}
		}
	}
	
	public String getJavascriptTemplateCodeNewStyle(){
		String resourcePath = "com/woshidaniu/safety/csrf/"+TEMPLATE_NAME;
		URL url = this.getClass().getClassLoader().getResource(resourcePath);
		
		//try again
		if(url == null) {
			url = this.getClass().getClassLoader().getResource("/"+resourcePath);
		}
		Scanner scanner = null;
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(resourcePath);
			log.debug(String.format("load resource path :%s",url));
			
			StringBuilder stringBuilder = new StringBuilder();
			
			scanner = new Scanner(inputStream);
			while(scanner.hasNextLine()){
				String line = scanner.nextLine();
				stringBuilder.append(line);
			}
			String result = stringBuilder.toString();
			return result;
		}finally {
			if(scanner != null){
				scanner.close();
			}
		}
	}
	/**
	 * 
	 * <p>方法说明：更新session csrf token<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月5日下午7:52:06<p>
	 */
	public void updateTokens(HttpSession session){
		String generate = tokenGenerator.generate();
		if(log.isDebugEnabled()){
			log.debug("CSRF TOKEN 被创建：{} = {}", new Object[]{SESSION_CSRF_TOKEN_KEY, generate});
		}
		session.setAttribute(SESSION_CSRF_TOKEN_KEY, generate);
	}
	
	/**
	 * 
	 * <p>方法说明：获取token<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月18日下午4:27:34<p>
	 */
	public String getToken(HttpSession session){
		if(session != null){
			return (String) session.getAttribute(SESSION_CSRF_TOKEN_KEY);
		}
		return null;
	}
	
	/**
	 * 
	 * <p>方法说明：验证csrf token<p>
	 * <p>作者：a href="#">Zhangxiaobin[1036]<a><p>
	 * <p>时间：2016年8月9日上午8:27:48<p>
	 */
	public boolean verifyCsrfToken(HttpServletRequest request, CsrfVerifyStrategy strategy){
		HttpSession session = request.getSession();
		if(session == null)
			return false;
		
		if(!strategy.needVerify(request)){	
			return true;
		}
		
		if(WebUtils.isAjaxRequest(request)){
			return verifyAjaxToken(request);
		}
		return verifySessionToken(request);
	}
	
	
	private boolean verifyAjaxToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String tokenFromSession = (String) session.getAttribute(SESSION_CSRF_TOKEN_KEY);
		String tokenFromRequest = request.getHeader(CSRF_TOKEN_NAME);

		if(log.isDebugEnabled()){
			log.debug("开始验证AJAX请求的CSRF TOKEN, SESSION TOKEN VALUE = {}, REQUEST HEADER TOKEN VALUE = {}.", new Object[]{tokenFromSession, tokenFromRequest});
		}
		
		if (tokenFromRequest == null) {
			/** FAIL: token is missing from the request **/
			return false;
		} else {
			//if there are two headers, then the result is comma separated
			if (!tokenFromSession.equals(tokenFromRequest)) {
				if (tokenFromRequest.contains(",")) {
					tokenFromRequest = tokenFromRequest.substring(0, tokenFromRequest.indexOf(',')).trim();
				}
				if (!tokenFromSession.equals(tokenFromRequest)) {
					/** FAIL: the request token does not match the session token **/
					return false;
				}
			}
		}
		return true;
	}
	
	
	private boolean verifySessionToken(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		String tokenFromSession = (String) session.getAttribute(SESSION_CSRF_TOKEN_KEY);
		String tokenFromRequest = request.getParameter(CSRF_TOKEN_NAME);

		if(log.isDebugEnabled()){
			log.debug("开始验证一般请求的CSRF TOKEN, SESSION TOKEN VALUE = {}, REQUEST HEADER TOKEN VALUE = {}.", new Object[]{tokenFromSession, tokenFromRequest});
		}
		
		if (tokenFromRequest == null) {
			/** FAIL: token is missing from the request **/
			return false;
		} else if (!tokenFromSession.equals(tokenFromRequest)) {
			/** FAIL: the request token does not match the session token **/
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * <p>
	 *   说明：单例持有
	 * <p>
	 */
	private static class SingleInstanceHolder{public final static  CsrfGuard instance = new CsrfGuard();}
	
	/**
	 * 
	 * <p>
	 *   说明：生成csrf token
	 *   <p>
	 */
	class RandomTokenGenerator{
		
		char[] CHARSET = new char[] { 'A', 'B', 'C', 'D', 'E',
			'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
			'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e',
			'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
			's', 't', 'u', 'v', 'w', 'x', 'y', 'z','0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9' };
		
		int LEN = 64;
		
		String generate(){
			return RandomStringUtils.random(LEN, CHARSET);
		}
	}
}
