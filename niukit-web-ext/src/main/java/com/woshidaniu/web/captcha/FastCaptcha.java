/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.web.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *@类名称	: FastCaptcha.java
 *@类描述	：
 *@创建人	：kangzhidong
 *@创建时间	：Mar 10, 2016 9:54:54 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public interface FastCaptcha {
	
  /**
   * 
   *@描述		：生成验证码
   *@创建人	: kangzhidong
   *@创建时间	: Mar 10, 20169:57:18 AM
   *@param request
   *@param response
   *@修改人	: 
   *@修改时间	: 
   *@修改描述	:
   */
    public void captcha(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 
     *@描述		：生成验证码到session,session中codeName自定义
     *@创建人	: kangzhidong
     *@创建时间	: Mar 10, 20169:57:11 AM
     *@param codeName
     *@param request
     *@param response
     *@return
     *@修改人	: 
     *@修改时间	: 
     *@修改描述	:
     */
    public String captcha(String codeName,HttpServletRequest request, HttpServletResponse response);

}
