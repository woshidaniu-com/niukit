package com.woshidaniu.web.utils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *@类名称	: WebResponseUtils.java
 *@类描述	：Web响应处理工具
 *@创建人	：kangzhidong
 *@创建时间	：Mar 8, 2016 9:19:58 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 */
public abstract class WebResponseUtils {

	protected static Logger LOG = LoggerFactory.getLogger(WebResponseUtils.class);
	// -- Content Type 定义 --//
	public static final String TEXT_TYPE = "text/plain;charset=UTF-8";
	public static final String JSON_TYPE = "text/x-json;charset=UTF-8";
	public static final String XML_TYPE = "text/xml;charset=UTF-8";
	public static final String HTML_TYPE = "text/html;charset=UTF-8";
	// -- 常用数值定义 --//
	public static final long ONE_YEAR_SECONDS = 60 * 60 * 24 * 365;

	/**
	 * 
	 * @description: 直接用语句生成新页面而不走action的转发
	 * @author : kangzhidong
	 * @date 上午11:38:12 2015-1-24
	 * @param response
	 * @param text
	 * @param contentType
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void render(HttpServletResponse response, String text, String contentType) {
		PrintWriter out = null;
		try {
			response.setContentType(contentType);
			out = response.getWriter();
			out.write(text);
			out.flush();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	/**
	 * 
	 * @description: 直接输出纯字符串.
	 * @author : kangzhidong
	 * @date 上午11:38:30 2015-1-24
	 * @param response
	 * @param text
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderText(HttpServletResponse response, String text) {
		render(response, text == null ? "" : text, TEXT_TYPE);
	}

	public static void renderText(HttpServletResponse response, int text) {
		renderText(response, String.valueOf(text));
	}

	/**
	 * 
	 * @description: 直接输出纯HTML.
	 * @author : kangzhidong
	 * @date 上午11:38:38 2015-1-24
	 * @param response
	 * @param text
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderHtml(HttpServletResponse response, String text) {
		render(response, text == null ? "" : text, HTML_TYPE);
	}

	/**
	 * 
	 * @description: 直接输出纯XML.
	 * @author : kangzhidong
	 * @date 上午11:38:45 2015-1-24
	 * @param response
	 * @param text
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderXML(HttpServletResponse response, String text) {
		render(response, text == null ? "" : text, XML_TYPE);
	}

	/**
	 * 
	 * @description: 直接输出纯JSON.
	 * @author : kangzhidong
	 * @date 上午11:38:53 2015-1-24
	 * @param response
	 * @param text
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderJSON(HttpServletResponse response, String text) {
		render(response, text == null ? "" : text, JSON_TYPE);
	}
	
	/**
	 * 
	 * @description: 输出二进制数据
	 * @author : kangzhidong
	 * @date 上午11:39:01 2015-1-24
	 * @param response
	 * @param mimetype
	 * @param content
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderBinary(HttpServletResponse response,
			String mimetype, byte[] content) {
		try {
			response.setContentType(mimetype);
			ServletOutputStream out = response.getOutputStream();
			BufferedOutputStream bo = new BufferedOutputStream(out, 1024);
			bo.write(content);
			bo.flush();
			bo.close();
			out.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}
	
	/**
	 * 
	 * @description: 输出二进制数据
	 * @author : kangzhidong
	 * @date 上午11:39:08 2015-1-24
	 * @param response
	 * @param mimetype
	 * @param source
	 * @return void 返回类型
	 * @throws
	 * @modify by:
	 * @modify date :
	 * @modify description : 
	 */
	public static void renderBinary(HttpServletResponse response,String mimetype, InputStream source) {
		try {
			response.setContentType(mimetype);
			ServletOutputStream out = response.getOutputStream();
			for (byte buffer[] = new byte[1024]; source.read(buffer) > 0; out.flush()){
				out.write(buffer);
			}
			out.close();
			source.close();
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @description: 设置客户端缓存过期时间 的Header.
	 * @author : kangzhidong
	 * @date 上午11:50:33 2015-1-24 
	 * @param response
	 * @param expiresSeconds
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void setExpiresHeader(HttpServletResponse response,long expiresSeconds) {
		// Http 1.0 header
		response.setDateHeader("Expires", System.currentTimeMillis() + expiresSeconds * 1000);
		// Http 1.1 header
		response.setHeader("Cache-Control", "private, max-age=" + expiresSeconds);
	}

	/**
	 * 
	 * @description: 设置禁止客户端缓存的Header.
	 * @author : kangzhidong
	 * @date 上午11:50:15 2015-1-24 
	 * @param response
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void setDisableCacheHeader(HttpServletResponse response) {
		// Http 1.0 header
		response.setDateHeader("Expires", 1L);
		response.addHeader("Pragma", "no-cache");
		// Http 1.1 header
		response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
	}

	/**
	 * 
	 * @description: 设置LastModified Header.
	 * @author : kangzhidong
	 * @date 上午11:50:06 2015-1-24 
	 * @param response
	 * @param lastModifiedDate
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void setLastModifiedHeader(HttpServletResponse response, long lastModifiedDate) {
		response.setDateHeader("Last-Modified", lastModifiedDate);
	}

	/**
	 * 
	 * @description:设置Etag Header.
	 * @author : kangzhidong
	 * @date 上午11:49:50 2015-1-24 
	 * @param response
	 * @param etag
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void setEtag(HttpServletResponse response, String etag) {
		response.setHeader("ETag", etag);
	}

	/**
	 * 
	 * @description: 根据浏览器If-Modified-Since Header, 计算文件是否已被修改.
	 * 				  如果无修改, checkIfModify返回false ,设置304 not modify status.
	 * @author : kangzhidong
	 * @date 上午11:46:14 2015-1-24 
	 * @param request
	 * @param response
	 * @param lastModified 内容的最后修改时间.
	 * @return
	 * @return  boolean 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static boolean checkIfModifiedSince(HttpServletRequest request,HttpServletResponse response, long lastModified) {
		long ifModifiedSince = request.getDateHeader("If-Modified-Since");
		if ((ifModifiedSince != -1) && (lastModified < ifModifiedSince + 1000)) {
			response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @description: 
	 * 根据浏览器 If-None-Match Header, 计算Etag是否已无效.
	 * 如果Etag有效, checkIfNoneMatch返回false, 设置304 not modify status.
	 * @author : kangzhidong
	 * @date 上午11:46:55 2015-1-24 
	 * @param request
	 * @param response
	 * @param etag	内容的ETag.
	 * @return  
	 * @return  boolean 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static boolean checkIfNoneMatchEtag(HttpServletRequest request,HttpServletResponse response, String etag) {
		String headerValue = request.getHeader("If-None-Match");
		if (headerValue != null) {
			boolean conditionSatisfied = false;
			if (!"*".equals(headerValue)) {
				StringTokenizer commaTokenizer = new StringTokenizer(headerValue, ",");
				while (!conditionSatisfied && commaTokenizer.hasMoreTokens()) {
					String currentToken = commaTokenizer.nextToken();
					if (currentToken.trim().equals(etag)) {
						conditionSatisfied = true;
					}
				}
			} else {
				conditionSatisfied = true;
			}
			if (conditionSatisfied) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				response.setHeader("ETag", etag);
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @description:设置让浏览器弹出下载对话框的Header.
	 * @author : kangzhidong
	 * @date 上午11:49:22 2015-1-24 
	 * @param response
	 * @param fileName	  下载后的文件名.
	 * @return  void 返回类型
	 * @throws  
	 * @modify by:
	 * @modify date :
	 * @modify description : TODO(描述修改内容)
	 */
	public static void setContentDisposition(HttpServletRequest request,HttpServletResponse response, boolean attachment,String fileName) {
		try {
			response.setHeader("Content-Disposition", getContentDisposition(request, attachment, fileName ) );
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	/**
	 * 
	 * @description: 关于下载中文文件名的问题，不同浏览器需要使用不同的编码，下载前要在Java中进行文件名编码
	 *  			 在多数浏览器中使用 UTF8 ，而在 firefox 和 safari 中使用 ISO8859-1 。
	 *  			 经测试在 IE、Firefox、Chorme、Safari、Opera 上都能正常显示中文文件名（只测试了较新的浏览器）
	 * @author : kangzhidong
	 * @date 下午07:03:10 2015-4-25 
	 * @param req
	 * @param name
	 * @return
	 * @throws UnsupportedEncodingException
	 * @return  String 返回类型
	 * @throws  
	 */
	public static String getEncodeFileName(HttpServletRequest request, String name) throws UnsupportedEncodingException {
		String agent = request.getHeader("USER-AGENT").toLowerCase();
		if (agent != null && agent.indexOf("firefox") < 0 && agent.indexOf("safari") < 0) {
			return URLEncoder.encode(name, "UTF8");
		}
		return new String(name.getBytes("UTF-8"), "ISO8859-1");
	}
	
	/**
	 * 方法用途和描述: 获取内容描述
	 * @param name
	 * @return
	 * <br>attachment：附件
	 * <br>inline：内联（意指打开时在浏览器中打开）
	 * @throws UnsupportedEncodingException 
	 */
	public static String getContentDisposition(HttpServletRequest request,boolean attachment,String fileName) throws UnsupportedEncodingException {
		return (new StringBuilder((attachment?"attachment":"inline")+";filename=\"").append(getEncodeFileName(request,fileName)).append("\"")).toString();
	}
	
	/**
	 * 方法用途和描述: 获取内容描述
	 * @param name
	 * @return
	 * <br>attachment：附件
	 * <br>inline：内联（意指打开时在浏览器中打开）
	 * @throws UnsupportedEncodingException 
	 */
	public static String getContentDisposition(boolean attachment,String fileName) throws UnsupportedEncodingException {
		return (new StringBuilder((attachment?"attachment":"inline")+";filename=\"").append(new String(fileName.getBytes("UTF-8"),"ISO8859-1").toString()).append("\"")).toString();
	}
}
