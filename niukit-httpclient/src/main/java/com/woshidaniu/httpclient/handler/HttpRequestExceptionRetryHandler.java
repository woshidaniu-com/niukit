 package com.woshidaniu.httpclient.handler;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpRequest;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.protocol.HttpContext;

import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.httpclient.utils.HttpConfigUtils;

/**
 * 
 *@类名称	: HttpRequestExceptionRetryHandler.java
 *@类描述	： 异常处理
 * <pre>
 *	HttpClient会被抛出两种类型的异常，一种是java.io.IOException，当遇到I/O异常时抛出（socket超时，或者socket被重置）;
 *	另一种是HttpException,表示Http失败，如Http协议使用不正确。通常认为，I/O错误时不致命、可修复的，而Http协议错误是致命了，不能自动修复的错误。
 *	1.4.1.HTTP传输安全
 *	
 *	Http协议不能满足所有类型的应用场景，我们需要知道这点。Http是个简单的面向协议的请求/响应的协议，当初它被设计用来支持静态或者动态生成的内容检索，
 *	之前从来没有人想过让它支持事务性操作。例如，Http服务器成功接收、处理请求后，生成响应消息，并且把状态码发送给客户端，这个过程是Http协议应该保证的。
 *	但是，如果客户端由于读取超时、取消请求或者系统崩溃导致接收响应失败，服务器不会回滚这一事务。如果客户端重新发送这个请求，服务器就会重复的解析、执行这个事务。
 *	在一些情况下，这会导致应用程序的数据损坏和应用程序的状态不一致。
 *
 *	即使Http当初设计是不支持事务操作，但是它仍旧可以作为传输协议为某些关键程序提供服务。
 *	为了保证Http传输层的安全性，系统必须保证应用层上的http方法的幂等性
 *	（To ensure HTTP transport layer safety the system must ensure the idempotency of HTTP methods on the application layer）。
 *	
 *	1.4.2.方法的幂等性
 *	
 *	HTTP/1.1规范中是这样定义幂等方法的，
 *	Methods can also have the property of “idempotence” in that (aside from error or expiration issues) the side-effects of N > 0 identical requests is the same as for a single request。
 *	用其他话来说，应用程序需要正确地处理同一方法多次执行造成的影响。添加一个具有唯一性的id就能避免重复执行同一个逻辑请求，问题解决。
 *	请知晓，这个问题不只是HttpClient才会有，基于浏览器的应用程序也会遇到Http方法不幂等的问题。
 *	HttpClient默认把非实体方法get、head方法看做幂等方法，把实体方法post、put方法看做非幂等方法。
 *	
 *	1.4.3.异常自动修复
 *	
 *	默认情况下，HttpClient会尝试自动修复I/O异常。这种自动修复仅限于修复几个公认安全的异常。
 *	HttpClient不会尝试修复任何逻辑或者http协议错误（即从HttpException衍生出来的异常）。
 *	HttpClient会自动再次发送幂等的方法（如果首次执行失败。
 *	HttpClient会自动再次发送遇到transport异常的方法，前提是Http请求仍旧保持着连接（例如http请求没有全部发送给目标服务器，HttpClient会再次尝试发送）。
 *	1.4.4.请求重试HANDLER
 *	
 *	如果要自定义异常处理机制，我们需要实现HttpRequestRetryHandler接口。
 * </pre>
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 3:10:08 PM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public class HttpRequestExceptionRetryHandler implements HttpRequestRetryHandler {
	
	
	//保持连接池内的长连接时长，单位毫秒，默认30秒
	private static long retryTime =  3;
	
	public HttpRequestExceptionRetryHandler(){
		try {
			retryTime = StringUtils.getSafeLong(HttpConfigUtils.getText("http.connection.retryTime"), "3");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean retryRequest(IOException exception, int executionCount,HttpContext context) {
        if (executionCount >= retryTime) {
            // 如果已经重试了指定次数，就放弃
            return false;
        }
        if (exception instanceof NoHttpResponseException) {
            // 如果服务器丢掉了连接，那么就重试
            return true;
        }
        if (exception instanceof InterruptedIOException) {
            // 超时
            return false;
        }
        if (exception instanceof UnknownHostException) {
            // 目标服务器不可达
            return false;
        }
        if (exception instanceof ConnectTimeoutException) {
            // 连接被拒绝
            return false;
        }
        if (exception instanceof SSLException) {
        	// 不要重试SSL握手异常
            return false;
        }
        HttpClientContext clientContext = HttpClientContext.adapt(context);
        HttpRequest request = clientContext.getRequest();
        boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
        if (idempotent) {
            // 如果请求是幂等的，就再次尝试
            return true;
        }
        return false;

	}

}

 
