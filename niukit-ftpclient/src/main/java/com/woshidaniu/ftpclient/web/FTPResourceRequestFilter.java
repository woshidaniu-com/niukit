package com.woshidaniu.ftpclient.web;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ExceptionUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.ftpclient.FTPConstants;
import com.woshidaniu.ftpclient.client.IFTPClient;
import com.woshidaniu.ftpclient.utils.FTPExceptionUtils;
import com.woshidaniu.ftpclient.utils.FTPPathUtils;

/**
 * 
 * @className	： FTPResourceRequestFilter
 * @description	： FTP资源请求过滤器，拦截文件访问请求，根据初始化参数决定是否去【FTP文件服务器】下载文件
 *<pre>&lt;filter&gt;    
 *	&lt;filter-name&gt;ftpResource&lt;/filter-name&gt;    
 *	&lt;filter-class&gt;com.woshidaniu.ftpclient.web.FTPResourceRequestFilter&lt;/filter-class&gt;  
 *&lt;/filter&gt;    
 *&lt;filter-mapping&gt;    
 *	&lt;filter-name&gt;ftpResource&lt;/filter-name&gt;    
 *	&lt;url-pattern&gt;/ftp/*&lt;/url-pattern&gt;    
 *&lt;/filter-mapping&gt;
 * </pre>
 * @author 		：kangzhidong
 * @date		： Jan 18, 2016 5:02:24 PM
 */
public abstract class FTPResourceRequestFilter implements Filter {
	
	protected static Logger LOG = LoggerFactory.getLogger(FTPResourceRequestFilter.class);
	//IFTPClient接口
	protected IFTPClient ftpClient = null;
	//文件本地存储路径
	protected String localDir = FTPConstants.DEFAULT_FTPCLIEN_WEB_LOCALDIR;
	//是否缓存FTP文件到本地存储路径
	protected boolean cacheLocal = false;
	//FTP文件在本地缓存的时间;默认10分钟
	protected long cacheExpiry = FTPConstants.DEFAULT_FTPCLIEN_WEB_CACHEEXPIRY;
	//请求过滤前缀;默认 /ftp/
	protected String requestPrefix = FTPConstants.DEFAULT_FTPCLIEN_WEB_REQUESTPREFIX;
	//文件本地存储目录
	protected File directory = null;
	//异常信息重定向路径
	protected String redirectURL = null;
 
	
	protected abstract IFTPClient initFTPClient(FilterConfig filterConfig);
	
	protected abstract String getDecryptPath(String encryptPath) throws Exception;
	
	protected HttpServletRequest httpRequest;
	protected HttpServletResponse httpResponse;
	
	/**
	 * 
	 * @description	： 根据文件扩展名设置相应的文件响应类型
	 *<pre>String contentType =  FilemimeUtils.getExtensionMimeType(extension);
	 *String fileName = filePath.substring(filePath.lastIndexOf(File.separator)+1);
     *if(contentType != null)   {
     *  response.setContentType(contentType);
     *}else{
     *   response.setContentType("application/octet-stream");
     *}
     *response.setHeader( "Content-Disposition",WebResponseUtils.getContentDisposition(isAttachment, fileName));
     * </pre>
	 * @author 		：kangzhidong
	 * @date 		：Jan 19, 2016 9:26:42 AM
	 * @param response
	 * @param filePath
	 * @param extension
	 * @param filename
	 */
	protected abstract void setHeader(HttpServletRequest httpRequest,HttpServletResponse response,String filePath,String extension,String filename) throws Exception;

	protected String wrapException(Exception e){
		return ExceptionUtils.getFullHtmlStackTrace(e);
	}
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		//获取文件存储路径
		localDir = StringUtils.getSafeStr(filterConfig.getInitParameter(FTPConstants.FTPCLIEN_WEB_LOCALDIR_KEY), localDir);
		//获取请求过滤前缀
		requestPrefix = StringUtils.getSafeStr(filterConfig.getInitParameter(FTPConstants.FTPCLIEN_WEB_REQUESTPREFIX_KEY), requestPrefix);
		//获取是否缓存FTP文件到本地存储路径
		cacheLocal = StringUtils.getSafeBoolean(filterConfig.getInitParameter(FTPConstants.FTPCLIEN_WEB_CACHELOCAL_KEY), "false");
		//获取共享文件在本地缓存的时间;默认10分钟
		cacheExpiry = StringUtils.getSafeLong(filterConfig.getInitParameter(FTPConstants.FTPCLIEN_WEB_CACHEEXPIRY_KEY), cacheExpiry + "");
		//获取文件本地存储目录
		directory = FTPPathUtils.getLocalDir(filterConfig.getServletContext(), localDir);
		//异常信息重定向路径
		redirectURL = StringUtils.getSafeStr(filterConfig.getInitParameter(FTPConstants.FTPCLIEN_WEB_REDIRECTURL_KEY), redirectURL);
		//调用初始化FTPClient抽象方法
		ftpClient = this.initFTPClient(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest request,
			ServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		if( ftpClient == null ){
			filterChain.doFilter(request, response);
			return;
		}
		
		httpRequest = (HttpServletRequest) request;
		httpResponse = (HttpServletResponse) response;
		
		try {
			// 获取请求基本路基 /ftp/加密内容
			String servletPath = httpRequest.getServletPath();
			// 获取解析后的文件路径，同时也是本地存储路径
			String filepath = this.getDecryptPath(FTPPathUtils.getEncryptPath(servletPath,requestPrefix));
			//获得文件目录
			String fileDir = FilenameUtils.getPath(filepath);
			//获得文件名 
			String fileName = FilenameUtils.getName(filepath);
			//启用本地缓存
			if(cacheLocal){
				//构建压缩后的文件路径
				File localFile = new File(directory,filepath);
				//判断是否是存在的文件，且未过期
				if(localFile.exists() && (System.currentTimeMillis() - localFile.lastModified() < cacheExpiry)){
					//重定向请求
					request.getRequestDispatcher(localFile.getAbsolutePath()).forward(request, response);
				}else{
					//文件绝对路径
					String localFilePath = localFile.getAbsolutePath();
					//文件名称，不包含后缀
					String baseName = FilenameUtils.getBaseName(localFilePath);
					//定义锁文件
					File lockFile = new File(FilenameUtils.getFullPath(localFilePath) + baseName + ".lock");
					if(!lockFile.exists()){
						File dir = lockFile.getParentFile();
						if(!dir.exists()){
							dir.mkdirs();
						}
						lockFile.setReadable(true);
						lockFile.setWritable(true);
						lockFile.createNewFile();
					}
					FileChannel inChannel = null;
					FileLock fileLock = null;
					try {
						//先按照“rw”访问模式打开localFile文件，如果这个文件还不存在，RandomAccessFile的构造方法会创建该文件
						//其中的“rws”参数中，rw代表读写方式，s代表同步方式，也就是锁。这种方式打开的文件，就是独占方式。
						//RandomAccessFile不支持只写模式，因为把参数设为“w”是非法的        
						inChannel = new RandomAccessFile(lockFile,"rw").getChannel();
						fileLock =  inChannel.tryLock();
						//设置阻塞当前线程的逻辑
						while(fileLock == null || !fileLock.isValid()){
							Thread.sleep(1000);
							fileLock =  inChannel.tryLock();
						}
						if(localFile.exists()){
							localFile.delete();
						}
						//去【FTP文件服务器】取回文件
						ftpClient.downloadToFile(fileDir, fileName, localFile);
						LOG.info(" [" + localFile.getName() + "] FTP取回成功!");
						//释放文件锁
						fileLock.release();
					} catch (Exception e) {
						LOG.error(ExceptionUtils.getFullStackTrace(e));
					} finally {
						try {
							//关闭输入通道
							IOUtils.closeQuietly(inChannel);
							//释放文件锁
							if(fileLock != null){
								fileLock.release();
							}
							//删除锁文件
							lockFile.delete();
						} catch (Exception e) {
							LOG.error(ExceptionUtils.getFullStackTrace(e));
						}
					}
				}
				//判断本地缓存的文件存在
				if(localFile.exists()){
					try {
						//文件绝对路径
						String localFilePath = localFile.getAbsolutePath();
						//获得文件扩展名 
						String extension = FilenameUtils.getExtension(localFilePath);
						//获得文件名 
						String filename = FilenameUtils.getName(localFilePath);
						//根据文件扩展名设置相应的文件响应类型
						this.setHeader(httpRequest,httpResponse,localFilePath,extension,filename);
						//重定向请求
						request.getRequestDispatcher(localFilePath).forward(request, response);
					} catch (Exception e) {
						//后台记录异常日志
						LOG.error(ExceptionUtils.getFullStackTrace(e));
						//指定了重定向页面
						if(!BlankUtils.isBlank(redirectURL)){
							request.setAttribute("message", wrapException(e));
							request.getRequestDispatcher(redirectURL).forward(request, response);
						}else {
							//将异常写到页面
							//FTPExceptionUtils.renderException(httpResponse, ExceptionUtils.getFullHtmlStackTrace(e));
							//后台记录异常日志
							LOG.error(ExceptionUtils.getFullStackTrace(e));
						}
					}
				}
			}else{
				try {
					//获得文件扩展名 
					String extension = FilenameUtils.getExtension(filepath);
					//根据文件扩展名设置相应的文件响应类型
					this.setHeader(httpRequest,httpResponse,filepath,extension,fileName);
					//从【FTP文件服务器】指定目录取文件直接写入到response的输出流
					ftpClient.downloadToResponse(fileDir, fileName, response);
				} catch (Exception e) {
					//后台记录异常日志
					LOG.error(ExceptionUtils.getFullStackTrace(e));
					//指定了重定向页面
					if(!BlankUtils.isBlank(redirectURL)){
						request.setAttribute("message", wrapException(e));
						request.getRequestDispatcher(redirectURL).forward(request, response);
					}else {
						//将异常写到页面
						//FTPExceptionUtils.renderException(httpResponse, ExceptionUtils.getFullHtmlStackTrace(e));
						//后台记录异常日志
						LOG.error(ExceptionUtils.getFullStackTrace(e));
					}
					
				}
			}
		} catch (Exception e) {
			//将异常写到页面
			//FTPExceptionUtils.renderException(httpResponse, ExceptionUtils.getFullHtmlStackTrace(e));
			//后台记录异常日志
			LOG.error(ExceptionUtils.getFullStackTrace(e));
		}
	}

	@Override
	public void destroy() {
		
	}

	/**
	 * @return the httpRequest
	 */
	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	/**
	 * @return the httpResponse
	 */
	public HttpServletResponse getHttpResponse() {
		return httpResponse;
	}
	

}
