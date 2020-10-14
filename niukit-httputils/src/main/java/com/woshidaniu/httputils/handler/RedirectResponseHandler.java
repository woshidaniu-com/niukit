/**
 * <p>Coyright (R) 2014 我是大牛软件股份有限公司。<p>
 */
package com.woshidaniu.httputils.handler;

import java.io.IOException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.StatusLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.httputils.exception.HttpResponseException;

/**
 * 
 * @类名称 : RedirectResponseHandler.java
 * @类描述 ：在JSP/Servlet编程中response.sendRedirect方法就是使用HTTP协议中的重定向机制。
 *      它与JSP中的<jsp:forward …>的区别在于后者是在服务器中实现页面的跳转，也就是说应用容器加载了所要跳转的页面的内容并返回给客户端；
 *      而前者是返回一个状态码，这些状态码的可能值见下表，然后客户端读取需要跳转到的页面的URL并重新加载新的页面。
 *      就是这样一个过程，所以我们编程的时候就要通过HttpMethod
 *      .getStatusCode()方法判断返回值是否为下表中的某个值来判断是否需要跳转。
 *      如果已经确认需要进行页面跳转了，那么可以通过读取HTTP头中的location属性来获取新的地址。
 *      <table cellspacing="0" cellpadding="0" border="1">
		<tbody>
			<tr>
				<td style="width: 67px">
					<p>状态码</p>
				</td>
				<td style="width: 216px">
					<p>对应&nbsp;HttpServletResponse&nbsp;的常量</p>
				</td>
				<td style="width: 261px">
					<p>详细描述</p>
				</td>
			</tr>
			<tr>
				<td style="width: 67px">
					<p>301</p>
				</td>
				<td style="width: 216px">
					<p>SC_MOVED_PERMANENTLY</p>
				</td>
				<td style="width: 261px">
					<p>页面已经永久移到另外一个新地址</p>
				</td>
			</tr>
			<tr>
				<td style="width: 67px">
					<p>302</p>
				</td>
				<td style="width: 216px">
					<p>SC_MOVED_TEMPORARILY</p>
				</td>
				<td style="width: 261px">
					<p>页面暂时移动到另外一个新的地址</p>
				</td>
			</tr>
			<tr>
				<td style="width: 67px">
					<p>303</p>
				</td>
				<td style="width: 216px">
					<p>SC_SEE_OTHER</p>
					<p>&nbsp;</p>
				</td>
				<td style="width: 261px">
					<p>客户端请求的地址必须通过另外的&nbsp;URL&nbsp;来访问</p>
				</td>
			</tr>
			<tr>
				<td style="width: 67px">
					<p>307</p>
				</td>
				<td style="width: 216px">
					<p>SC_TEMPORARY_REDIRECT</p>
				</td>
				<td style="width: 261px">
					<p>同&nbsp;SC_MOVED_TEMPORARILY</p>
					<p>&nbsp;</p>
				</td>
			</tr>
		</tbody>
	</table>
 * 
 * @创建人 ：kangzhidong
 * @创建时间 ：2016年4月27日 下午12:20:24
 * @修改人 ：
 * @修改时间 ：
 * @版本号 :v1.0
 */
public class RedirectResponseHandler implements ResponseHandler<String> {
	
	protected static Logger LOG = LoggerFactory.getLogger(RedirectResponseHandler.class);
	
	@Override
	public void handleClient(HttpClient httpclient) {
		
	}

	@Override
	public String handleResponse(HttpMethodBase httpMethod) throws IOException {
		StatusLine statusLine = httpMethod.getStatusLine();
		// 检查是否重定向 
		int statuscode = statusLine.getStatusCode();
		if ((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) 
				|| (statuscode == HttpStatus.SC_MOVED_PERMANENTLY) || (statuscode ==HttpStatus.SC_SEE_OTHER) 
				|| (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT) ) {  
			// 读取新的 URL 地址 
			Header header = httpMethod.getResponseHeader("location");  
			if (header != null ){  
				 String redirectURI = header.getValue();  
				 if((redirectURI==null)||(redirectURI.equals(""))) { 
					 redirectURI="/";  
				 }
				 LOG.debug("Redirect:"+ redirectURI);
				 return redirectURI;
			}else {
				throw new HttpResponseException("Invalid redirect .");
			}
		} else {
			throw new HttpResponseException(statusLine.getStatusCode(),statusLine.getReasonPhrase());
		}
	}
}