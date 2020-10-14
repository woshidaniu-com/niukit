package com.woshidaniu.shiro.filter.mgt;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.web.util.WebUtils;

/**
 * hotfix 1.2.6到最新版本之间的所有安全问题
 * 
 * 后续若出现类似问题，找到最新shiro源码，把getPathWithinApplication(HttpServletRequest request)函数和所有调用函数拷贝到这里即可!!!
 *  
 * @author 1571
 */
class SafeInnerWebUtils {

	public static String getPathWithinApplication(HttpServletRequest request) {
		return normalize(removeSemicolon(getServletPath(request) + getPathInfo(request)));
	}
	
	private static String normalize(String path) {
        return normalize(path, true);
    }
    
    private static String normalize(String path, boolean replaceBackSlash) {

        if (path == null)
            return null;

        // Create a place for the normalized path
        String normalized = path;

        if (replaceBackSlash && normalized.indexOf('\\') >= 0)
            normalized = normalized.replace('\\', '/');

        if (normalized.equals("/."))
            return "/";

        // Add a leading "/" if necessary
        if (!normalized.startsWith("/"))
            normalized = "/" + normalized;

        // Resolve occurrences of "//" in the normalized path
        while (true) {
            int index = normalized.indexOf("//");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 1);
        }

        // Resolve occurrences of "/./" in the normalized path
        while (true) {
            int index = normalized.indexOf("/./");
            if (index < 0)
                break;
            normalized = normalized.substring(0, index) +
                    normalized.substring(index + 2);
        }

        // Resolve occurrences of "/../" in the normalized path
        while (true) {
            int index = normalized.indexOf("/../");
            if (index < 0)
                break;
            if (index == 0)
                return (null);  // Trying to go outside our context
            int index2 = normalized.lastIndexOf('/', index - 1);
            normalized = normalized.substring(0, index2) +
                    normalized.substring(index + 3);
        }

        // Return the normalized path that we have completed
        return (normalized);

    }
	
    private static String getServletPath(HttpServletRequest request) {
        String servletPath = (String) request.getAttribute(WebUtils.INCLUDE_SERVLET_PATH_ATTRIBUTE);
        return servletPath != null ? servletPath : valueOrEmpty(request.getServletPath());
    }
    
    private static String removeSemicolon(String uri) {
        int semicolonIndex = uri.indexOf(';');
        return (semicolonIndex != -1 ? uri.substring(0, semicolonIndex) : uri);
    }
    
    private static String valueOrEmpty(String input) {
        if (input == null) {
            return "";
        }
        return input;
    }
    
    private static String getPathInfo(HttpServletRequest request) {
        String pathInfo = (String) request.getAttribute(WebUtils.INCLUDE_PATH_INFO_ATTRIBUTE);
        return pathInfo != null ? pathInfo : valueOrEmpty(request.getPathInfo());
    }
    
}
