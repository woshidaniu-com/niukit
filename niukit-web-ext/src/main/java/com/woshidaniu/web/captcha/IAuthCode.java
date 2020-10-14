package com.woshidaniu.web.captcha;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 策略模式接口
 * @author 438523432@qq.com(740)
 */
public interface IAuthCode {
    /**
     * 生成验证码
     */
    public void code(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * 生成验证码到session,session中codeName自定义
     * @param codeName
     * @param request
     * @param response
     * @return
     */
    public String code(String codeName,HttpServletRequest request, HttpServletResponse response);
}
