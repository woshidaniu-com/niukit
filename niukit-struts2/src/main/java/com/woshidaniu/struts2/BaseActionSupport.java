package com.woshidaniu.struts2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ApplicationAware;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.plus.dispatcher.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;
import com.opensymphony.xwork2.inject.Inject;
import com.opensymphony.xwork2.util.ValueStack;
import com.woshidaniu.basicutils.BlankUtils;
import com.woshidaniu.basicutils.ExceptionUtils;
import com.woshidaniu.basicutils.StringUtils;
import com.woshidaniu.io.utils.FileUtils;
import com.woshidaniu.io.utils.IOUtils;
import com.woshidaniu.struts2.factory.SessionFactory;
import com.woshidaniu.web.utils.WebContextUtils;
import com.woshidaniu.web.utils.WebRequestUtils;

/**
 * 
 *@类名称	: BaseActionSupport
 *@类描述	：基于Struts2的action基础支撑类
 *@创建人	：kangzhidong
 *@创建时间	：Mar 7, 2016 9:54:15 AM
 *@修改人	：
 *@修改时间	：
 *@版本号	:v1.0
 *@param <T>
 */
@SuppressWarnings("serial")
public abstract class BaseActionSupport extends ActionSupport
		implements ServletResponseAware, ServletRequestAware, RequestAware,
		SessionAware, CookiesAware, ApplicationAware, ActionAware, Preparable {
	protected static final transient Logger LOG = LoggerFactory.getLogger(BaseActionSupport.class);
	public static final String FAILED = Result.FAILED;// 返回失败
	public static final String OPER_SAVE = "save";// 操作保存
	public static final String DATA = Result.DATA;
	public static final String QUERY = Result.QUERY;
	public static final String STREAM = Result.STREAM;
	
	/**json格式**/
	protected String json;
	/**友情提示信息**/
	protected String tooltip; 
	/**通告说明**/
	protected String notes;  
	/**actionName**/
	protected String actionName; 
	/**从第几条开始显示**/
	protected Integer start = DEFAULT_STARTOFRECORD;
	/**每页大小**/
	protected Integer limit = DEFAULT_PAGESIZE;
	/**排序的顺序**/
	protected String dir = null;
	/**排序的字段**/
	protected String sort = null;
	/**验证码的字段**/
	protected String verificationCode = null;
	/**请求区别参数标识；防止缓存**/
	protected String time;
	/**文件对象*/
	protected File file;
	/** 用来封装上传文件的类型*/
	protected String fileContentType;
	/** 用来封装上传文件的文件名*/
	protected String fileFileName;
	/**文件名，在处理导出时候会用到*/
	protected String fileName;
	/**文件路径，在处理下载时候会用到*/
	protected String filePath;
	/**字节数组，在处理下载时候会用到*/
	protected byte[] bytes;
	/**响应类型，处理返回结果时候用到*/
	protected String contentType;
	/**输入流，处理流的下载时候用到*/
	protected InputStream inputStream;
	/**空对象列表，用于除了jGrid外需要补充集合不足的空集合*/
	protected List<Object> emptylist = new ArrayList<Object>();
	/**http响应对象*/
	protected HttpServletResponse response;
	/**http请求对象*/
	protected HttpServletRequest request;
	/**http请求参数的Map对象*/
	protected Map<String, Object> requestMap;
	/**http请求会话的Map对象*/
	protected Map<String, Object> sessionMap;
	/**http请求cookie的Map对象*/
	protected Map<String, String> cookieMap;
	/**web应用application的Map对象*/
	protected Map<String,Object> applicationMap;
	/**主键值集合*/
	protected List<String> keyList; 
	/**Struts指定的国际化资源文件名称*/
	protected String[] resources;
	
	@Inject(value = org.apache.struts2.StrutsConstants.STRUTS_CUSTOM_I18N_RESOURCES)
    public void setResources(String resources) {
        this.resources = StringUtils.tokenizeToStringArray(resources);
    }
	
	/**
	 * 
	 * @description	： 手动切换国际化语音
	 * @author 		： kangzhidong
	 * @date 		：Jan 29, 2016 11:53:15 AM
	 * @return
	 */
	public String changeLocal() {
		//国际化语音参数：language_country；如 ：zh_CN 
		String language = getRequest().getParameter("language");
		if (language != null) {
			String loc[] = language.split("_");
			Locale locale = new Locale(loc[0], loc[1]);
			getSession().setAttribute("WW_TRANS_I18N_LOCALE", locale);
			ActionContext.getContext().setLocale(locale);
			getValueStack().set(DATA, "1");
		}else{
			getValueStack().set(DATA, "0");
		}
		return Result.DATA;
	}
	
	/**
	 * 重写getLocale，实现优先从session中获取国际化语音
	 */
	@Override
	public Locale getLocale() {
		ActionContext ctx = ActionContext.getContext();
        if (ctx != null) {
        	Locale localeInSessin = (Locale)SessionFactory.getSession().getAttribute("WW_TRANS_I18N_LOCALE");
    		if(null != localeInSessin){
    			ctx.setLocale(localeInSessin);
    			return localeInSessin;
    		}
        	Locale locale = ctx.getLocale();
            if (locale == null) {
            	locale = Locale.getDefault();
            	ctx.setLocale(locale);
            }
            return locale;
        } else {
            if (LOG.isDebugEnabled()) {
            	LOG.debug("Action context not initialized");
            }
            return null;
        }
    }
	
	@Override
	public void prepare() throws Exception {
		
	}
	
	/*============ActionAware接口实现 Start====================================*/
    
    /**
	 * 返回Request对象
	 */
	@Override
	public HttpServletRequest getRequest() {
		return this.request == null ? ServletActionContext.getRequest() : this.request;
	}
	
	/**
	 * 返回Response对象
	 */
	@Override
	public HttpServletResponse getResponse() {
		return this.response == null ? ServletActionContext.getResponse() : this.response;
	}
	
	/**
	 * 返回Session对象
	 */
	@Override
	public HttpSession getSession() {
		return getRequest().getSession();
	}
	
	/**
	 * 返回ServletContext对象
	 */
	@Override
	public ServletContext getServletContext() {
		return ServletActionContext.getServletContext();
	}
	
	/**
	 * 返回ValueStack对象
	 */
	@Override
	public ValueStack getValueStack() {
		return ServletActionContext.getContext().getValueStack();
	}
	
	/*============ActionAware接口实现 End====================================*/
	
	/*============ServletRequestAware,ServletResponseAware接口实现 Start=====*/
	
	@Override
	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	
	@Override
	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
	
	/*============ServletRequestAware,ServletResponseAware接口实现 End=======*/
	
	/*============RequestAware,SessionAware,CookiesAware,ApplicationAware 接口实现 Start=====*/
	
	
	@Override
	public void setRequest(Map<String, Object> request) {
		this.requestMap = request;
	}
	
	public final Map<String, Object> getRequestMap() {
		return requestMap;
	}
	
	@Override
	public void setSession(Map<String, Object> session) {
		this.sessionMap = session;
	}

	public final Map<String, Object> getSessionMap() {
		return this.sessionMap;
	}

	@Override
	public void setCookiesMap(Map<String, String> cookies) {
		this.cookieMap = cookies;
	}

	public Map<String, String> getCookieMap() {
		return cookieMap;
	}
	
	@Override
	public void setApplication(Map<String, Object> application) {
		this.applicationMap = application;
	}

	public Map<String, Object> getApplicationMap() {
		return applicationMap;
	}

	/*============RequestAware,SessionAware,CookiesAware,ApplicationAware 接口实现 End=======*/
	
	/**
	 * 
	 *@描述		：向栈保存对象
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:10:40 AM
	 *@param key 唯一标识key
	 *@param value 对象的值或引用
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void setProperty(String key, Object value) {
		getValueStack().set(key, value);
	}
	
	/**
	 * 
	 *@描述		：调用此方法将搜集当前请求中的参数设置到栈中用于下一个响应页面的使用
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:11:12 AM
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public void transmitParameters() {
		//迭代请求的参数，将参数传递下去
		Enumeration<String> emus = this.getRequest().getParameterNames();
		while (emus.hasMoreElements()) {
			String name = (String) emus.nextElement();
			this.setProperty(name, this.getRequest().getParameter(name));
		}
	}

	/**
	 * 
	 *@描述		：公用异常处理:控制台打印和log4j记录日志 + 值栈记录日志详情；该方法在 return ERROR;时使用
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:11:20 AM
	 *@param ex
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	protected void logException(Throwable ex) {
		// 控制台打印和log4j记录日志
		logStackException(ex);
		// ValueStack 设置错误消息，用于返回客户端
		getValueStack().set(Result.MESSAGE,  ExceptionUtils.getFullHtmlStackTrace(ex));
	}
	
	/**
	 * 
	 *@描述		：公用异常处理:控制台打印和log4j记录日志
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:11:27 AM
	 *@param ex
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	protected void logStackException(Throwable e) {
		// 调试打印控制台
		e.printStackTrace();
		 // 日志记录异常信息
		LOG.error(e.getLocalizedMessage(),e);
	}

	public File getFile() {
	
		return file;
	}

	public void setFile(File file) {
	
		this.file = file;
	}

	public String getFileContentType() {
	
		return fileContentType;
	}

	public void setFileContentType(String fileContentType) {
	
		this.fileContentType = fileContentType;
	}

	public String getFileFileName() {
	
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
	
		this.fileFileName = fileFileName;
	}

	/**
	 * 
	 *@描述		：//对于配置中的 ${fileName}, 获得下载保存时的文件名       
			     public String getFileName() {       
			         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");       
			         String fileName = "序列号(" + df.format(new Date()) + ").txt";       
			         try {       
			             //中文文件名也是需要转码为 ISO8859-1，否则乱码       
			             return new String(fileName.getBytes(), "ISO8859-1");       
			         } catch (UnsupportedEncodingException e) {       
			             return "impossible.txt";       
			         }       
			     }     
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:19:16 AM
	 *@return
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public String getFileName() {
		try {
			if(!BlankUtils.isBlank(fileName)){
				String userAgent = this.request.getHeader("user-agent").toLowerCase();
				if(userAgent.contains("msie") || userAgent.contains("rv:11") || userAgent.contains("edge") ) {
					//win10 ie edge浏览器 和 其他系统的IE
					this.fileName = URLEncoder.encode(new String(fileName.getBytes("ISO8859-1")),"UTF-8")
							.replaceAll("%21", "\\!").replaceAll("%40", "\\@").replaceAll("%23", "\\#").replaceAll("%24", "\\$").replaceAll("%25", "\\%")
							.replaceAll("%5E", "\\^").replaceAll("%26", "\\&").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%5B", "\\[").replaceAll("%5D", "\\]")
							.replaceAll("%2B", "\\+").replaceAll("%3D", "\\=").replaceAll("%7B", "\\{").replaceAll("%7D", "\\}");
				}
				return fileName;
			}
		} catch (UnsupportedEncodingException e) {
			
		}
		return fileName;
	}
	
	

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}


	/**
	 * 
	 *@描述：//获得下载文件的内容，可以直接读入一个物理文件或从数据库中获取内容       
		     public InputStream getInputStream() throws Exception {       
		         //return new FileInputStream("somefile.rar"); 直接下载 somefile.rar       
		        
		         //和 Servlet 中不一样，这里我们不需对输出的中文转码为 ISO8859-1       
		         return new ByteArrayInputStream("Struts2 文件下载测试".getBytes());       
		     }    
	 *@创建人:kangzhidong
	 *@创建时间:2014-10-11上午10:16:31
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@return
	 */
	public InputStream getInputStream(){
		try {
			if(BlankUtils.isBlank(inputStream)){
				if(!BlankUtils.isBlank(file)){
					fileName = BlankUtils.isBlank(fileName) ? new String(file.getName().getBytes(), "ISO8859-1")  : fileName;
					return new FileInputStream(file);
				}
			}else if(!BlankUtils.isBlank(bytes)){
				return new ByteArrayInputStream(bytes);
			}
		} catch (IOException e) {
			logException(e);
		}
		return inputStream;
	}

	/**
	 * 
	 *@描述：这里是处理文件流，直接将文件返回，在FileStreamResult中将会对象返回的文件进行进一步处理
	 *@创建人:kangzhidong
	 *@创建时间:2014-12-15上午11:45:25
	 *@修改人:
	 *@修改时间:
	 *@修改描述:
	 *@return
	 */
	public File getInputFile() throws IOException{
		fileName = BlankUtils.isBlank(fileName) ? new String(file.getName().getBytes(), "ISO8859-1") : fileName;
		return file;
	}
	
	/**
	 * 
	 *@描述		：返回文件或者流的字节数组
	 *@创建人	: kangzhidong
	 *@创建时间	: Mar 7, 201610:16:54 AM
	 *@return
	 *@throws IOException
	 *@修改人	: 
	 *@修改时间	: 
	 *@修改描述	:
	 */
	public byte[] getInputBytes() throws IOException{
		if(BlankUtils.isBlank(bytes)){
			if(!BlankUtils.isBlank(file)){
				return FileUtils.toByteArray(file);
			}else if(!BlankUtils.isBlank(inputStream)){
				ByteArrayOutputStream output = null;
				try {
					output = new ByteArrayOutputStream();
					IOUtils.copy(inputStream, output);
					return output.toByteArray();
				} finally{
					IOUtils.closeQuietly(output);
				}
			}
		}
		return bytes;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public List<Object> getEmptylist() {
		return emptylist;
	}

	public void setEmptylist(List<Object> emptylist) {
		this.emptylist = emptylist;
	}

	
	//---------------分页、验证码------------------------

	/**
	 * 
	 * @description:显示的页码
	 * @author <a href="mailto:540367164@qq.com">kangzhidong</a>
	 * @date 2011-3-29
	 * @return
	 */
	protected Integer getPageNo() {
		return start / limit + 1;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getJson() {
	
		return json;
	}

	public void setJson(String json) {
	
		this.json = json;
	}

	public String getTooltip() {
	
		return tooltip;
	}

	public void setTooltip(String tooltip) {
	
		this.tooltip = tooltip;
	}

	public String getNotes() {
	
		return notes;
	}

	public void setNotes(String notes) {
	
		this.notes = notes;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	protected String getFolder(String folder) {
		String realPath = getServletContext().getRealPath(File.separator);
		if (!realPath.endsWith(File.separator)) {
			realPath += File.separator;
		}
		File file = new File(realPath + folder);
		if (!file.exists()) {
			file.mkdirs();
		}
 		return file.getAbsolutePath() + File.separator;
	}
	
	public String getRemoteAddr() {
		return WebRequestUtils.getRemoteAddr(getRequest());
	}
	
	public String getContextPath() {
		return WebContextUtils.getBaseContextPath(getRequest());
	}
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

}
