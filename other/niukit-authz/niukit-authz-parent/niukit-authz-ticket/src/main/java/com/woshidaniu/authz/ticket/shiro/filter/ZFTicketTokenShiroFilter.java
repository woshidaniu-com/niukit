/**
 * <p>Coyright (R) 2014 我是大牛股份有限公司。<p>
 */
package com.woshidaniu.authz.ticket.shiro.filter;


import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.PathMatchingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.authz.ticket.utils.ResultUtils;
import com.woshidaniu.authz.ticket.utils.TicketTokenUtils;
import com.woshidaniu.web.utils.WebResponseUtils;

/**
 * 
 * @className	： ZFTicketTokenShiroFilter
 * @description	：ticket登录的token生成Filter，配置在Shiro的Filter链中
 * 
 * 根据请求内的xxdm，appid，secret计算得到token，拼装并返回结果json字符串
 * 形如:
 * {
 * 		"status":"1",
 * 		"token":"jW8gUR6gyOA2O2ahXRwPCfpjokFNgtVFU1vC0QII%2BdIFWDclRmKyNK3lYflDMf%2FjxrI%2BOFKgGEdB1%2BP0mqEKpw%3D%3D"
 * }
 * 后续Filter链中的Filter将不再处理请求
 * 
 * @author 		：康康（1571）
 * @date		： 2018年5月2日 上午10:28:37
 * @version 	V1.0
 */
public class ZFTicketTokenShiroFilter extends PathMatchingFilter{
	
	protected static final Logger log = LoggerFactory.getLogger(ZFTicketTokenShiroFilter.class);
	
	private TicketTokenUtils ticketTokenUtils;

	@Override
	protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServeltResponse =  (HttpServletResponse)response;
		this.doCreateToken(httpServletRequest, httpServeltResponse);
		return false;
	}
	
	private void doCreateToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServeltResponse)throws Exception{
		if(log.isDebugEnabled()) {
			log.debug("请求token:{}",httpServletRequest.getRequestURL().toString());
		}
		try {
			// 学校代码
			String xxdm = this.ticketTokenUtils.getXxdm();
			// 业务系统ID
			String appid = httpServletRequest.getParameter("appid");
			
			// 验证appid
			if (!this.ticketTokenUtils.validAppid(xxdm, appid)) {//验证未通过
				// 无效的appid值
				String result = ResultUtils.token("0", "无效的appid值");
				if(log.isDebugEnabled()) {
					log.debug("返回:"+result);
				}
				WebResponseUtils.renderJSON(httpServeltResponse, result);
			}else {//验证通过
				// RSA的私钥，验证方法会通过对应的公钥进行验证
				String secret = httpServletRequest.getParameter("secret");
				//返回token
				String token = ResultUtils.token("1", this.ticketTokenUtils.genToken(appid , secret , xxdm));
				if(log.isDebugEnabled()) {
					log.debug("返回token:"+token);
				}
				WebResponseUtils.renderJSON(httpServeltResponse, token);
			}
		} catch (Exception e) {
			String msg = ResultUtils.token("0", "token值生成失败.");
			WebResponseUtils.renderJSON(httpServeltResponse, msg);
			if(log.isDebugEnabled()) {
				log.debug("返回:"+msg);
			}
			log.error("create token error",e);
		}
	}
	
	public void setTicketTokenUtils(TicketTokenUtils ticketTokenUtils) {
		this.ticketTokenUtils = ticketTokenUtils;
	}
	
}
