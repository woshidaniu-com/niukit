//package com.woshidaniu.fastpdf.struts2.filter;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.UUID;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletContext;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.struts2.ServletActionContext;
//
//import com.opensymphony.xwork2.util.ValueStack;
//
//public class PDFDocumentCacheFilter implements Filter {
//
//	public void doFilter(ServletRequest req, ServletResponse res,
//			FilterChain chain) throws IOException, ServletException {
//		HttpServletRequest request = (HttpServletRequest) req;
//		HttpServletResponse response = (HttpServletResponse) res;
//		ServletContext sc = request.getSession().getServletContext();
//		// 根据ID得到要导出文档的相关信息
//		String documentID = request.getParameter("documentID");
//		ItextXMLElement element = ItextContext.getElement(documentID);
//		if ("html".equalsIgnoreCase(element.attr("model"))) {
//			ValueStack stack = ServletActionContext.getValueStack(request);
//			String uuid = UUID.randomUUID().toString();
//			stack.set("uuid", uuid);
//			stack.set("documentID", documentID);
//			File tempDir = (File) sc.getAttribute("javax.servlet.context.tempdir");
//			// get possible cache
//			String temp = tempDir.getAbsolutePath();
//			File file = new File( temp + File.separator + uuid + ".html");
//			try {
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				CacheResponseWrapper wrappedResponse = new CacheResponseWrapper(response, baos);
//				chain.doFilter(req, wrappedResponse);
//				FileOutputStream fos = new FileOutputStream(file);
//				fos.write(baos.toByteArray());
//				fos.flush();
//				fos.close();
//			} catch (ServletException e) {
//				if (!file.exists()) {
//					throw new ServletException(e);
//				}
//			} catch (IOException e) {
//				if (!file.exists()) {
//					throw e;
//				}
//			}
//		} else {
//			chain.doFilter(request, response);
//		}
//	}
//
//	public void init(FilterConfig filterConfig) {
//	}
//
//	public void destroy() {
//	}
//}
