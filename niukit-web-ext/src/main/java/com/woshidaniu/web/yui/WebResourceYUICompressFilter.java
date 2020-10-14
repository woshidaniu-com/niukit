package com.woshidaniu.web.yui;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.io.utils.DirectoryUtils;
import com.woshidaniu.web.ExtParameter;
import com.woshidaniu.web.ExtParameters;
import com.woshidaniu.web.servlet.filter.OncePerRequestFilter;
import com.woshidaniu.web.utils.WebUtils;
import com.woshidaniu.yuicompressor.YUICompressUtils;
/**
 * 
 * @className	： StaticResourceYUICompressFilter
 * @description	： JS,CSS静态文件压缩过滤器:该过滤器负责检查js,css资源是否有对应的YUI压缩文件，如果已经压缩则交给下一个过滤器，如GZIP压缩过滤器；
 * 					如果没有压缩过，则使用YUICompress工具包进行压缩输出，并重定向请求到压缩后的资源路径;建议与com.woshidaniu.designer.listener.StaticResourceYUICompressListener
 * 					一起使用，可以在应用启动后就执行压缩，减少请求过程中对文件压缩时的系统资源消耗;
 * @see			: com.woshidaniu.globalweb.listener.StaticResourceYUICompressListener
 * @author 		： kangzhidong
 * @date		： 2015-6-11 上午10:21:38
 */
public class WebResourceYUICompressFilter extends OncePerRequestFilter {

	protected final Logger LOG = LoggerFactory.getLogger(WebResourceYUICompressFilter.class);
	protected ConcurrentSkipListSet<String> resourceSet = new ConcurrentSkipListSet<String>();
	//压缩后文件的后缀前的扩展名称
	protected String suffix = "min";
	//是否Debug模式,是则不会进行js,css资源的压缩处理：该模式通常在开发时设置为true,部署时设置为false
	protected boolean debug = true;
	
	@Override
	protected void onFilterConfigSet(FilterConfig filterConfig) throws Exception {
		//初始化web运行参数
		ExtParameters.initialize(filterConfig);
		//获取调试模式参数
		debug  = ExtParameters.getGlobalBoolean(ExtParameter.YUI_DEBUG);
		//获取后缀
		suffix 	= ExtParameters.getGlobalString(ExtParameter.YUI_SUFFIX);
		LOG.info("WebResourceYUICompressFilter inited.");
	}

	@Override
	protected void doFilterInternal(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest httpRequest = WebUtils.toHttp(request);
		//非调试模式
		if(!debug){
			// 获取请求基本路基
			String resourcePath = httpRequest.getServletPath();
			String extension = FilenameUtils.getExtension(resourcePath);
			String filename = FilenameUtils.getName(resourcePath);
			if(("js".equalsIgnoreCase(extension)||"css".equalsIgnoreCase(extension)) && filename.indexOf(suffix) <= 0 ){
				//文件名称，不包含后缀
				String baseName = FilenameUtils.getBaseName(resourcePath);
				// 应用程序根目录
				String filePath = httpRequest.getSession().getServletContext().getRealPath(DirectoryUtils.getResolvePath(resourcePath));
				//构建压缩后的文件路径
				File destFile = new File(FilenameUtils.getFullPath(filePath),baseName + "-" + suffix + "." + extension);
				destFile.delete();
				//判断是否是存在的文件
				if(destFile.exists()){
					resourcePath =  FilenameUtils.getFullPath(resourcePath) + baseName + "-" + suffix + "." + extension; 
				}else{
					//判断是否是已经处理过的资源
					if(!resourceSet.contains(baseName)){
						//文件不存在，则进行压缩操作
						try {
							YUICompressUtils.compressFile(new File(filePath),destFile);
							if(destFile.exists()){
								resourcePath =  FilenameUtils.getFullPath(resourcePath) + baseName + "-" + suffix + "." + extension;
								resourceSet.add(baseName);
								LOG.info(" [" + destFile.getName() + "] 压缩成功!");
							}
						} catch (Exception e) {
							LOG.error(" ["+destFile.getName()+"] 压缩失败 !",e);
						}
					}
				}
				request.getRequestDispatcher(resourcePath).forward(request, response);
			}else{
				filterChain.doFilter(request, response);
			}
		}else{
			filterChain.doFilter(request, response);
		}
	}

	@Override
	public void destroy() {
		LOG.info("WebResourceYUICompressFilter destroy.");
	}
	 

}
