/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.captcha;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.woshidaniu.web.captcha.factory.FastCaptchaFactory;
import com.woshidaniu.web.captcha.impl.DefaultFastCaptcha;

/**
 * 
 *@类名称 : FastCaptchaServlet.java
 *@类描述 ：
 *@创建人 ：kangzhidong
 *@创建时间 ：Mar 10, 2016 10:24:14 AM
 *@修改人 ：
 *@修改时间 ：
 *@版本号 :v1.0
 */
@SuppressWarnings("serial")
public class CaptchaHttpServlet extends HttpServlet {

	protected String captchaType = "default";
	protected FastCaptchaFactory FACTORY = FastCaptchaFactory.getInstance();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		FACTORY.register("default", new DefaultFastCaptcha());
		
		//获取指定的验证码实现
		captchaType = config.getInitParameter("captcha");
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		
		
		FACTORY.getCaptcha(captchaType).captcha(request, response);
		
		
		
	}

	@Override
	protected void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doHead(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doOptions(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	@Override
	protected void doTrace(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

	}

	public void destroy() {
	}

}
